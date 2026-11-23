package com.indexlogic.comparadorservice;

import com.indexlogic.comparadorservice.dto.TrazaRequestDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class TrazaServiceClient {

    private final RestTemplate restTemplate = new RestTemplate();

    private final String trazaServiceUrl;

    public TrazaServiceClient(
            @Value("${traza.service.url:http://localhost:8082/api/trazas}") String trazaServiceUrl
    ) {
        this.trazaServiceUrl = trazaServiceUrl;
    }

    public Object guardarTraza(TrazaRequestDto traza) {
        return restTemplate.postForObject(
                trazaServiceUrl,
                traza,
                Object.class
        );
    }
}