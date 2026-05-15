import { Component, EventEmitter, inject, Input, model, OnInit, Output } from '@angular/core';
import { IonicModule } from '@ionic/angular';
import { RouterModule } from '@angular/router';
import { AuthService } from '../../services/auth/auth-service';

@Component({
  selector: 'app-aside',
  templateUrl: './aside.component.html',
  styleUrls: ['./aside.component.scss'],
  standalone: true,
  imports: [IonicModule, RouterModule],
})
export class AsideComponent implements OnInit{
  isCollapsed = false; 

  ngOnInit() {
    if (window.innerWidth <= 768) {
      this.isCollapsed = true;
    }
  }
  toggleAside() {
    this.isCollapsed = !this.isCollapsed;
  }
}