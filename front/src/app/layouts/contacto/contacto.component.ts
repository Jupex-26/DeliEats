import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { IonContent, IonIcon } from '@ionic/angular/standalone';
import { addIcons } from 'ionicons';
import { 
  mailOutline, 
  callOutline, 
  locationOutline, 
  logoFacebook, 
  logoInstagram, 
  logoTwitter,
  sendOutline
} from 'ionicons/icons';

@Component({
  selector: 'app-contacto',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, IonContent, IonIcon],
  templateUrl: './contacto.component.html',
  styleUrls: ['./contacto.component.scss']
})
export class ContactoComponent implements OnInit {
  contactoForm: FormGroup;
  enviado = false;

  constructor(private fb: FormBuilder) {
    addIcons({
      mailOutline,
      callOutline,
      locationOutline,
      logoFacebook,
      logoInstagram,
      logoTwitter,
      sendOutline
    });

    this.contactoForm = this.fb.group({
      nombre: ['', [Validators.required, Validators.minLength(3)]],
      email: ['', [Validators.required, Validators.email]],
      asunto: ['', [Validators.required]],
      mensaje: ['', [Validators.required, Validators.minLength(10)]]
    });
  }

  ngOnInit(): void {}

  onSubmit(): void {
    if (this.contactoForm.valid) {
      console.log('Formulario enviado:', this.contactoForm.value);
      this.enviado = true;
      this.contactoForm.reset();
      
      setTimeout(() => {
        this.enviado = false;
      }, 5000);
    }
  }
}
