import { Component, OnInit } from '@angular/core';
import { HeaderComponent } from '../header/header.component';
import { IonContent, IonRouterOutlet } from '@ionic/angular/standalone';

@Component({
  selector: 'app-public-layout',
  templateUrl: './public-layout.component.html',
  styleUrls: ['./public-layout.component.scss'],
  imports: [HeaderComponent, IonRouterOutlet, IonContent],
  host: {
    class: 'ion-page',
  },
})
export class PublicLayoutComponent implements OnInit {
  constructor() { }

  ngOnInit() { }
}
