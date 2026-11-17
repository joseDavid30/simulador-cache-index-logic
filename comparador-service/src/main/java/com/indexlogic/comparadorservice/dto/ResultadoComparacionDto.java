package com.indexlogic.comparadorservice.dto;

public class ResultadoComparacionDto {

    private ConfiguracionCacheDto configuracion;
    private Object estadisticas;

    public ConfiguracionCacheDto getConfiguracion() {
        return configuracion;
    }

    public void setConfiguracion(ConfiguracionCacheDto configuracion) {
        this.configuracion = configuracion;
    }

    public Object getEstadisticas() {
        return estadisticas;
    }

    public void setEstadisticas(Object estadisticas) {
        this.estadisticas = estadisticas;
    }
}
