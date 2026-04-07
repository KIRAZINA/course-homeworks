CREATE DATABASE IF NOT EXISTS sales_database;

USE sales_database;

CREATE TABLE IF NOT EXISTS sales (
    id INT AUTO_INCREMENT PRIMARY KEY,
    product VARCHAR(255) NOT NULL,
    price DECIMAL(10, 2) NOT NULL,
    quantity INT NOT NULL
);

INSERT INTO sales (product, price, quantity) VALUES ('Laptop', 1000.00, 5);
INSERT INTO sales (product, price, quantity) VALUES ('Phone', 700.00, 3);
INSERT INTO sales (product, price, quantity) VALUES ('Tablet', 500.00, 2);
INSERT INTO sales (product, price, quantity) VALUES ('Printer', 300.00, 4);

SELECT * FROM sales;

SELECT * FROM sales LIMIT 2;

SELECT SUM(price * quantity) AS total_cost FROM sales;

SELECT
    product,
    SUM(quantity) AS total_quantity,
    AVG(price) AS average_price_per_unit
FROM sales
GROUP BY product;