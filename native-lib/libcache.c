#include <stdio.h>
#include <stdint.h>

// Estructura que mapea con EstadisticasCache.java (usa long en C para
// que coincida con NativeLong de Java en Windows de 32/64 bits)
typedef struct {
    long aciertos;
    long fallos;
    long reemplazos;
} EstadisticasCache;

/*
 * Usamos uint64_t (o unsigned long long) para que cada dirección de memoria
 * ocupe 8 bytes exactos, igual que el long[] de Java.
 */
int cache_simular(const uint64_t *direcciones, int n,
                  int tam_linea, int num_conjuntos, int vias,
                  int politica, EstadisticasCache *salida) {

    // Validación de configuración (RN-05)
    if (n <= 0 || tam_linea <= 0 || num_conjuntos <= 0 || vias <= 0) {
        return -1;
    }

    if (direcciones == NULL || salida == NULL) {
        return -2;
    }

    // Stub temporal para validar el enlace JNA
    salida->aciertos = (long)n;
    salida->fallos = 0;
    salida->reemplazos = 0;

    return 0; // Éxito
}