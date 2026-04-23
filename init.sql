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