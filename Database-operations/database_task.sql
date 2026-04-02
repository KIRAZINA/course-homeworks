-- 1. Create the database
CREATE DATABASE IF NOT EXISTS my_database;

-- 2. Use the created database
USE my_database;

-- 3. Create the users table
CREATE TABLE IF NOT EXISTS users (
    id    INT AUTO_INCREMENT PRIMARY KEY,
    name  VARCHAR(100) NOT NULL,
    age   INT,
    email VARCHAR(100)
);

-- 4. Insert data into the table
INSERT INTO users (name, age, email) VALUES 
('John', 30, 'john@example.com'),
('Alice', 25, 'alice@example.com'),
('Bob', 35, 'bob@example.com');

-- 5. Select all records from the table (before deletion)
SELECT * FROM users;

-- 6. Delete the user with name "Bob"
DELETE FROM users WHERE name = 'Bob';

-- 7. Select all records from the table (after deletion)
SELECT * FROM users;

-- Additional: Show table structure
DESCRIBE users;

-- Additional: Show all tables in the current database
SHOW TABLES;