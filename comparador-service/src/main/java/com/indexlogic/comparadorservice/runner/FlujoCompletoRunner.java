package com.indexlogic.comparadorservice.runner;

import com.indexlogic.comparadorservice.ComparadorService;
import com.indexlogic.comparadorservice.dto.ComparacionRequestDto;
import com.indexlogic.comparadorservice.dto.ConfiguracionCacheDto;
import com.indexlogic.comparadorservice.dto.ResultadoComparacionDto;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class FlujoCompletoRunner implements CommandLineRunner {

    private final ComparadorService comparadorService;

    public FlujoCompletoRunner(ComparadorService comparadorService) {
        this.comparadorService = comparadorService;
    }

    @Override
    public void run(String... args) throws Exception {
        // Descomenta la siguiente línea si deseas que la prueba corra sola al iniciar
        // if (true) return;

        System.out.println("\n=========================================================");
        System.out.println("     INICIANDO PRUEBA DE FLUJO COMPLETO (ORQUESTADOR)    ");
        System.out.println("=========================================================");

        // 1. Cargar el archivo de traza de prueba
// En lugar de Paths.get(...), puedes cargarlo así de forma segura:
        ClassPathResource resource = new ClassPathResource("trazas/traza_prueba1.txt");
        List<String> direcciones = Files.readAllLines(resource.getFile().toPath()).stream()
                .map(String::trim)
                .filter(s -> !s.isEmpty() && !s.startsWith("#"))
                .collect(Collectors.toList());

        System.out.println("Traza leída con éxito. Total de direcciones: " + direcciones.size());

        // 2. Definir la configuración usando int para la política ("0 LRU" o "1 FIFO")
        ConfiguracionCacheDto config1 = new ConfiguracionCacheDto();
        config1.setTamLinea(32);
        config1.setNumConjuntos(4);
        config1.setVias(4);
        config1.setPolitica(0);

        ComparacionRequestDto request = new ComparacionRequestDto();
        request.setDirecciones(direcciones);
        request.setConfiguraciones(List.of(config1));
        request.setTrazaRuta("trazas/traza_prueba1.txt");

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