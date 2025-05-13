-- Profile table
CREATE TABLE Profile (
                         PROFILE_USERNAME VARCHAR(25) PRIMARY KEY,
                         PROFILE_NAME VARCHAR(30),
                         PROFILE_PASSWORD VARCHAR(60) NOT NULL,
                         PROFILE_AUTH_CODE INTEGER NOT NULL,
                         PROFILE_SALARY DOUBLE NOT NULL
);

-- Project table
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

-- Task table
CREATE TABLE Task (
                      TASK_ID CHAR(36) PRIMARY KEY,
                      TASK_PROJECT_ID CHAR(36) NOT NULL,
                      TASK_NAME VARCHAR(50) NOT NULL,
                      TASK_DESC VARCHAR(500),
                      TASK_MAX_TIME DOUBLE NOT NULL,
                      TASK_MAX_PRICE DOUBLE NOT NULL,
                      FOREIGN KEY (TASK_PROJECT_ID) REFERENCES Project(PROJECT_ID) ON DELETE CASCADE
);

-- Subtask table
CREATE TABLE Subtask (
                         SUBTASK_ID CHAR(36) PRIMARY KEY,
                         SUBTASK_TASK_ID CHAR(36) NOT NULL,
                         SUBTASK_NAME VARCHAR(50) NOT NULL,
                         SUBTASK_DESC VARCHAR(500),
                         SUBTASK_TIME DOUBLE NOT NULL,
                         SUBTASK_STATUS INTEGER NOT NULL DEFAULT 1,
                         FOREIGN KEY (SUBTASK_TASK_ID) REFERENCES Task(TASK_ID) ON DELETE CASCADE
);

-- Skill table
CREATE TABLE Skill (
                       SKILL_ID CHAR(36) PRIMARY KEY,
                       SKILL_NAME VARCHAR(30) UNIQUE NOT NULL
);

-- Profile_Skill table
CREATE TABLE Profile_Skill (
                               PROFILE_USERNAME VARCHAR(25) NOT NULL,
                               SKILL_ID CHAR(36) NOT NULL,
                               PRIMARY KEY (PROFILE_USERNAME, SKILL_ID),
                               FOREIGN KEY (PROFILE_USERNAME) REFERENCES Profile(PROFILE_USERNAME) ON DELETE CASCADE,
                               FOREIGN KEY (SKILL_ID) REFERENCES Skill(SKILL_ID) ON DELETE CASCADE
);

-- Profile_Project table
CREATE TABLE Profile_Project (
                                 PROJECT_ID CHAR(36) NOT NULL,
                                 PROFILE_USERNAME VARCHAR(25) NOT NULL,
                                 PRIMARY KEY (PROFILE_USERNAME, PROJECT_ID),
                                 FOREIGN KEY (PROFILE_USERNAME) REFERENCES Profile(PROFILE_USERNAME) ON DELETE CASCADE,
                                 FOREIGN KEY (PROJECT_ID) REFERENCES Project(PROJECT_ID) ON DELETE CASCADE
);

-- Subtask_Profile table
CREATE TABLE Subtask_Profile (
                                 SUBTASK_ID CHAR(36) NOT NULL,
                                 PROFILE_USERNAME VARCHAR(25) NOT NULL,
                                 PRIMARY KEY (PROFILE_USERNAME, SUBTASK_ID),
                                 FOREIGN KEY (PROFILE_USERNAME) REFERENCES Profile(PROFILE_USERNAME) ON DELETE CASCADE,
                                 FOREIGN KEY (SUBTASK_ID) REFERENCES Subtask(SUBTASK_ID) ON DELETE CASCADE
);
INSERT INTO Profile (
    PROFILE_USERNAME,
    PROFILE_NAME,
    PROFILE_PASSWORD,
    PROFILE_AUTH_CODE,
    PROFILE_SALARY
) VALUES (
             'admin',
             'Admin User',
             'password123',  -- Replace with a hashed password in production
             0,
             60000.00
         );

