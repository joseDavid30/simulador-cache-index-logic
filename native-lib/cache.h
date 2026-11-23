#ifndef CACHE_H
#define CACHE_H

#include <stdint.h>

#ifdef _WIN32
  #define CACHE_API __declspec(dllexport)
#else
  #define CACHE_API
#endif

/*
 * Interfaz pública de la biblioteca nativa de simulación de caché (libcache).
 * Expone únicamente la estructura de resultados y la función invocada desde Java vía JNA.
 */

/* Estructura mapeada con EstadisticasCache.java en JNA (@FieldOrder) */
typedef struct {
    long aciertos;
    long fallos;
    long reemplazos;
} EstadisticasCache;

/*
 * Ejecuta la simulación de memoria caché asociativa por conjuntos sobre una traza.
 *
 * Parámetros:
 *   direcciones   -> Arreglo de direcciones de memoria de 64 bits (long[] en Java).
 *   n             -> Cantidad total de accesos en la traza (n > 0).
 *   tam_linea     -> Tamaño de bloque/línea en bytes (potencia de 2).
 *   num_conjuntos -> Cantidad de conjuntos de la caché (potencia de 2).
 *   vias          -> Asociatividad o líneas por conjunto (vias > 0).
 *   politica      -> Política de reemplazo: 0 = LRU, 1 = FIFO.
 *   salida        -> Puntero a EstadisticasCache donde C escribe aciertos, fallos y reemplazos.
 *
 * Retorno:
 *   0 si finalizó correctamente; código negativo (-1 a -7) si hubo error de parámetros o memoria.
 */
CACHE_API int cache_simular(const uint64_t *direcciones, int n,
                            int tam_linea, int num_conjuntos, int vias,
                            int politica, EstadisticasCache *salida);

#endif // CACHE_H