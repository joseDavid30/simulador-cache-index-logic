package com.indexlogic.cacheservice.controller;

import com.indexlogic.cacheservice.dto.SimulacionRequestDto;
import com.indexlogic.cacheservice.jna.EstadisticasCache;
import com.indexlogic.cacheservice.service.SimulacionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cache")
@CrossOrigin(origins = "*") // Permite llamadas desde Angular (CORS abierto para desarrollo)
public class CacheController {

    private final SimulacionService simulacionService;

    public CacheController(SimulacionService simulacionService) {
        this.simulacionService = simulacionService;
    }

    @PostMapping("/simular")
    public ResponseEntity<EstadisticasCache> simularCache(@RequestBody SimulacionRequestDto request) {
        EstadisticasCache estadisticas = simulacionService.ejecutarSimulacion(
                request.getDirecciones(),
                request.getTamLinea(),
                request.getNumConjuntos(),
                request.getVias(),
                request.getPolitica()
        );
        return ResponseEntity.ok(estadisticas);
    }
}