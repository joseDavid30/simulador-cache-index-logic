package com.indexlogic.cacheservice.dto;

public class SimulacionRequestDto {
    private long[] direcciones;
    private int tamLinea;
    private int numConjuntos;
    private int vias;
    private int politica; // 0 = LRU, 1 = FIFO

    // Getters y Setters
    public long[] getDirecciones() {
        return direcciones;
    }

    public void setDirecciones(long[] direcciones) {
        this.direcciones = direcciones;
    }

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

    public int getPolitica() {
        return politica;
    }

    public void setPolitica(int politica) {
        this.politica = politica;
    }
}