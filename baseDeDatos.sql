DROP DATABASE IF EXISTS ventas_ds;

CREATE DATABASE ventas_ds;
USE ventas_ds;

CREATE TABLE usuario (
    id_usuario INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    contrasenia VARCHAR(255) NOT NULL
);

CREATE TABLE ejercicios (
    id_ejercicio INT AUTO_INCREMENT PRIMARY KEY,
    mejor_tiempo INT DEFAULT 0,
    titulo VARCHAR(150) NOT NULL,
    nivel VARCHAR(50) NOT NULL,
    contenido_json JSON NOT NULL,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

INSERT INTO usuario (NOMBRE, CONTRASENIA) VALUES ('admin', '1234');


