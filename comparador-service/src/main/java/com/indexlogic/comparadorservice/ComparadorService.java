package com.indexlogic.comparadorservice;

import com.indexlogic.comparadorservice.dto.ComparacionRequestDto;
import com.indexlogic.comparadorservice.dto.ConfiguracionCacheDto;
import com.indexlogic.comparadorservice.dto.ResultadoComparacionDto;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ComparadorService {

    private final CacheServiceClient cacheServiceClient;

    public ComparadorService(CacheServiceClient cacheServiceClient) {
        this.cacheServiceClient = cacheServiceClient;
    }

    public List<ResultadoComparacionDto> compararSimulaciones(ComparacionRequestDto request) {
        List<ResultadoComparacionDto> resultados = new ArrayList<>();

        // 1. Convertimos la lista de strings hexadecimales ("0x00", "0x04", etc.) a un arreglo de tipo long[]
        long[] direccionesLong = request.getDirecciones().stream()
                .mapToLong(Long::decode) // Long.decode interpreta los prefijos "0x" correctamente
                .toArray();

        for (ConfiguracionCacheDto config : request.getConfiguraciones()) {
            // Mapeamos los datos limpios para enviarlos al cache-service
            Map<String, Object> simRequest = new HashMap<>();
            simRequest.put("direcciones", direccionesLong); // <- Mandamos el arreglo numérico
            simRequest.put("tamLinea", config.getTamLinea());
            simRequest.put("numConjuntos", config.getNumConjuntos());
            simRequest.put("vias", config.getVias());
            simRequest.put("politica", config.getPolitica());

            // Invocamos al cache-service mediante el cliente HTTP
            Object stats = cacheServiceClient.simular(simRequest);

            // Empaquetamos la configuración junto con el resultado obtenido
            ResultadoComparacionDto resDto = new ResultadoComparacionDto();
            resDto.setConfiguracion(config);
            resDto.setEstadisticas(stats);

            resultados.add(resDto);
        }

        return resultados;
    }
}