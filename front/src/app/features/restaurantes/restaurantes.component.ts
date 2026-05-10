import { Component, OnInit, inject, signal, computed } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import {
  IonContent,
  IonIcon
} from '@ionic/angular/standalone';
import { addIcons } from 'ionicons';
import {
  searchOutline,
  timeOutline,
  bicycleOutline,
  restaurantOutline
} from 'ionicons/icons';
import { EmpresaService } from '../../services/empresa/empresa-service';
import { TipoCocinaService } from '../../services/tipococina/tipococina-service';
import { EmpresaOutputDto, TipoCocinaOutputDto } from '../../types';

import { environment } from '../../../environments/environment';

@Component({
  selector: 'app-restaurantes',
  templateUrl: './restaurantes.component.html',
  styleUrls: ['./restaurantes.component.scss'],
  standalone: true,
  imports: [CommonModule, IonContent, IonIcon, RouterLink],
})
export class RestaurantesComponent implements OnInit {
  protected environment = environment;
  private readonly empresaService = inject(EmpresaService);
  private readonly tipoCocinaService = inject(TipoCocinaService);

  constructor() {
    addIcons({
      searchOutline,
      timeOutline,
      bicycleOutline,
      restaurantOutline
    });
  }

  // --- State Signals ---
  // Lista bruta de la API
  restaurantes = signal<EmpresaOutputDto[]>([]);
  // Término de búsqueda
  searchTerm = signal<string>('');
  // Categoría seleccionada (Nombre)
  selectedCategory = signal<string>('Todos');
  // Lista de categorías desde la API
  categories = signal<TipoCocinaOutputDto[]>([]);

  // --- Computed Signal (Filtrado en tiempo real) ---
  filteredRestaurants = computed(() => {
    let list = this.restaurantes();

    // Filtro por nombre
    if (this.searchTerm()) {
      list = list.filter((res) =>
        res.nombre.toLowerCase().includes(this.searchTerm().toLowerCase()),
      );
    }

    // Filtro por categoría (usando el nombre del tipo de cocina)
    if (this.selectedCategory() !== 'Todos') {
      list = list.filter((res) => res.tipoCocina.nombre === this.selectedCategory());
    }

    return list;
  });

  ngOnInit() {
    this.loadRestaurantes();
    this.loadCategories();
  }

  loadRestaurantes() {
    this.empresaService.listar().subscribe({
      next: (data) => {
        this.restaurantes.set(data.content);
      },
      error: (err) => console.error('Error cargando restaurantes', err),
    });
  }

  loadCategories() {
    // Listamos todas las categorías (suponiendo que no son miles, el default size 100 bastaría o pedir todas)
    this.tipoCocinaService.listar(0, 100).subscribe({
      next: (data) => {
        this.categories.set(data.content);
      },
      error: (err) => console.error('Error cargando categorías', err),
    });
  }

  // --- Handlers ---
  updateSearch(event: Event) {
    const input = event.target as HTMLInputElement;
    this.searchTerm.set(input.value);
  }

  selectCategory(categoryName: string) {
    this.selectedCategory.set(categoryName);
  }

  resetFilters() {
    this.searchTerm.set('');
    this.selectedCategory.set('Todos');
  }

  estaAbierto(empresa: EmpresaOutputDto): boolean {
    if (!empresa.aperturas || empresa.aperturas.length === 0) return false;

    const ahora = new Date();
    // Días en MAYÚSCULAS y sin acentos como vienen del back
    const diasSemana = ['DOMINGO', 'LUNES', 'MARTES', 'MIERCOLES', 'JUEVES', 'VIERNES', 'SABADO'];
    const diaActual = diasSemana[ahora.getDay()];
    
    // Hora actual en minutos
    const horaActualMin = ahora.getHours() * 60 + ahora.getMinutes();

    const aperturaHoy = empresa.aperturas.find(a => a.dia === diaActual);
    if (!aperturaHoy) return false;

    // El back envía HH:mm:ss o HH:mm
    const [hA, mA] = aperturaHoy.horaApertura.split(':').map(Number);
    const [hC, mC] = aperturaHoy.horaCierre.split(':').map(Number);

    const minApertura = hA * 60 + mA;
    const minCierre = hC * 60 + mC;

    return horaActualMin >= minApertura && horaActualMin <= minCierre;
  }
}
