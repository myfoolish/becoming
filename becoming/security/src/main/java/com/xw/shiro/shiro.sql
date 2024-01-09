CREATE DATABASE shiro CHARACTER SET UTF8;

USE shiro;

-- 用户表
CREATE TABLE user
(
    id       INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50),
    password VARCHAR(50)
);

INSERT INTO user(username, password)
VALUES ('passerby', '123456');
INSERT INTO user(username, password)
VALUES ('xwCoding', '123');

-- 用户角色表
CREATE TABLE user_role
(
    id        INT PRIMARY KEY AUTO_INCREMENT,
    username  VARCHAR(50),
    role_name VARCHAR(50)
);

INSERT INTO user_role(username, role_name)
VALUES ('passerby', 'admin');
INSERT INTO user_role(username, role_name)
VALUES ('passerby', 'user');
INSERT INTO user_role(username, role_name)
VALUES ('xwCoding', 'user');

-- 角色权限表
CREATE TABLE roles_permissions
(
    id         int primary key auto_increment,
    role_name  VARCHAR(50),
    permission VARCHAR(200)
);

INSERT INTO roles_permissions(role_name, permission)
VALUES ('admin', 'user:add');
INSERT INTO roles_permissions(role_name, permission)
VALUES ('admin', 'user:delete');
INSERT INTO roles_permissions(role_name, permission)
VALUES ('admin', 'user:update');
INSERT INTO roles_permissions(role_name, permission)
VALUES ('admin', 'user:select');
INSERT INTO roles_permissions(role_name, permission)
VALUES ('user', 'user:select');
