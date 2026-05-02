import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { LoginRequestDto, LoginResponseDto } from '../../types';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private readonly http = inject(HttpClient);
  private readonly urlApi = `${environment.apiUrl.replace('/api', '')}/auth`;

  login(credentials: LoginRequestDto): Observable<LoginResponseDto> {
    return this.http.post<LoginResponseDto>(`${this.urlApi}/login`, credentials);
  }
}
