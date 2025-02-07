export interface User {
    id?: number;
    firstName: string;
    lastName: string;
    baptismName: string;
    state: string;
    country: string;
    city: string;
    phoneNumber: string;
    email: string;
    churchName: string;
    role: 'ADMIN' | 'USER';
    cohort: string;
}
