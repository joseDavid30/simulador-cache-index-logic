
package com.indexlogic.comparadorservice.dto;

public class TrazaRequestDto {

    private String trazaRuta;

    private int capacidad;
    private int tamLinea;
    private int numConjuntos;
    private int vias;
    private int politica;

    private int hits;
    private int misses;

    private long aciertos;
    private long fallos;
    private long reemplazos;

    private long tiempoTotal;
    private double tasaAciertos;

    public String getTrazaRuta() {
        return trazaRuta;
    }

    public void setTrazaRuta(String trazaRuta) {
        this.trazaRuta = trazaRuta;
    }

    public int getCapacidad() {
        return capacidad;
    }

    public void setCapacidad(int capacidad) {
        this.capacidad = capacidad;
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

    public int getHits() {
        return hits;
    }

    public void setHits(int hits) {
        this.hits = hits;
    }

    public int getMisses() {
        return misses;
    }

    public void setMisses(int misses) {
        this.misses = misses;
    }

    public long getAciertos() {
        return aciertos;
    }

    public void setAciertos(long aciertos) {
        this.aciertos = aciertos;
    }

    public long getFallos() {
        return fallos;
    }

    public void setFallos(long fallos) {
        this.fallos = fallos;
    }

    public long getReemplazos() {
        return reemplazos;
    }

    public void setReemplazos(long reemplazos) {
        this.reemplazos = reemplazos;
    }

    public long getTiempoTotal() {
        return tiempoTotal;
    }

    public void setTiempoTotal(long tiempoTotal) {
        this.tiempoTotal = tiempoTotal;
    }

    public double getTasaAciertos() {
        return tasaAciertos;
    }

    public void setTasaAciertos(double tasaAciertos) {
        this.tasaAciertos = tasaAciertos;
    }
}
