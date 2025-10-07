package com.emrsystem.core.config;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableBatchProcessing
public class BatchConfig {
    // Spring Batch 기본 설정
    // Job, Step, ItemReader, ItemProcessor, ItemWriter는 각 도메인별로 구현
}
