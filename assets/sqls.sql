CREATE USER 'star'@'%' IDENTIFIED BY 'startemp';
SELECT user FROM mysql.user;

GRANT ALL PRIVILEGES ON *.* TO 'star'@'%' WITH GRANT OPTION;
FLUSH PRIVILEGES;
SHOW GRANTS FOR 'star'@'%';

CREATE DATABASE IF NOT EXISTS spring_boot_project;
SHOW CREATE DATABASE spring_boot_project;
USE spring_boot_project;

-- CHANGE DB CHARSET
ALTER DATABASE spring_boot_project CHARACTER SET utf8mb4;
-- ALTER DATABASE spring_boot_project CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci

-- DDL
CREATE TABLE IF NOT EXISTS users(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    email VARCHAR(100) NOT NULL,
    password VARCHAR(100) NOT NULL,
    phone_number VARCHAR(20) NOT NULL
);

-- ALTER TABLE UNIQUE INDEX  EMAIL
ALTER TABLE users ADD CONSTRAINT u_email UNIQUE (email);

-- SHOW INDEX
SHOW INDEX FROM users;

-- RENAME TABLE
ALTER TABLE user_information RENAME TO users;

-- DROP TABLE
DROP TABLE user_information;

CREATE TABLE coupons(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    description VARCHAR(100),
    expiry_date DATETIME
);

CREATE TABLE user_coupon(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    coupon_id BIGINT NOT NULL
);
-- Foreign Key
-- user_id BIGINT NOT NULL FOREIGN KEY CONSTRAINT fk_userinfo  REFERENCES users(id),

-- SHOW TABLE
SHOW CREATE TABLE coupons;

-- DML
-- table users
INSERT INTO users(name,email,password, phone_number)
VALUES('Alex','alex@outlook.com','test','18500212343'),
('Emma','emma@outlook.com','test','18611110000'),
('Lisa','lisa@outlook.com','test','18634579875'),
('Lisa1','lisa1@outlook.com','test','18634579875');
SELECT name, email, password, phone_number FROM users WHERE name = 'Alex';
UPDATE users SET password='testAgain' WHERE name='Lisa';
DELETE FROM users WHERE name='Lisa1';

-- table coupons
INSERT INTO coupons(name, description,expiry_date)
VALUES('YoukuVip','vip coupons for one month','2023-04-13 16:00:00'),
('DingdongVip','vip coupons for 3 months','2023-04-13 16:30:00');

-- table user_coupon
INSERT INTO user_coupon(user_id, coupon_id)
VALUES('1','1');
INSERT INTO user_coupon(user_id, coupon_id)
VALUES('2','2');
INSERT INTO user_coupon(user_id, coupon_id)
VALUES('3','2');

-- EXAMPLE DATA
INSERT INTO users(user_name,email,password,phone_number)
VALUES('Emma','emma@outlook.com','test','18611110000');

INSERT INTO users(user_name,email,password,phone_number)
VALUES('Alex','alex@outlook.com','test','18622220000');

INSERT INTO coupons(coupon_name, user_id)
VALUES('YoukuVip', 1);
VALUES('YoukuVip', 2);

-- FIND THE USERNAME AND EMAIL OF ALL COUPONS
SELECT u.name, u.email, c.id AS coupon_id, c.description, c.name AS coupon_name, c.expiry_date FROM users u
LEFT JOIN user_coupon uc ON u.id=uc.user_id
LEFT JOIN coupons c ON uc.coupon_id=c.id
WHERE c.id IS NOT NULL;

-- ALTER TABLE USERS COLUMN
ALTER TABLE users MODIFY phone_number VARCHAR(20);
