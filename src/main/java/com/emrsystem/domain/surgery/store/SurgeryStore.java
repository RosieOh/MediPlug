package com.emrsystem.domain.surgery.store;

import com.emrsystem.domain.surgery.entity.PreOpChecklist;
import com.emrsystem.domain.surgery.entity.Surgery;
import com.emrsystem.domain.surgery.entity.SurgeryTeam;
import com.emrsystem.domain.surgery.repository.PreOpChecklistRepository;
import com.emrsystem.domain.surgery.repository.SurgeryRepository;
import com.emrsystem.domain.surgery.repository.SurgeryTeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class SurgeryStore {

    private final SurgeryRepository surgeryRepository;
    private final SurgeryTeamRepository surgeryTeamRepository;
    private final PreOpChecklistRepository preOpChecklistRepository;

    // Surgery operations
    public Surgery saveSurgery(Surgery surgery) {
        return surgeryRepository.save(surgery);
    }

    public Optional<Surgery> findSurgeryById(Long id) {
        return surgeryRepository.findById(id);
    }

    public List<Surgery> findSurgeriesByPatient(Long patientId) {
        return surgeryRepository.findByPatient_PatientId(patientId);
    }

    public Page<Surgery> findSurgeriesByPatient(Long patientId, Pageable pageable) {
        return surgeryRepository.findByPatient_PatientId(patientId, pageable);
    }

    public List<Surgery> findSurgeriesBySurgeon(Long surgeonId) {
        return surgeryRepository.findBySurgeon_DoctorId(surgeonId);
    }

    public List<Surgery> findSurgeriesByDateRange(LocalDateTime start, LocalDateTime end) {
        return surgeryRepository.findByScheduledDateTimeBetween(start, end);
    }

    public List<Surgery> findSurgeriesByStatus(String status) {
        return surgeryRepository.findByStatus(status);
    }

    public List<Surgery> findSurgeriesByRoomAndDateRange(String surgeryRoom, LocalDateTime start, LocalDateTime end) {
        return surgeryRepository.findBySurgeryRoomAndScheduledDateTimeBetween(surgeryRoom, start, end);
    }

    // SurgeryTeam operations
    public SurgeryTeam saveSurgeryTeam(SurgeryTeam surgeryTeam) {
        return surgeryTeamRepository.save(surgeryTeam);
    }

    public List<SurgeryTeam> findSurgeryTeamsBySurgery(Long surgeryId) {
        return surgeryTeamRepository.findBySurgery_SurgeryId(surgeryId);
    }

    public List<SurgeryTeam> findSurgeryTeamsByDoctor(Long doctorId) {
        return surgeryTeamRepository.findByDoctor_DoctorId(doctorId);
    }

    public void deleteSurgeryTeam(Long id) {
        surgeryTeamRepository.deleteById(id);
    }

    // PreOpChecklist operations
    public PreOpChecklist savePreOpChecklist(PreOpChecklist checklist) {
        return preOpChecklistRepository.save(checklist);
    }

    public List<PreOpChecklist> findPreOpChecklistsBySurgery(Long surgeryId) {
        return preOpChecklistRepository.findBySurgery_SurgeryId(surgeryId);
    }

    public List<PreOpChecklist> findUncheckedPreOpChecklistsBySurgery(Long surgeryId) {
        return preOpChecklistRepository.findBySurgery_SurgeryIdAndChecked(surgeryId, false);
    }

    public Optional<PreOpChecklist> findPreOpChecklistById(Long id) {
        return preOpChecklistRepository.findById(id);
    }

    public void deletePreOpChecklist(Long id) {
        preOpChecklistRepository.deleteById(id);
    }
}

