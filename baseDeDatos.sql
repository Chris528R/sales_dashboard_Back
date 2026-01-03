DROP DATABASE IF EXISTS ventas_ds;

CREATE DATABASE ventas_ds;
USE ventas_ds;

CREATE TABLE usuario (
    id_usuario INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    contrasenia VARCHAR(255) NOT NULL
);

CREATE TABLE categorias (
    id_categoria INT AUTO_INCREMENT PRIMARY KEY,
    nombre_categoria VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE productos (
    id_producto INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(150) NOT NULL,
    descripcion TEXT,
    tipo VARCHAR(50),
    id_categoria INT,
    precio DECIMAL(10,2) NOT NULL,
    stock INT NOT NULL DEFAULT 0,
    unidad_medida VARCHAR(50),
    fecha_registro TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_categoria_producto
        FOREIGN KEY (id_categoria)
        REFERENCES categorias(id_categoria)
        ON UPDATE CASCADE
        ON DELETE SET NULL,

    CONSTRAINT chk_precio CHECK (precio >= 0),
    CONSTRAINT chk_stock CHECK (stock >= 0)
);

CREATE TABLE ventas (
    id_venta INT AUTO_INCREMENT PRIMARY KEY,
    fecha_venta TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    total DECIMAL(10,2) NOT NULL,

    CONSTRAINT chk_total CHECK (total >= 0)
);

CREATE TABLE detalle_venta (
    id_detalle INT AUTO_INCREMENT PRIMARY KEY,
    id_venta INT NOT NULL,
    id_producto INT NOT NULL,
    cantidad INT NOT NULL,
    precio_unitario DECIMAL(10,2) NOT NULL,
    subtotal DECIMAL(10,2) NOT NULL,

    CONSTRAINT fk_detalle_venta
        FOREIGN KEY (id_venta)
        REFERENCES ventas(id_venta)
        ON UPDATE CASCADE
        ON DELETE CASCADE,

    CONSTRAINT fk_detalle_producto
        FOREIGN KEY (id_producto)
        REFERENCES productos(id_producto)
        ON UPDATE CASCADE
        ON DELETE RESTRICT,

    CONSTRAINT chk_cantidad CHECK (cantidad > 0),
    CONSTRAINT chk_precio_unit CHECK (precio_unitario >= 0),
    CONSTRAINT chk_subtotal CHECK (subtotal >= 0)
);


INSERT INTO usuario (NOMBRE, CONTRASENIA) VALUES ('admin', '1234');

INSERT INTO categorias (id_categoria, nombre_categoria) VALUES
(1, 'Bebidas'),
(2, 'Botanas'),
(3, 'Electrónica'),
(4, 'Limpieza'),
(5, 'Abarrotes');

INSERT INTO productos (id_producto, nombre, descripcion, tipo, id_categoria, precio, stock, unidad_medida) VALUES
(1, 'Coca Cola 600ml', 'Refresco de cola, botella plástico', 'General', 1, 18.00, 50, 'Pieza'),
(2, 'Sabritas Sal 45g', 'Papas fritas clásicas', 'General', 2, 22.50, 30, 'Bolsa'),
(3, 'Laptop HP Pavilion', 'Ryzen 5, 8GB RAM, 256SSD', 'Tecnología', 3, 12500.00, 3, 'Pieza'),
(4, 'Cloralex 1L', 'Blanqueador desinfectante', 'General', 4, 25.00, 100, 'Botella'),
(5, 'Galletas Emperador', 'Chocolate, paquete familiar', 'General', 2, 15.00, 4, 'Paquete'),
(6, 'Agua Ciel 1.5L', 'Agua purificada', 'General', 1, 12.00, 60, 'Botella'),
(7, 'Mouse Logitech', 'Inalámbrico color negro', 'Tecnología', 3, 350.00, 15, 'Pieza'),
(8, 'Aceite 1-2-3', 'Aceite vegetal 1L', 'Cocina', 5, 45.00, 2, 'Botella');


INSERT INTO ventas (id_venta, fecha_venta, total) VALUES (1, DATE_SUB(NOW(), INTERVAL 3 MONTH), 12500.00);
INSERT INTO detalle_venta (id_venta, id_producto, cantidad, precio_unitario, subtotal) VALUES (1, 3, 1, 12500.00, 12500.00);

INSERT INTO ventas (id_venta, fecha_venta, total) VALUES (2, DATE_SUB(NOW(), INTERVAL 2 MONTH), 90.00);
INSERT INTO detalle_venta (id_venta, id_producto, cantidad, precio_unitario, subtotal) VALUES (2, 1, 5, 18.00, 90.00);


INSERT INTO ventas (id_venta, fecha_venta, total) VALUES (3, DATE_SUB(NOW(), INTERVAL 1 MONTH), 700.00);
INSERT INTO detalle_venta (id_venta, id_producto, cantidad, precio_unitario, subtotal) VALUES (3, 7, 2, 350.00, 700.00);

INSERT INTO ventas (id_venta, fecha_venta, total) VALUES (4, DATE_SUB(NOW(), INTERVAL 1 DAY), 45.00);
INSERT INTO detalle_venta (id_venta, id_producto, cantidad, precio_unitario, subtotal) VALUES (4, 2, 2, 22.50, 45.00);

INSERT INTO ventas (id_venta, fecha_venta, total) VALUES (5, NOW(), 51.00);
INSERT INTO detalle_venta (id_venta, id_producto, cantidad, precio_unitario, subtotal) VALUES
(5, 1, 2, 18.00, 36.00),
(5, 5, 1, 15.00, 15.00);

INSERT INTO ventas (id_venta, fecha_venta, total) VALUES (6, NOW(), 25.00);
INSERT INTO detalle_venta (id_venta, id_producto, cantidad, precio_unitario, subtotal) VALUES (6, 4, 1, 25.00, 25.00);
