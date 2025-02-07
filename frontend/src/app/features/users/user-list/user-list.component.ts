import { AfterViewInit, Component, OnInit, ViewChild } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { MatTableModule, MatTableDataSource } from '@angular/material/table';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { MatPaginator, MatPaginatorModule } from '@angular/material/paginator';
import { MatSort, MatSortModule } from '@angular/material/sort';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';

import { UserService } from '../../../core/services/user.service';
import { User } from '../../../models/user.interface';
import { ConfirmDialogComponent } from '../../../shared/components/confirm-dialog/confirm-dialog.component';
import { MatCardModule } from '@angular/material/card';

@Component({
  selector: 'app-user-list',
  templateUrl: './user-list.component.html',
  styleUrls: ['./user-list.component.css'],
  standalone: true,
  imports: [
    CommonModule,
    RouterModule,
    MatTableModule,
    MatProgressSpinnerModule,
    MatIconModule,
    MatButtonModule,
    MatDialogModule,
    MatPaginatorModule,
    MatSortModule,
    MatFormFieldModule,
    MatInputModule,
    MatSnackBarModule,
    MatSelectModule,
    MatCardModule
  ]
})
export class UserListComponent implements OnInit, AfterViewInit {
  displayedColumns: string[] = [
    'firstName',
    'lastName',
    'baptismName',
    'email',
    'phoneNumber',
    'churchName',
    'country',
    'state',
    'city',
    'cohort',
    'attendancePercentage',
    'actions'  // Add this back
  ];
  dataSource: MatTableDataSource<User>;
  loading = false;
  error: string | null = null;
  cohorts: string[] = [];
  selectedCohort: string = '';
  searchText: string = '';

  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;

  constructor(
    private userService: UserService,
    private snackBar: MatSnackBar,
    private dialog: MatDialog
  ) {
    this.dataSource = new MatTableDataSource<User>();
  }

  ngOnInit(): void {
    this.loadUsers();
  }

  ngAfterViewInit() {
    this.dataSource.paginator = this.paginator;
    this.dataSource.sort = this.sort;
  }

  loadUsers(): void {
    this.loading = true;
    this.error = null;
    this.userService.getUsers().subscribe({
      next: (users) => {
        this.cohorts = [...new Set(users.map(user => user.cohort))].sort();
        this.dataSource = new MatTableDataSource(users);
        this.dataSource.paginator = this.paginator;  // Add this line
        this.dataSource.sort = this.sort;  // Add this line
        this.loading = false;
      },
      error: (error) => {
        this.error = 'Failed to load users';
        this.loading = false;
        console.error('Error loading users:', error);
      }
    });
  }

  filterByCohort(cohort: string) {
    this.selectedCohort = cohort;
    this.filterData();
  }

  applyFilter(event: Event) {
    const filterValue = (event.target as HTMLInputElement).value;
    this.searchText = filterValue.trim().toLowerCase();
    this.filterData();
  }

  filterData() {
    if (this.dataSource) {
      this.dataSource.filterPredicate = (data: User, filter: string) => {
        const searchStr = Object.values(data)
          .filter(val => val !== null && val !== undefined)
          .join(' ')
          .toLowerCase();
        
        const matchesCohort = !this.selectedCohort || data.cohort === this.selectedCohort;
        const matchesSearch = !this.searchText || searchStr.includes(this.searchText);

        return matchesCohort && matchesSearch;
      };

      this.dataSource.filter = this.selectedCohort + '|' + this.searchText;

      if (this.dataSource.paginator) {
        this.dataSource.paginator.firstPage();
      }
    }
  }

  deleteUser(user: User): void {
    const dialogRef = this.dialog.open(ConfirmDialogComponent, {
      width: '300px',
      data: {
        title: 'Confirm Delete',
        message: `Are you sure you want to delete ${user.firstName} ${user.lastName}?`
      }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result && user.id) {
        this.userService.deleteUser(user.id).subscribe({
          next: () => {
            this.dataSource.data = this.dataSource.data.filter(u => u.id !== user.id);
            this.snackBar.open('User deleted successfully', 'Close', {
              duration: 3000,
              horizontalPosition: 'end',
              verticalPosition: 'top'
            });
          },
          error: (error) => {
            console.error('Error deleting user:', error);
            this.snackBar.open('Error deleting user', 'Close', {
              duration: 3000,
              horizontalPosition: 'end',
              verticalPosition: 'top',
              panelClass: ['error-snackbar']
            });
          }
        });
      }
    });
  }
}