package com.indexlogic.cacheservice.jna;

import com.sun.jna.Library;
import com.sun.jna.Native;

public interface CacheLibrary extends Library {
    // Carga la librería compartida 'libcache.so' (o el binario correspondiente en el sistema)
    CacheLibrary INSTANCE = Native.load("cache", CacheLibrary.class);

    // Firma exacta de la función implementada en C
    EstadisticasCache cache_simular(String trazaRuta, int capacidad);
}