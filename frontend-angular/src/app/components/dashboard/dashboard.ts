import { ChangeDetectorRef,Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { SimuladorService } from '../../services/simulador.service';
import { Router } from '@angular/router';

import {
  ComparacionRequest,
  ConfiguracionCacheUI,
  HistorialTraza,
  ResultadoSimulacionUI
} from '../../models/simulador.models';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './dashboard.html',
  styleUrl: './dashboard.css'
})
export class DashboardComponent implements OnInit {
  // --- BLOQUE 01: TRAZA DE ACCESOS ---
  trazaTexto: string = `0x0040A000\n0x0040A004\n0x0040C000\n0x0040A008\n0x0040A000`;
  nombreTrazaActual: string = 'traza_manual';
  tipoPresetActivo: 'filas' | 'columnas' | 'manual' = 'manual';

  // --- BLOQUE 02: CONFIGURACIONES ---
  configuraciones: ConfiguracionCacheUI[] = [
    {
      letra: 'A',
      capacidadBytes: 32768,
      tamLinea: 64,
      vias: 4,
      numConjuntos: 128,
      politica: 1,
      politicaNombre: 'LRU'
    },
    {
      letra: 'B',
      capacidadBytes: 32768,
      tamLinea: 64,
      vias: 2,
      numConjuntos: 256,
      politica: 0,
      politicaNombre: 'FIFO'
    }
  ];

  mostrandoFormularioConfig: boolean = false;
  nuevaCapacidadBytes: number = 32768;
  nuevoTamLinea: number = 64;
  nuevasVias: number = 4;
  nuevaPoliticaNombre: 'LRU' | 'FIFO' = 'LRU';

  // Opciones de los selectores
  opcionesCapacidad = [
    { label: '4 KB', bytes: 4096 },
    { label: '8 KB', bytes: 8192 },
    { label: '16 KB', bytes: 16384 },
    { label: '32 KB', bytes: 32768 },
    { label: '64 KB', bytes: 65536 }
  ];
  opcionesLinea = [16, 32, 64, 128];
  opcionesVias = [1, 2, 4, 8];

  // --- BLOQUE 03: RESULTADOS ---
  resultados: ResultadoSimulacionUI[] = [];
  simulando: boolean = false;
  errorSimulacion: string = '';

  // --- BLOQUE 04: HISTORIAL ---
  historial: HistorialTraza[] = [];

  constructor(
    private simuladorService: SimuladorService,
    private cdr: ChangeDetectorRef,
    private router: Router
  ) {}
  ngOnInit(): void {
    this.cargarHistorial();
  }

  // --- PARSEO Y VALIDACIÓN DE DIRECCIONES (SIN LÍMITE DE CANTIDAD) ---
  get direccionesLimpias(): string[] {
    return this.trazaTexto
      .split(/\r?\n/)
      .map(linea => linea.trim())
      .filter(linea => linea.length > 0);
  }

  get trazaEsValida(): boolean {
    const dirs = this.direccionesLimpias;
    if (dirs.length === 0) return false;
    const hexRegex = /^0x[0-9a-fA-F]+$/;
    return dirs.every(d => hexRegex.test(d));
  }

  alEditarTraza(): void {
    this.tipoPresetActivo = 'manual';
    this.nombreTrazaActual = `traza_manual_${this.direccionesLimpias.length}`;
  }

  // --- PRESETS DE MATRIZ 512x512 ---
  cargarMatrizFilas(): void {
    this.tipoPresetActivo = 'filas';
    this.nombreTrazaActual = 'matriz_512_filas';
    const base = 0x00400000;
    const dirs: string[] = [];
    // Genera accesos contiguos en memoria (localidad espacial alta)
    for (let i = 0; i < 64; i++) {
      const addr = (base + i * 4).toString(16).toUpperCase().padStart(8, '0');
      dirs.push(`0x${addr}`);
    }
    this.trazaTexto = dirs.join('\n');
  }

  cargarMatrizColumnas(): void {
    this.tipoPresetActivo = 'columnas';
    this.nombreTrazaActual = 'matriz_512_cols';
    const base = 0x00400000;
    const stride = 512 * 4; // Salto de 2048 bytes por columna
    const dirs: string[] = [];
    for (let i = 0; i < 64; i++) {
      const addr = (base + i * stride).toString(16).toUpperCase().padStart(8, '0');
      dirs.push(`0x${addr}`);
    }
    this.trazaTexto = dirs.join('\n');
  }

  // --- LÓGICA DE CONFIGURACIONES ---
  get conjuntosCalculados(): number {
    const divisor = this.nuevoTamLinea * this.nuevasVias;
    return divisor > 0 ? Math.max(1, Math.floor(this.nuevaCapacidadBytes / divisor)) : 1;
  }

  abrirFormularioConfig(): void {
    if (this.configuraciones.length >= 3) return;
    this.nuevaCapacidadBytes = 32768;
    this.nuevoTamLinea = 64;
    this.nuevasVias = 4;
    this.nuevaPoliticaNombre = 'LRU';
    this.mostrandoFormularioConfig = true;
  }

  cancelarFormularioConfig(): void {
    this.mostrandoFormularioConfig = false;
  }

  guardarConfiguracion(): void {
    if (this.configuraciones.length >= 3) return;

    this.configuraciones.push({
      letra: '',
      capacidadBytes: this.nuevaCapacidadBytes,
      tamLinea: this.nuevoTamLinea,
      vias: this.nuevasVias,
      numConjuntos: this.conjuntosCalculados,
      politica: this.nuevaPoliticaNombre === 'LRU' ? 1 : 0,
      politicaNombre: this.nuevaPoliticaNombre
    });

    this.reordenarLetras();
    this.mostrandoFormularioConfig = false;
  }

  eliminarConfiguracion(index: number): void {
    this.configuraciones.splice(index, 1);
    this.reordenarLetras();
  }

  private reordenarLetras(): void {
    const letras = ['A', 'B', 'C'];
    this.configuraciones.forEach((cfg, idx) => {
      cfg.letra = letras[idx] || `${idx + 1}`;
    });
  }

  formatearCapacidad(bytes: number): string {
    return bytes >= 1024 ? `${bytes / 1024} KB` : `${bytes} B`;
  }

  simular(): void {
    if (!this.trazaEsValida || this.configuraciones.length === 0) return;

    this.simulando = true;
    this.errorSimulacion = '';

    const payload: ComparacionRequest = {
      trazaRuta: this.nombreTrazaActual,
      direcciones: this.direccionesLimpias,
      configuraciones: this.configuraciones.map(c => ({
        tamLinea: c.tamLinea,
        numConjuntos: c.numConjuntos,
        vias: c.vias,
        politica: c.politica
      }))
    };

    this.simuladorService.ejecutarComparacion(payload).subscribe({
      next: (respuesta) => {
        const lista = Array.isArray(respuesta) ? respuesta : [];

        this.resultados = lista.map((item, idx) => {
          const cfgBackend = item?.configuracion || {};
          const stats = item?.estadisticas || {};

          // Buscamos la configuración UI correspondiente por parámetros; si no, usamos el índice
          const cfgMatch =
            this.configuraciones.find(
              c =>
                c.tamLinea === Number(cfgBackend.tamLinea) &&
                c.numConjuntos === Number(cfgBackend.numConjuntos) &&
                c.vias === Number(cfgBackend.vias) &&
                c.politica === Number(cfgBackend.politica)
            ) || this.configuraciones[idx];

          const aciertos = Number(stats.aciertos ?? 0);
          const fallos = Number(stats.fallos ?? 0);
          const reemplazos = Number(stats.reemplazos ?? 0);
          const total = aciertos + fallos;
          const tasa = total > 0 ? (aciertos * 100) / total : 0;

          const codigoPolitica =
            cfgBackend.politica !== undefined
              ? Number(cfgBackend.politica)
              : cfgMatch?.politica ?? 1;

          return {
            letra: cfgMatch ? cfgMatch.letra : String.fromCharCode(65 + idx),
            vias: cfgBackend.vias ?? cfgMatch?.vias ?? 1,
            politicaNombre: codigoPolitica === 1 ? 'LRU' : 'FIFO',
            aciertos,
            fallos,
            reemplazos,
            tasaAciertos: Number(tasa.toFixed(2))
          };
        });

        this.simulando = false;
        this.cdr.detectChanges();
        this.cargarHistorial();
      },
      error: (err) => {
        console.error('Error al simular:', err);
        this.errorSimulacion = 'No se pudo conectar con comparador-service (puerto 8083).';
        this.simulando = false;
        this.cdr.detectChanges();
      }
    });
  }

  // --- HISTORIAL ---
  cargarHistorial(): void {
    this.simuladorService.obtenerHistorial().subscribe({
      next: (datos) => {
        // Mostramos los últimos registros primero
        this.historial = [...datos].sort((a, b) => b.id - a.id).slice(0, 8);
      },
      error: () => {
        // Si traza-service aún no responde, se mantiene vacío sin romper la vista
      }
    });
  }

  formatearNumero(valor: number): string {
    return valor.toLocaleString('es-CO').replace(/,/g, ' ');
  }

  obtenerNombrePolitica(codigo: number): string {
    return codigo === 1 ? 'LRU' : 'FIFO';
  }
  cerrarSesion(): void {
    localStorage.removeItem('usuario_index_logic');
    this.router.navigate(['/login']);
  }
}
