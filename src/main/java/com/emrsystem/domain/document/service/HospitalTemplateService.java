package com.emrsystem.domain.document.service;

import com.emrsystem.domain.document.entity.DocumentTemplate;
import com.emrsystem.domain.document.entity.HospitalTemplate;
import com.emrsystem.domain.document.store.DocumentTemplateStore;
import com.emrsystem.domain.hospital.entity.Hospital;
import com.emrsystem.domain.hospital.store.HospitalStore;
import com.emrsystem.global.common.enums.ErrorCode;
import com.emrsystem.global.common.exception.CommonException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class HospitalTemplateService {

    private final DocumentTemplateStore templateStore;
    private final HospitalStore hospitalStore;

    public List<HospitalTemplate> getTemplatesByHospital(Long hospitalId) {
        return templateStore.findHospitalTemplatesByHospitalId(hospitalId);
    }

    public List<HospitalTemplate> getSharedTemplates() {
        return templateStore.findSharedTemplates();
    }

    public HospitalTemplate getHospitalTemplate(Long templateId, Long hospitalId) {
        return templateStore.findHospitalTemplateByTemplateAndHospital(templateId, hospitalId)
                .orElseThrow(() -> new CommonException(ErrorCode.DATA_NOT_FOUND,
                        "병원 템플릿을 찾을 수 없습니다."));
    }

    @Transactional
    public HospitalTemplate assignTemplateToHospital(Long templateId, Long hospitalId, boolean shared, String notes) {
        DocumentTemplate template = templateStore.findTemplateById(templateId)
                .orElseThrow(() -> new CommonException(ErrorCode.DATA_NOT_FOUND, "템플릿을 찾을 수 없습니다."));
        Hospital hospital = hospitalStore.findById(hospitalId)
                .orElseThrow(() -> new CommonException(ErrorCode.DATA_NOT_FOUND, "병원을 찾을 수 없습니다."));

        // 이미 할당되어 있는지 확인
        var existing = templateStore.findHospitalTemplateByTemplateAndHospital(templateId, hospitalId);
        if (existing.isPresent()) {
            HospitalTemplate hospitalTemplate = existing.get();
            hospitalTemplate.update(shared, notes);
            return templateStore.saveHospitalTemplate(hospitalTemplate);
        }

        HospitalTemplate hospitalTemplate = HospitalTemplate.create(template, hospital, shared, notes);
        return templateStore.saveHospitalTemplate(hospitalTemplate);
    }

    @Transactional
    public void shareTemplate(Long templateId, Long hospitalId) {
        HospitalTemplate hospitalTemplate = getHospitalTemplate(templateId, hospitalId);
        hospitalTemplate.share();
        templateStore.saveHospitalTemplate(hospitalTemplate);
    }

    @Transactional
    public void unshareTemplate(Long templateId, Long hospitalId) {
        HospitalTemplate hospitalTemplate = getHospitalTemplate(templateId, hospitalId);
        hospitalTemplate.unshare();
        templateStore.saveHospitalTemplate(hospitalTemplate);
    }

    @Transactional
    public void removeTemplateFromHospital(Long templateId, Long hospitalId) {
        HospitalTemplate hospitalTemplate = getHospitalTemplate(templateId, hospitalId);
        hospitalTemplate.deactivate();
        templateStore.saveHospitalTemplate(hospitalTemplate);
    }
}
