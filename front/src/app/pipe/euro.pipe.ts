import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'euro',
  standalone: true
})
export class EuroPipe implements PipeTransform {
  transform(value: number | string | null | undefined): string {
    if (value === null || value === undefined) return '';
    const num = typeof value === 'string' ? parseFloat(value) : value;
    if (isNaN(num)) return '';
    
    return num.toLocaleString('es-ES', {
      minimumFractionDigits: 2,
      maximumFractionDigits: 2,
    }) + ' €';
  }
}