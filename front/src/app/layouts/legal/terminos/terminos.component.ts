import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { IonContent } from '@ionic/angular/standalone';

@Component({
  selector: 'app-terminos',
  standalone: true,
  imports: [CommonModule, RouterLink, IonContent],
  templateUrl: './terminos.component.html',
  styleUrls: ['./terminos.component.scss']
})
export class TerminosComponent { }