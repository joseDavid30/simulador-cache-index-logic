package com.indexlogic.cacheservice.controller;

import com.indexlogic.cacheservice.dto.SimulacionRequestDto;
import com.indexlogic.cacheservice.jna.EstadisticasCache;
import com.indexlogic.cacheservice.service.CacheService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cache")
@CrossOrigin(origins = "*") // Permite llamadas desde Angular (CORS abierto para desarrollo)
public class CacheController {

    private final CacheService cacheService;

    public CacheController(CacheService cacheService) {
        this.cacheService = cacheService;
    }

    @PostMapping("/simular")
    public ResponseEntity<EstadisticasCache> simularCache(@RequestBody SimulacionRequestDto request) {
        EstadisticasCache estadisticas = cacheService.ejecutarSimulacion(
                request.getTrazaRuta(),
                request.getCapacidad()
        );
        return ResponseEntity.ok(estadisticas);
    }
}