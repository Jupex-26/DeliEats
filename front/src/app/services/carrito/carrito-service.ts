import { inject, Injectable, signal } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable, tap } from 'rxjs';
import { environment } from '../../../environments/environment';
import { CarritoInputDto, CarritoOutputDto } from '../../types';

@Injectable({
  providedIn: 'root'
})
export class CarritoService {
  private readonly http = inject(HttpClient);
  private readonly urlApi = `${environment.apiUrl}/carritos`;

  // Estado reactivo compartido
  private carritoState = signal<CarritoOutputDto | null>(null);
  carrito = this.carritoState.asReadonly();

  crear(carrito: CarritoInputDto): Observable<CarritoOutputDto> {
    return this.http.post<CarritoOutputDto>(this.urlApi, carrito).pipe(
      tap(c => this.carritoState.set(c))
    );
  }

  listar(page: number = 0, size: number = 10, sort?: string): Observable<any> {
    let params = new HttpParams().set('page', page).set('size', size);
    if (sort) params = params.set('sort', sort);
    return this.http.get<any>(this.urlApi, { params });
  }

  obtenerPorId(id: number): Observable<CarritoOutputDto> {
    return this.http.get<CarritoOutputDto>(`${this.urlApi}/${id}`).pipe(
      tap(c => this.carritoState.set(c))
    );
  }

  obtenerPorUsuario(usuarioId: number): Observable<CarritoOutputDto> {
    return this.http.get<CarritoOutputDto>(`${this.urlApi}/usuario/${usuarioId}`).pipe(
      tap(c => this.carritoState.set(c))
    );
  }

  eliminar(id: number): Observable<void> {
    return this.http.delete<void>(`${this.urlApi}/${id}`).pipe(
      tap(() => this.carritoState.set(null))
    );
  }

  limpiar(id: number): Observable<void> {
    return this.http.post<void>(`${this.urlApi}/${id}/limpiar`, {}).pipe(
      tap(() => {
        const current = this.carritoState();
        if (current) {
          this.carritoState.set({ ...current, detalles: [], precioTotal: 0 });
        }
      })
    );
  }

  actualizarCantidadProducto(id: number, productoId: number, cantidad: number): Observable<CarritoOutputDto> {
    return this.http.put<CarritoOutputDto>(`${this.urlApi}/${id}/productos/${productoId}?cantidad=${cantidad}`, {}).pipe(
      tap(c => this.carritoState.set(c))
    );
  }

  calcularTotal(id: number): Observable<number> {
    return this.http.get<number>(`${this.urlApi}/${id}/total`);
  }

  eliminarProducto(id: number, productoId: number): Observable<void> {
    return this.http.delete<void>(`${this.urlApi}/${id}/productos/${productoId}`).pipe(
      tap(() => {
        // Podríamos volver a cargar el carrito completo o filtrar localmente
        this.obtenerPorId(id).subscribe();
      })
    );
  }

  // Método para limpiar el estado local (ej. tras checkout)
  limpiarEstadoLocal() {
    this.carritoState.set(null);
  }
}
