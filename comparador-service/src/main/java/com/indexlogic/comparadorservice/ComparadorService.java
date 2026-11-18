package com.indexlogic.comparadorservice;

import com.indexlogic.comparadorservice.dto.ComparacionRequestDto;
import com.indexlogic.comparadorservice.dto.ConfiguracionCacheDto;
import com.indexlogic.comparadorservice.dto.ResultadoComparacionDto;
import com.indexlogic.comparadorservice.dto.TrazaRequestDto;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ComparadorService {

    private final CacheServiceClient cacheServiceClient;
    private final TrazaServiceClient trazaServiceClient;

    public ComparadorService(
            CacheServiceClient cacheServiceClient,
            TrazaServiceClient trazaServiceClient) {

        this.cacheServiceClient = cacheServiceClient;
        this.trazaServiceClient = trazaServiceClient;
    }

    public List<ResultadoComparacionDto> compararSimulaciones(
            ComparacionRequestDto request) {

        List<ResultadoComparacionDto> resultados = new ArrayList<>();

        long[] direccionesLong = request.getDirecciones().stream()
                .mapToLong(Long::decode)
                .toArray();

        for (ConfiguracionCacheDto config : request.getConfiguraciones()) {

            Map<String, Object> simRequest = new HashMap<>();

            simRequest.put("direcciones", direccionesLong);
            simRequest.put("tamLinea", config.getTamLinea());
            simRequest.put("numConjuntos", config.getNumConjuntos());
            simRequest.put("vias", config.getVias());
            simRequest.put("politica", config.getPolitica());

            // 1. Ejecutar simulación en cache-service
            Object stats = cacheServiceClient.simular(simRequest);

            // 2. Guardar resultados en el DTO de respuesta
            ResultadoComparacionDto resDto =
                    new ResultadoComparacionDto();

            resDto.setConfiguracion(config);
            resDto.setEstadisticas(stats);

            resultados.add(resDto);

            // 3. Preparar información para traza-service
            if (!(stats instanceof Map)) {
                throw new IllegalStateException(
                        "Las estadísticas recibidas no tienen el formato esperado: "
                                + stats
                );
            }

            Map<?, ?> estadisticas = (Map<?, ?>) stats;

            long aciertos = obtenerNumero(estadisticas, "aciertos");
            long fallos = obtenerNumero(estadisticas, "fallos");
            long reemplazos = obtenerNumero(estadisticas, "reemplazos");

            TrazaRequestDto nuevaTraza = new TrazaRequestDto();

            nuevaTraza.setTrazaRuta(request.getTrazaRuta());

            // Configuración de caché
            int capacidad = Math.multiplyExact(
                    Math.multiplyExact(config.getTamLinea(),
                            config.getNumConjuntos()),
                    config.getVias()
            );

            nuevaTraza.setCapacidad(capacidad);
            nuevaTraza.setTamLinea(config.getTamLinea());
            nuevaTraza.setNumConjuntos(config.getNumConjuntos());
            nuevaTraza.setVias(config.getVias());
            nuevaTraza.setPolitica(config.getPolitica());

            // Estadísticas de simulación
            nuevaTraza.setAciertos(aciertos);
            nuevaTraza.setFallos(fallos);
            nuevaTraza.setReemplazos(reemplazos);

            // Correspondencia provisional con las columnas antiguas
            nuevaTraza.setHits(Math.toIntExact(aciertos));
            nuevaTraza.setMisses(Math.toIntExact(fallos));

            // Tiempo no proporcionado todavía por cache-service
            nuevaTraza.setTiempoTotal(0);

            long total = aciertos + fallos;

            double tasa = total > 0
                    ? (aciertos * 100.0) / total
                    : 0;

            nuevaTraza.setTasaAciertos(tasa);

            // 4. Enviar resultados a traza-service
            System.out.println(
                    "Enviando resultados hacia traza-service..."
            );

            trazaServiceClient.guardarTraza(nuevaTraza);

            System.out.println(
                    "Resultados guardados correctamente en traza-service."
            );
        }

        return resultados;
    }

    private long obtenerNumero(Map<?, ?> datos, String campo) {

        Object valor = datos.get(campo);

        if (valor instanceof Number) {
            return ((Number) valor).longValue();
        }

        throw new IllegalStateException(
                "No se encontró una estadística numérica llamada: " + campo
        );
    }
}