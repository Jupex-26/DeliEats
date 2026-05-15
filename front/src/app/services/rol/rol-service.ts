import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { RolInputDto, RolOutputDto } from '../../types';

@Injectable({
  providedIn: 'root'
})
export class RolService {
  private readonly http = inject(HttpClient);
  private readonly urlApi = `${environment.apiUrl}/roles`;

  crear(rol: RolInputDto): Observable<RolOutputDto> {
    return this.http.post<RolOutputDto>(this.urlApi, rol);
  }

  listar(page: number = 0, size: number = 10, sort?: string): Observable<any> {
    let params = new HttpParams().set('page', page).set('size', size);
    if (sort) params = params.set('sort', sort);
    return this.http.get<any>(this.urlApi, { params });
  }

  obtenerPorId(id: number): Observable<RolOutputDto> {
    return this.http.get<RolOutputDto>(`${this.urlApi}/${id}`);
  }

  actualizar(id: number, rol: RolInputDto): Observable<RolOutputDto> {
    return this.http.put<RolOutputDto>(`${this.urlApi}/${id}`, rol);
  }

  eliminar(id: number): Observable<void> {
    return this.http.delete<void>(`${this.urlApi}/${id}`);
  }
}