import { AfterViewInit, Component, OnInit, ViewChild } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatTableModule, MatTableDataSource } from '@angular/material/table';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatNativeDateModule } from '@angular/material/core';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatPaginator, MatPaginatorModule } from '@angular/material/paginator';
import { MatSort, MatSortModule } from '@angular/material/sort';
import { ReactiveFormsModule, FormsModule, FormGroup, FormBuilder } from '@angular/forms';
import { AttendanceService } from '../../../core/services/attendance.service';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatIconModule } from '@angular/material/icon';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { MatSelectModule } from '@angular/material/select';
import { UserService } from '../../../core/services/user.service';
import { MatCardModule } from '@angular/material/card';
import { SmsService } from '../../../core/services/sms.service';

interface AttendanceRow {
  name: string;
  [key: string]: any;
}

@Component({
  selector: 'app-user-attendance',
  templateUrl: './user-attendance.component.html',
  styleUrls: ['./user-attendance.component.css'],
  standalone: true,
  imports: [
    CommonModule,
    MatTableModule,
    MatDatepickerModule,
    MatNativeDateModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatProgressSpinnerModule,
    ReactiveFormsModule,
    FormsModule,
    MatPaginatorModule,
    MatSortModule,
    MatIconModule,
    MatSelectModule,
    MatSnackBarModule,
    MatCardModule
  ]
})
export class UserAttendanceComponent implements OnInit, AfterViewInit {
  dateRange: FormGroup;
  displayedColumns: string[] = ['name'];
  dataSource: MatTableDataSource<AttendanceRow>;
  loading = false;
  error: string | null = null;
  meetingId: string = '';
  isCollecting = false;
  cohorts: string[] = [];
  selectedCohort: string = '';
  searchText: string = '';

  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;

  constructor(
    private fb: FormBuilder,
    private attendanceService: AttendanceService,
    private snackBar: MatSnackBar,
    private userService: UserService,
    private smsService: SmsService  // Add this
  ) {
    this.dateRange = this.fb.group({
      start: [null],
      end: [null]
    });

    this.dateRange.valueChanges.subscribe(() => {
      if (this.dateRange.valid) {
        this.loadAttendance();
      }
    });

    this.dataSource = new MatTableDataSource<AttendanceRow>([]);
  }

  ngOnInit() {
    // Load cohorts first
    this.loadCohorts();

    // Initialize date range with first meeting date
    this.attendanceService.getFirstMeetingDate().subscribe({
        next: (firstDate) => {
            const endDate = new Date();
            
            this.dateRange.patchValue({
                start: new Date(firstDate),
                end: endDate
            });

            this.loadAttendance();
        },
        error: (error) => {
            console.error('Error loading first meeting date:', error);
            // Fallback to first day of current month
            const today = new Date();
            const startOfMonth = new Date(today.getFullYear(), today.getMonth(), 1);
            
            this.dateRange.patchValue({
                start: startOfMonth,
                end: today
            });

            this.loadAttendance();
        }
    });
  }

  ngAfterViewInit() {
    this.dataSource.paginator = this.paginator;
    this.dataSource.sort = this.sort;
  }

  loadCohorts() {
    this.userService.getUsers().subscribe({
      next: (users) => {
        this.cohorts = [...new Set(users.map(user => user.cohort))].sort();
      },
      error: (error) => {
        console.error('Error loading cohorts:', error);
      }
    });
  }

  filterByCohort(cohort: string) {
    this.selectedCohort = cohort;
    this.filterData();
    this.loadAttendance();
  }

  loadAttendance() {
    const { start, end } = this.dateRange.value;
    this.loading = true;
    this.error = null;

    // Debug log
    console.log('Loading attendance for dates:', { start, end });

    this.attendanceService.getAttendanceReport(start, end).subscribe({
      next: (data) => {
        // Debug log
        console.log('Raw attendance data received:', data);
        this.processAttendanceData(data);
        this.loading = false;
      },
      error: (error) => {
        this.error = 'Failed to load attendance data';
        this.loading = false;
        console.error('Error loading attendance:', error);
      }
    });
  }

  processAttendanceData(data: any[]) {
    this.displayedColumns = ['name'];
    
    if (data.length > 0) {
        // Define dates and data based on cohort selection
        let filteredData = this.selectedCohort 
        ? data.filter(record => record.user.cohort === this.selectedCohort)
        : data;

        // Collect unique dates ONLY for the selected cohort
        const allDates = new Set<string>();
        filteredData.forEach(record => {
            if (!this.selectedCohort || record.user.cohort === this.selectedCohort) {
                record.attendance.forEach((att: any) => {
                    const date = new Date(att.date).toLocaleDateString();
                    allDates.add(date);
                });
            }
        });

        // Convert to array and sort
        const attendanceDates = Array.from(allDates).sort((a, b) => {
            return new Date(a).getTime() - new Date(b).getTime();
        });
        
        // Add date columns and attendance percentage
        this.displayedColumns = ['name', ...attendanceDates, 'attendancePercentage'];
        
        // Transform data for table
        const tableData = filteredData.map(record => {
            const row: any = {
                name: `${record.user.firstName} ${record.user.lastName}`,
                attendancePercentage: record.attendancePercentage,
                cohort: record.user.cohort,
                user: record.user
            };
            
            // Only add dates and attendance if user is in selected cohort
            if (!this.selectedCohort || record.user.cohort === this.selectedCohort) {
              attendanceDates.forEach(date => {
                  row[date] = '-'; // Default to absent
              });
              
              record.attendance.forEach((att: any) => {
                  const date = new Date(att.date).toLocaleDateString();
                  if (allDates.has(date)) {
                      row[date] = att.present ? 'Present' : 'Absent';
                  }
              });
          }
            
            return row;
        });

        this.dataSource = new MatTableDataSource(tableData);

        this.dataSource.filterPredicate = (data: any, filter: string) => {
            if (!filter) return true;
            return data.cohort === filter;
        };

        if (this.dataSource.paginator) {
            this.dataSource.paginator.firstPage();
        }

        setTimeout(() => {
            this.dataSource.paginator = this.paginator;
            this.dataSource.sort = this.sort;
        });
    }
  }

  onSearch() {
    if (this.dateRange.valid) {
      this.loadAttendance();
    }
  }

  collectAttendance() {
    if (!this.selectedCohort) {
      this.snackBar.open('Please select a cohort first', 'Close', {
        duration: 3000,
        horizontalPosition: 'end',
        verticalPosition: 'top'
      });
      return;
    }

    this.isCollecting = true;
    this.attendanceService.collectAttendance(this.selectedCohort).subscribe({
      next: () => {
        this.snackBar.open('Attendance collected successfully', 'Close', {
          duration: 3000,
          horizontalPosition: 'end',
          verticalPosition: 'top'
        });
        this.isCollecting = false;
        this.loadAttendance(); // Refresh the data
      },
      error: (error) => {
        console.error('Error collecting attendance:', error);
        this.snackBar.open('Failed to collect attendance', 'Close', {
          duration: 3000,
          horizontalPosition: 'end',
          verticalPosition: 'top',
          panelClass: ['error-snackbar']
        });
        this.isCollecting = false;
      }
    });
  }

  formatDate(date: string): string {
    return new Date(date).toLocaleDateString();
  }

  applyFilter(event: Event) {
    const filterValue = (event.target as HTMLInputElement).value;
    this.searchText = filterValue.trim().toLowerCase();
    this.filterData();
  }

  filterData() {
    if (this.dataSource) {
      this.dataSource.filterPredicate = (data: any, filter: string) => {
        // First check if data matches cohort filter
        const matchesCohort = !this.selectedCohort || data.cohort === this.selectedCohort;
        
        // Then check if data matches search text
        const searchStr = Object.values(data)
          .filter(val => val !== null && val !== undefined)
          .join(' ')
          .toLowerCase();
        const matchesSearch = !this.searchText || searchStr.includes(this.searchText);

        return matchesCohort && matchesSearch;
      };

      // Apply the filter
      this.dataSource.filter = this.selectedCohort + '|' + this.searchText;

      if (this.dataSource.paginator) {
        this.dataSource.paginator.firstPage();
      }
    }
  }

  sendReminders() {
    if (!this.selectedCohort) {
      this.snackBar.open('Please select a cohort first', 'Close', {
        duration: 3000,
        horizontalPosition: 'end',
        verticalPosition: 'top'
      });
      return;
    }

    this.smsService.sendReminders(this.selectedCohort).subscribe({
      next: () => {
        this.snackBar.open('Meeting reminders sent successfully', 'Close', {
          duration: 3000,
          horizontalPosition: 'end',
          verticalPosition: 'top'
        });
      },
      error: (error) => {
        console.error('Error sending reminders:', error);
        this.snackBar.open('Failed to send reminders', 'Close', {
          duration: 3000,
          horizontalPosition: 'end',
          verticalPosition: 'top',
          panelClass: ['error-snackbar']
        });
      }
    });
  }
}
