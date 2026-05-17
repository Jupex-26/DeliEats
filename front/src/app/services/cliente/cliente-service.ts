import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { ClienteInputDto, ClienteOutputDto } from '../../types';

@Injectable({
  providedIn: 'root'
})
export class ClienteService {
  private readonly http = inject(HttpClient);
  private readonly urlApi = `${environment.apiUrl}/clientes`;

  crear(cliente: ClienteInputDto): Observable<ClienteOutputDto> {
    return this.http.post<ClienteOutputDto>(this.urlApi, cliente);
  }

  listar(page: number = 0, size: number = 10, search?: string, sort?: string): Observable<any> {
    let params = new HttpParams().set('page', page).set('size', size);
    if (search) params = params.set('search', search);
    if (sort) params = params.set('sort', sort);
    return this.http.get<any>(this.urlApi, { params });
  }

  obtenerPorId(id: number): Observable<ClienteOutputDto> {
    return this.http.get<ClienteOutputDto>(`${this.urlApi}/${id}`);
  }

  actualizar(id: number, cliente: ClienteInputDto): Observable<ClienteOutputDto> {
    return this.http.put<ClienteOutputDto>(`${this.urlApi}/${id}`, cliente);
  }

  eliminar(id: number): Observable<void> {
    return this.http.delete<void>(`${this.urlApi}/${id}`);
  }

  solicitarSerRepartidor(id: number): Observable<void> {
    return this.http.post<void>(`${this.urlApi}/${id}/solicitar-repartidor`, {});
  }
}