import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { EstadoInputDto, EstadoOutputDto } from '../../types';

@Injectable({
  providedIn: 'root'
})
export class EstadoService {
  private readonly http = inject(HttpClient);
  private readonly urlApi = `${environment.apiUrl}/estados`;

  crear(estado: EstadoInputDto): Observable<EstadoOutputDto> {
    return this.http.post<EstadoOutputDto>(this.urlApi, estado);
  }

  listar(page: number = 0, size: number = 10, sort?: string): Observable<any> {
    let params = new HttpParams().set('page', page).set('size', size);
    if (sort) params = params.set('sort', sort);
    return this.http.get<any>(this.urlApi, { params });
  }

  obtenerPorId(id: number): Observable<EstadoOutputDto> {
    return this.http.get<EstadoOutputDto>(`${this.urlApi}/${id}`);
  }

  actualizar(id: number, estado: EstadoInputDto): Observable<EstadoOutputDto> {
    return this.http.put<EstadoOutputDto>(`${this.urlApi}/${id}`, estado);
  }

  eliminar(id: number): Observable<void> {
    return this.http.delete<void>(`${this.urlApi}/${id}`);
  }
}