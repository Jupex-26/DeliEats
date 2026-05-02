import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { CarritoInputDto, CarritoOutputDto } from '../../types';

@Injectable({
  providedIn: 'root'
})
export class CarritoService {
  private readonly http = inject(HttpClient);
  private readonly urlApi = `${environment.apiUrl}/carritos`;

  crear(carrito: CarritoInputDto): Observable<CarritoOutputDto> {
    return this.http.post<CarritoOutputDto>(this.urlApi, carrito);
  }

  listar(page: number = 0, size: number = 10, sort?: string): Observable<any> {
    let params = new HttpParams().set('page', page).set('size', size);
    if (sort) params = params.set('sort', sort);
    return this.http.get<any>(this.urlApi, { params });
  }

  obtenerPorId(id: number): Observable<CarritoOutputDto> {
    return this.http.get<CarritoOutputDto>(`${this.urlApi}/${id}`);
  }

  obtenerPorUsuario(usuarioId: number): Observable<CarritoOutputDto> {
    return this.http.get<CarritoOutputDto>(`${this.urlApi}/usuario/${usuarioId}`);
  }

  eliminar(id: number): Observable<void> {
    return this.http.delete<void>(`${this.urlApi}/${id}`);
  }

  limpiar(id: number): Observable<void> {
    return this.http.post<void>(`${this.urlApi}/${id}/limpiar`, {});
  }

  actualizarCantidadProducto(id: number, productoId: number, cantidad: number): Observable<CarritoOutputDto> {
    return this.http.put<CarritoOutputDto>(`${this.urlApi}/${id}/productos/${productoId}?cantidad=${cantidad}`, {});
  }

  calcularTotal(id: number): Observable<number> {
    return this.http.get<number>(`${this.urlApi}/${id}/total`);
  }

  eliminarProducto(id: number, productoId: number): Observable<void> {
    return this.http.delete<void>(`${this.urlApi}/${id}/productos/${productoId}`);
  }
}
