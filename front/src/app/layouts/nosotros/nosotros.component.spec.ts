import { ComponentFixture, TestBed } from '@angular/core/testing';
import { NosotrosComponent } from './nosotros.component';
import { RouterTestingModule } from '@angular/router/testing';

describe('NosotrosComponent', () => {
  let component: NosotrosComponent;
  let fixture: ComponentFixture<NosotrosComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ NosotrosComponent, RouterTestingModule ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(NosotrosComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should render the mission title', () => {
    const compiled = fixture.nativeElement as HTMLElement;
    expect(compiled.querySelector('h2.section-title')?.textContent).toContain('Nuestra Misión');
  });
});
