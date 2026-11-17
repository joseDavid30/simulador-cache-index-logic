package com.indexlogic.cacheservice.service;

import com.indexlogic.cacheservice.jna.EstadisticasCache;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class SimulacionServiceTest {

    @Autowired
    private SimulacionService simulacionService;

    @Test
    @DisplayName("Debe leer traza desde un archivo .txt e imprimir reporte detallado en consola")
    void testSimulacionDesdeArchivoTxt() throws Exception {
        // 1. Cargamos el archivo de traza desde src/test/resources/trazas/traza_prueba1.txt
        Path rutaArchivo = Paths.get(getClass().getClassLoader().getResource("trazas/traza_prueba1.txt").toURI());
        List<String> lineas = Files.readAllLines(rutaArchivo);

        // 2. Convertimos cada línea del archivo de texto a un arreglo de tipo long[]
        long[] direcciones = lineas.stream()
                .map(String::trim)
                .filter(s -> !s.isEmpty() && !s.startsWith("#")) // Ignora líneas vacías o comentarios
                .mapToLong(Long::decode)
                .toArray();

        // 3. Definimos los parámetros de configuración de la caché
        int tamLinea = 4;       // Tamaño de la línea de caché
        int numConjuntos = 4;   // Número de conjuntos
        int vias = 2;           // Asociatividad (vías)
        int politica = 0;       // 0 = LRU, 1 = FIFO

        // 4. Ejecutamos la simulación llamando a la biblioteca en C a través de JNA
        EstadisticasCache resultado = simulacionService.ejecutarSimulacion(
                direcciones, tamLinea, numConjuntos, vias, politica
        );

        // 5. Imprimimos el informe detallado directamente en la consola
        System.out.println("\n=================================================");
        System.out.println("       INFORME DE SIMULACIÓN DESDE ARCHIVO       ");
        System.out.println("=================================================");
        System.out.println("Archivo de traza:    trazas/traza_prueba1.txt");
        System.out.println("Total de accesos:    " + direcciones.length);
        System.out.println("Configuración:       Línea=" + tamLinea + ", Conjuntos=" + numConjuntos +
                ", Vías=" + vias + ", Política=" + (politica == 0 ? "LRU" : "FIFO"));
        System.out.println("-------------------------------------------------");
        System.out.println("Aciertos (Hits):     " + resultado.aciertos.longValue());
        System.out.println("Fallos (Misses):     " + resultado.fallos.longValue());
        System.out.println("Reemplazos:          " + resultado.reemplazos.longValue());

        long totalCalculado = resultado.aciertos.longValue() + resultado.fallos.longValue();
        boolean invarianteOk = (direcciones.length == totalCalculado);
        System.out.println("Invariante (H+F==Total): " + (invarianteOk ? "CUMPLE (OK)" : "FALLA"));
        System.out.println("=================================================\n");

        // 6. Aserciones de validación final
        assertNotNull(resultado, "El objeto de estadísticas no debe ser nulo");
        assertTrue(invarianteOk, "La suma de aciertos y fallos debe ser igual al total de accesos de la traza");
    }
}