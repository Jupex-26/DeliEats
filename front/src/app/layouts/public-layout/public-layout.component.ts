import { Component, OnInit } from '@angular/core';
import { HeaderComponent } from '../header/header.component';
import { IonRouterOutlet, IonContent } from '@ionic/angular/standalone';

@Component({
  selector: 'app-public-layout',
  templateUrl: './public-layout.component.html',
  styleUrls: ['./public-layout.component.scss'],
  imports: [HeaderComponent, IonRouterOutlet, IonContent],
})
export class PublicLayoutComponent implements OnInit {
  constructor() {}

  ngOnInit() {}
}
