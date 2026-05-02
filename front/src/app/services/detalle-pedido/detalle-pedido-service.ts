import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { DetallePedidoInputDto, DetallePedidoOutputDto } from '../../types';

@Injectable({
  providedIn: 'root'
})
export class DetallePedidoService {
  private readonly http = inject(HttpClient);
  private readonly urlApi = `${environment.apiUrl}/detalles-pedido`;

  crear(detalle: DetallePedidoInputDto): Observable<DetallePedidoOutputDto> {
    return this.http.post<DetallePedidoOutputDto>(this.urlApi, detalle);
  }

  listar(page: number = 0, size: number = 10, sort?: string): Observable<any> {
    let params = new HttpParams().set('page', page).set('size', size);
    if (sort) params = params.set('sort', sort);
    return this.http.get<any>(this.urlApi, { params });
  }

  obtenerPorId(id: number): Observable<DetallePedidoOutputDto> {
    return this.http.get<DetallePedidoOutputDto>(`${this.urlApi}/${id}`);
  }

  obtenerPorPedido(pedidoId: number): Observable<DetallePedidoOutputDto[]> {
    return this.http.get<DetallePedidoOutputDto[]>(`${this.urlApi}/pedido/${pedidoId}`);
  }

  actualizar(id: number, detalle: DetallePedidoInputDto): Observable<DetallePedidoOutputDto> {
    return this.http.put<DetallePedidoOutputDto>(`${this.urlApi}/${id}`, detalle);
  }

  eliminar(id: number): Observable<void> {
    return this.http.delete<void>(`${this.urlApi}/${id}`);
  }
}
