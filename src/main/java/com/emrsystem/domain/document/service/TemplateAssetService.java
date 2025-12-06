package com.emrsystem.domain.document.service;

import com.emrsystem.domain.document.entity.DocumentTemplate;
import com.emrsystem.domain.document.entity.TemplateAsset;
import com.emrsystem.domain.document.store.DocumentTemplateStore;
import com.emrsystem.global.common.enums.ErrorCode;
import com.emrsystem.global.common.exception.CommonException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TemplateAssetService {

    private final DocumentTemplateStore templateStore;
    
    @Value("${app.document.template.asset.upload-dir:uploads/template-assets/}")
    private String uploadDir;

    public List<TemplateAsset> getAssetsByTemplate(Long templateId) {
        // 템플릿 존재 여부만 확인
        if (!templateStore.findTemplateById(templateId).isPresent()) {
            throw new CommonException(ErrorCode.DATA_NOT_FOUND, "템플릿을 찾을 수 없습니다.");
        }
        return templateStore.findActiveAssetsByTemplateId(templateId);
    }

    public List<TemplateAsset> getPublicAssets() {
        return templateStore.findPublicAssets();
    }

    public List<TemplateAsset> getAssetsByType(String assetType) {
        return templateStore.findAssetsByType(assetType);
    }

    public TemplateAsset getAsset(Long assetId) {
        return templateStore.findAssetById(assetId)
                .orElseThrow(() -> new CommonException(ErrorCode.DATA_NOT_FOUND, "에셋을 찾을 수 없습니다."));
    }

    @Transactional
    public TemplateAsset uploadAsset(Long templateId, String assetName, String assetType,
                                     MultipartFile file, String description) throws IOException {
        DocumentTemplate template = null;
        if (templateId != null) {
            template = templateStore.findTemplateById(templateId)
                    .orElseThrow(() -> new CommonException(ErrorCode.DATA_NOT_FOUND, "템플릿을 찾을 수 없습니다."));
        }

        // 파일 저장
        String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }
        Path filePath = uploadPath.resolve(fileName);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        TemplateAsset asset = TemplateAsset.create(
                template,
                assetName != null ? assetName : file.getOriginalFilename(),
                assetType,
                filePath.toString(),
                file.getContentType(),
                file.getSize(),
                description
        );

        return templateStore.saveAsset(asset);
    }

    @Transactional
    public TemplateAsset updateAsset(Long assetId, String assetName, String description) {
        TemplateAsset asset = getAsset(assetId);
        asset.update(assetName, description);
        return templateStore.saveAsset(asset);
    }

    @Transactional
    public void deleteAsset(Long assetId) throws IOException {
        TemplateAsset asset = getAsset(assetId);
        
        // 파일 삭제
        if (asset.getFilePath() != null) {
            Path filePath = Paths.get(asset.getFilePath());
            if (Files.exists(filePath)) {
                Files.delete(filePath);
            }
        }

        templateStore.deleteAsset(assetId);
    }
}
