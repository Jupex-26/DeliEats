import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { UserInputDto, UserOutputDto } from '../../types';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private readonly http = inject(HttpClient);
  private readonly urlApi = `${environment.apiUrl}/users`;

  crear(user: UserInputDto): Observable<UserOutputDto> {
    return this.http.post<UserOutputDto>(this.urlApi, user);
  }

  listar(page: number = 0, size: number = 10, sort?: string): Observable<any> {
    let params = new HttpParams().set('page', page).set('size', size);
    if (sort) params = params.set('sort', sort);
    return this.http.get<any>(this.urlApi, { params });
  }

  obtenerPorId(id: number): Observable<UserOutputDto> {
    return this.http.get<UserOutputDto>(`${this.urlApi}/${id}`);
  }

  obtenerPorEmail(email: string): Observable<UserOutputDto> {
    return this.http.get<UserOutputDto>(`${this.urlApi}/email/${email}`);
  }

  actualizar(id: number, user: UserInputDto): Observable<UserOutputDto> {
    return this.http.put<UserOutputDto>(`${this.urlApi}/${id}`, user);
  }

  actualizarPassword(id: number, newPassword: string): Observable<void> {
    return this.http.patch<void>(`${this.urlApi}/${id}/password`, newPassword);
  }

  eliminar(id: number): Observable<void> {
    return this.http.delete<void>(`${this.urlApi}/${id}`);
  }

  subirFoto(id: number, formData: FormData): Observable<UserOutputDto> {
    return this.http.post<UserOutputDto>(`${this.urlApi}/${id}/foto`, formData);
  }

  aprobarRepartidor(id: number, aprobado: boolean): Observable<void> {
    return this.http.patch<void>(`${environment.apiUrl}/repartidores/${id}/aprobar?aprobado=${aprobado}`, {});
  }
}