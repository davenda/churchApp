import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { UserListComponent } from './user-list/user-list.component';
import { UserFormComponent } from './user-form/user-form.component';
import { UserAttendanceComponent } from '../attendance/user-attendance/user-attendance.component';

const routes: Routes = [
  {
    path: '',
    component: UserListComponent
  },
  {
    path: 'attendance',  // Put this before the :id route
    component: UserAttendanceComponent
  },
  {
    path: ':id',        // This should come after specific routes
    component: UserFormComponent
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class UsersRoutingModule { }