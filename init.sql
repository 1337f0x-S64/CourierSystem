CREATE TABLE public.roles (
    id SERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL
);
CREATE TABLE public.users (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100),
    last_name VARCHAR(100),
    username VARCHAR(100) UNIQUE,
    password VARCHAR(256),
    email VARCHAR(100),
    role_id INTEGER REFERENCES public.roles(id)
);
CREATE TABLE public.packages (
    id SERIAL PRIMARY KEY,
    user_id INTEGER REFERENCES public.users(id),
    weight NUMERIC(10, 2),
    description VARCHAR(255),
    tracking_number VARCHAR(50),
    status VARCHAR(5) DEFAULT 'w'
);
CREATE TABLE public.routes (
    id SERIAL PRIMARY KEY,
    package_id INTEGER REFERENCES public.packages(id),
    start_location VARCHAR(100),
    end_location VARCHAR(100),
    tracking_number VARCHAR(50),
    delivery_date TIMESTAMP,
    optimal_path VARCHAR(255),
    status VARCHAR(5) DEFAULT 'w',
    courier_id INTEGER REFERENCES public.users(id),
    possible_reception_date DATE,
    reception_date TIMESTAMP
);
-- Seed roles
INSERT INTO public.roles (name)
VALUES ('Administrator'),
    ('Courier'),
    ('Customer');
CREATE EXTENSION IF NOT EXISTS pgcrypto;
CREATE OR REPLACE FUNCTION hash_update_tg() RETURNS trigger AS $$ BEGIN -- Check if it's a brand new record
    IF (TG_OP = 'INSERT') THEN NEW.password = encode(digest(NEW.password, 'sha256'), 'hex');
-- Check if it's an update AND the password field was actually modified
ELSIF (TG_OP = 'UPDATE') THEN IF (
    NEW.password IS DISTINCT
    FROM OLD.password
) THEN NEW.password = encode(digest(NEW.password, 'sha256'), 'hex');
END IF;
END IF;
RETURN NEW;
END;
$$ LANGUAGE plpgsql;
CREATE TRIGGER trg_hash_password BEFORE
INSERT
    OR
UPDATE ON public.users FOR EACH ROW EXECUTE FUNCTION hash_update_tg();
INSERT INTO public.users (
        name,
        last_name,
        username,
        password,
        email,
        role_id
    )
VALUES (
        'Admin',
        'User',
        'admin',
        'admin123',
        'admin@example.com',
        1
    );