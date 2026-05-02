import { CanActivateFn } from '@angular/router';
import { AuthService } from '../services/auth/auth-service';
import { inject } from '@angular/core/primitives/di';

export const roleGuard: CanActivateFn = (route, state) => {
  const authService: AuthService = inject(AuthService);
  return route.data['role'].includes(authService.getRol());
};
