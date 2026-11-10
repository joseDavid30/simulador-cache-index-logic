package com.indexlogic.cacheservice.service;

import com.indexlogic.cacheservice.jna.CacheLibrary;
import com.indexlogic.cacheservice.jna.EstadisticasCache;
import org.springframework.stereotype.Service;

@Service
public class CacheService {

    public EstadisticasCache ejecutarSimulacion(String trazaRuta, int capacidad) {
        try {
            // Intenta invocar la función real en la librería nativa en C mediante JNA
            return CacheLibrary.INSTANCE.cache_simular(trazaRuta, capacidad);
        } catch (UnsatisfiedLinkError e) {
            // Fallback temporal mientras Juan integra la librería compilada en el entorno
            System.err.println("Aviso: No se encontró la librería nativa. Usando datos de prueba (Mock).");
            EstadisticasCache mock = new EstadisticasCache();
            mock.hits = 150;
            mock.misses = 50;
            mock.tiempoTotal = 1200; // en milisegundos o microsegundos
            mock.tasaAciertos = 75.0;
            return mock;
        }
    }
}