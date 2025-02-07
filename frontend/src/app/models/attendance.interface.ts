export interface Attendance {
    id?: number;
    userId: number;
    meetingDate: Date;
    joinTime: Date;
    leaveTime: Date;
    durationMinutes: number;
    present: boolean;
}