DROP DATABASE IF EXISTS ProjectManagementDB;
CREATE DATABASE ProjectManagementDB;
USE ProjectManagementDB;

-- Opretter Profile Tabellen
CREATE TABLE Profile
(
PROFILE_USERNAME varchar(25) PRIMARY KEY unique,
 PROFILE_NAME varchar(30),
 PROFILE_PASSWORD varchar(60) not NULL,
 PROFILE_AUTH_CODE integer(1) not Null
);
-- Opretter Project tabellen
CREATE TABLE Project
(
PROJECT_ID CHAR(36) unique PRIMARY KEY DEFAULT (UUID()),
PROJECT_OWNER_PROFILE_USERNAME varchar(25) not null,
PROJECT_NAME varchar(50) not null,
PROJECT_DESC varchar(500),
PROJECT_MAX_TIME double(30,2) not null,
PROJECT_MAX_PRICE double(30,2) not null,
FOREIGN KEY (PROJECT_OWNER_PROFILE_USERNAME) references PROFILE (PROFILE_USERNAME)
);
CREATE TABLE Task
(
TASK_ID char(36) unique PRIMARY KEY DEFAULT (UUID()),
TASK_PROJECT_ID char(36) not null,
TASK_NAME varchar(50) not null,
TASK_DESC varchar (500),
TASK_MAX_TIME double(30,2) not null,
TASK_MAX_PRICE double(30,2) not null,
FOREIGN KEY (TASK_PROJECT_ID) REFERENCES PROJECT (PROJECT_ID)
);
CREATE TABLE SUBTASK
(
SUBTASK_ID char(36) unique PRIMARY KEY DEFAULT (UUID()),
SUBTASK_TASK_ID char(36) not null,
SUBTASK_NAME varchar(50) not null,
SUBTASK_DESC varchar(500),
SUBTASK_TIME double(30,2) not null,
foreign key (SUBTASK_TASK_ID) references TASK (TASK_ID)
);
CREATE TABLE SKILL
(
   SKILL_ID char(36) unique PRIMARY KEY DEFAULT (UUID()),
    SKILL_NAME varchar(30) unique not null
);


CREATE TABLE PROFILE_SKILL(
                                PROFILE_USERNAME varchar(25) not null ,
                                SKILL_ID char(36) not null,
                                PRIMARY KEY(PROFILE_USERNAME,SKILL_ID),
                                FOREIGN KEY(PROFILE_USERNAME) REFeRENCES Profile(PROFILE_USERNAME) ON DELETE CASCADE,
                                FOREIGN KEY(SKILL_ID) REFERENCES SKILL(SKILL_ID) ON DELETE CASCADE
);

CREATE TABLE PROFILE_PROJECT(
                              PROJECT_ID char(36) not null,
                              PROFILE_USERNAME varchar(25) not null,
                              PRIMARY KEY(PROFILE_USERNAME,PROJECT_ID),
                              FOREIGN KEY(PROFILE_USERNAME) REFeRENCES Profile(PROFILE_USERNAME) ON DELETE CASCADE,
                              FOREIGN KEY(PROJECT_ID) REFERENCES Project(PROJECT_ID) ON DELETE CASCADE
);

CREATE TABLE SUBTASK_PROFILE(
                                SUBTASK_ID char(36) not null,
                                PROFILE_USERNAME varchar(25) not null,
                                PRIMARY KEY(PROFILE_USERNAME,SUBTASK_ID),
                                FOREIGN KEY(PROFILE_USERNAME) REFeRENCES Profile(PROFILE_USERNAME) ON DELETE CASCADE,
                                FOREIGN KEY(SUBTASK_ID) REFERENCES SUBTASK(SUBTASK_ID) ON DELETE CASCADE
);



