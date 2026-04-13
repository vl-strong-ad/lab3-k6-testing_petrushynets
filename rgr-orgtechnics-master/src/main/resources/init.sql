-- 1. СТВОРЕННЯ БАЗИ ДАНИХ
CREATE DATABASE IF NOT EXISTS rgr_orgtechnics;
USE rgr_orgtechnics;

-- 2. СТВОРЕННЯ ТАБЛИЦЬ
CREATE TABLE IF NOT EXISTS users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL
);

CREATE TABLE IF NOT EXISTS shops (
    id SERIAL PRIMARY KEY,
    city VARCHAR(255) NOT NULL,
    name VARCHAR(255) NOT NULL,
    address VARCHAR(255),
    description VARCHAR(500)
);

CREATE TABLE IF NOT EXISTS products (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    price DECIMAL(10, 2) NOT NULL,
    shop_id BIGINT UNSIGNED NOT NULL,
    CONSTRAINT fk_shop FOREIGN KEY (shop_id) REFERENCES shops (id) ON DELETE CASCADE
);

-- 3. НАПОВНЕННЯ ПОЧАТКОВИМИ ДАНИМИ
INSERT INTO users (username, password, role)
SELECT 'admin', 'YWRtaW4=', 'ROLE_ADMIN' WHERE NOT EXISTS (SELECT 1 FROM users WHERE username = 'admin');

INSERT INTO users (username, password, role)
SELECT 'user', 'cGFzcw==', 'ROLE_USER' WHERE NOT EXISTS (SELECT 1 FROM users WHERE username = 'user');

INSERT INTO shops (city, name, address, description)
SELECT 'Kyiv', 'Rozetka', 'Ave. Stepana Bandery, 6', 'Main store' WHERE NOT EXISTS (SELECT 1 FROM shops WHERE name = 'Rozetka');

INSERT INTO shops (city, name, address, description)
SELECT 'Lviv', 'Comfy', 'Pid Dubom St, 7B', 'Electronics' WHERE NOT EXISTS (SELECT 1 FROM shops WHERE name = 'Comfy');

INSERT INTO products (name, price, shop_id)
SELECT 'Laptop HP ProBook', 28999.00, id FROM shops WHERE name = 'Rozetka' LIMIT 1;

INSERT INTO products (name, price, shop_id)
SELECT 'Printer Canon', 4500.00, id FROM shops WHERE name = 'Comfy' LIMIT 1;