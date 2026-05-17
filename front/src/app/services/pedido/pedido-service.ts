import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable, Subject, tap } from 'rxjs';
import { environment } from '../../../environments/environment';
import { PedidoInputDto, PedidoOutputDto } from '../../types/pedido';

@Injectable({
  providedIn: 'root'
})
export class PedidoService {
  private readonly http = inject(HttpClient);
  private readonly urlApi = `${environment.apiUrl}/pedidos`;

  private refreshPedidosSource = new Subject<void>();
  refreshPedidos$ = this.refreshPedidosSource.asObservable();

  notificarCambio() {
    this.refreshPedidosSource.next();
  }

  crear(pedido: PedidoInputDto): Observable<PedidoOutputDto> {
    return this.http.post<PedidoOutputDto>(this.urlApi, pedido);
  }

  listar(page: number = 0, size: number = 10, search?: string, sort: string = 'fechaCompra,desc'): Observable<any> {
    let params = new HttpParams().set('page', page).set('size', size).set('sort', sort);
    if (search) params = params.set('search', search);
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

  listarPorCliente(clienteId: number, page: number = 0, size: number = 10, sort: string = 'fechaCompra,desc'): Observable<any> {
    let params = new HttpParams().set('page', page).set('size', size).set('sort', sort);
    return this.http.get<any>(`${this.urlApi}/cliente/${clienteId}`, { params });
  }

  listarPorEmpresa(empresaId: number, page: number = 0, size: number = 10, sort: string = 'fechaCompra,desc'): Observable<any> {
    let params = new HttpParams().set('page', page).set('size', size).set('sort', sort);
    return this.http.get<any>(`${this.urlApi}/empresa/${empresaId}`, { params });
  }

  listarPorEmpresaMesActual(empresaId: number, page: number = 0, size: number = 10): Observable<any> {
    let params = new HttpParams().set('page', page).set('size', size);
    return this.http.get<any>(`${this.urlApi}/empresa/${empresaId}/mes-actual`, { params });
  }

  listarPorEmpresaYMes(empresaId: number, mes: number, anio: number, page: number = 0, size: number = 10): Observable<any> {
    let params = new HttpParams()
      .set('page', page)
      .set('size', size)
      .set('mes', mes)
      .set('anio', anio);
    return this.http.get<any>(`${this.urlApi}/empresa/${empresaId}/mes`, { params });
  }

  actualizarEstado(id: number, estadoId: number): Observable<PedidoOutputDto> {
    return this.http.patch<PedidoOutputDto>(`${this.urlApi}/${id}/estado/${estadoId}`, {});
  }

  cancelar(id: number): Observable<PedidoOutputDto> {
    return this.http.patch<PedidoOutputDto>(`${this.urlApi}/${id}/cancelar`, {}).pipe(
      tap(() => this.notificarCambio())
    );
  }

  descargarFactura(id: number): Observable<Blob> {
    return this.http.get(`${this.urlApi}/${id}/factura`, { responseType: 'blob' });
  }
}