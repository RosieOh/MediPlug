package com.emrsystem.domain.vitalsign.repository;

import com.emrsystem.domain.vitalsign.entity.VitalSignAlert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VitalSignAlertRepository extends JpaRepository<VitalSignAlert, Long> {
    List<VitalSignAlert> findByVitalSign_VitalSignId(Long vitalSignId);
    List<VitalSignAlert> findByAcknowledgedFalse();
    List<VitalSignAlert> findBySeverityAndAcknowledgedFalse(String severity);
}

