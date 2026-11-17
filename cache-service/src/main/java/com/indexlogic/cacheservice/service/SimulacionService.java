package com.indexlogic.cacheservice.service;

import com.indexlogic.cacheservice.jna.EstadisticasCache;
import com.indexlogic.cacheservice.jna.LibCache;
import org.springframework.stereotype.Service;

@Service
public class SimulacionService {

    public EstadisticasCache ejecutarSimulacion(long[] direcciones, int tamLinea, int numConjuntos, int vias, int politica) {
        EstadisticasCache salida = new EstadisticasCache();

        int codigoRetorno = LibCache.INSTANCE.cache_simular(
                direcciones,
                direcciones.length,
                tamLinea,
                numConjuntos,
                vias,
                politica,
                salida
        );

        if (codigoRetorno < 0) {
            throw new IllegalArgumentException("Configuración de caché inválida o error en la simulación nativa (Código: " + codigoRetorno + ")");
        }

        return salida;
    }
}