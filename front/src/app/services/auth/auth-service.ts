import { inject, Injectable, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, tap } from 'rxjs';
import { environment } from '../../../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private readonly http = inject(HttpClient);
  private readonly urlApi = `${environment.apiUrl}/auth`;

  // Se inicializa leyendo 'user' del storage
  private currentUserSignal = signal<any>(this.getUserFromStorage());
  readonly currentUser = this.currentUserSignal.asReadonly();

  private getUserFromStorage(): any {
    const user = localStorage.getItem('user');
    try {
      return user ? JSON.parse(user) : null;
    } catch (e) {
      return null;
    }
  }

  login(userData: any): Observable<any> {
    return this.http.post<any>(`${this.urlApi}/login`, userData).pipe(
      tap((response) => {
        // Guardamos el objeto completo (incluye token y userOutputDto)
        localStorage.setItem('user', JSON.stringify(response));
        this.currentUserSignal.set(response);
      }),
    );
  }

  logout(): void {
    localStorage.removeItem('user');
    this.currentUserSignal.set(null);
  }

  updateUser(userData: any): void {
    const current = this.currentUser();
    if (current) {
      const updated = { ...current, userOutputDto: { ...current.userOutputDto, ...userData } };
      localStorage.setItem('user', JSON.stringify(updated));
      this.currentUserSignal.set(updated);
    }
  }

  getRol(): string | null {
    return this.currentUser()?.userOutputDto?.nombreRol || null;
  }

  isLogin() {
    // Es válido si existe el objeto y tiene el DTO del usuario
    return !!this.currentUser()?.userOutputDto;
  }
}
