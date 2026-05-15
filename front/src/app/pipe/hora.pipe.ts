import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'hora',
  standalone: true
})
export class HoraPipe implements PipeTransform {
  transform(value: string | null | undefined): string {
    if (!value) return '—';

    let timePart = value;
    if (value.includes('T')) {
      timePart = value.split('T')[1];
    }

    const partes = timePart.split(':');
    if (partes.length >= 2) {
      
      const hh = partes[0].slice(-2).padStart(2, '0');
      const mm = partes[1].padStart(2, '0');
      return `${hh}:${mm}`;
    }

    return value;
  }
}