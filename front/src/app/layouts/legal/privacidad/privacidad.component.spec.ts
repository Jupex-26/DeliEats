import { ComponentFixture, TestBed } from '@angular/core/testing';
import { PrivacidadComponent } from './privacidad.component';

describe('PrivacidadComponent', () => {
  let component: PrivacidadComponent;
  let fixture: ComponentFixture<PrivacidadComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PrivacidadComponent]
    }).compileComponents();

    fixture = TestBed.createComponent(PrivacidadComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should render the title Política de Privacidad', () => {
    const compiled = fixture.nativeElement as HTMLElement;
    expect(compiled.querySelector('h1')?.textContent).toContain('Política de Privacidad');
  });
});