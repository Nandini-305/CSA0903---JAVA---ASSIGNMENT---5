CREATE DATABASE IF NOT EXISTS java_assignment5;
USE java_assignment5;
CREATE TABLE IF NOT EXISTS users (
    id VARCHAR(80) PRIMARY KEY,
    pwd VARCHAR(255) NOT NULL
);
DELETE FROM users;
INSERT INTO users(id, pwd) VALUES ('student1', 'correct-password');
