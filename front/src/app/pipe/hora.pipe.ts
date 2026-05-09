import { Pipe, PipeTransform } from '@angular/core';

/**
 * Formatea una hora recibida del backend en formato LocalTime de Spring Boot.
 * El servidor envía strings como "09:00:00" o "HH:mm:ss".
 * Este pipe los transforma a "09:00" (HH:mm) para mostrarlos de forma limpia.
 *
 * Uso: {{ '09:00:00' | hora }} → '09:00'
 *      {{ '21:30:00' | hora }} → '21:30'
 */
@Pipe({
  name: 'hora',
  standalone: true
})
export class HoraPipe implements PipeTransform {
  transform(value: string | null | undefined): string {
    if (!value) return '—';

    // Si es LocalDateTime (contiene 'T'), nos quedamos con la parte del tiempo
    let timePart = value;
    if (value.includes('T')) {
      timePart = value.split('T')[1];
    }

    // Si tiene formato HH:mm:ss o HH:mm:ss.SSS (Spring LocalTime)
    const partes = timePart.split(':');
    if (partes.length >= 2) {
      // Nos aseguramos de tomar solo los primeros 2 dígitos de la hora por si viene la fecha pegada
      // o el formato es extraño, aunque el split('T') ya debería haberlo limpiado.
      const hh = partes[0].slice(-2).padStart(2, '0');
      const mm = partes[1].padStart(2, '0');
      return `${hh}:${mm}`;
    }

    return value;
  }
}
