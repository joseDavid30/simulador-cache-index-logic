package com.indexlogic.cacheservice.jna;

import com.sun.jna.Structure;
import java.util.Arrays;
import java.util.List;
import com.sun.jna.NativeLong;

@Structure.FieldOrder({"aciertos", "fallos", "reemplazos"})
public class EstadisticasCache extends Structure {
    public NativeLong aciertos;
    public NativeLong fallos;
    public NativeLong reemplazos;

    public static class ByReference extends EstadisticasCache implements Structure.ByReference {}
    public static class ByValue extends EstadisticasCache implements Structure.ByValue {}
}