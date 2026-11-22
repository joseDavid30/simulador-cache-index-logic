import { Component, signal } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import {DashboardComponent} from './components/dashboard/dashboard';

@Component({
  imports: [RouterOutlet, DashboardComponent],
  selector: 'app-root',
  styleUrl: './app.css',
  templateUrl: './app.html',
})
export class App {
  protected readonly title = signal('frontend-angular');
public nombre: string = "jose";
}
