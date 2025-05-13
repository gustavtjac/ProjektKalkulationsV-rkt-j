-- Sletter gamle tabeller
DROP TABLE IF EXISTS Subtask_Profile;
DROP TABLE IF EXISTS Profile_Project;
DROP TABLE IF EXISTS Profile_Skill;
DROP TABLE IF EXISTS Subtask;
DROP TABLE IF EXISTS Task;
DROP TABLE IF EXISTS Project;
DROP TABLE IF EXISTS Skill;
DROP TABLE IF EXISTS Profile;




-- Laver profile tabel
CREATE TABLE Profile (
                         PROFILE_USERNAME VARCHAR(25) PRIMARY KEY,
                         PROFILE_NAME VARCHAR(30),
                         PROFILE_PASSWORD VARCHAR(60) NOT NULL,
                         PROFILE_AUTH_CODE INTEGER NOT NULL,
                         PROFILE_SALARY DOUBLE NOT NULL
);

-- Laver Project tabel
CREATE TABLE Project (
                         PROJECT_ID CHAR(36) PRIMARY KEY,
                         PROJECT_OWNER_PROFILE_USERNAME VARCHAR(25) NOT NULL,
                         PROJECT_NAME VARCHAR(50) NOT NULL,
                         PROJECT_DESC VARCHAR(500),
                         PROJECT_MAX_TIME DOUBLE NOT NULL,
                         PROJECT_MAX_PRICE DOUBLE NOT NULL,
                         PROJECT_ENDDATE DATE NOT NULL,
                         FOREIGN KEY (PROJECT_OWNER_PROFILE_USERNAME) REFERENCES Profile(PROFILE_USERNAME) ON DELETE CASCADE
);
-- Laver Task tabel
CREATE TABLE Task (
                      TASK_ID CHAR(36) PRIMARY KEY,
                      TASK_PROJECT_ID CHAR(36) NOT NULL,
                      TASK_NAME VARCHAR(50) NOT NULL,
                      TASK_DESC VARCHAR(500),
                      TASK_MAX_TIME DOUBLE NOT NULL,
                      TASK_MAX_PRICE DOUBLE NOT NULL,
                      FOREIGN KEY (TASK_PROJECT_ID) REFERENCES Project(PROJECT_ID) ON DELETE CASCADE
);

-- Laver Subtask tabel
CREATE TABLE Subtask (
                         SUBTASK_ID CHAR(36) PRIMARY KEY,
                         SUBTASK_TASK_ID CHAR(36) NOT NULL,
                         SUBTASK_NAME VARCHAR(50) NOT NULL,
                         SUBTASK_DESC VARCHAR(500),
                         SUBTASK_TIME DOUBLE NOT NULL,
                         SUBTASK_STATUS INTEGER NOT NULL DEFAULT 1,
                         FOREIGN KEY (SUBTASK_TASK_ID) REFERENCES Task(TASK_ID) ON DELETE CASCADE
);

-- Laver Skill tabel
CREATE TABLE Skill (
                       SKILL_ID CHAR(36) PRIMARY KEY,
                       SKILL_NAME VARCHAR(30) UNIQUE NOT NULL
);

-- Laver Profile_Skill tabel
CREATE TABLE Profile_Skill (
                               PROFILE_USERNAME VARCHAR(25) NOT NULL,
                               SKILL_ID CHAR(36) NOT NULL,
                               PRIMARY KEY (PROFILE_USERNAME, SKILL_ID),
                               FOREIGN KEY (PROFILE_USERNAME) REFERENCES Profile(PROFILE_USERNAME) ON DELETE CASCADE,
                               FOREIGN KEY (SKILL_ID) REFERENCES Skill(SKILL_ID) ON DELETE CASCADE
);

-- Laver Profile_Project tabel
CREATE TABLE Profile_Project (
                                 PROJECT_ID CHAR(36) NOT NULL,
                                 PROFILE_USERNAME VARCHAR(25) NOT NULL,
                                 PRIMARY KEY (PROFILE_USERNAME, PROJECT_ID),
                                 FOREIGN KEY (PROFILE_USERNAME) REFERENCES Profile(PROFILE_USERNAME) ON DELETE CASCADE,
                                 FOREIGN KEY (PROJECT_ID) REFERENCES Project(PROJECT_ID) ON DELETE CASCADE
);

-- Laver Subtask_Profile tabel
CREATE TABLE Subtask_Profile (
                                 SUBTASK_ID CHAR(36) NOT NULL,
                                 PROFILE_USERNAME VARCHAR(25) NOT NULL,
                                 PRIMARY KEY (PROFILE_USERNAME, SUBTASK_ID),
                                 FOREIGN KEY (PROFILE_USERNAME) REFERENCES Profile(PROFILE_USERNAME) ON DELETE CASCADE,
                                 FOREIGN KEY (SUBTASK_ID) REFERENCES Subtask(SUBTASK_ID) ON DELETE CASCADE
);

-- indsætter noget testdata :)
INSERT INTO Profile (
    PROFILE_USERNAME,
    PROFILE_NAME,
    PROFILE_PASSWORD,
    PROFILE_AUTH_CODE,
    PROFILE_SALARY
) VALUES (
             'admin',
             'Admin User',
             'password123',
             0,
             60000.00
         );

-- 2. Insert a project
INSERT INTO Project (
    PROJECT_ID,
    PROJECT_OWNER_PROFILE_USERNAME,
    PROJECT_NAME,
    PROJECT_DESC,
    PROJECT_MAX_TIME,
    PROJECT_MAX_PRICE,
    PROJECT_ENDDATE
) VALUES (
             'project-123',
             'admin',
             'Test Project',
             'This is a test project.',
             100.0,
             100000.0,
             '2025-12-31'
         );

-- 3. Insert a task
INSERT INTO Task (
    TASK_ID,
    TASK_PROJECT_ID,
    TASK_NAME,
    TASK_DESC,
    TASK_MAX_TIME,
    TASK_MAX_PRICE
) VALUES (
             'task-123',
             'project-123',
             'Test Task',
             'This is a test task.',
             50.0,
             50000.0
         );

-- 4. Insert a subtask
INSERT INTO Subtask (
    SUBTASK_ID,
    SUBTASK_TASK_ID,
    SUBTASK_NAME,
    SUBTASK_DESC,
    SUBTASK_TIME,
    SUBTASK_STATUS
) VALUES (
             'test-id-123',
             'task-123',
             'Test Subtask',
             'This is a test subtask.',
             5.0,
             1
         );
INSERT INTO Skill (SKILL_ID, SKILL_NAME) VALUES
                                             ('1', 'Java Programming'),
                                             ('2', 'Spring Boot'),
                                             ('3', 'SQL'),
                                             ('4', 'Project Management');
