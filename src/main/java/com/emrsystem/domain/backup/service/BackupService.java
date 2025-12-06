package com.emrsystem.domain.backup.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BackupService {

    private static final String BACKUP_DIR = "backups";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");

    @Transactional
    public String createBackup() throws IOException {
        String backupFileName = "backup_" + LocalDateTime.now().format(DATE_FORMATTER) + ".zip";
        Path backupDir = Paths.get(BACKUP_DIR);
        
        if (!Files.exists(backupDir)) {
            Files.createDirectories(backupDir);
        }
        
        Path backupPath = backupDir.resolve(backupFileName);
        
        try (ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(backupPath.toFile()))) {
            // 데이터베이스 백업 (실제 구현 시 mysqldump 또는 다른 DB 백업 도구 사용)
            ZipEntry dbEntry = new ZipEntry("database_backup.sql");
            zos.putNextEntry(dbEntry);
            zos.write("-- Database backup placeholder\n".getBytes());
            zos.closeEntry();
            
            // 설정 파일 백업
            ZipEntry configEntry = new ZipEntry("config/application.properties");
            zos.putNextEntry(configEntry);
            zos.write("-- Configuration backup placeholder\n".getBytes());
            zos.closeEntry();
            
            log.info("Backup created: {}", backupPath);
        }
        
        return backupPath.toString();
    }

    public File getBackupFile(String fileName) {
        Path backupPath = Paths.get(BACKUP_DIR, fileName);
        File file = backupPath.toFile();
        if (!file.exists()) {
            throw new RuntimeException("Backup file not found: " + fileName);
        }
        return file;
    }

    public void deleteBackup(String fileName) {
        Path backupPath = Paths.get(BACKUP_DIR, fileName);
        try {
            Files.deleteIfExists(backupPath);
            log.info("Backup deleted: {}", backupPath);
        } catch (IOException e) {
            log.error("Failed to delete backup: {}", backupPath, e);
            throw new RuntimeException("Failed to delete backup file", e);
        }
    }
}

