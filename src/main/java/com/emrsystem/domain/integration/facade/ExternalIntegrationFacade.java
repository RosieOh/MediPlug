package com.emrsystem.domain.integration.facade;

import com.emrsystem.domain.integration.response.IntegrationResponses;
import com.emrsystem.domain.integration.service.ExternalIntegrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ExternalIntegrationFacade {

    private final ExternalIntegrationService externalIntegrationService;

    public IntegrationResponses.IntegrationStatus getIntegrationStatus(String systemType) {
        return externalIntegrationService.getIntegrationStatus(systemType);
    }

    public void syncWithExternalSystem(String systemType) {
        externalIntegrationService.syncWithExternalSystem(systemType);
    }
}

