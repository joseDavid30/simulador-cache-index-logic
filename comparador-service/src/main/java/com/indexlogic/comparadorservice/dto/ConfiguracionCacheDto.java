package com.indexlogic.comparadorservice.dto;

public class ConfiguracionCacheDto {

    private int tamLinea;
    private int numConjuntos;
    private int vias;
    private String politica;

    public int getTamLinea() {
        return tamLinea;
    }

    public void setTamLinea(int tamLinea) {
        this.tamLinea = tamLinea;
    }

    public int getNumConjuntos() {
        return numConjuntos;
    }

    public void setNumConjuntos(int numConjuntos) {
        this.numConjuntos = numConjuntos;
    }

    public int getVias() {
        return vias;
    }

    public void setVias(int vias) {
        this.vias = vias;
    }

    public String getPolitica() {
        return politica;
    }

    public void setPolitica(String politica) {
        this.politica = politica;
    }
}
