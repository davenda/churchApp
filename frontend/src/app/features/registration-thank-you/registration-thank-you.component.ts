import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatDividerModule } from '@angular/material/divider';

@Component({
  selector: 'app-registration-thank-you',
  standalone: true,
  imports: [
    CommonModule, 
    MatCardModule, 
    MatButtonModule, 
    MatIconModule,
    MatDividerModule
  ],
  templateUrl: './registration-thank-you.component.html',
  styleUrls: ['./registration-thank-you.component.css'] 
})
export class RegistrationThankYouComponent implements OnInit {
  firstName: string = '';
  email: string = '';

  constructor(private router: Router) {
    const navigation = this.router.getCurrentNavigation();
    const state = navigation?.extras.state as {
      firstName: string;
      email: string;
    };

    if (state) {
      this.firstName = state.firstName;
      this.email = state.email;
    }
  }

  ngOnInit(): void {
    if (!this.firstName || !this.email) {
      this.router.navigate(['/']);
    }
  }

  goHome(): void {
    this.router.navigate(['/']);
  }

  copyZelle(): void {
    navigator.clipboard.writeText('beteyared@gmail.com').then(() => {
      // You could add a snackbar notification here
      alert('Zelle email copied to clipboard!');
    });
  }
}