import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, map, of, catchError } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class GeocodingService {
  private nominatimUrl = 'https://nominatim.openstreetmap.org/search';

  constructor(private http: HttpClient) { }

  verificarDireccion(direccion: string): Observable<boolean> {
    if (!direccion || direccion.trim().length < 5) {
      return of(false);
    }

    const url = `${this.nominatimUrl}?format=json&q=${encodeURIComponent(direccion)}&limit=1`;

    return this.http.get<any[]>(url).pipe(
      map(results => results && results.length > 0),
      catchError(() => of(false))
    );
  }

  obtenerCoordenadas(direccion: string): Observable<{ lat: number, lon: number } | null> {
    const url = `${this.nominatimUrl}?format=json&q=${encodeURIComponent(direccion)}&limit=1`;

    return this.http.get<any[]>(url).pipe(
      map(results => {
        if (results && results.length > 0) {
          return {
            lat: parseFloat(results[0].lat),
            lon: parseFloat(results[0].lon)
          };
        }
        return null;
      }),
      catchError(() => of(null))
    );
  }
}