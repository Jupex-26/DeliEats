import { HttpClient } from '@angular/common/http';
import { Injectable, inject, signal } from '@angular/core';
import { Observable, tap } from 'rxjs';
import { environment } from '../../../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private readonly http = inject(HttpClient);
  private readonly urlApi = `${environment.apiUrl}/auth/`;

  private currentUserSignal = signal<any>(this.getUserFromStorage());

  readonly currentUser = this.currentUserSignal.asReadonly();

  private getUserFromStorage(): any {
    const user = localStorage.getItem('user');
    try {
      return user ? JSON.parse(user) : null;
    } catch (e) {
      console.error('Error parseando el usuario del localStorage', e);
      return null;
    }
  }

  login(userData: any): Observable<any> {
    return this.http.post<any>(`${this.urlApi}login`, userData).pipe(
      tap((response) => {
        localStorage.setItem('user', JSON.stringify(response));
        this.currentUserSignal.set(response);
      }),
    );
  }

  /**
   * Limpia la sesión
   */
  logout(): void {
    localStorage.removeItem('user');
    this.currentUserSignal.set(null);
  }

  /**
   * Obtiene el nombre del rol del usuario actual
   */
  getRol(): string | null {
    const user = this.currentUserSignal();
    // Acceso seguro usando encadenamiento opcional
    return user?.user?.rol?.nombre || null;
  }
  isLogin() {
    return !!this.currentUser()?.user;
  }
}
