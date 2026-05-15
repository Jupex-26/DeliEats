import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { EmpresaInputDto, EmpresaOutputDto } from '../../types';

@Injectable({
  providedIn: 'root'
})
export class EmpresaService {
  private readonly http = inject(HttpClient);
  private readonly urlApi = `${environment.apiUrl}/empresas`;

  crear(empresa: EmpresaInputDto): Observable<EmpresaOutputDto> {
    return this.http.post<EmpresaOutputDto>(this.urlApi, empresa);
  }

  listar(page: number = 0, size: number = 10, sort?: string): Observable<any> {
    let params = new HttpParams().set('page', page).set('size', size);
    if (sort) params = params.set('sort', sort);
    return this.http.get<any>(this.urlApi, { params });
  }

  obtenerPorId(id: number): Observable<EmpresaOutputDto> {
    return this.http.get<EmpresaOutputDto>(`${this.urlApi}/${id}`);
  }

  actualizar(id: number, empresa: EmpresaInputDto): Observable<EmpresaOutputDto> {
    return this.http.put<EmpresaOutputDto>(`${this.urlApi}/${id}`, empresa);
  }

  eliminar(id: number): Observable<void> {
    return this.http.delete<void>(`${this.urlApi}/${id}`);
  }
}