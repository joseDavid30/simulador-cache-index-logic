package com.indexlogic.comparadorservice;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class CacheServiceClient {

    private final RestTemplate restTemplate = new RestTemplate();

    private final String cacheServiceUrl;

    public CacheServiceClient(
            @Value("${cache.service.url:http://localhost:8081/api/cache/simular}") String cacheServiceUrl
    ) {
        this.cacheServiceUrl = cacheServiceUrl;
    }

    public Object simular(Object requestBody) {
        return restTemplate.postForObject(cacheServiceUrl, requestBody, Object.class);
    }
}