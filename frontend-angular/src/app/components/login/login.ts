import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './login.html',
  styleUrl: './login.css'
})
export class LoginComponent {
  modoRegistro: boolean = false;

  // Campos del formulario
  nombreCompleto: string = '';
  correo: string = '';
  contrasena: string = '';
  errorMensaje: string = '';
  mensajeExito: string = '';

  // Matriz visual decorativa de 6 filas x 8 columnas (0 = oscuro, 1 = verde acierto, 2 = naranja fallo)
  matrizCache: number[][] = [
    [1, 0, 2, 0, 1, 0, 0, 2],
    [0, 1, 0, 1, 0, 2, 0, 1],
    [2, 0, 1, 0, 0, 1, 2, 0],
    [0, 0, 0, 2, 1, 1, 0, 0],
    [1, 0, 0, 1, 0, 2, 0, 0],
    [0, 1, 2, 0, 1, 0, 0, 2]
  ];

  constructor(private router: Router) {}

  alternarModo(): void {
    this.modoRegistro = !this.modoRegistro;
    this.errorMensaje = '';
    this.mensajeExito = '';
  }

  enviarFormulario(): void {
    this.errorMensaje = '';
    this.mensajeExito = '';

    if (!this.correo.trim() || !this.contrasena.trim()) {
      this.errorMensaje = 'Por favor ingresa tu correo o usuario y contraseña.';
      return;
    }

    if (this.modoRegistro) {
      if (!this.nombreCompleto.trim()) {
        this.errorMensaje = 'Por favor ingresa tu nombre completo.';
        return;
      }
      // Guardamos usuario localmente para simular registro o conectar luego a tu backend
      localStorage.setItem('usuario_index_logic', JSON.stringify({
        nombre: this.nombreCompleto.trim(),
        correo: this.correo.trim()
      }));
      this.mensajeExito = 'Cuenta creada correctamente. Iniciando sesión...';
      setTimeout(() => {
        this.router.navigate(['/dashboard']);
      }, 700);
      return;
    }

    // Inicio de sesión: guardamos sesión básica y navegamos al dashboard
    localStorage.setItem('usuario_index_logic', JSON.stringify({
      nombre: this.correo.split('@')[0],
      correo: this.correo.trim()
    }));
    this.router.navigate(['/dashboard']);
  }
}
