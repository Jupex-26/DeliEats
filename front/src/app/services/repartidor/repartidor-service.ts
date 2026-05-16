import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { RepartidorInputDto, RepartidorOutputDto } from '../../types';

@Injectable({
  providedIn: 'root'
})
export class RepartidorService {
  private readonly http = inject(HttpClient);
  private readonly urlApi = `${environment.apiUrl}/repartidores`;

  crear(repartidor: RepartidorInputDto): Observable<RepartidorOutputDto> {
    return this.http.post<RepartidorOutputDto>(this.urlApi, repartidor);
  }

  listar(page: number = 0, size: number = 10, sort?: string): Observable<any> {
    let params = new HttpParams().set('page', page).set('size', size);
    if (sort) params = params.set('sort', sort);
    return this.http.get<any>(this.urlApi, { params });
  }

  obtenerPorId(id: number): Observable<RepartidorOutputDto> {
    return this.http.get<RepartidorOutputDto>(`${this.urlApi}/${id}`);
  }

  obtenerDisponibles(page: number = 0, size: number = 10, sort?: string): Observable<any> {
    let params = new HttpParams().set('page', page).set('size', size);
    if (sort) params = params.set('sort', sort);
    return this.http.get<any>(`${this.urlApi}/disponibles`, { params });
  }

  actualizar(id: number, repartidor: any): Observable<RepartidorOutputDto> {
    return this.http.put<RepartidorOutputDto>(`${this.urlApi}/${id}`, repartidor);
  }

  actualizarDisponibilidad(id: number, disponible: boolean): Observable<RepartidorOutputDto> {
    return this.http.put<RepartidorOutputDto>(`${this.urlApi}/${id}/disponibilidad?disponible=${disponible}`, {});
  }

  eliminar(id: number): Observable<void> {
    return this.http.delete<void>(`${this.urlApi}/${id}`);
  }

  obtenerPorAprobado(aprobado: boolean, page: number = 0, size: number = 10, sort?: string): Observable<any> {
    let params = new HttpParams()
      .set('aprobado', aprobado.toString()) 
      .set('page', page)
      .set('size', size);

    if (sort) params = params.set('sort', sort);

    return this.http.get<any>(`${this.urlApi}/aprobado`, { params });
  }

}