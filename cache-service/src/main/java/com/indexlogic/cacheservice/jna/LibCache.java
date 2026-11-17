package com.indexlogic.cacheservice.jna;

import com.sun.jna.Library;
import com.sun.jna.Native;

public interface LibCache extends Library {
    LibCache INSTANCE = Native.load("cache", LibCache.class);

    int cache_simular(long[] direcciones, int n,
                      int tamLinea, int numConjuntos, int vias,
                      int politica, EstadisticasCache salida);
}