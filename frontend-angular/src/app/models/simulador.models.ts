export interface ConfiguracionCacheUI {
  letra: string;           // 'A', 'B', 'C'
  capacidadBytes: number;  // Ej. 32768 (32 KB)
  tamLinea: number;        // Ej. 64
  numConjuntos: number;    // Calculado: capacidadBytes / (tamLinea * vias)
  vias: number;            // Ej. 4
  politica: number;        // 0 = FIFO, 1 = LRU (ajusta según tu librería C)
  politicaNombre: 'LRU' | 'FIFO';
}

export interface ComparacionRequest {
  trazaRuta: string;
  direcciones: string[];
  configuraciones: {
    tamLinea: number;
    numConjuntos: number;
    vias: number;
    politica: number;
  }[];
}

export interface ResultadoSimulacionUI {
  letra: string;
  vias: number;
  politicaNombre: string;
  aciertos: number;
  fallos: number;
  reemplazos: number;
  tasaAciertos: number;
}

export interface HistorialTraza {
  id: number;
  trazaRuta: string;
  capacidad: number;
  vias: number;
  politica: number;
  tasaAciertos: number;
}
