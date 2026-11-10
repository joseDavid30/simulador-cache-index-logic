package com.indexlogic.cacheservice.jna;

import com.sun.jna.Structure;
import java.util.Arrays;
import java.util.List;

@Structure.FieldOrder({"hits", "misses", "tiempoTotal", "tasaAciertos"})
public class EstadisticasCache extends Structure {
    public int hits;
    public int misses;
    public long tiempoTotal;
    public double tasaAciertos;

    public static class ByReference extends EstadisticasCache implements Structure.ByReference {}
    public static class ByValue extends EstadisticasCache implements Structure.ByValue {}
}