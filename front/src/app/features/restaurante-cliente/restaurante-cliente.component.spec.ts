import { ComponentFixture, TestBed } from '@angular/core/testing';
import { RestauranteClienteComponent } from './restaurante-cliente.component';

describe('RestauranteClienteComponent', () => {
  let component: RestauranteClienteComponent;
  let fixture: ComponentFixture<RestauranteClienteComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RestauranteClienteComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RestauranteClienteComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});