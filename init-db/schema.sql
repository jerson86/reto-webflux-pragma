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
    description VARCHAR(90) NOT NULL
);

CREATE TABLE IF NOT EXISTS bootcamp_capability (
    bootcamp_id BIGINT NOT NULL,
    capability_id BIGINT NOT NULL,
    PRIMARY KEY (bootcamp_id, capability_id),
    CONSTRAINT fk_bootcamp FOREIGN KEY (bootcamp_id) REFERENCES bootcamps(id)
);