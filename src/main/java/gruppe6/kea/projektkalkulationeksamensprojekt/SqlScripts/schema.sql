DROP DATABASE IF EXISTS ProjectManagementDB;
CREATE DATABASE ProjectManagementDB;
USE ProjectManagementDB;

-- Opretter Profile tabel
CREATE TABLE Profile (
                         PROFILE_USERNAME VARCHAR(25) PRIMARY KEY,
                         PROFILE_NAME VARCHAR(30),
                         PROFILE_PASSWORD VARCHAR(60) NOT NULL,
                         PROFILE_AUTH_CODE INTEGER(1) NOT NULL
);

CREATE TABLE Project (
                         PROJECT_ID CHAR(36) PRIMARY KEY DEFAULT (UUID()),
                         PROJECT_OWNER_PROFILE_USERNAME VARCHAR(25) NOT NULL,
                         PROJECT_NAME VARCHAR(50) NOT NULL,
                         PROJECT_DESC VARCHAR(500),
                         PROJECT_MAX_TIME DOUBLE(30,2) NOT NULL,
                         PROJECT_MAX_PRICE DOUBLE(30,2) NOT NULL,
                         PROJECT_ENDDATE DATE not null,
                         FOREIGN KEY (PROJECT_OWNER_PROFILE_USERNAME) REFERENCES Profile(PROFILE_USERNAME)
);

-- Opretter Task tabel
CREATE TABLE Task (
                      TASK_ID CHAR(36) PRIMARY KEY DEFAULT (UUID()),
                      TASK_PROJECT_ID CHAR(36) NOT NULL,
                      TASK_NAME VARCHAR(50) NOT NULL,
                      TASK_DESC VARCHAR(500),
                      TASK_MAX_TIME DOUBLE(30,2) NOT NULL,
                      TASK_MAX_PRICE DOUBLE(30,2) NOT NULL,
                      FOREIGN KEY (TASK_PROJECT_ID) REFERENCES Project(PROJECT_ID)
);

-- Opretter Subtask tabel
CREATE TABLE Subtask (
                         SUBTASK_ID CHAR(36) PRIMARY KEY DEFAULT (UUID()),
                         SUBTASK_TASK_ID CHAR(36) NOT NULL,
                         SUBTASK_NAME VARCHAR(50) NOT NULL,
                         SUBTASK_DESC VARCHAR(500),
                         SUBTASK_TIME DOUBLE(30,2) NOT NULL,
                         SUBTASK_STATUS integer(1) not null default (1),
                         FOREIGN KEY (SUBTASK_TASK_ID) REFERENCES Task(TASK_ID)
);

-- Opretter  Skill tabel
CREATE TABLE Skill (
                       SKILL_ID CHAR(36) PRIMARY KEY DEFAULT (UUID()),
                       SKILL_NAME VARCHAR(30) UNIQUE NOT NULL
);

-- Opretter  Profile_Skill tabel
CREATE TABLE Profile_Skill (
                               PROFILE_USERNAME VARCHAR(25) NOT NULL,
                               SKILL_ID CHAR(36) NOT NULL,
                               PRIMARY KEY (PROFILE_USERNAME, SKILL_ID),
                               FOREIGN KEY (PROFILE_USERNAME) REFERENCES Profile(PROFILE_USERNAME) ON DELETE CASCADE,
                               FOREIGN KEY (SKILL_ID) REFERENCES Skill(SKILL_ID) ON DELETE CASCADE
);

-- Opretter  Profile_Project tabel
CREATE TABLE Profile_Project (
                                 PROJECT_ID CHAR(36) NOT NULL,
                                 PROFILE_USERNAME VARCHAR(25) NOT NULL,
                                 PRIMARY KEY (PROFILE_USERNAME, PROJECT_ID),
                                 FOREIGN KEY (PROFILE_USERNAME) REFERENCES Profile(PROFILE_USERNAME) ON DELETE CASCADE,
                                 FOREIGN KEY (PROJECT_ID) REFERENCES Project(PROJECT_ID) ON DELETE CASCADE
);

-- Opretter Subtask_Profile tabel
CREATE TABLE Subtask_Profile (
                                 SUBTASK_ID CHAR(36) NOT NULL,
                                 PROFILE_USERNAME VARCHAR(25) NOT NULL,
                                 PRIMARY KEY (PROFILE_USERNAME, SUBTASK_ID),
                                 FOREIGN KEY (PROFILE_USERNAME) REFERENCES Profile(PROFILE_USERNAME) ON DELETE CASCADE,
                                 FOREIGN KEY (SUBTASK_ID) REFERENCES Subtask(SUBTASK_ID) ON DELETE CASCADE
);