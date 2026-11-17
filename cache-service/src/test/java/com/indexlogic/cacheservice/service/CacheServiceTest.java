package com.indexlogic.cacheservice.service;

import com.indexlogic.cacheservice.jna.EstadisticasCache;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SimulacionServiceTest {

    private final SimulacionService simulacionService = new SimulacionService();

    @Test
    @DisplayName("Debe retornar estadísticas ejecutando la simulación nativa vía JNA")
    void testEjecutarSimulacion() {
        // Arrange: Preparamos una traza de prueba y los parámetros de la caché
        long[] direcciones = { 0x00L, 0x01L, 0x04L, 0x00L, 0x08L, 0x01L };
        int tamLinea = 4;
        int numConjuntos = 4;
        int vias = 1;
        int politica = 0; // 0 para LRU, 1 para FIFO

        // Act
        EstadisticasCache resultado = simulacionService.ejecutarSimulacion(
                direcciones, tamLinea, numConjuntos, vias, politica
        );

        // Assert
        assertNotNull(resultado, "El objeto de estadísticas no debe ser nulo");
        assertTrue(resultado.aciertos.longValue() >= 0, "Los aciertos deben ser mayores o iguales a 0");
        assertTrue(resultado.fallos.longValue() >= 0, "Los fallos deben ser mayores o iguales a 0");
        assertTrue(resultado.reemplazos.longValue() >= 0, "Los reemplazos deben ser mayores o iguales a 0");

// Verificamos el invariante fundamental del sistema: Aciertos + Fallos = Total de accesos (6)
        assertEquals(direcciones.length, resultado.aciertos.longValue() + resultado.fallos.longValue(),
                "La suma de aciertos y fallos debe ser igual al total de accesos de la traza");
    }
}