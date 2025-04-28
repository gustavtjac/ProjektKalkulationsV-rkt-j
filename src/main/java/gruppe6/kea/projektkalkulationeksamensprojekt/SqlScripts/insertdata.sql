-- Opretter testdata i Profile tabellen med timeløn og ændret adgangskode
INSERT INTO Profile (PROFILE_USERNAME, PROFILE_NAME, PROFILE_PASSWORD, PROFILE_AUTH_CODE, PROFILE_SALARY)
VALUES
    ('john_doe', 'John Doe', '123', 1, 25.00),  -- Timeløn: 25 kr. per time
    ('jane_doe', 'Jane Doe', '123', 2, 30.00),  -- Timeløn: 30 kr. per time
    ('admin', 'Admin User', '123', 0, 35.00);  -- Timeløn: 35 kr. per time

-- Opretter testdata i Project tabellen
INSERT INTO Project (PROJECT_OWNER_PROFILE_USERNAME, PROJECT_NAME, PROJECT_DESC, PROJECT_MAX_TIME, PROJECT_MAX_PRICE, PROJECT_ENDDATE)
VALUES
    ('john_doe', 'Web Development', 'Projekt for udvikling af en hjemmeside', 100.00, 2000.00, '2025-12-31'),
    ('jane_doe', 'App Development', 'Projekt for udvikling af en mobilapplikation', 150.00, 3000.00, '2025-10-15');

-- Opretter testdata i Task tabellen
INSERT INTO Task (TASK_PROJECT_ID, TASK_NAME, TASK_DESC, TASK_MAX_TIME, TASK_MAX_PRICE)
VALUES
    ((SELECT PROJECT_ID FROM Project WHERE PROJECT_NAME = 'Web Development'), 'Design Website', 'Designe layout og brugergrænseflade for hjemmesiden', 50.00, 1000.00),
    ((SELECT PROJECT_ID FROM Project WHERE PROJECT_NAME = 'App Development'), 'Develop API', 'Udvikling af API til appen', 75.00, 1500.00);

-- Opretter testdata i Subtask tabellen
INSERT INTO Subtask (SUBTASK_TASK_ID, SUBTASK_NAME, SUBTASK_DESC, SUBTASK_TIME, SUBTASK_STATUS)
VALUES
    ((SELECT TASK_ID FROM Task WHERE TASK_NAME = 'Design Website'), 'Create Mockup', 'Lav et mockup af hjemmesiden', 20.00, 1),
    ((SELECT TASK_ID FROM Task WHERE TASK_NAME = 'Develop API'), 'Setup Authentication', 'Opsæt brugerautentifikation for API', 30.00, 1);

-- Opretter testdata i Skill tabellen
INSERT INTO Skill (SKILL_NAME)
VALUES
    ('HTML'), ('CSS'), ('JavaScript'), ('Java'), ('SQL');

-- Opretter testdata i Profile_Skill tabellen
INSERT INTO Profile_Skill (PROFILE_USERNAME, SKILL_ID)
VALUES
    ('john_doe', (SELECT SKILL_ID FROM Skill WHERE SKILL_NAME = 'HTML')),
    ('john_doe', (SELECT SKILL_ID FROM Skill WHERE SKILL_NAME = 'CSS')),
    ('jane_doe', (SELECT SKILL_ID FROM Skill WHERE SKILL_NAME = 'Java')),
    ('jane_doe', (SELECT SKILL_ID FROM Skill WHERE SKILL_NAME = 'SQL')),
    ('admin', (SELECT SKILL_ID FROM Skill WHERE SKILL_NAME = 'JavaScript'));

-- Opretter testdata i Profile_Project tabellen
INSERT INTO Profile_Project (PROJECT_ID, PROFILE_USERNAME)
VALUES
    ((SELECT PROJECT_ID FROM Project WHERE PROJECT_NAME = 'Web Development'), 'john_doe'),
    ((SELECT PROJECT_ID FROM Project WHERE PROJECT_NAME = 'App Development'), 'jane_doe');

-- Opretter testdata i Subtask_Profile tabellen
INSERT INTO Subtask_Profile (SUBTASK_ID, PROFILE_USERNAME)
VALUES
    ((SELECT SUBTASK_ID FROM Subtask WHERE SUBTASK_NAME = 'Create Mockup'), 'john_doe'),
    ((SELECT SUBTASK_ID FROM Subtask WHERE SUBTASK_NAME = 'Setup Authentication'), 'jane_doe');
