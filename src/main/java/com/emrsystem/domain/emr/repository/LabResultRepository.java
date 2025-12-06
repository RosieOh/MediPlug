package com.emrsystem.domain.emr.repository;

import com.emrsystem.domain.emr.entity.LabResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface LabResultRepository extends JpaRepository<LabResult, Long> {
    List<LabResult> findByLabOrder_LabOrderId(Long labOrderId);
    List<LabResult> findByLabOrder_LabOrderIdAndStatus(Long labOrderId, String status);
    List<LabResult> findByAbnormalFlagIn(List<String> abnormalFlags);
    
    // Trend analysis queries
    @Query("SELECT lr FROM LabResult lr " +
           "JOIN lr.labOrder lo " +
           "WHERE lo.patient.patientId = :patientId " +
           "AND lr.testItemName = :testItemName " +
           "AND lr.status = 'FINAL' " +
           "AND lr.createdAt BETWEEN :startDate AND :endDate " +
           "ORDER BY lr.createdAt ASC")
    List<LabResult> findTrendByPatientAndTestItem(@Param("patientId") Long patientId,
                                                   @Param("testItemName") String testItemName,
                                                   @Param("startDate") LocalDateTime startDate,
                                                   @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT DISTINCT lr.testItemName FROM LabResult lr " +
           "JOIN lr.labOrder lo " +
           "WHERE lo.patient.patientId = :patientId " +
           "AND lr.status = 'FINAL' " +
           "ORDER BY lr.testItemName")
    List<String> findDistinctTestItemNamesByPatient(@Param("patientId") Long patientId);
    
    @Query("SELECT lr FROM LabResult lr " +
           "JOIN lr.labOrder lo " +
           "WHERE lo.patient.patientId = :patientId " +
           "AND lr.testItemName = :testItemName " +
           "AND lr.status = 'FINAL' " +
           "ORDER BY lr.createdAt DESC")
    List<LabResult> findLatestByPatientAndTestItem(@Param("patientId") Long patientId,
                                                    @Param("testItemName") String testItemName);
}

