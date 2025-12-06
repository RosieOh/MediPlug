package com.emrsystem.domain.backup.controller;

import com.emrsystem.domain.backup.service.BackupService;
import com.emrsystem.domain.user.enums.RoleType;
import com.emrsystem.global.common.controller.BaseController;
import com.emrsystem.global.common.dto.ApiResponse;
import com.emrsystem.global.security.authorization.RequireRole;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Tag(name = "Backup & Recovery", description = "데이터 백업 및 복구 API")
@RestController
@RequestMapping("/api/backup")
@RequiredArgsConstructor
public class BackupController extends BaseController {

    private final BackupService backupService;

    @PostMapping("/create")
    @Operation(summary = "백업 생성", description = "시스템 데이터를 백업합니다.")
    @RequireRole(RoleType.MASTER_ADMIN)
    public ResponseEntity<ApiResponse<String>> createBackup() {
        try {
            String backupPath = backupService.createBackup();
            return ok("백업이 생성되었습니다: " + backupPath);
        } catch (IOException e) {
            throw new com.emrsystem.global.common.exception.CommonException(
                    com.emrsystem.global.common.enums.ErrorCode.INTERNAL_SERVER_ERROR, 
                    "백업 생성에 실패했습니다: " + e.getMessage());
        }
    }

    @GetMapping("/download/{fileName}")
    @Operation(summary = "백업 파일 다운로드", description = "백업 파일을 다운로드합니다.")
    @RequireRole(RoleType.MASTER_ADMIN)
    public ResponseEntity<Resource> downloadBackup(
            @Parameter(description = "백업 파일명") @PathVariable String fileName) {
        Resource resource = new FileSystemResource(backupService.getBackupFile(fileName));
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                .body(resource);
    }

    @DeleteMapping("/{fileName}")
    @Operation(summary = "백업 파일 삭제", description = "백업 파일을 삭제합니다.")
    @RequireRole(RoleType.MASTER_ADMIN)
    public ResponseEntity<ApiResponse<String>> deleteBackup(
            @Parameter(description = "백업 파일명") @PathVariable String fileName) {
        backupService.deleteBackup(fileName);
        return ok("백업 파일이 삭제되었습니다.");
    }
}

