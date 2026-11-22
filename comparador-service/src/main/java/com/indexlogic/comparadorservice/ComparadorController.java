package com.indexlogic.comparadorservice;

import com.indexlogic.comparadorservice.dto.ComparacionRequestDto;
import com.indexlogic.comparadorservice.dto.ResultadoComparacionDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comparador")
@CrossOrigin(origins = "http://localhost:4200")
public class ComparadorController {

    private final ComparadorService comparadorService;

    public ComparadorController(ComparadorService comparadorService) {
        this.comparadorService = comparadorService;
    }

    @PostMapping("/ejecutar")
    public ResponseEntity<List<ResultadoComparacionDto>> comparar(@RequestBody ComparacionRequestDto request) {
        List<ResultadoComparacionDto> comparativa = comparadorService.compararSimulaciones(request);
        return ResponseEntity.ok(comparativa);
    }
}