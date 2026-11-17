package com.indexlogic.comparadorservice.runner;

import com.indexlogic.comparadorservice.ComparadorService;
import com.indexlogic.comparadorservice.dto.ComparacionRequestDto;
import com.indexlogic.comparadorservice.dto.ConfiguracionCacheDto;
import com.indexlogic.comparadorservice.dto.ResultadoComparacionDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class FlujoCompletoRunner implements CommandLineRunner {

    @Autowired
    private ComparadorService comparadorService;

    @Override
    public void run(String... args) throws Exception {
        // Descomenta la siguiente línea si deseas que la prueba corra sola al iniciar
        // if (true) return;

        System.out.println("\n=========================================================");
        System.out.println("     INICIANDO PRUEBA DE FLUJO COMPLETO (ORQUESTADOR)    ");
        System.out.println("=========================================================");

        // 1. Cargar el archivo de traza de prueba
// En lugar de Paths.get(...), puedes cargarlo así de forma segura:
        org.springframework.core.io.Resource resource = new org.springframework.core.io.ClassPathResource("trazas/traza_prueba1.txt");
        List<String> direcciones = java.nio.file.Files.readAllLines(resource.getFile().toPath()).stream()
                .map(String::trim)
                .filter(s -> !s.isEmpty() && !s.startsWith("#"))
                .collect(java.util.stream.Collectors.toList());

        System.out.println("Traza leída con éxito. Total de direcciones: " + direcciones.size());

        // 2. Definir la configuración usando String para la política ("LRU" o "FIFO")
        ConfiguracionCacheDto config1 = new ConfiguracionCacheDto();
        config1.setTamLinea(4);
        config1.setNumConjuntos(4);
        config1.setVias(2);
        config1.setPolitica(0);

        ComparacionRequestDto request = new ComparacionRequestDto();
        request.setDirecciones(direcciones);
        request.setConfiguraciones(List.of(config1));

        // 3. Ejecutar a través del servicio orquestador
        try {
            System.out.println("Enviando solicitud desde comparador-service hacia cache-service...");

            List<ResultadoComparacionDto> resultados = comparadorService.compararSimulaciones(request);

            System.out.println("\n---------------------------------------------------------");
            System.out.println("                 RESULTADOS OBTENIDOS                    ");
            System.out.println("---------------------------------------------------------");

            for (int i = 0; i < resultados.size(); i++) {
                ResultadoComparacionDto res = resultados.get(i);
                System.out.println("Configuración #" + (i + 1));

                // Imprimimos la configuración asociada que viene en el resultado
                if (res.getConfiguracion() != null) {
                    System.out.println("  - Línea: " + res.getConfiguracion().getTamLinea() +
                            " | Conjuntos: " + res.getConfiguracion().getNumConjuntos() +
                            " | Vías: " + res.getConfiguracion().getVias() +
                            " | Política: " + res.getConfiguracion().getPolitica());
                }

                // Como 'estadisticas' es un Object, imprimimos el objeto contenedor o inspeccionamos su contenido
                System.out.println("  - Estadísticas brutas: " + res.getEstadisticas());
            }
            System.out.println("=========================================================\n");

        } catch (Exception e) {
            System.err.println("Error en el flujo: ¿Están encendidos cache-service (8081) y traza-service (8082)?");
            e.printStackTrace();
        }
    }
}