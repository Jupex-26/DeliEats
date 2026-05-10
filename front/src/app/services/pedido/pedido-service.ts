import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { PedidoInputDto, PedidoOutputDto } from '../../types/pedido';

@Injectable({
  providedIn: 'root'
})
export class PedidoService {
  private readonly http = inject(HttpClient);
  private readonly urlApi = `${environment.apiUrl}/pedidos`;

  crear(pedido: PedidoInputDto): Observable<PedidoOutputDto> {
    return this.http.post<PedidoOutputDto>(this.urlApi, pedido);
  }

  listar(page: number = 0, size: number = 10, sort?: string): Observable<any> {
    let params = new HttpParams().set('page', page).set('size', size);
    if (sort) params = params.set('sort', sort);
    return this.http.get<any>(this.urlApi, { params });
  }

  obtenerPorId(id: number): Observable<PedidoOutputDto> {
    return this.http.get<PedidoOutputDto>(`${this.urlApi}/${id}`);
  }

  actualizar(id: number, pedido: PedidoInputDto): Observable<PedidoOutputDto> {
    return this.http.put<PedidoOutputDto>(`${this.urlApi}/${id}`, pedido);
  }

  eliminar(id: number): Observable<void> {
    return this.http.delete<void>(`${this.urlApi}/${id}`);
  }

  listarPorCliente(clienteId: number, page: number = 0, size: number = 10): Observable<any> {
    let params = new HttpParams().set('page', page).set('size', size);
    return this.http.get<any>(`${this.urlApi}/cliente/${clienteId}`, { params });
  }

  /** GET /api/pedidos/empresa/:id?page=&size=&sort=  */
  listarPorEmpresa(empresaId: number, page: number = 0, size: number = 10, sort: string = 'fechaCompra,desc'): Observable<any> {
    let params = new HttpParams().set('page', page).set('size', size).set('sort', sort);
    return this.http.get<any>(`${this.urlApi}/empresa/${empresaId}`, { params });
  }

  /** GET /api/pedidos/empresa/:id/mes-actual?page=&size=  */
  listarPorEmpresaMesActual(empresaId: number, page: number = 0, size: number = 10): Observable<any> {
    let params = new HttpParams().set('page', page).set('size', size);
    return this.http.get<any>(`${this.urlApi}/empresa/${empresaId}/mes-actual`, { params });
  }

  actualizarEstado(id: number, estadoId: number): Observable<PedidoOutputDto> {
    return this.http.patch<PedidoOutputDto>(`${this.urlApi}/${id}/estado/${estadoId}`, {});
  }

  // Cancela el pedido del cliente (estado CANCELADO = ID 5)
  cancelar(id: number): Observable<PedidoOutputDto> {
    return this.http.patch<PedidoOutputDto>(`${this.urlApi}/${id}/cancelar`, {});
  }

  // TODO: Endpoint por definir — GET /api/pedidos/{id}/factura (devuelve PDF como Blob)
  descargarFactura(id: number): Observable<Blob> {
    return this.http.get(`${this.urlApi}/${id}/factura`, { responseType: 'blob' });
  }
}
