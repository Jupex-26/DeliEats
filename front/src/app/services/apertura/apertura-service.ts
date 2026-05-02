import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { AperturaInputDto, AperturaOutputDto } from '../../types';

@Injectable({
  providedIn: 'root'
})
export class AperturaService {
  private readonly http = inject(HttpClient);
  private readonly urlApi = `${environment.apiUrl}/aperturas`;

  crear(apertura: AperturaInputDto): Observable<AperturaOutputDto> {
    return this.http.post<AperturaOutputDto>(this.urlApi, apertura);
  }

  listar(page: number = 0, size: number = 10, sort?: string): Observable<any> {
    let params = new HttpParams().set('page', page).set('size', size);
    if (sort) params = params.set('sort', sort);
    return this.http.get<any>(this.urlApi, { params });
  }

  obtenerPorId(id: number): Observable<AperturaOutputDto> {
    return this.http.get<AperturaOutputDto>(`${this.urlApi}/${id}`);
  }

  actualizar(id: number, apertura: AperturaInputDto): Observable<AperturaOutputDto> {
    return this.http.put<AperturaOutputDto>(`${this.urlApi}/${id}`, apertura);
  }

  eliminar(id: number): Observable<void> {
    return this.http.delete<void>(`${this.urlApi}/${id}`);
  }
}
