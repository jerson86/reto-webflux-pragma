CREATE TABLE IF NOT EXISTS technologies (
    id SERIAL PRIMARY KEY,
    name VARCHAR(50) UNIQUE NOT NULL,
    description VARCHAR(90) NOT NULL
);

CREATE TABLE IF NOT EXISTS capabilities (
    id SERIAL PRIMARY KEY,
    name VARCHAR(50) UNIQUE NOT NULL,
    description VARCHAR(90) NOT NULL
);

CREATE TABLE IF NOT EXISTS capability_technology (
    capability_id BIGINT NOT NULL,
    technology_id BIGINT NOT NULL,
    PRIMARY KEY (capability_id, technology_id),
    CONSTRAINT fk_capability FOREIGN KEY (capability_id) REFERENCES capabilities(id),
    CONSTRAINT fk_technology FOREIGN KEY (technology_id) REFERENCES technologies(id)
);

CREATE TABLE IF NOT EXISTS bootcamps (
    id SERIAL PRIMARY KEY,
    name VARCHAR(50) UNIQUE NOT NULL,
    description VARCHAR(90) NOT NULL,
    start_date DATE NOT NULL,
    duration_days INTEGER NOT NULL
);

CREATE TABLE IF NOT EXISTS bootcamp_capability (
    bootcamp_id BIGINT NOT NULL,
    capability_id BIGINT NOT NULL,
    PRIMARY KEY (bootcamp_id, capability_id),
    CONSTRAINT fk_bootcamp FOREIGN KEY (bootcamp_id) REFERENCES bootcamps(id)
);

CREATE TABLE person_bootcamp (
    id BIGSERIAL PRIMARY KEY,
    person_id BIGINT NOT NULL,
    bootcamp_id BIGINT NOT NULL,
    enrollment_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT unique_person_bootcamp UNIQUE (person_id, bootcamp_id)
);

CREATE INDEX idx_person_bootcamp_person ON person_bootcamp(person_id);
CREATE INDEX idx_person_bootcamp_bootcamp ON person_bootcamp(bootcamp_id);

CREATE TABLE users (
     id BIGSERIAL PRIMARY KEY,
     email VARCHAR(100) NOT NULL UNIQUE,
     password VARCHAR(255) NOT NULL,
     role VARCHAR(20) NOT NULL -- 'ADMIN' o 'CLIENT'
);

CREATE INDEX idx_users_email ON users(email);