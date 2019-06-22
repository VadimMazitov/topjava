DELETE FROM user_roles;
DELETE FROM users;
ALTER SEQUENCE global_seq RESTART WITH 100000;

INSERT INTO users (name, email, password) VALUES
  ('User', 'user@yandex.ru', 'password'),
  ('Admin', 'admin@gmail.com', 'admin');

INSERT INTO meals (localdatetime, description, calories, user_id) VALUES
('2019-06-22 17:17:17', 'Ужин', 1000, 100000),
('2019-06-22 10:10:10', 'Завтрак', 510, 100000),
('2019-06-22 14:14:14', 'Обед', 500, 100000),
('2019-06-22 17:17:17', 'Ужин', 1000, 100001),
('2019-06-22 10:10:10', 'Завтрак', 500, 100001),
('2019-06-22 14:14:14', 'Обед', 500, 100001);

INSERT INTO user_roles (role, user_id) VALUES
  ('ROLE_USER', 100000),
  ('ROLE_ADMIN', 100001);
