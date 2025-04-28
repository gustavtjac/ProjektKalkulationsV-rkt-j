-- Indsætter testprofiler
INSERT INTO Profile (PROFILE_USERNAME, PROFILE_NAME, PROFILE_PASSWORD, PROFILE_AUTH_CODE) VALUES
                                                                                              ('martin123', 'Martin Madsen', '123', 1),
                                                                                              ('sara456', 'Sara Sørensen', '123', 1),
                                                                                              ('ole789', 'Ole Olesen', '123', 2);

-- Indsætter testprojekter
INSERT INTO Project (PROJECT_OWNER_PROFILE_USERNAME, PROJECT_NAME, PROJECT_DESC, PROJECT_MAX_TIME, PROJECT_MAX_PRICE, PROJECT_ENDDATE) VALUES
                                                                                                                                           ('martin123', 'Website Redesign', 'Vi redesigner kundens hjemmeside.', 120.00, 25000.00, ADDDATE(CURDATE(), INTERVAL 1 YEAR)),
                                                                                                                                           ('sara456', 'App Udvikling', 'Udvikling af mobilapplikation.', 200.00, 75000.00, ADDDATE(CURDATE(), INTERVAL 1 YEAR));

-- Indsætter testopgaver (Tasks)
INSERT INTO Task (TASK_PROJECT_ID, TASK_NAME, TASK_DESC, TASK_MAX_TIME, TASK_MAX_PRICE) VALUES
                                                                                            ((SELECT PROJECT_ID FROM Project WHERE PROJECT_NAME = 'Website Redesign'), 'Frontend udvikling', 'Udvikle frontend med React.', 60.00, 12000.00),
                                                                                            ((SELECT PROJECT_ID FROM Project WHERE PROJECT_NAME = 'Website Redesign'), 'Backend udvikling', 'Udvikle backend API.', 60.00, 13000.00),
                                                                                            ((SELECT PROJECT_ID FROM Project WHERE PROJECT_NAME = 'App Udvikling'), 'Design af app', 'UX/UI design.', 80.00, 30000.00);

-- Indsætter testdelopgaver (Subtasks)
INSERT INTO Subtask (SUBTASK_TASK_ID, SUBTASK_NAME, SUBTASK_DESC, SUBTASK_TIME) VALUES
                                                                                    ((SELECT TASK_ID FROM Task WHERE TASK_NAME = 'Frontend udvikling'), 'Opsætte komponentstruktur', 'Oprette React komponenter.', 20.00),
                                                                                    ((SELECT TASK_ID FROM Task WHERE TASK_NAME = 'Frontend udvikling'), 'Responsivt design', 'Sikre mobilvenligt layout.', 20.00),
                                                                                    ((SELECT TASK_ID FROM Task WHERE TASK_NAME = 'Backend udvikling'), 'Database setup', 'Oprette MySQL database.', 30.00),
                                                                                    ((SELECT TASK_ID FROM Task WHERE TASK_NAME = 'Design af app'), 'Figma prototyper', 'Lave klikbare prototyper.', 40.00);

-- Indsætter testskills
INSERT INTO Skill (SKILL_NAME) VALUES
                                   ('HTML'),
                                   ('CSS'),
                                   ('JavaScript'),
                                   ('React'),
                                   ('MySQL'),
                                   ('Node.js');

-- Indsætter profiler-skills
INSERT INTO Profile_Skill (PROFILE_USERNAME, SKILL_ID) VALUES
                                                           ('martin123', (SELECT SKILL_ID FROM Skill WHERE SKILL_NAME = 'React')),
                                                           ('martin123', (SELECT SKILL_ID FROM Skill WHERE SKILL_NAME = 'JavaScript')),
                                                           ('sara456', (SELECT SKILL_ID FROM Skill WHERE SKILL_NAME = 'HTML')),
                                                           ('sara456', (SELECT SKILL_ID FROM Skill WHERE SKILL_NAME = 'CSS')),
                                                           ('ole789', (SELECT SKILL_ID FROM Skill WHERE SKILL_NAME = 'MySQL')),
                                                           ('ole789', (SELECT SKILL_ID FROM Skill WHERE SKILL_NAME = 'Node.js'));

-- Indsætter profiler på projekter
INSERT INTO Profile_Project (PROFILE_USERNAME, PROJECT_ID) VALUES
                                                               ('martin123', (SELECT PROJECT_ID FROM Project WHERE PROJECT_NAME = 'Website Redesign')),
                                                               ('sara456', (SELECT PROJECT_ID FROM Project WHERE PROJECT_NAME = 'Website Redesign')),
                                                               ('sara456', (SELECT PROJECT_ID FROM Project WHERE PROJECT_NAME = 'App Udvikling')),
                                                               ('ole789', (SELECT PROJECT_ID FROM Project WHERE PROJECT_NAME = 'App Udvikling'));

-- Indsætter profiler på subtasks
INSERT INTO Subtask_Profile (PROFILE_USERNAME, SUBTASK_ID) VALUES
                                                               ('martin123', (SELECT SUBTASK_ID FROM Subtask WHERE SUBTASK_NAME = 'Opsætte komponentstruktur')),
                                                               ('sara456', (SELECT SUBTASK_ID FROM Subtask WHERE SUBTASK_NAME = 'Responsivt design')),
                                                               ('ole789', (SELECT SUBTASK_ID FROM Subtask WHERE SUBTASK_NAME = 'Database setup')),
                                                               ('sara456', (SELECT SUBTASK_ID FROM Subtask WHERE SUBTASK_NAME = 'Figma prototyper'));
