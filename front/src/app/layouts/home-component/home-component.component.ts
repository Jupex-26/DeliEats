import { Component, OnInit } from '@angular/core';
import { IonContent } from '@ionic/angular/standalone';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-home-component',
  templateUrl: './home-component.component.html',
  styleUrls: ['./home-component.component.scss'],
  imports: [IonContent, RouterLink],
})
export class HomeComponentComponent implements OnInit {
  constructor() {}

  ngOnInit() {}
}
