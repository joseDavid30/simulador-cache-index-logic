import { Component, signal } from '@angular/core';
import {DashboardComponent} from './components/dashboard/dashboard';

@Component({
  imports: [DashboardComponent],
  selector: 'app-root',
  styleUrl: './app.css',
  templateUrl: './app.html',
})
export class App {
  protected readonly title = signal('frontend-angular');
public nombre: string = "jose";
}
