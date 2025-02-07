import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { UserAttendanceComponent } from './user-attendance/user-attendance.component';

const routes: Routes = [
  {
    path: '',
    component: UserAttendanceComponent
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class AttendanceRoutingModule { }