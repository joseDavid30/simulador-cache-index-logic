
package com.indexlogic.trazaservice.controller;

import com.indexlogic.trazaservice.entity.TrazaEntity;
import com.indexlogic.trazaservice.service.TrazaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/trazas")
@CrossOrigin(origins = "*")
public class TrazaController {

    private final TrazaService trazaService;

    public TrazaController(TrazaService trazaService) {
        this.trazaService = trazaService;
    }

    @PostMapping
    public ResponseEntity<TrazaEntity> crearTraza(
            @RequestBody TrazaEntity traza) {

        TrazaEntity nuevaTraza = trazaService.guardarTraza(traza);

        return ResponseEntity.status(HttpStatus.CREATED).body(nuevaTraza);
    }

    @GetMapping
    public ResponseEntity<List<TrazaEntity>> listarTrazas() {
        return ResponseEntity.ok(trazaService.obtenerTodas());
    }
}
