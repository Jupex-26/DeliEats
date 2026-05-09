import { Pipe, PipeTransform } from '@angular/core';
import { AperturaOutputDto } from '../types';

@Pipe({
  name: 'aperturaHoy',
  standalone: true
})
export class AperturaHoyPipe implements PipeTransform {
  transform(aperturas: AperturaOutputDto[] | undefined | null, diaHoy: string): AperturaOutputDto | null {
    if (!aperturas || !diaHoy) return null;
    return aperturas.find(a => a.dia.toLowerCase() === diaHoy.toLowerCase()) || null;
  }
}
