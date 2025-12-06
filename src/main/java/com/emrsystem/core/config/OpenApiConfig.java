package com.emrsystem.core.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(title = "MediPlug HIS API", version = "v1", description = "Hospital Information System APIs"),
        servers = {@Server(url = "/", description = "Default Server")}
)
public class OpenApiConfig {

    @Bean
    public GroupedOpenApi hospitalApi() {
        return GroupedOpenApi.builder()
                .group("hospital")
                .pathsToMatch("/api/hospitals/**")
                .build();
    }

    @Bean
    public GroupedOpenApi departmentApi() {
        return GroupedOpenApi.builder()
                .group("department")
                .pathsToMatch("/api/departments/**")
                .build();
    }

    @Bean
    public GroupedOpenApi doctorApi() {
        return GroupedOpenApi.builder()
                .group("doctor")
                .pathsToMatch("/api/doctors/**")
                .build();
    }

    @Bean
    public GroupedOpenApi patientApi() {
        return GroupedOpenApi.builder()
                .group("patient")
                .pathsToMatch("/api/patients/**")
                .build();
    }

    @Bean
    public GroupedOpenApi appointmentApi() {
        return GroupedOpenApi.builder()
                .group("appointment")
                .pathsToMatch("/api/appointments/**")
                .build();
    }

    @Bean
    public GroupedOpenApi calendarApi() {
        return GroupedOpenApi.builder()
                .group("calendar")
                .pathsToMatch("/api/calendar/**")
                .build();
    }

    @Bean
    public GroupedOpenApi resourceApi() {
        return GroupedOpenApi.builder()
                .group("resource")
                .pathsToMatch("/api/resources/**")
                .build();
    }

    @Bean
    public GroupedOpenApi notificationApi() {
        return GroupedOpenApi.builder()
                .group("notification")
                .pathsToMatch("/api/notifications/**")
                .build();
    }

    @Bean
    public GroupedOpenApi emrApi() {
        return GroupedOpenApi.builder()
                .group("emr")
                .pathsToMatch("/api/emr/**")
                .build();
    }

    @Bean
    public GroupedOpenApi billingApi() {
        return GroupedOpenApi.builder()
                .group("billing")
                .pathsToMatch("/api/billing/**")
                .build();
    }

    @Bean
    public GroupedOpenApi pharmacyApi() {
        return GroupedOpenApi.builder()
                .group("pharmacy")
                .pathsToMatch("/api/pharmacy/**")
                .build();
    }

    @Bean
    public GroupedOpenApi bedApi() {
        return GroupedOpenApi.builder()
                .group("bed")
                .pathsToMatch("/api/beds/**")
                .build();
    }

    @Bean
    public GroupedOpenApi userApi() {
        return GroupedOpenApi.builder()
                .group("user")
                .pathsToMatch("/api/users/**")
                .build();
    }

    @Bean
    public GroupedOpenApi surgeryApi() {
        return GroupedOpenApi.builder()
                .group("surgery")
                .pathsToMatch("/api/surgeries/**")
                .build();
    }

    @Bean
    public GroupedOpenApi infectionApi() {
        return GroupedOpenApi.builder()
                .group("infection")
                .pathsToMatch("/api/infections/**")
                .build();
    }

    @Bean
    public GroupedOpenApi transfusionApi() {
        return GroupedOpenApi.builder()
                .group("transfusion")
                .pathsToMatch("/api/transfusions/**")
                .build();
    }

    @Bean
    public GroupedOpenApi vitalSignApi() {
        return GroupedOpenApi.builder()
                .group("vital-signs")
                .pathsToMatch("/api/vital-signs/**")
                .build();
    }

    @Bean
    public GroupedOpenApi inpatientApi() {
        return GroupedOpenApi.builder()
                .group("inpatient")
                .pathsToMatch("/api/inpatients/**")
                .build();
    }

    @Bean
    public GroupedOpenApi statisticsApi() {
        return GroupedOpenApi.builder()
                .group("statistics")
                .pathsToMatch("/api/statistics/**")
                .build();
    }

    @Bean
    public GroupedOpenApi reportApi() {
        return GroupedOpenApi.builder()
                .group("report")
                .pathsToMatch("/api/reports/**")
                .build();
    }

    @Bean
    public GroupedOpenApi prescriptionHistoryApi() {
        return GroupedOpenApi.builder()
                .group("prescription-history")
                .pathsToMatch("/api/prescription-history/**")
                .build();
    }

    @Bean
    public GroupedOpenApi appointmentEnhancementApi() {
        return GroupedOpenApi.builder()
                .group("appointment-enhancement")
                .pathsToMatch("/api/appointments/enhancement/**")
                .build();
    }

    @Bean
    public GroupedOpenApi inventoryApi() {
        return GroupedOpenApi.builder()
                .group("inventory")
                .pathsToMatch("/api/inventory/**")
                .build();
    }

    @Bean
    public GroupedOpenApi scheduleApi() {
        return GroupedOpenApi.builder()
                .group("schedule")
                .pathsToMatch("/api/schedules/**")
                .build();
    }

    @Bean
    public GroupedOpenApi performanceApi() {
        return GroupedOpenApi.builder()
                .group("performance")
                .pathsToMatch("/api/performance/**")
                .build();
    }

    @Bean
    public GroupedOpenApi patientPortalApi() {
        return GroupedOpenApi.builder()
                .group("patient-portal")
                .pathsToMatch("/api/patient-portal/**")
                .build();
    }

    @Bean
    public GroupedOpenApi integrationApi() {
        return GroupedOpenApi.builder()
                .group("integration")
                .pathsToMatch("/api/integration/**")
                .build();
    }

    @Bean
    public GroupedOpenApi securityApi() {
        return GroupedOpenApi.builder()
                .group("security")
                .pathsToMatch("/api/security/**")
                .build();
    }

    @Bean
    public GroupedOpenApi backupApi() {
        return GroupedOpenApi.builder()
                .group("backup")
                .pathsToMatch("/api/backup/**")
                .build();
    }

    @Bean
    public GroupedOpenApi documentTemplateApi() {
        return GroupedOpenApi.builder()
                .group("document-template")
                .pathsToMatch("/api/document-templates/**")
                .build();
    }

    @Bean
    public GroupedOpenApi allApi() {
        return GroupedOpenApi.builder()
                .group("all")
                .pathsToMatch("/api/**")
                .build();
    }
}


