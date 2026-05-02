import { Component, OnInit } from '@angular/core';
import { IonContent } from '@ionic/angular/standalone';

@Component({
  selector: 'app-home-component',
  templateUrl: './home-component.component.html',
  styleUrls: ['./home-component.component.scss'],
  imports: [IonContent],
})
export class HomeComponentComponent implements OnInit {
  constructor() {}

  ngOnInit() {}
}
