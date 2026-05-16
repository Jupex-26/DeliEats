import { ComponentFixture, TestBed } from '@angular/core/testing';
import { PedidosAdminComponent } from './pedidos-admin.component';
import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';

describe('PedidosAdminComponent', () => {
  let component: PedidosAdminComponent;
  let fixture: ComponentFixture<PedidosAdminComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PedidosAdminComponent],
      providers: [
        provideHttpClient(),
        provideHttpClientTesting()
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(PedidosAdminComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});