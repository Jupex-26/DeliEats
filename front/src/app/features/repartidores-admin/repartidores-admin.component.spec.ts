import { ComponentFixture, TestBed } from '@angular/core/testing';
import { RepartidoresAdminComponent } from './repartidores-admin.component';
import { RepartidorService } from '../../services/repartidor/repartidor-service';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { IonicModule } from '@ionic/angular';

describe('RepartidoresAdminComponent', () => {
  let component: RepartidoresAdminComponent;
  let fixture: ComponentFixture<RepartidoresAdminComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        RepartidoresAdminComponent,
        HttpClientTestingModule,
        IonicModule.forRoot()
      ],
      providers: [
        RepartidorService
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(RepartidoresAdminComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});