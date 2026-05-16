import { ComponentFixture, TestBed } from '@angular/core/testing';
import { RestaurantesAdminComponent } from './restaurantes-admin.component';
import { EmpresaService } from '../../services/empresa/empresa-service';
import { ProductoService } from '../../services/producto/producto-service';
import { IonicModule } from '@ionic/angular';

describe('RestaurantesAdminComponent', () => {
  let component: RestaurantesAdminComponent;
  let fixture: ComponentFixture<RestaurantesAdminComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        RestaurantesAdminComponent,
        IonicModule.forRoot()
      ],
      providers: [
        EmpresaService,
        ProductoService
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(RestaurantesAdminComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});