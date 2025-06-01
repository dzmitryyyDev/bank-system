-- changeset dzmitry-sivachenko:1
INSERT INTO bank_system.users (id, name, date_of_birth, password) VALUES (1, 'user1', null, '$2a$10$5VzLVbd.NDBEcn3LbOSpzOv5s68DTOx5fvjDnDOFXDV9MJ9n3sLyu');
INSERT INTO bank_system.users (id, name, date_of_birth, password) VALUES (2, 'user2', null, '$2a$10$bu202F8D4FrcTNC1M61gXuOU42JTYhYhth0FGb5boyOSoFUxycew6');
INSERT INTO bank_system.users (id, name, date_of_birth, password) VALUES (3, 'user3', null, '$2a$10$BoT8nN/ZC2kqUZ9PJ64UB.8H5JBt08NxdnFE97wpaZrB1bmV8huQe');

-- changeset dzmitry-sivachenko:2
INSERT INTO bank_system.email_data (id, user_id, email) VALUES (1, 1, 'user1@example.com');
INSERT INTO bank_system.email_data (id, user_id, email) VALUES (2, 2, 'user2@example.com');
INSERT INTO bank_system.email_data (id, user_id, email) VALUES (3, 3, 'user3@example.com');

-- changeset dzmitry-sivachenko:3
INSERT INTO bank_system.phone_data (id, user_id, phone) VALUES (1, 1, '+375333333331');
INSERT INTO bank_system.phone_data (id, user_id, phone) VALUES (2, 2, '+375333333332');
INSERT INTO bank_system.phone_data (id, user_id, phone) VALUES (3, 3, '+375333333333');

-- changeset dzmitry-sivachenko:4
INSERT INTO bank_system.accounts (id, user_id, balance, initial_balance) VALUES (1, 1, 2070207.00, 1000100.00);
INSERT INTO bank_system.accounts (id, user_id, balance, initial_balance) VALUES (2, 2, 2070414.00, 1000200.00);
INSERT INTO bank_system.accounts (id, user_id, balance, initial_balance) VALUES (3, 3, 2070621.00, 1000300.00);
