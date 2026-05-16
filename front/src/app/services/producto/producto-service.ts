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

  crear(producto: ProductoInputDto, file?: File | null): Observable<ProductoOutputDto> {
    const formData = new FormData();
    Object.entries(producto).forEach(([key, value]) => {
      if (value !== undefined && value !== null) {
        formData.append(key, value.toString());
      }
    });
    if (file) {
      formData.append('file', file);
    }
    return this.http.post<ProductoOutputDto>(this.urlApi, formData);
  }

  listar(page: number = 0, size: number = 10, sort?: string): Observable<any> {
    let params = new HttpParams().set('page', page).set('size', size);
    if (sort) params = params.set('sort', sort);
    return this.http.get<any>(this.urlApi, { params });
  }

  listarPorEmpresa(empresaId: number, page: number = 0, size: number = 10): Observable<any> {
    let params = new HttpParams().set('page', page).set('size', size);
    return this.http.get<any>(`${this.urlApi}/empresa/${empresaId}`, { params });
  }

  obtenerPorId(id: number): Observable<ProductoOutputDto> {
    return this.http.get<ProductoOutputDto>(`${this.urlApi}/${id}`);
  }

  obtenerPorCategoria(categoriaId: number): Observable<ProductoOutputDto[]> {
    return this.http.get<ProductoOutputDto[]>(`${this.urlApi}/categoria/${categoriaId}`);
  }

  actualizar(id: number, producto: ProductoInputDto, file?: File | null): Observable<ProductoOutputDto> {
    const formData = new FormData();
    Object.entries(producto).forEach(([key, value]) => {
      if (value !== undefined && value !== null) {
        formData.append(key, value.toString());
      }
    });
    if (file) {
      formData.append('file', file);
    }
    return this.http.put<ProductoOutputDto>(`${this.urlApi}/${id}`, formData);
  }

  actualizarStock(id: number, cantidad: number): Observable<ProductoOutputDto> {
    return this.http.patch<ProductoOutputDto>(`${this.urlApi}/${id}/stock?cantidad=${cantidad}`, {});
  }

  eliminar(id: number): Observable<void> {
    return this.http.delete<void>(`${this.urlApi}/${id}`);
  }
}