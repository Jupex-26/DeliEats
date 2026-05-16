import { Component, OnInit } from '@angular/core';
import { RouterLink } from '@angular/router';
import { IonIcon } from '@ionic/angular/standalone';
import { addIcons } from 'ionicons';
import { 
  timeOutline, 
  leafOutline, 
  bicycleOutline, 
  starOutline 
} from 'ionicons/icons';

@Component({
  selector: 'app-home-component',
  templateUrl: './home-component.component.html',
  styleUrls: ['./home-component.component.scss'],
  standalone: true,
  imports: [RouterLink, IonIcon],
})
export class HomeComponentComponent implements OnInit {
  constructor() {
    addIcons({
      timeOutline,
      leafOutline,
      bicycleOutline,
      starOutline
    });
  }

  ngOnInit() {}
}