package com.indexlogic.comparadorservice;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class CacheServiceClient {

    private final RestTemplate restTemplate = new RestTemplate();
    
    // URL del endpoint de tu cache-service (asegúrate de que apunte al puerto 8081 o el que use tu cache-service)
    private final String CACHE_SERVICE_URL = "http://localhost:8081/api/cache/simular";

    public Object simular(Object requestBody) {
        return restTemplate.postForObject(CACHE_SERVICE_URL, requestBody, Object.class);
    }
}