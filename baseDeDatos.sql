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


INSERT INTO ejercicios (titulo, nivel, contenido_json) VALUES
('Clasificación Básica', 'Principiante', '{
    "tipo": "drag_drop",
    "pregunta": "Arrastra cada producto a su categoría correcta",
    "drags": [
        {"id": 1, "valor": "Coca Cola", "imagen": "https://via.placeholder.com/150"},
        {"id": 2, "valor": "Laptop", "imagen": "https://via.placeholder.com/150"}
    ],
    "targets": [
        {"valor": "Bebidas", "aceptados": [1]},
        {"valor": "Tecnología", "aceptados": [2]}
    ]
}');

INSERT INTO ejercicios (titulo, nivel, contenido_json) VALUES
('Análisis de Ventas Q1', 'Intermedio', '{
    "tipo": "analisis_grafico",
    "pregunta": "¿Qué producto tuvo más ventas?",
    "tipoGrafica": "pastel",
    "datosGrafica": [
        {"nombre": "Laptop", "valor": "10"},
        {"nombre": "Mouse", "valor": "30"},
        {"nombre": "Teclado", "valor": "5"}
    ],
    "opciones": [
        {"texto": "Laptop", "esCorrecta": false},
        {"texto": "Mouse", "esCorrecta": true},
        {"texto": "Teclado", "esCorrecta": false}
    ]
}');

INSERT INTO ejercicios (titulo, nivel, contenido_json) VALUES
('Gestión de Crisis de Stock', 'Avanzado', '{
    "tipo": "simulacion_dashboard",
    "pregunta": "El sistema marca 0 stock en los Monitores. Edita el producto para agregar 50 unidades y luego registra una venta de 2 monitores.",
    "datos_iniciales": {
        "productos": [
            {"id": 101, "nombre": "Monitor Dell 24", "precio": 3500.00, "stock": 0, "categoria": 2, "descripcion": "Monitor Full HD", "unidad": "Pieza"},
            {"id": 102, "nombre": "Teclado Mecánico", "precio": 1200.00, "stock": 15, "categoria": 2, "descripcion": "RGB Red Switch", "unidad": "Pieza"}
        ],
        "ventas": []
    }
}');

INSERT INTO ejercicios (titulo, nivel, contenido_json) VALUES
('Análisis de Tendencias Q1', 'Intermedio', '{
    "tipo": "analisis_grafico",
    "pregunta": "Observando la tendencia, ¿cuál fue el mes con el peor desempeño en ventas?",
    "tipoGrafica": "linea",
    "datosGrafica": [
        {"nombre": "Enero", "valor": 45000},
        {"nombre": "Febrero", "valor": 42000},
        {"nombre": "Marzo", "valor": 15000},
        {"nombre": "Abril", "valor": 55000}
    ],
    "opciones": [
        {"texto": "Enero", "esCorrecta": false},
        {"texto": "Marzo", "esCorrecta": true},
        {"texto": "Abril", "esCorrecta": false}
    ]
}');
