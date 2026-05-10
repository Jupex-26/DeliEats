import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { PedidoOutputDto } from '../../types';

@Injectable({
  providedIn: 'root'
})
export class CheckoutService {
  private readonly http = inject(HttpClient);
  private readonly urlApi = `${environment.apiUrl}/checkout`;

  finalizarCompra(): Observable<PedidoOutputDto> {
    return this.http.post<PedidoOutputDto>(this.urlApi, {});
  }
}
