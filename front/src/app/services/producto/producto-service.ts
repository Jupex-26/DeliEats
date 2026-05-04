import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { ProductoInputDto, ProductoOutputDto } from '../../types';

@Injectable({
  providedIn: 'root'
})
export class ProductoService {
  private readonly http = inject(HttpClient);
  private readonly urlApi = `${environment.apiUrl}/productos`;

  crear(producto: ProductoInputDto): Observable<ProductoOutputDto> {
    return this.http.post<ProductoOutputDto>(this.urlApi, producto);
  }

  listar(page: number = 0, size: number = 10, sort?: string): Observable<any> {
    let params = new HttpParams().set('page', page).set('size', size);
    if (sort) params = params.set('sort', sort);
    return this.http.get<any>(this.urlApi, { params });
  }

  obtenerPorId(id: number): Observable<ProductoOutputDto> {
    return this.http.get<ProductoOutputDto>(`${this.urlApi}/${id}`);
  }

  obtenerPorCategoria(categoriaId: number): Observable<ProductoOutputDto[]> {
    return this.http.get<ProductoOutputDto[]>(`${this.urlApi}/categoria/${categoriaId}`);
  }

  actualizar(id: number, producto: ProductoInputDto): Observable<ProductoOutputDto> {
    return this.http.put<ProductoOutputDto>(`${this.urlApi}/${id}`, producto);
  }

  actualizarStock(id: number, cantidad: number): Observable<ProductoOutputDto> {
    return this.http.patch<ProductoOutputDto>(`${this.urlApi}/${id}/stock?cantidad=${cantidad}`, {});
  }

  eliminar(id: number): Observable<void> {
    return this.http.delete<void>(`${this.urlApi}/${id}`);
  }
}
