package com.emrsystem.domain.emr.service;

import com.emrsystem.domain.emr.entity.LabResult;
import com.emrsystem.domain.emr.repository.LabResultRepository;
import com.emrsystem.domain.emr.response.LabTrendResponses;
import com.emrsystem.domain.patient.entity.Patient;
import com.emrsystem.domain.patient.store.PatientStore;
import com.emrsystem.global.common.enums.ErrorCode;
import com.emrsystem.global.common.exception.CommonException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LabTrendService {

    private final LabResultRepository labResultRepository;
    private final PatientStore patientStore;

    /**
     * 환자별 검사 결과 추이 조회
     */
    public LabTrendResponses.LabTrendAnalysis getTrendAnalysis(Long patientId, String testItemName,
                                                               LocalDateTime startDate, LocalDateTime endDate) {
        Patient patient = patientStore.findById(patientId)
                .orElseThrow(() -> new CommonException(ErrorCode.PATIENT_NOT_FOUND));

        List<LabResult> results = labResultRepository.findTrendByPatientAndTestItem(
                patientId, testItemName, startDate, endDate);

        if (results.isEmpty()) {
            throw new CommonException(ErrorCode.DATA_NOT_FOUND,
                    "해당 기간의 검사 결과를 찾을 수 없습니다.");
        }

        // 추이 데이터 생성
        List<LabTrendResponses.TrendDataPoint> trendData = results.stream()
                .map(result -> {
                    Double numericValue = parseNumericValue(result.getResultValue());
                    return LabTrendResponses.TrendDataPoint.builder()
                            .date(result.getCreatedAt().toLocalDate())
                            .value(numericValue)
                            .unit(result.getUnit())
                            .abnormalFlag(result.getAbnormalFlag())
                            .referenceRange(result.getReferenceRange())
                            .build();
                })
                .collect(Collectors.toList());

        // 통계 계산
        LabTrendResponses.TrendStatistics statistics = calculateStatistics(trendData, results.get(0));

        // 변화율 계산
        List<LabTrendResponses.ChangeAlert> changeAlerts = detectSignificantChanges(trendData);

        return LabTrendResponses.LabTrendAnalysis.builder()
                .patientId(patientId)
                .patientName(patient.getName())
                .testItemName(testItemName)
                .startDate(startDate.toLocalDate())
                .endDate(endDate.toLocalDate())
                .trendData(trendData)
                .statistics(statistics)
                .changeAlerts(changeAlerts)
                .build();
    }

    /**
     * 환자별 검사 항목 목록 조회
     */
    public List<String> getTestItemNames(Long patientId) {
        Patient patient = patientStore.findById(patientId)
                .orElseThrow(() -> new CommonException(ErrorCode.PATIENT_NOT_FOUND));

        return labResultRepository.findDistinctTestItemNamesByPatient(patientId);
    }

    /**
     * 검사 결과 비교 (최신 결과와 이전 결과)
     */
    public LabTrendResponses.LabResultComparison compareResults(Long patientId, String testItemName, int compareCount) {
        Patient patient = patientStore.findById(patientId)
                .orElseThrow(() -> new CommonException(ErrorCode.PATIENT_NOT_FOUND));

        List<LabResult> results = labResultRepository.findLatestByPatientAndTestItem(patientId, testItemName);

        if (results.isEmpty()) {
            throw new CommonException(ErrorCode.DATA_NOT_FOUND,
                    "검사 결과를 찾을 수 없습니다.");
        }

        // 최신 결과와 이전 결과 비교
        LabResult latestResult = results.get(0);
        List<LabResult> previousResults = results.stream()
                .skip(1)
                .limit(compareCount - 1)
                .collect(Collectors.toList());

        List<LabTrendResponses.ComparisonItem> comparisons = new ArrayList<>();

        // 최신 결과
        comparisons.add(LabTrendResponses.ComparisonItem.builder()
                .date(latestResult.getCreatedAt().toLocalDate())
                .value(parseNumericValue(latestResult.getResultValue()))
                .unit(latestResult.getUnit())
                .abnormalFlag(latestResult.getAbnormalFlag())
                .referenceRange(latestResult.getReferenceRange())
                .isLatest(true)
                .build());

        // 이전 결과들
        for (LabResult previousResult : previousResults) {
            Double previousValue = parseNumericValue(previousResult.getResultValue());
            Double latestValue = parseNumericValue(latestResult.getResultValue());

            String changeDirection = null;
            Double changePercent = null;

            if (previousValue != null && latestValue != null && previousValue != 0) {
                changePercent = ((latestValue - previousValue) / previousValue) * 100;
                if (changePercent > 10) {
                    changeDirection = "INCREASE";
                } else if (changePercent < -10) {
                    changeDirection = "DECREASE";
                } else {
                    changeDirection = "STABLE";
                }
            }

            comparisons.add(LabTrendResponses.ComparisonItem.builder()
                    .date(previousResult.getCreatedAt().toLocalDate())
                    .value(previousValue)
                    .unit(previousResult.getUnit())
                    .abnormalFlag(previousResult.getAbnormalFlag())
                    .referenceRange(previousResult.getReferenceRange())
                    .isLatest(false)
                    .changePercent(changePercent != null ? BigDecimal.valueOf(changePercent)
                            .setScale(2, RoundingMode.HALF_UP) : null)
                    .changeDirection(changeDirection)
                    .build());
        }

        return LabTrendResponses.LabResultComparison.builder()
                .patientId(patientId)
                .patientName(patient.getName())
                .testItemName(testItemName)
                .comparisons(comparisons)
                .build();
    }

    /**
     * 숫자 값 파싱
     */
    private Double parseNumericValue(String value) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }
        try {
            // 숫자만 추출 (단위 제거)
            String numericStr = value.replaceAll("[^0-9.-]", "").trim();
            if (numericStr.isEmpty()) {
                return null;
            }
            return Double.parseDouble(numericStr);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    /**
     * 통계 계산
     */
    private LabTrendResponses.TrendStatistics calculateStatistics(List<LabTrendResponses.TrendDataPoint> trendData,
                                                                  LabResult firstResult) {
        List<Double> values = trendData.stream()
                .map(LabTrendResponses.TrendDataPoint::getValue)
                .filter(v -> v != null)
                .collect(Collectors.toList());

        if (values.isEmpty()) {
            return LabTrendResponses.TrendStatistics.builder()
                    .count(0)
                    .min(null)
                    .max(null)
                    .average(null)
                    .referenceRange(firstResult.getReferenceRange())
                    .build();
        }

        Double min = values.stream().min(Comparator.naturalOrder()).orElse(null);
        Double max = values.stream().max(Comparator.naturalOrder()).orElse(null);
        Double average = values.stream()
                .mapToDouble(Double::doubleValue)
                .average()
                .orElse(0.0);

        return LabTrendResponses.TrendStatistics.builder()
                .count(values.size())
                .min(min)
                .max(max)
                .average(BigDecimal.valueOf(average).setScale(2, RoundingMode.HALF_UP))
                .referenceRange(firstResult.getReferenceRange())
                .build();
    }

    /**
     * 급격한 변화 감지
     */
    private List<LabTrendResponses.ChangeAlert> detectSignificantChanges(
            List<LabTrendResponses.TrendDataPoint> trendData) {
        List<LabTrendResponses.ChangeAlert> alerts = new ArrayList<>();

        for (int i = 1; i < trendData.size(); i++) {
            LabTrendResponses.TrendDataPoint current = trendData.get(i);
            LabTrendResponses.TrendDataPoint previous = trendData.get(i - 1);

            if (current.getValue() != null && previous.getValue() != null && previous.getValue() != 0) {
                double changePercent = ((current.getValue() - previous.getValue()) / previous.getValue()) * 100;

                // 20% 이상 변화 시 경고
                if (Math.abs(changePercent) >= 20) {
                    alerts.add(LabTrendResponses.ChangeAlert.builder()
                            .date(current.getDate())
                            .previousValue(previous.getValue())
                            .currentValue(current.getValue())
                            .changePercent(BigDecimal.valueOf(changePercent).setScale(2, RoundingMode.HALF_UP))
                            .severity(Math.abs(changePercent) >= 50 ? "CRITICAL" : "WARNING")
                            .message(String.format("%.2f%% 변화 감지 (이전: %.2f, 현재: %.2f)",
                                    changePercent, previous.getValue(), current.getValue()))
                            .build());
                }
            }
        }

        return alerts;
    }
}

