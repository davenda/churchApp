-- Insert sample users
INSERT INTO users (first_name, last_name, baptism_name, state, country, city, phone_number, email, church_name, is_active, role, cohort, created_at, updated_at) VALUES
-- 1st Cohort Members
('Abebe', 'Kebede', 'Gabriel', 'California', 'USA', 'San Jose', '+14081234567', 'abebe.k@example.com', 'St. Mary Church', true, 'USER', '1st', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Tigist', 'Haile', 'Mary', 'Texas', 'USA', 'Dallas', '+12141234567', 'tigist.h@example.com', 'Holy Trinity Church', true, 'USER', '1st', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Dawit', 'Gizaw', 'Daniel', 'Virginia', 'USA', 'Alexandria', '+17031234567', 'dawit.g@example.com', 'Debre Selam Medhanealem', true, 'ADMIN', '1st', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Bethlehem', 'Alemu', 'Elizabeth', 'Maryland', 'USA', 'Silver Spring', '+13011234567', 'beth.a@example.com', 'St. Michael Church', true, 'USER', '1st', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Solomon', 'Tadesse', 'Paul', 'Georgia', 'USA', 'Atlanta', '+14041234567', 'solomon.t@example.com', 'Debre Bisrat St. Gabriel', true, 'USER', '1st', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Hirut', 'Bekele', 'Sarah', 'Washington', 'USA', 'Seattle', '+12061234567', 'hirut.b@example.com', 'St. Gebriel Church', true, 'USER', '1st', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Yonas', 'Desta', 'John', 'Colorado', 'USA', 'Denver', '+13031234567', 'yonas.d@example.com', 'Medhane Alem Church', true, 'USER', '1st', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Martha', 'Worku', 'Ruth', 'Minnesota', 'USA', 'Minneapolis', '+16121234567', 'martha.w@example.com', 'St. Mary Church', true, 'USER', '1st', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Binyam', 'Mengist', 'Thomas', 'Illinois', 'USA', 'Chicago', '+13121234567', 'binyam.m@example.com', 'Debre Mehret St. Michael', true, 'USER', '1st', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Helen', 'Yilma', 'Hannah', 'Arizona', 'USA', 'Phoenix', '+16021234567', 'helen.y@example.com', 'Kidane Mihret Church', true, 'USER', '1st', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Henok', 'Assefa', 'Peter', 'Nevada', 'USA', 'Las Vegas', '+17021234567', 'henok.a@example.com', 'St. Gebriel Church', true, 'USER', '1st', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Sara', 'Tesfaye', 'Rebecca', 'New York', 'USA', 'New York', '+12121234567', 'sara.t@example.com', 'Debre Selam Church', true, 'USER', '1st', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Yared', 'Mulugeta', 'Matthew', 'Massachusetts', 'USA', 'Boston', '+16171234567', 'yared.m@example.com', 'Holy Trinity Church', true, 'USER', '1st', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Rahel', 'Girma', 'Esther', 'Pennsylvania', 'USA', 'Philadelphia', '+12151234567', 'rahel.g@example.com', 'St. Michael Church', true, 'USER', '1st', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Samuel', 'Tilahun', 'David', 'Michigan', 'USA', 'Detroit', '+13131234567', 'samuel.t@example.com', 'Medhane Alem Church', true, 'USER', '1st', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

-- 2nd Cohort Members
('Meron', 'Abebe', 'Mary', 'Texas', 'USA', 'Houston', '+12811234567', 'meron.a@example.com', 'St. Mary Church', true, 'USER', '2nd', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Tewodros', 'Negash', 'Theodore', 'California', 'USA', 'Oakland', '+15101234567', 'tewodros.n@example.com', 'Holy Trinity Church', true, 'USER', '2nd', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Selamawit', 'Mengistu', 'Peace', 'Virginia', 'USA', 'Richmond', '+18041234567', 'selamawit.m@example.com', 'Debre Haile Church', true, 'USER', '2nd', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Bereket', 'Tekle', 'Blessed', 'Maryland', 'USA', 'Baltimore', '+14101234567', 'bereket.t@example.com', 'St. Gabriel Church', true, 'USER', '2nd', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Feven', 'Zewde', 'Light', 'Georgia', 'USA', 'Marietta', '+17701234567', 'feven.z@example.com', 'Kidane Mihret Church', true, 'USER', '2nd', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Kibrom', 'Hailu', 'Isaac', 'Florida', 'USA', 'Miami', '+13051234567', 'kibrom.h@example.com', 'St. Michael Church', true, 'ADMIN', '2nd', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Selam', 'Teshome', 'Peace', 'California', 'USA', 'Sacramento', '+19161234567', 'selam.t@example.com', 'Debre Selam Church', true, 'USER', '2nd', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Ermias', 'Bogale', 'Jeremiah', 'Texas', 'USA', 'Austin', '+15121234567', 'ermias.b@example.com', 'Holy Trinity Church', true, 'USER', '2nd', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Mahlet', 'Debebe', 'Maria', 'Washington', 'USA', 'Tacoma', '+12531234567', 'mahlet.d@example.com', 'St. Mary Church', true, 'USER', '2nd', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Yohannes', 'Kifle', 'John', 'Illinois', 'USA', 'Aurora', '+16301234567', 'yohannes.k@example.com', 'Debre Mehret Church', true, 'USER', '2nd', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

-- 3rd Cohort Members
('Naod', 'Lemma', 'Nathan', 'Florida', 'USA', 'Orlando', '+14071234567', 'naod.l@example.com', 'St. Mary Church', true, 'USER', '3rd', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Hiwot', 'Demelash', 'Life', 'North Carolina', 'USA', 'Charlotte', '+17041234567', 'hiwot.d@example.com', 'Debre Sina Church', true, 'USER', '3rd', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Tsion', 'Alemayehu', 'Zion', 'Georgia', 'USA', 'Alpharetta', '+17701234568', 'tsion.a@example.com', 'St. Gabriel Church', true, 'USER', '3rd', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Bemnet', 'Belay', 'Benedict', 'Texas', 'USA', 'Arlington', '+18171234567', 'bemnet.b@example.com', 'Holy Trinity Church', true, 'USER', '3rd', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Lidya', 'Tessema', 'Lydia', 'California', 'USA', 'Fresno', '+15591234567', 'lidya.t@example.com', 'St. Michael Church', true, 'USER', '3rd', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Kirubel', 'Wasihun', 'Gabriel', 'Virginia', 'USA', 'Norfolk', '+17571234567', 'kirubel.w@example.com', 'Debre Selam Church', true, 'ADMIN', '3rd', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Rediet', 'Mekonnen', 'Martha', 'Maryland', 'USA', 'Rockville', '+13011234568', 'rediet.m@example.com', 'St. Mary Church', true, 'USER', '3rd', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Eyob', 'Wolde', 'Job', 'Washington', 'USA', 'Bellevue', '+14251234567', 'eyob.w@example.com', 'Kidane Mihret Church', true, 'USER', '3rd', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Mehret', 'Kebede', 'Mercy', 'Illinois', 'USA', 'Naperville', '+16301234568', 'mehret.k@example.com', 'Holy Trinity Church', true, 'USER', '3rd', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Biruk', 'Eshetu', 'Blessed', 'New York', 'USA', 'Brooklyn', '+17181234567', 'biruk.e@example.com', 'St. Michael Church', true, 'USER', '3rd', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

-- 4th Cohort Members
('Eden', 'Mamo', 'Paradise', 'Texas', 'USA', 'Plano', '+14691234567', 'eden.m@example.com', 'Debre Selam Church', true, 'USER', '4th', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Abel', 'Getachew', 'Abel', 'California', 'USA', 'San Diego', '+16191234567', 'abel.g@example.com', 'St. Mary Church', true, 'USER', '4th', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Hanna', 'Feyisa', 'Hannah', 'Virginia', 'USA', 'Reston', '+17031234568', 'hanna.f@example.com', 'Holy Trinity Church', true, 'USER', '4th', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Amanuel', 'Demissie', 'Emmanuel', 'Georgia', 'USA', 'Decatur', '+14041234568', 'amanuel.d@example.com', 'St. Gabriel Church', true, 'ADMIN', '4th', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Leah', 'Solomon', 'Leah', 'Washington', 'USA', 'Kent', '+12531234568', 'leah.s@example.com', 'Debre Mehret Church', true, 'USER', '4th', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

-- 5th Cohort Members
('Caleb', 'Tiruneh', 'Caleb', 'Maryland', 'USA', 'Bethesda', '+13011234569', 'caleb.t@example.com', 'St. Michael Church', true, 'USER', '5th', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Ruth', 'Arega', 'Ruth', 'Illinois', 'USA', 'Evanston', '+18471234567', 'ruth.a@example.com', 'St. Mary Church', true, 'USER', '5th', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Michael', 'Gebre', 'Michael', 'Texas', 'USA', 'Irving', '+19721234567', 'michael.g@example.com', 'Holy Trinity Church', true, 'USER', '5th', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Sarah', 'Dagne', 'Sarah', 'California', 'USA', 'Berkeley', '+15101234568', 'sarah.d@example.com', 'Debre Selam Church', true, 'USER', '5th', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Daniel', 'Kassahun', 'Daniel', 'Virginia', 'USA', 'Falls Church', '+17031234569', 'daniel.k@example.com', 'St. Gabriel Church', true, 'ADMIN', '5th', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Rebecca', 'Tefera', 'Rebecca', 'Georgia', 'USA', 'Roswell', '+17701234569', 'rebecca.t@example.com', 'Kidane Mihret Church', true, 'USER', '5th', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Nathan', 'Wondemu', 'Nathan', 'Washington', 'USA', 'Renton', '+14251234568', 'nathan.w@example.com', 'St. Mary Church', true, 'USER', '5th', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Grace', 'Fekadu', 'Grace', 'Maryland', 'USA', 'Columbia', '+14101234568', 'grace.f@example.com', 'Holy Trinity Church', true, 'USER', '5th', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Joseph', 'Berhane', 'Joseph', 'Illinois', 'USA', 'Oak Park', '+17081234567', 'joseph.b@example.com', 'St. Michael Church', true, 'USER', '5th', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Elizabeth', 'Abate', 'Elizabeth', 'New York', 'USA', 'Queens', '+17181234568', 'elizabeth.a@example.com', 'Debre Selam Church', true, 'USER', '5th', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Delete existing data
DELETE FROM attendance;
DELETE FROM cohort_meetings;

-- Insert cohort meetings with their specific days
INSERT INTO cohort_meetings (cohort, meeting_id, meeting_day, active) VALUES
                                                                          ('1st', '85749632145', 1, true),    -- Monday for 1st cohort
                                                                          ('2nd', '85749632146', 2, true),    -- Tuesday for 2nd cohort
                                                                          ('3rd', '85749632147', 3, true),    -- Wednesday for 3rd cohort
                                                                          ('4th', '85749632148', 4, true),    -- Thursday for 4th cohort
                                                                          ('5th', '85749632149', 5, true);    -- Friday for 5th cohort

-- For each user in 1st Cohort (Mondays)
INSERT INTO attendance (user_id, cohort, meeting_date)
SELECT u.id, u.cohort, m.meeting_date
FROM users u
         CROSS JOIN (
    VALUES
        ('2025-01-13 19:00:00'::timestamp),
        ('2025-01-20 19:00:00'::timestamp),
        ('2025-01-27 19:00:00'::timestamp),
        ('2025-02-03 19:00:00'::timestamp)
) AS m(meeting_date)
WHERE u.cohort = '1st'
  AND (random() > 0.3);  -- 70% chance of attendance

-- For 2nd Cohort (Tuesdays)
INSERT INTO attendance (user_id, cohort, meeting_date)
SELECT u.id, u.cohort, m.meeting_date
FROM users u
         CROSS JOIN (
    VALUES
        ('2025-01-14 19:00:00'::timestamp),
        ('2025-01-21 19:00:00'::timestamp),
        ('2025-01-28 19:00:00'::timestamp),
        ('2025-02-04 19:00:00'::timestamp)
) AS m(meeting_date)
WHERE u.cohort = '2nd'
  AND (random() > 0.3);

-- For 3rd Cohort (Wednesdays)
INSERT INTO attendance (user_id, cohort, meeting_date)
SELECT u.id, u.cohort, m.meeting_date
FROM users u
         CROSS JOIN (
    VALUES
        ('2025-01-15 19:00:00'::timestamp),
        ('2025-01-22 19:00:00'::timestamp),
        ('2025-01-29 19:00:00'::timestamp),
        ('2025-02-05 19:00:00'::timestamp)
) AS m(meeting_date)
WHERE u.cohort = '3rd'
  AND (random() > 0.3);

-- For 4th Cohort (Thursdays)
INSERT INTO attendance (user_id, cohort, meeting_date)
SELECT u.id, u.cohort, m.meeting_date
FROM users u
         CROSS JOIN (
    VALUES
        ('2025-01-16 19:00:00'::timestamp),
        ('2025-01-23 19:00:00'::timestamp),
        ('2025-01-30 19:00:00'::timestamp),
        ('2025-02-06 19:00:00'::timestamp)
) AS m(meeting_date)
WHERE u.cohort = '4th'
  AND (random() > 0.3);

-- For 5th Cohort (Fridays)
INSERT INTO attendance (user_id, cohort, meeting_date)
SELECT u.id, u.cohort, m.meeting_date
FROM users u
         CROSS JOIN (
    VALUES
        ('2025-01-17 19:00:00'::timestamp),
        ('2025-01-24 19:00:00'::timestamp),
        ('2025-01-31 19:00:00'::timestamp),
        ('2025-02-07 19:00:00'::timestamp)
) AS m(meeting_date)
WHERE u.cohort = '5th'
  AND (random() > 0.3);

-- Add default admin (password: admin123)
INSERT INTO admins (first_name, last_name, email, password)
VALUES ('Admin', 'User', 'admin@beteyared.com',
        '$2a$10$bymeLpgVW9PtMm/YOpl8v.Ik6oLgIx/4vd8MzDMTGG2jP2wIrBnnO');