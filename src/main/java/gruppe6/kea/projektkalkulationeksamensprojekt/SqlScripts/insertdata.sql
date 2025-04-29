-- Opretter testdata i Profile tabellen med timeløn og ændret adgangskode
INSERT INTO Profile (PROFILE_USERNAME, PROFILE_NAME, PROFILE_PASSWORD, PROFILE_AUTH_CODE, PROFILE_SALARY)
VALUES
    ('john_doe', 'John Doe', '123', 1, 25.00),
    ('jane_doe', 'Jane Doe', '123', 2, 30.00),
    ('admin', 'Admin Bruger', '123', 0, 35.00),
    ('alice_smith', 'Alice Smith', '123', 2, 28.00),
    ('bob_jones', 'Bob Jones', '123', 1, 40.00),
    ('charlie_brown', 'Charlie Brown', '123', 2, 32.00);

-- Opretter testdata i Project tabellen
INSERT INTO Project (PROJECT_OWNER_PROFILE_USERNAME, PROJECT_NAME, PROJECT_DESC, PROJECT_MAX_TIME, PROJECT_MAX_PRICE, PROJECT_ENDDATE)
VALUES
    ('john_doe', 'Web Development', 'Projekt for udvikling af en hjemmeside', 100.00, 2000.00, '2025-12-31'),
    ('john_doe', 'App Development', 'Projekt for udvikling af en mobilapplikation', 150.00, 3000.00, '2025-10-15'),
    ('bob_jones', 'E-commerce Platform', 'Udvikling af en e-commerce platform', 120.00, 4000.00, '2025-11-20'),
    ('bob_jones', 'AI Integration', 'Integrering af kunstig intelligens i en eksisterende applikation', 200.00, 5000.00, '2025-09-30'),
    ('john_doe', 'Cloud Migration', 'Migrering af servere til skyen', 180.00, 6000.00, '2025-07-15');

-- Opretter testdata i Task tabellen
INSERT INTO Task (TASK_PROJECT_ID, TASK_NAME, TASK_DESC, TASK_MAX_TIME, TASK_MAX_PRICE)
VALUES
    ((SELECT PROJECT_ID FROM Project WHERE PROJECT_NAME = 'Web Development'), 'Design Website', 'Designe layout og brugergrænseflade for hjemmesiden', 50.00, 1000.00),
    ((SELECT PROJECT_ID FROM Project WHERE PROJECT_NAME = 'App Development'), 'Develop API', 'Udvikling af API til appen', 75.00, 1500.00),
    ((SELECT PROJECT_ID FROM Project WHERE PROJECT_NAME = 'E-commerce Platform'), 'Set Up Payment Gateway', 'Opsætning af betalingsgateway til platformen', 40.00, 800.00),
    ((SELECT PROJECT_ID FROM Project WHERE PROJECT_NAME = 'E-commerce Platform'), 'Design User Flow', 'Design af brugerflow og interaktioner', 60.00, 1200.00),
    ((SELECT PROJECT_ID FROM Project WHERE PROJECT_NAME = 'AI Integration'), 'Train Model', 'Træning af maskinlæringsmodel', 100.00, 2000.00),
    ((SELECT PROJECT_ID FROM Project WHERE PROJECT_NAME = 'Cloud Migration'), 'Cloud Architecture Design', 'Design af cloud-arkitektur', 150.00, 3000.00);

-- Opretter testdata i Subtask tabellen
INSERT INTO Subtask (SUBTASK_TASK_ID, SUBTASK_NAME, SUBTASK_DESC, SUBTASK_TIME, SUBTASK_STATUS)
VALUES
    ((SELECT TASK_ID FROM Task WHERE TASK_NAME = 'Design Website'), 'Create Mockup', 'Lav et mockup af hjemmesiden', 20.00, 1),
    ((SELECT TASK_ID FROM Task WHERE TASK_NAME = 'Develop API'), 'Setup Authentication', 'Opsæt brugerautentifikation for API', 30.00, 1),
    ((SELECT TASK_ID FROM Task WHERE TASK_NAME = 'Set Up Payment Gateway'), 'Integrate Stripe', 'Integrer Stripe betalingssystemet', 25.00, 1),
    ((SELECT TASK_ID FROM Task WHERE TASK_NAME = 'Design User Flow'), 'Create Flow Diagram', 'Lav diagram af brugerflow', 35.00, 1),
    ((SELECT TASK_ID FROM Task WHERE TASK_NAME = 'Train Model'), 'Preprocess Data', 'Forbehandling af data før træning', 50.00, 1),
    ((SELECT TASK_ID FROM Task WHERE TASK_NAME = 'Cloud Architecture Design'), 'Choose Cloud Provider', 'Vælg cloud-udbyder til projektet', 40.00, 1);

-- Opretter testdata i Skill tabellen
INSERT INTO Skill (SKILL_NAME)
VALUES
    ('HTML'), ('CSS'), ('JavaScript'), ('Java'), ('SQL'), ('Python'), ('Machine Learning'), ('Cloud Architecture'), ('UI/UX Design'), ('Payment Integration');

-- Opretter testdata i Profile_Skill tabellen
INSERT INTO Profile_Skill (PROFILE_USERNAME, SKILL_ID)
VALUES
    ('john_doe', (SELECT SKILL_ID FROM Skill WHERE SKILL_NAME = 'HTML')),
    ('john_doe', (SELECT SKILL_ID FROM Skill WHERE SKILL_NAME = 'CSS')),
    ('jane_doe', (SELECT SKILL_ID FROM Skill WHERE SKILL_NAME = 'Java')),
    ('jane_doe', (SELECT SKILL_ID FROM Skill WHERE SKILL_NAME = 'SQL')),
    ('alice_smith', (SELECT SKILL_ID FROM Skill WHERE SKILL_NAME = 'Python')),
    ('alice_smith', (SELECT SKILL_ID FROM Skill WHERE SKILL_NAME = 'Machine Learning')),
    ('bob_jones', (SELECT SKILL_ID FROM Skill WHERE SKILL_NAME = 'Cloud Architecture')),
    ('bob_jones', (SELECT SKILL_ID FROM Skill WHERE SKILL_NAME = 'UI/UX Design')),
    ('charlie_brown', (SELECT SKILL_ID FROM Skill WHERE SKILL_NAME = 'Payment Integration')),
    ('admin', (SELECT SKILL_ID FROM Skill WHERE SKILL_NAME = 'JavaScript'));

-- Opretter testdata i Profile_Project tabellen
INSERT INTO Profile_Project (PROJECT_ID, PROFILE_USERNAME)
VALUES
    ((SELECT PROJECT_ID FROM Project WHERE PROJECT_NAME = 'Web Development'), 'john_doe'),
    ((SELECT PROJECT_ID FROM Project WHERE PROJECT_NAME = 'App Development'), 'john_doe'),
    ((SELECT PROJECT_ID FROM Project WHERE PROJECT_NAME = 'E-commerce Platform'), 'bob_jones'),
    ((SELECT PROJECT_ID FROM Project WHERE PROJECT_NAME = 'AI Integration'), 'bob_jones'),
    ((SELECT PROJECT_ID FROM Project WHERE PROJECT_NAME = 'Cloud Migration'), 'john_doe'),
    ((SELECT PROJECT_ID FROM Project WHERE PROJECT_NAME = 'Web Development'), 'jane_doe'),
    ((SELECT PROJECT_ID FROM Project WHERE PROJECT_NAME = 'App Development'), 'alice_smith'),
    ((SELECT PROJECT_ID FROM Project WHERE PROJECT_NAME = 'E-commerce Platform'), 'charlie_brown');

-- Opretter testdata i Subtask_Profile tabellen (kun arbejdere tildeles subtasks)
INSERT INTO Subtask_Profile (SUBTASK_ID, PROFILE_USERNAME)
VALUES
    ((SELECT SUBTASK_ID FROM Subtask WHERE SUBTASK_NAME = 'Create Mockup'), 'jane_doe'),  -- erstattet john_doe
    ((SELECT SUBTASK_ID FROM Subtask WHERE SUBTASK_NAME = 'Setup Authentication'), 'jane_doe'),
    ((SELECT SUBTASK_ID FROM Subtask WHERE SUBTASK_NAME = 'Integrate Stripe'), 'alice_smith'),
    ((SELECT SUBTASK_ID FROM Subtask WHERE SUBTASK_NAME = 'Create Flow Diagram'), 'charlie_brown'),  -- erstattet bob_jones
    ((SELECT SUBTASK_ID FROM Subtask WHERE SUBTASK_NAME = 'Preprocess Data'), 'charlie_brown');
