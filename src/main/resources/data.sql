INSERT INTO users(username,name,role ) VALUES ('caregiver1', 'Pietje', 'CAREGIVER');
INSERT INTO users(username,name,role, caregiver_id ) VALUES ('client1','Marie','CLIENT',1 );
INSERT INTO users(username,name,role, caregiver_id ) VALUES ('client2','Kees','CLIENT',1 );

INSERT INTO schedules (date, user_id, name) VALUES ('2026-10-20', 2, 'schooldag');
INSERT INTO schedules (date, user_id, name) VALUES ('2026-10-10', 3, 'weekend');

INSERT INTO progress_reports (status) VALUES ('OPEN');
INSERT INTO progress_reports (status) VALUES ('BEZIG');
INSERT INTO progress_reports (status) VALUES ('AFGEROND');
INSERT INTO progress_reports (status) VALUES ('AFGEROND');

INSERT INTO tasks(title,day_part,sort_order,schedule_id,progress_id) VALUES ('Tandenpoetsen', 'OCHTEND', 1,1,1);
INSERT INTO tasks(title,day_part,sort_order,schedule_id,progress_id) VALUES ('Aankleden', 'OCHTEND', 2,1,2);
INSERT INTO tasks(title,day_part,sort_order,schedule_id,progress_id) VALUES ('Huiswerk maken', 'MIDDAG', 1,1,3);
INSERT INTO tasks(title,day_part,sort_order,schedule_id,progress_id) VALUES ('Kamer opruimen', 'MIDDAG', 1,2,4);

INSERT INTO subtasks(title,completed,sort_order,task_id) VALUES ('Pak je tandenborstel',false,1,1);
INSERT INTO subtasks(title,completed,sort_order,task_id) VALUES ('Pak de tandpasta',false,2,1);
INSERT INTO subtasks(title,completed,sort_order,task_id) VALUES ('Doe de tandpasta op je tandenborstel',false,3,1);
INSERT INTO subtasks(title,completed,sort_order,task_id) VALUES ('Kies een broek',true,1,2);
INSERT INTO subtasks(title,completed,sort_order,task_id) VALUES ('Kies een trui',true,2,2);
INSERT INTO subtasks(title,completed,sort_order,task_id) VALUES ('Trek je broek aan',false,3,2);
INSERT INTO subtasks(title,completed,sort_order,task_id) VALUES ('Pak een pen',true,1,3);
INSERT INTO subtasks(title,completed,sort_order,task_id) VALUES ('Pak een papier',true,2,3);
INSERT INTO subtasks(title,completed,sort_order,task_id) VALUES ('Pak het huiswerkblad',true,3,3);
INSERT INTO subtasks(title,completed,sort_order,task_id) VALUES ('Schrijf je antwoorden op het papier',true,4,3);
INSERT INTO subtasks(title,completed,sort_order,task_id) VALUES ('Sorteer de losse spullen van je bureau',true,1,4);
INSERT INTO subtasks(title,completed,sort_order,task_id) VALUES ('Doe de lego in de legobak',true,2,4);
INSERT INTO subtasks(title,completed,sort_order,task_id) VALUES ('Leg de tekeningen in je bureaula',true,3,4);

