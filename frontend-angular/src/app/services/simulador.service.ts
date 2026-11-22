import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ComparacionRequest, HistorialTraza } from '../models/simulador.models';

@Injectable({
  providedIn: 'root'
})
export class SimuladorService {
  private readonly comparadorUrl = 'http://localhost:8083/api/comparador/ejecutar';
  private readonly trazaUrl = 'http://localhost:8082/api/trazas';

  constructor(private http: HttpClient) {}

  ejecutarComparacion(payload: ComparacionRequest): Observable<any[]> {
    return this.http.post<any[]>(this.comparadorUrl, payload);
  }

  obtenerHistorial(): Observable<HistorialTraza[]> {
    return this.http.get<HistorialTraza[]>(this.trazaUrl);
  }
}
