package com.indexlogic.comparadorservice.dto;

import java.util.List;

public class ComparacionRequestDto {

    private List<String> direcciones;
    private List<ConfiguracionCacheDto> configuraciones;

    public List<String> getDirecciones() {
        return direcciones;
    }

    public void setDirecciones(List<String> direcciones) {
        this.direcciones = direcciones;
    }

    public List<ConfiguracionCacheDto> getConfiguraciones() {
        return configuraciones;
    }

    public void setConfiguraciones(List<ConfiguracionCacheDto> configuraciones) {
        this.configuraciones = configuraciones;
    }

    private String trazaRuta;

    public String getTrazaRuta() {
        return trazaRuta;
    }

    public void setTrazaRuta(String trazaRuta) {
        this.trazaRuta = trazaRuta;
    }
}
