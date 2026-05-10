import { ComponentFixture, TestBed } from '@angular/core/testing';
import { EmpresaPedidosComponent } from './empresa-pedidos.component';

describe('EmpresaPedidosComponent', () => {
  let component: EmpresaPedidosComponent;
  let fixture: ComponentFixture<EmpresaPedidosComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EmpresaPedidosComponent]
    }).compileComponents();

    fixture = TestBed.createComponent(EmpresaPedidosComponent);
    component = fixture.componentInstance;
    component.empresaId = 1;
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
