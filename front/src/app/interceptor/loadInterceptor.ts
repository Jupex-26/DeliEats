import { HttpInterceptorFn } from '@angular/common/http';
import { finalize } from 'rxjs';
import { LoadingService } from '../services/load/loading-service';
import { inject } from '@angular/core';

export const loadinterceptor: HttpInterceptorFn = (req, next) => {
  const loadingService = inject(LoadingService);
  loadingService.show();

  return next(req).pipe(
    finalize(() => {
      loadingService.hide();
    }),
  );
};