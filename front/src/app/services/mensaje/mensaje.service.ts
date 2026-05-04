import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { MensajeInputDto, MensajeOutputDto } from '../../types';

@Injectable({
  providedIn: 'root'
})
export class MensajeService {
  private readonly http = inject(HttpClient);
  private readonly urlApi = `${environment.apiUrl}/mensajes`;

  crear(mensaje: MensajeInputDto): Observable<MensajeOutputDto> {
    return this.http.post<MensajeOutputDto>(this.urlApi, mensaje);
  }

  listar(): Observable<any> {
    return this.http.get<any>(this.urlApi);
  }

  obtenerPorId(id: number): Observable<MensajeOutputDto> {
    return this.http.get<MensajeOutputDto>(`${this.urlApi}/${id}`);
  }

  eliminar(id: number): Observable<void> {
    return this.http.delete<void>(`${this.urlApi}/${id}`);
  }

  marcarComoLeido(id: number): Observable<void> {
    return this.http.patch<void>(`${this.urlApi}/${id}/leer`, {});
  }
}
