CREATE TABLE roles (
                       id BIGSERIAL PRIMARY KEY,
                       name text NOT NULL
);

CREATE TABLE users (
                       id BIGSERIAL PRIMARY KEY,
                       username text NOT NULL UNIQUE,
                       password text NOT NULL,
                       full_name text not null
);

CREATE TABLE users_roles (
                            user_id BIGSERIAL,
                            role_id BIGSERIAL,
                            FOREIGN KEY (user_id) REFERENCES users (id),
                            FOREIGN KEY (role_id) REFERENCES roles (id)
);