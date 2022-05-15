CREATE DATABASE databasename DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
CREATE USER 'username'@'localhost' IDENTIFIED BY 'password';
GRANT ALL PRIVILEGES ON databasename.* TO 'username'@'localhost';
FLUSH PRIVILEGES;
