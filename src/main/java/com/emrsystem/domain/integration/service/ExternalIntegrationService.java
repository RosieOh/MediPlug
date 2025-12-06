package com.emrsystem.domain.integration.service;

import com.emrsystem.domain.integration.response.IntegrationResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ExternalIntegrationService {

    public IntegrationResponses.IntegrationStatus getIntegrationStatus(String systemType) {
        // 외부 시스템 연동 상태 조회 (기본 구현)
        return IntegrationResponses.IntegrationStatus.builder()
                .systemType(systemType)
                .connected(false)
                .lastSyncTime(null)
                .status("NOT_CONFIGURED")
                .message("외부 시스템 연동이 설정되지 않았습니다.")
                .build();
    }

    public void syncWithExternalSystem(String systemType) {
        // 외부 시스템과 동기화 (기본 구현)
        log.info("Synchronizing with external system: {}", systemType);
        // 실제 구현 시 HL7 FHIR, LIS, PACS 등과 연동
    }
}

