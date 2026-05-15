import { ComponentFixture, TestBed } from '@angular/core/testing';
import { EmpresaPerfilEdicionComponent } from './empresa-perfil-edicion.component';

describe('EmpresaPerfilEdicionComponent', () => {
  let component: EmpresaPerfilEdicionComponent;
  let fixture: ComponentFixture<EmpresaPerfilEdicionComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EmpresaPerfilEdicionComponent]
    }).compileComponents();

    fixture = TestBed.createComponent(EmpresaPerfilEdicionComponent);
    component = fixture.componentInstance;
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});