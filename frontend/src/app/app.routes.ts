import { Routes } from '@angular/router';
import { RegistrationComponent } from './features/registration/registration.component';
import { RegistrationThankYouComponent } from './features/registration-thank-you/registration-thank-you.component';
import { ScheduleMeetingComponent } from './features/zoom/schedule-meeting/schedule-meeting.component';
import { AuthGuard } from './core/services/auth.guard';
import { LoginComponent } from './features/login/login.component';
import { DonationComponent } from './features/donation/donation.component';
import { PrivacyPolicyComponent } from './features/privacy-policy/privacy-policy.component';

export const routes: Routes = [
  {
    path: '',
    component: RegistrationComponent
  },
  {
    path: 'registration-thank-you',
    component: RegistrationThankYouComponent
  },
  {
    path: 'login',
    component: LoginComponent
  },
  {
    path: 'users',
    loadChildren: () => import('./features/users/users.module').then(m => m.UsersModule),
    canActivate: [AuthGuard]
  },
  {
    path: 'attendance',
    loadChildren: () => import('./features/attendance/attendance.module').then(m => m.AttendanceModule),
    canActivate: [AuthGuard]
  },
  {
    path: 'schedule-meeting',
    component: ScheduleMeetingComponent,
    canActivate: [AuthGuard]
  },  
  {
    path: 'privacy-policy',
    component: PrivacyPolicyComponent
  },
  {
    path: 'donate',
    component: DonationComponent
  }
  
];