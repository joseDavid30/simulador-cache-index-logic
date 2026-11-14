#include <stdio.h>
#include <stdlib.h>

// 1. Estructura para devolverle a Java los 3 números que pide
typedef struct {
    long aciertos;
    long fallos;
    long reemplazos;
} EstadisticasCache;

// 2. Función auxiliar para saber si un número es potencia de 2 (ej: 4, 16, 64)
static int es_potencia_de_dos(int x) {
    return x > 0 && (x & (x - 1)) == 0;
}

// 3. Función auxiliar para contar bits (saber cuántos ceros/unos ocupa un número)
static int contar_bits(int x) {
    int bits = 0;
    while (x > 1) {
        x >>= 1;
        bits++;
    }
    return bits;
}

// 4. FUNCIÓN PRINCIPAL QUE LLAMA JAVA
int cache_simular(const unsigned long *direcciones, int n,
                  int tam_linea, int num_conjuntos, int vias,
                  int politica, EstadisticasCache *salida) {

    // --- REVISIÓN DE ERRORES (RN-05) ---
    // Si algún dato de la pantalla viene mal, cancelamos y devolvemos un número negativo.
    if (salida == NULL)                     return -6;
    if (direcciones == NULL || n <= 0)      return -4;
    if (!es_potencia_de_dos(tam_linea))     return -1;
    if (!es_potencia_de_dos(num_conjuntos)) return -2;
    if (vias <= 0)                          return -3;
    if (politica != 0 && politica != 1)     return -5; // 0 = LRU, 1 = FIFO

    // --- CÁLCULO DE MÁSCARAS DE BITS (RN-01) ---
    // Sacamos cuántos bits se usan para la posición dentro de la línea y el conjunto
    int bits_offset = contar_bits(tam_linea);
    int bits_indice = contar_bits(num_conjuntos);
    unsigned long mascara_indice = (unsigned long)(num_conjuntos - 1);

    // --- CREACIÓN DE LA CACHÉ EN MEMORIA ---
    // Total de "casilleros" disponibles para guardar datos
    size_t total_casilleros = (size_t)num_conjuntos * (size_t)vias;

    // Tres listas para saber qué hay en cada casillero:
    // - ocupado: 1 si hay un dato, 0 si está vacío.
    // - etiqueta: La "tarjeta de identidad" del dato guardado.
    // - turno: Número para saber cuál llegó primero o cuál se usó de último.
    unsigned char *ocupado = calloc(total_casilleros, sizeof(unsigned char));
    unsigned long *etiqueta_guardada = calloc(total_casilleros, sizeof(unsigned long));
    unsigned long *turno = calloc(total_casilleros, sizeof(unsigned long));

    if (!ocupado || !etiqueta_guardada || !turno) {
        free(ocupado); free(etiqueta_guardada); free(turno);
        return -7; // Error de memoria
    }

    long aciertos = 0;
    long fallos = 0;
    long reemplazos = 0;
    unsigned long reloj = 0; // Contador de tiempo para saber el orden

    // --- RECORRER LA LISTA DE DIRECCIONES (SIMULACIÓN) ---
    for (int i = 0; i < n; i++) {
        reloj++;
        unsigned long dir = direcciones[i];

        // Desarmamos la dirección usando solo desplazamientos de bits (>>) y máscaras (&)
        unsigned long conjunto = (dir >> bits_offset) & mascara_indice;
        unsigned long mi_etiqueta = dir >> (bits_offset + bits_indice);

        // Posición inicial del conjunto en nuestros arreglos
        size_t inicio_conjunto = (size_t)conjunto * (size_t)vias;
        int casillero_encontrado = -1;

        // BÚSQUEDA: Revisamos los casilleros de este conjunto a ver si el dato ya está
        for (int v = 0; v < vias; v++) {
            if (ocupado[inicio_conjunto + v] && etiqueta_guardada[inicio_conjunto + v] == mi_etiqueta) {
                casillero_encontrado = v;
                break;
            }
        }

        if (casillero_encontrado >= 0) {
            // ¡LO ENCONTRÓ! (Acierto)
            aciertos++;
            // Si la política es LRU (0), le actualizamos la hora a la actual para decir "se usó recién"
            if (politica == 0) {
                turno[inicio_conjunto + casillero_encontrado] = reloj;
            }
        } else {
            // NO ESTABA (Fallo)
            fallos++;
            int destino = -1;

            // 1. Buscamos si hay algún casillero vacío en este conjunto
            for (int v = 0; v < vias; v++) {
                if (!ocupado[inicio_conjunto + v]) {
                    destino = v;
                    break;
                }
            }

            // 2. Si no hay vacíos, tocó sacar a alguien (Reemplazo)
            if (destino < 0) {
                reemplazos++;
                // Buscamos cuál tiene el número de "turno" más viejo/pequeño
                unsigned long mas_viejo = turno[inicio_conjunto];
                destino = 0;

                for (int v = 1; v < vias; v++) {
                    if (turno[inicio_conjunto + v] < mas_viejo) {
                        mas_viejo = turno[inicio_conjunto + v];
                        destino = v;
                    }
                }
            }

            // Guardamos el nuevo dato en el casillero elegido
            ocupado[inicio_conjunto + destino] = 1;
            etiqueta_guardada[inicio_conjunto + destino] = mi_etiqueta;
            turno[inicio_conjunto + destino] = reloj;
        }
    }

    // Liberamos la memoria usada
    free(ocupado);
    free(etiqueta_guardada);
    free(turno);

    // Escribimos el resultado para devolvérselo a Java
    salida->aciertos = aciertos;
    salida->fallos = fallos;
    salida->reemplazos = reemplazos;

    return 0; // Todo salió bien
}