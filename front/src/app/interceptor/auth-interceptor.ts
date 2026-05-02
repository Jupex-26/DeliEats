import { HttpInterceptorFn } from '@angular/common/http';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const token = localStorage.getItem('user')
    ? JSON.parse(localStorage.getItem('user')!).token
    : null;

  if (!token) {
    return next(req);
  }

  const apiReq = req.clone({
    setHeaders: {
      Authorization: `Bearer ${token}`,
    },
  });
  return next(apiReq);
};
