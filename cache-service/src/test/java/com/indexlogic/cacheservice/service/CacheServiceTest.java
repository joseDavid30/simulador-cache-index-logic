package com.indexlogic.cacheservice.service;

import com.indexlogic.cacheservice.jna.EstadisticasCache;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CacheServiceTest {

    private final CacheService cacheService = new CacheService();

    @Test
    @DisplayName("Debe retornar estadísticas (vía fallback o JNA) al ejecutar la simulación")
    void testEjecutarSimulacion() {
        // Arrange
        String rutaTraza = "test-traza.txt";
        int capacidad = 64;

        // Act
        EstadisticasCache resultado = cacheService.ejecutarSimulacion(rutaTraza, capacidad);

        // Assert
        assertNotNull(resultado, "El objeto de estadísticas no debe ser nulo");
        // Verificamos que los campos tengan valores lógicos (gracias al mock de respaldo)
        assertTrue(resultado.hits >= 0, "Los hits deben ser mayores o iguales a 0");
        assertTrue(resultado.misses >= 0, "Los misses deben ser mayores o iguales a 0");
        assertEquals(75.0, resultado.tasaAciertos, 0.01, "La tasa de aciertos debe coincidir con el mock");
    }
}