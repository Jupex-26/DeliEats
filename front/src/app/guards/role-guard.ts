import { CanActivateFn } from '@angular/router';
import { AuthService } from '../services/auth/auth-service';
import { inject } from '@angular/core';

export const roleGuard: CanActivateFn = (route, state) => {
  const authService = inject(AuthService);
  const requiredRoles = route.data['role'] as string[];

  if (!requiredRoles) {
    return true; // Si no hay roles requeridos en la ruta, permitimos el acceso
  }

  const userRole = authService.getRol();

  if (!userRole) {
    return false; // Si no hay rol (usuario no logueado), denegamos el acceso
  }

  const isAllowed = requiredRoles.includes(userRole);
  if (!isAllowed) {
    console.warn(`Acceso denegado. Rol: ${userRole}, Requerido: ${requiredRoles}`);
  }
  return isAllowed;
};
