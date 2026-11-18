
package com.indexlogic.comparadorservice;

import com.indexlogic.comparadorservice.dto.TrazaRequestDto;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class TrazaServiceClient {

    private final RestTemplate restTemplate = new RestTemplate();

    private final String TRAZA_SERVICE_URL =
            "http://localhost:8082/api/trazas";

    public Object guardarTraza(TrazaRequestDto traza) {

        return restTemplate.postForObject(
                TRAZA_SERVICE_URL,
                traza,
                Object.class
        );
    }
}
