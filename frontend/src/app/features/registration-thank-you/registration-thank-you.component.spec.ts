import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RegistrationThankYouComponent } from './registration-thank-you.component';

describe('RegistrationThankYouComponent', () => {
  let component: RegistrationThankYouComponent;
  let fixture: ComponentFixture<RegistrationThankYouComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RegistrationThankYouComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RegistrationThankYouComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
