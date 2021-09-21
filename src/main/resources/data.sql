INSERT INTO TB_USER(name, email, password) VALUES('Aluno', 'aluno@email.com', '$2a$10$YUQ00zfy71uAeyWRDIqxye/xfoPbGan5PRJPZSq2L7NnZL5V32QZ.');

INSERT INTO TB_COURSE(name, category) VALUES('Spring Boot', 'Programação');
INSERT INTO TB_COURSE(name, category) VALUES('HTML 5', 'Front-end');

INSERT INTO TB_TOPIC(title, message, creation_date, status, author_id, course_id) VALUES('Dúvida', 'Erro ao criar projeto', '2019-05-05 18:00:00', 'NOT_ANSWERED', 1, 1);
INSERT INTO TB_TOPIC(title, message, creation_date, status, author_id, course_id) VALUES('Dúvida 2', 'Projeto não compila', '2019-05-05 19:00:00', 'NOT_ANSWERED', 1, 1);
INSERT INTO TB_TOPIC(title, message, creation_date, status, author_id, course_id) VALUES('Dúvida 3', 'Tag HTML', '2019-05-05 20:00:00', 'NOT_ANSWERED', 1, 2);