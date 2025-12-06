package com.emrsystem.domain.surgery.service;

import com.emrsystem.domain.appointment.entity.Appointment;
import com.emrsystem.domain.appointment.store.AppointmentStore;
import com.emrsystem.domain.doctor.entity.Doctor;
import com.emrsystem.domain.doctor.store.DoctorStore;
import com.emrsystem.domain.patient.entity.Patient;
import com.emrsystem.domain.patient.store.PatientStore;
import com.emrsystem.domain.surgery.entity.PreOpChecklist;
import com.emrsystem.domain.surgery.entity.Surgery;
import com.emrsystem.domain.surgery.entity.SurgeryTeam;
import com.emrsystem.domain.surgery.request.SurgeryRequests;
import com.emrsystem.domain.surgery.store.SurgeryStore;
import com.emrsystem.global.common.enums.ErrorCode;
import com.emrsystem.global.common.exception.CommonException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SurgeryService {

    private final SurgeryStore surgeryStore;
    private final PatientStore patientStore;
    private final DoctorStore doctorStore;
    private final AppointmentStore appointmentStore;

    public Surgery get(Long id) {
        return surgeryStore.findSurgeryById(id)
                .orElseThrow(() -> new CommonException(ErrorCode.DATA_NOT_FOUND, "수술 정보를 찾을 수 없습니다."));
    }

    public List<Surgery> getByPatient(Long patientId) {
        return surgeryStore.findSurgeriesByPatient(patientId);
    }

    public Page<Surgery> getByPatient(Long patientId, Pageable pageable) {
        return surgeryStore.findSurgeriesByPatient(patientId, pageable);
    }

    public List<Surgery> getBySurgeon(Long surgeonId) {
        return surgeryStore.findSurgeriesBySurgeon(surgeonId);
    }

    public List<Surgery> getByDateRange(LocalDateTime start, LocalDateTime end) {
        return surgeryStore.findSurgeriesByDateRange(start, end);
    }

    public List<Surgery> getByStatus(String status) {
        return surgeryStore.findSurgeriesByStatus(status);
    }

    public List<Surgery> getByRoomAndDateRange(String surgeryRoom, LocalDateTime start, LocalDateTime end) {
        return surgeryStore.findSurgeriesByRoomAndDateRange(surgeryRoom, start, end);
    }

    @Transactional
    public Surgery create(SurgeryRequests.CreateSurgeryRequest request) {
        Patient patient = patientStore.findById(request.getPatientId())
                .orElseThrow(() -> new CommonException(ErrorCode.PATIENT_NOT_FOUND));

        Appointment appointment = null;
        if (request.getAppointmentId() != null) {
            appointment = appointmentStore.findById(request.getAppointmentId())
                    .orElseThrow(() -> new CommonException(ErrorCode.APPOINTMENT_NOT_FOUND));
        }

        Doctor surgeon = doctorStore.findById(request.getSurgeonId())
                .orElseThrow(() -> new CommonException(ErrorCode.DOCTOR_NOT_FOUND));

        Doctor anesthesiologist = null;
        if (request.getAnesthesiologistId() != null) {
            anesthesiologist = doctorStore.findById(request.getAnesthesiologistId())
                    .orElseThrow(() -> new CommonException(ErrorCode.DOCTOR_NOT_FOUND));
        }

        // 수술실 중복 체크
        List<Surgery> conflictingSurgeries = surgeryStore.findSurgeriesByRoomAndDateRange(
                request.getSurgeryRoom(),
                request.getScheduledDateTime().minusHours(1),
                request.getScheduledDateTime().plusHours(6)
        );
        if (!conflictingSurgeries.isEmpty()) {
            throw new CommonException(ErrorCode.DATA_INTEGRITY_VIOLATION,
                    "해당 시간에 수술실이 이미 예약되어 있습니다.");
        }

        Surgery surgery = Surgery.create(
                patient,
                appointment,
                request.getSurgeryName(),
                request.getSurgeryCode(),
                request.getScheduledDateTime(),
                surgeon,
                anesthesiologist,
                request.getAnesthesiaType(),
                request.getSurgeryRoom(),
                request.getPreOpDiagnosis()
        );

        surgery = surgeryStore.saveSurgery(surgery);

        // 수술팀 구성원 추가
        if (request.getTeamMembers() != null) {
            for (SurgeryRequests.TeamMemberRequest teamMember : request.getTeamMembers()) {
                Doctor doctor = doctorStore.findById(teamMember.getDoctorId())
                        .orElseThrow(() -> new CommonException(ErrorCode.DOCTOR_NOT_FOUND));
                SurgeryTeam team = SurgeryTeam.create(surgery, doctor, teamMember.getRole(), teamMember.getNotes());
                surgeryStore.saveSurgeryTeam(team);
            }
        }

        // 체크리스트 항목 추가
        if (request.getChecklistItems() != null) {
            for (String item : request.getChecklistItems()) {
                PreOpChecklist checklist = PreOpChecklist.create(surgery, item);
                surgeryStore.savePreOpChecklist(checklist);
            }
        }

        return surgery;
    }

    @Transactional
    public Surgery update(Long id, SurgeryRequests.UpdateSurgeryRequest request) {
        Surgery surgery = get(id);

        if (request.getSurgeryName() != null) {
            // 수정 로직은 엔티티에 update 메서드 추가 필요
            // 일단 간단하게 처리
        }

        // 수술실 변경 시 중복 체크
        if (request.getSurgeryRoom() != null && !request.getSurgeryRoom().equals(surgery.getSurgeryRoom())) {
            LocalDateTime scheduledTime = request.getScheduledDateTime() != null 
                    ? request.getScheduledDateTime() 
                    : surgery.getScheduledDateTime();
            List<Surgery> conflictingSurgeries = surgeryStore.findSurgeriesByRoomAndDateRange(
                    request.getSurgeryRoom(),
                    scheduledTime.minusHours(1),
                    scheduledTime.plusHours(6)
            );
            if (!conflictingSurgeries.isEmpty()) {
                throw new CommonException(ErrorCode.DATA_INTEGRITY_VIOLATION,
                        "해당 시간에 수술실이 이미 예약되어 있습니다.");
            }
        }

        return surgeryStore.saveSurgery(surgery);
    }

    @Transactional
    public Surgery start(Long id) {
        Surgery surgery = get(id);
        if (!"SCHEDULED".equals(surgery.getStatus())) {
            throw new CommonException(ErrorCode.DATA_INTEGRITY_VIOLATION,
                    "예정된 수술만 시작할 수 있습니다.");
        }
        surgery.start(LocalDateTime.now());
        return surgeryStore.saveSurgery(surgery);
    }

    @Transactional
    public Surgery complete(Long id, SurgeryRequests.CompleteSurgeryRequest request) {
        Surgery surgery = get(id);
        if (!"IN_PROGRESS".equals(surgery.getStatus())) {
            throw new CommonException(ErrorCode.DATA_INTEGRITY_VIOLATION,
                    "진행 중인 수술만 완료할 수 있습니다.");
        }
        surgery.complete(request.getEndDateTime(), request.getPostOpDiagnosis(),
                request.getProcedure(), request.getFindings());
        return surgeryStore.saveSurgery(surgery);
    }

    @Transactional
    public Surgery cancel(Long id, String reason) {
        Surgery surgery = get(id);
        surgery.cancel(reason);
        return surgeryStore.saveSurgery(surgery);
    }

    @Transactional
    public Surgery postpone(Long id, LocalDateTime newDateTime, String reason) {
        Surgery surgery = get(id);
        surgery.postpone(newDateTime, reason);
        return surgeryStore.saveSurgery(surgery);
    }

    @Transactional
    public Surgery addComplication(Long id, SurgeryRequests.AddComplicationRequest request) {
        Surgery surgery = get(id);
        surgery.addComplication(request.getComplication());
        return surgeryStore.saveSurgery(surgery);
    }

    // SurgeryTeam operations
    public List<SurgeryTeam> getTeamMembers(Long surgeryId) {
        return surgeryStore.findSurgeryTeamsBySurgery(surgeryId);
    }

    @Transactional
    public SurgeryTeam addTeamMember(Long surgeryId, SurgeryRequests.TeamMemberRequest request) {
        Surgery surgery = get(surgeryId);
        Doctor doctor = doctorStore.findById(request.getDoctorId())
                .orElseThrow(() -> new CommonException(ErrorCode.DOCTOR_NOT_FOUND));
        SurgeryTeam team = SurgeryTeam.create(surgery, doctor, request.getRole(), request.getNotes());
        return surgeryStore.saveSurgeryTeam(team);
    }

    @Transactional
    public void removeTeamMember(Long teamId) {
        surgeryStore.deleteSurgeryTeam(teamId);
    }

    // PreOpChecklist operations
    public List<PreOpChecklist> getChecklist(Long surgeryId) {
        return surgeryStore.findPreOpChecklistsBySurgery(surgeryId);
    }

    @Transactional
    public PreOpChecklist addChecklistItem(Long surgeryId, String item) {
        Surgery surgery = get(surgeryId);
        PreOpChecklist checklist = PreOpChecklist.create(surgery, item);
        return surgeryStore.savePreOpChecklist(checklist);
    }

    @Transactional
    public PreOpChecklist checkItem(Long checklistId, String checkedBy) {
        PreOpChecklist checklist = surgeryStore.findPreOpChecklistById(checklistId)
                .orElseThrow(() -> new CommonException(ErrorCode.DATA_NOT_FOUND, "체크리스트 항목을 찾을 수 없습니다."));
        checklist.check(checkedBy);
        return surgeryStore.savePreOpChecklist(checklist);
    }

    @Transactional
    public void removeChecklistItem(Long checklistId) {
        surgeryStore.deletePreOpChecklist(checklistId);
    }
}

