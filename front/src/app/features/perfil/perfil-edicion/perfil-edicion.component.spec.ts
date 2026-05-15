import { TestBed } from '@angular/core/testing';
import { PerfilEdicionComponent } from './perfil-edicion.component';
import { provideHttpClient } from '@angular/common/http';
import { ReactiveFormsModule } from '@angular/forms';
import { IonInput, IonItem } from '@ionic/angular/standalone';

describe('PerfilEdicionComponent', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PerfilEdicionComponent, ReactiveFormsModule, IonInput, IonItem],
      providers: [
        provideHttpClient(),
      ]
    }).compileComponents();
  });

  it('should create', () => {
    const fixture = TestBed.createComponent(PerfilEdicionComponent);
    const component = fixture.componentInstance;
    
    fixture.componentRef.setInput('cliente', {
      id: 1,
      nombre: 'Test',
      email: 'test@test.com',
      nombreRol: 'ROLE_CLIENTE'
    });
    
    expect(component).toBeTruthy();
  });

  it('should initialize forms', () => {
    const fixture = TestBed.createComponent(PerfilEdicionComponent);
    const component = fixture.componentInstance;
    
    fixture.componentRef.setInput('cliente', {
      id: 1,
      nombre: 'Test',
      email: 'test@test.com',
      nombreRol: 'ROLE_CLIENTE'
    });
    
    fixture.detectChanges();
    expect(component.passwordForm).toBeDefined();
  });
});