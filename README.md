# Documentacion de swagger
```
    http://localhost:8080/service/v1/swagger-ui/index.html#/
```

# Base de datos
```
    CREATE DATABASE inventario_prueba;

	CREATE TABLE charge (
		id SERIAL PRIMARY KEY,
		name VARCHAR(100) NOT NULL
	);

	CREATE TABLE users (
		id SERIAL PRIMARY KEY,
		name VARCHAR(255) NOT NULL,
		age INT NOT NULL,
		charge_id INT NOT NULL,
		entry_date DATE NOT NULL,
		status BOOLEAN NOT NULL,
		CONSTRAINT fk_charge FOREIGN KEY (charge_id) REFERENCES charge (id)
	);


	CREATE TABLE inventory (
		id SERIAL PRIMARY KEY,
		product_name VARCHAR(255) NOT NULL UNIQUE,
		quantity INT NOT NULL,
		entry_date DATE,
		edition_date DATE,
		status BOOLEAN NOT NULL,
		user_creator INT NOT NULL,
		user_editor INT,
		CONSTRAINT fk_user_creator FOREIGN KEY (user_creator) REFERENCES "users" (id),
		CONSTRAINT fk_user_editor FOREIGN KEY (user_editor) REFERENCES "users" (id)
	);

	INSERT INTO charge (name) VALUES ('Asesor de ventas');
	INSERT INTO charge (name) VALUES ('Administrador');
	INSERT INTO charge (name) VALUES ('Soporte');
```