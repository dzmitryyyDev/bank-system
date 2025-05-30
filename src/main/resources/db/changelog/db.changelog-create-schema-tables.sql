-- changeset dzmitry-sivachenko:1
CREATE SCHEMA bank_system;

-- changeset dzmitry-sivachenko:2
CREATE TABLE bank_system.users (
                                   id BIGSERIAL PRIMARY KEY,
                                   name VARCHAR(500) NOT NULL,
                                   date_of_birth DATE,
                                   password VARCHAR(500) CHECK (char_length(password) >= 8 AND char_length(password) <= 500) NOT NULL
);

-- changeset dzmitry-sivachenko:3
CREATE TABLE bank_system.accounts (
                                      id BIGSERIAL PRIMARY KEY,
                                      user_id BIGINT NOT NULL,
                                      balance DECIMAL(15, 2) NOT NULL,
                                      FOREIGN KEY (user_id) REFERENCES bank_system.users(id)
);

-- changeset dzmitry-sivachenko:4
CREATE TABLE bank_system.email_data (
                                        id BIGSERIAL PRIMARY KEY,
                                        user_id BIGINT NOT NULL,
                                        email VARCHAR(200) UNIQUE NOT NULL,
                                        FOREIGN KEY (user_id) REFERENCES bank_system.users(id)
);

-- changeset dzmitry-sivachenko:5
CREATE TABLE bank_system.phone_data (
                                        id BIGSERIAL PRIMARY KEY,
                                        user_id BIGINT NOT NULL,
                                        phone VARCHAR(13) UNIQUE NOT NULL,
                                        FOREIGN KEY (user_id) REFERENCES bank_system.users(id)
);