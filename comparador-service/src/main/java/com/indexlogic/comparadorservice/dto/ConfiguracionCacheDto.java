package com.indexlogic.comparadorservice.dto;

public class ConfiguracionCacheDto {

    private int tamLinea;
    private int numConjuntos;
    private int vias;
    private int politica; // <-- Cambiado de String a int

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

    // Métodos actualizados a int:
    public int getPolitica() {
        return politica;
    }

    public void setPolitica(int politica) {
        this.politica = politica;
    }
}