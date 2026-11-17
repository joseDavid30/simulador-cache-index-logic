package com.indexlogic.trazaservice.service;

import com.indexlogic.trazaservice.entity.TrazaEntity;
import com.indexlogic.trazaservice.repository.TrazaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TrazaService {

    private final TrazaRepository trazaRepository;

    public TrazaService(TrazaRepository trazaRepository) {
        this.trazaRepository = trazaRepository;
    }

    // Guarda una nueva traza de simulación en PostgreSQL
    public TrazaEntity guardarTraza(TrazaEntity traza) {
        return trazaRepository.save(traza);
    }

    // Devuelve el listado completo de simulaciones guardadas históricamente
    public List<TrazaEntity> obtenerTodas() {
        return trazaRepository.findAll();
    }
}