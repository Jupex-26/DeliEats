import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { DetalleCarritoInputDto, DetalleCarritoOutputDto } from '../../types';

@Injectable({
  providedIn: 'root'
})
export class DetalleCarritoService {
  private readonly http = inject(HttpClient);
  private readonly urlApi = `${environment.apiUrl}/detalles-carrito`;

  crear(detalle: DetalleCarritoInputDto): Observable<DetalleCarritoOutputDto> {
    return this.http.post<DetalleCarritoOutputDto>(this.urlApi, detalle);
  }

  listar(page: number = 0, size: number = 10, sort?: string): Observable<any> {
    let params = new HttpParams().set('page', page).set('size', size);
    if (sort) params = params.set('sort', sort);
    return this.http.get<any>(this.urlApi, { params });
  }

  obtenerPorId(id: number): Observable<DetalleCarritoOutputDto> {
    return this.http.get<DetalleCarritoOutputDto>(`${this.urlApi}/${id}`);
  }

  obtenerPorCarrito(carritoId: number): Observable<DetalleCarritoOutputDto[]> {
    return this.http.get<DetalleCarritoOutputDto[]>(`${this.urlApi}/carrito/${carritoId}`);
  }

  actualizar(id: number, detalle: DetalleCarritoInputDto): Observable<DetalleCarritoOutputDto> {
    return this.http.put<DetalleCarritoOutputDto>(`${this.urlApi}/${id}`, detalle);
  }

  eliminar(id: number): Observable<void> {
    return this.http.delete<void>(`${this.urlApi}/${id}`);
  }
}