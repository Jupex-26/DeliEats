import { Component, OnInit, inject, signal, computed } from '@angular/core';
import { CommonModule } from '@angular/common';
import {
  IonContent,
  IonHeader,
  IonTitle,
  IonToolbar
} from '@ionic/angular/standalone';
import { EmpresaService } from '../../services/empresa/empresa-service';
import { Empresa } from '../../types';

@Component({
  selector: 'app-restaurantes',
  templateUrl: './restaurantes.component.html',
  styleUrls: ['./restaurantes.component.scss'],
  standalone: true,
  imports: [CommonModule, IonTitle, IonToolbar, IonHeader, IonContent],
})
export class RestaurantesComponent implements OnInit {
  private readonly empresaService = inject(EmpresaService);

  // --- State Signals ---
  // Lista bruta de la API
  restaurantes = signal<Empresa[]>([]);
  // Término de búsqueda
  searchTerm = signal<string>('');
  // Categoría seleccionada
  selectedCategory = signal<string>('Todos');
  // Lista de categorías únicas (puedes traerlas de la API o generarlas del array)
  categories = signal<string[]>(['Todos', 'Italiana', 'Hamburguesas', 'Saludable', 'Postres']);

  // --- Computed Signal (Filtrado en tiempo real) ---
  // Este signal se actualiza automáticamente cuando cambia searchTerm o selectedCategory
  filteredRestaurants = computed(() => {
    let list = this.restaurantes();

    // Filtro por nombre
    if (this.searchTerm()) {
      list = list.filter((res) =>
        res.nombre.toLowerCase().includes(this.searchTerm().toLowerCase()),
      );
    }

    // Filtro por categoría
    if (this.selectedCategory() !== 'Todos') {
      list = list.filter((res) => res.tipoCocina === this.selectedCategory());
    }

    return list;
  });

  ngOnInit() {
    this.loadRestaurantes();
  }

  loadRestaurantes() {
    this.empresaService.listar().subscribe({
      next: (data) => {
        this.restaurantes.set(data.content);
        // Opcional: Extraer categorías únicas dinámicamente de la respuesta
        // const cats = ['Todos', ...new Set(data.map(r => r.categoria))];
        // this.categories.set(cats);
      },
      error: (err) => console.error('Error cargando restaurantes', err),
    });
  }

  // --- Handlers ---
  updateSearch(event: Event) {
    const input = event.target as HTMLInputElement;
    this.searchTerm.set(input.value);
  }

  selectCategory(category: string) {
    this.selectedCategory.set(category);
  }

  resetFilters() {
    this.searchTerm.set('');
    this.selectedCategory.set('Todos');
  }
}
