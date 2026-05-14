import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
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

  listar(page: number = 0, size: number = 10, sort?: string): Observable<any> {
    let params = new HttpParams().set('page', page).set('size', size);
    if (sort) params = params.set('sort', sort);
    return this.http.get<any>(this.urlApi, { params });
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

  obtenerChat(usuario1Id: number, usuario2Id: number, page: number = 0, size: number = 50): Observable<any> {
    const params = new HttpParams()
      .set('usuario1Id', usuario1Id.toString())
      .set('usuario2Id', usuario2Id.toString())
      .set('page', page.toString())
      .set('size', size.toString())
      .set('sort', 'fecha,asc');
    return this.http.get<any>(`${this.urlApi}/chat`, { params });
  }
}
