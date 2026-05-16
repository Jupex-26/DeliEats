import { ComponentFixture, TestBed } from '@angular/core/testing';
import { EmpresaPerfilComponent } from './empresa-perfil.component';

describe('EmpresaPerfilComponent', () => {
  let component: EmpresaPerfilComponent;
  let fixture: ComponentFixture<EmpresaPerfilComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EmpresaPerfilComponent]
    }).compileComponents();

    fixture = TestBed.createComponent(EmpresaPerfilComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});