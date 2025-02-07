import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environments';

export interface MeetingDetails {
  topic: string;
  startTime: string;
  duration: number;
}

export interface Participant {
  email: string;
  firstName: string;
  lastName: string;
}

@Injectable({
  providedIn: 'root'
})
export class ZoomService {
  private apiUrl = `${environment.apiUrl}/api/zoom`;

  constructor(private http: HttpClient) {}

  createMeeting(meetingDetails: MeetingDetails, participants: Participant[]): Observable<any> {
    return this.http.post(`${this.apiUrl}/meetings`, {
      meetingDetails,
      participants
    });
  }

  getZoomToken(): Observable<{ token: string }> {
    return this.http.post<{ token: string }>(`${this.apiUrl}/token`, {});
  }

  addRegistrant(token: string, meetingId: string, participant: Participant): Observable<any> {
    return this.http.post(`${this.apiUrl}/meetings/${meetingId}/registrants`, {
      token,
      participant
    });
  }
}