import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environments';

@Injectable({
  providedIn: 'root'
})
export class SmsService {
  private apiUrl = `${environment.apiUrl}/api/sms`;

  constructor(private http: HttpClient) {}

  sendReminders(cohort: string): Observable<any> {
    return this.http.post(`${this.apiUrl}/send-reminders/${cohort}`, {});
  }

  sendCustomMessage(recipientType: string, message: string, cohort?: string, userId?: number): Observable<any> {
    const payload = {
      message,
      recipientType,
      cohort,
      userId
    };
    return this.http.post(`${this.apiUrl}/send-message`, payload);
  }
}