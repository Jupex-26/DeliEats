import { TestBed } from '@angular/core/testing';
import { PerfilComponent } from './perfil.component';
import { provideRouter } from '@angular/router';
import { provideHttpClient } from '@angular/common/http';
import { IonContent, IonIcon } from '@ionic/angular/standalone';

describe('PerfilComponent', () => {

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PerfilComponent, IonContent, IonIcon],
      providers: [
        provideRouter([]),
        provideHttpClient(),
      ]
    }).compileComponents();
  });

  it('should create', () => {
    const fixture = TestBed.createComponent(PerfilComponent);
    const component = fixture.componentInstance;
    expect(component).toBeTruthy();
  });

  it('should default to perfil tab', () => {
    const fixture = TestBed.createComponent(PerfilComponent);
    const component = fixture.componentInstance;
    expect(component.activeTab()).toBe('perfil');
  });

  it('should switch to pedidos tab', () => {
    const fixture = TestBed.createComponent(PerfilComponent);
    const component = fixture.componentInstance;
    component.setTab('pedidos');
    expect(component.activeTab()).toBe('pedidos');
  });

  it('should start in loading state', () => {
    const fixture = TestBed.createComponent(PerfilComponent);
    const component = fixture.componentInstance;
    expect(component.loading()).toBe(true);
  });
});