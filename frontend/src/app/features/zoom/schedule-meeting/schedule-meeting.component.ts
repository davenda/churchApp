import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators, FormControl } from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatNativeDateModule } from '@angular/material/core';
import { MatSelectModule } from '@angular/material/select';
import { MatSnackBar } from '@angular/material/snack-bar';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';

import { ZoomService } from '../../../core/services/zoom.service';
import { UserService } from '../../../core/services/user.service';
import { User } from '../../../models/user.interface';
import { SmsService } from '../../../core/services/sms.service';
import { MatCardModule } from '@angular/material/card';
import { MatIcon, MatIconModule } from '@angular/material/icon';

@Component({
  selector: 'app-schedule-meeting',
  templateUrl: './schedule-meeting.component.html',
  styleUrls: ['./schedule-meeting.component.css'],
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatDatepickerModule,
    MatNativeDateModule,
    MatSelectModule,
    MatProgressSpinnerModule,
    MatCardModule,
    MatIconModule
  ]
})
export class ScheduleMeetingComponent implements OnInit {
  meetingForm: FormGroup;
  messageForm: FormGroup;  
  users: User[] = [];
  loading = false;
  error: string | null = null;
  cohorts: string[] = [];
  recipientTypes = ['All Users', 'Cohort', 'Single User'];  
  messageSent = false;

  constructor(
    private fb: FormBuilder,
    private zoomService: ZoomService,
    private userService: UserService,
    private smsService: SmsService,
    private snackBar: MatSnackBar,
    private cdr: ChangeDetectorRef  // Add this
  ) {
    this.meetingForm = this.fb.group({
      topic: ['', Validators.required],
      startTime: ['', Validators.required],
      duration: ['60', [Validators.required, Validators.min(15)]],
      selectedCohort: ['', Validators.required]  
    });

    this.messageForm = this.fb.group({
      message: ['', Validators.required],
      recipientType: ['', Validators.required],
      selectedCohort: [''],
      selectedUser: ['']
    });
  }

  ngOnInit() {
    this.loadUsers();
  }

  loadUsers() {
    this.loading = true;
    this.userService.getUsers().subscribe({
      next: (users) => {
        this.users = users;
        this.cohorts = [...new Set(users.map(user => user.cohort))].sort();
        this.loading = false;
      },
      error: (error) => {
        console.error('Error loading users:', error);
        this.error = 'Failed to load users';
        this.loading = false;
      }
    });
  }

  onSubmit() {
    if (this.meetingForm.valid) {
      this.loading = true;
      const formData = this.meetingForm.value;

      const selectedUsers = this.users.filter(
        user => user.cohort === formData.selectedCohort
      );

      // Format the meeting data
      const meetingDetails = {
        topic: formData.topic,
        startTime: this.formatDateTime(formData.startTime),
        duration: parseInt(formData.duration, 10), // Convert to number
        selectedCohort: ['', Validators.required]
      };

      // Format participants for Zoom
      const participants = selectedUsers.map(user => ({
        email: user.email,
        firstName: user.firstName,
        lastName: user.lastName
      }));

      this.zoomService.createMeeting(meetingDetails, participants).subscribe({
        next: (response) => {
          this.loading = false;
          this.snackBar.open('Meeting scheduled successfully!', 'Close', {
            duration: 3000
          });
          this.meetingForm.reset({
            duration: '60'
          });
        },
        error: (error) => {
          this.loading = false;
          console.error('Error scheduling meeting:', error);
          this.snackBar.open('Failed to schedule meeting', 'Close', {
            duration: 3000,
            panelClass: ['error-snackbar']
          });
        }
      });
    }
  }

  private formatDateTime(date: Date): string {
    return date.toISOString();
  }

  sendMessage() {
    if (this.messageForm.valid) {
      const formData = this.messageForm.value;
      
      this.smsService.sendCustomMessage(
        formData.recipientType,
        formData.message,
        formData.recipientType === 'Cohort' ? formData.selectedCohort : undefined,
        formData.recipientType === 'Single User' ? formData.selectedUser : undefined
      ).subscribe({
        next: () => {
          this.snackBar.open('Message sent successfully!', 'Close', {
            duration: 3000,
            horizontalPosition: 'end',
            verticalPosition: 'top'
          });
          console.log(this.messageForm.valid);
  
          // Keep current values
          const currentValues = {
            recipientType: this.messageForm.get('recipientType')?.value,
            selectedUser: this.messageForm.get('selectedUser')?.value,
            selectedCohort: this.messageForm.get('selectedCohort')?.value
          };
  
          // Create new form with same structure
          this.messageForm = this.fb.group({
            message: ['', Validators.required],
            recipientType: [currentValues.recipientType, Validators.required],
            selectedCohort: [currentValues.selectedCohort],
            selectedUser: [currentValues.selectedUser],
          });
          console.log(this.messageForm.valid);


        // Reset the form to its initial state
        // this.messageForm.reset({
        //   message: '',
        //   recipientType: '',
        //   selectedCohort: '',
        //   selectedUser: ''
        // });
          
          Object.keys(this.messageForm.controls).forEach(key => {
            const control = this.messageForm.get(key);
            control?.markAsUntouched();
            control?.markAsPristine();
            control?.setErrors(null);
          });
          console.log(this.messageForm.valid);

        },
        error: (error) => {
          console.error('Error sending message:', error);
          this.snackBar.open(error.error?.error || 'Failed to send message', 'Close', {
            duration: 3000,
            horizontalPosition: 'end',
            verticalPosition: 'top',
            panelClass: ['error-snackbar']
          });
        }
      });
    } else {
      // Mark fields as touched to trigger validation display
      // Object.keys(this.messageForm.controls).forEach(key => {
      //   const control = this.messageForm.get(key);
      //   control?.markAsTouched();
      // });
    }
  }

  // Add form control visibility methods
  showCohortSelect(): boolean {
    return this.messageForm.get('recipientType')?.value === 'Cohort';
  }

  showUserSelect(): boolean {
    return this.messageForm.get('recipientType')?.value === 'Single User';
  }

  private markFormGroupTouched(formGroup: FormGroup) {
    Object.values(formGroup.controls).forEach(control => {
      if (control instanceof FormGroup) {
        this.markFormGroupTouched(control);
      } else {
        control.markAsTouched();
        if (control.errors) {
          control.setErrors(control.errors);
        }
      }
    });
  }
}