import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { IonIcon, IonContent } from '@ionic/angular/standalone';
import { addIcons } from 'ionicons';
import { 
  heartOutline, 
  rocketOutline, 
  shieldCheckmarkOutline, 
  peopleOutline,
  restaurantOutline,
  bicycleOutline
} from 'ionicons/icons';

import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-nosotros',
  standalone: true,
  imports: [CommonModule, IonIcon, IonContent, RouterLink],
  templateUrl: './nosotros.component.html',
  styleUrls: ['./nosotros.component.scss']
})
export class NosotrosComponent implements OnInit {

  constructor() {
    addIcons({ 
      heartOutline, 
      rocketOutline, 
      shieldCheckmarkOutline, 
      peopleOutline,
      restaurantOutline,
      bicycleOutline
    });
  }

  ngOnInit(): void {}

}
