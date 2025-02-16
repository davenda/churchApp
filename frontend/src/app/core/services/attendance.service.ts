import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environments';

@Injectable({
  providedIn: 'root'
})
export class AttendanceService {
  private apiUrl = `${environment.apiUrl}/attendance`;

  constructor(private http: HttpClient) {}

  getAttendanceReport(startDate: Date | null, endDate: Date | null): Observable<any> {
    // Default to current month if no dates provided
    if (!startDate || !endDate) {
      const today = new Date();
      startDate = new Date(today.getFullYear(), today.getMonth(), 1); // First day of current month
      endDate = new Date(today.getFullYear(), today.getMonth() + 1, 0); // Last day of current month
    }

    const params = new HttpParams()
      .set('startDate', startDate.toISOString().split('T')[0])
      .set('endDate', endDate.toISOString().split('T')[0]);

    return this.http.get(`${this.apiUrl}/report/matrix`, { params });
  }

  collectAttendance(cohort: string): Observable<any> {
    return this.http.post(`${this.apiUrl}/collect`, { cohort });
  }

  getFirstMeetingDate(): Observable<string> {
    return this.http.get<string>(`${this.apiUrl}/first-meeting-date`);
}
}