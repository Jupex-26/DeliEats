import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { TipoCocinaInputDto, TipoCocinaOutputDto } from '../../types';

@Injectable({
  providedIn: 'root'
})
export class TipoCocinaService {
  private readonly http = inject(HttpClient);
  private readonly urlApi = `${environment.apiUrl}/tiposcocina`;

  crear(tipoCocina: TipoCocinaInputDto): Observable<TipoCocinaOutputDto> {
    return this.http.post<TipoCocinaOutputDto>(this.urlApi, tipoCocina);
  }

  listar(page: number = 0, size: number = 10, sort?: string): Observable<any> {
    let params = new HttpParams().set('page', page).set('size', size);
    if (sort) params = params.set('sort', sort);
    return this.http.get<any>(this.urlApi, { params });
  }

  obtenerPorId(id: number): Observable<TipoCocinaOutputDto> {
    return this.http.get<TipoCocinaOutputDto>(`${this.urlApi}/${id}`);
  }

  actualizar(id: number, tipoCocina: TipoCocinaInputDto): Observable<TipoCocinaOutputDto> {
    return this.http.put<TipoCocinaOutputDto>(`${this.urlApi}/${id}`, tipoCocina);
  }

  eliminar(id: number): Observable<void> {
    return this.http.delete<void>(`${this.urlApi}/${id}`);
  }
}