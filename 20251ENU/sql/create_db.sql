DROP SCHEMA IF EXISTS ubd_20251 CASCADE;

CREATE SCHEMA ubd_20251 AUTHORIZATION postgres;

SET search_path TO ubd_20251;

BEGIN WORK;

SET TRANSACTION READ WRITE;

SET datestyle = DMY;

-- create tables

CREATE TABLE WINERY (
    winery_id INTEGER PRIMARY KEY,
    winery_name VARCHAR(100) NOT NULL,
    town VARCHAR(50) NOT NULL,
    established_year INTEGER,
    winery_phone VARCHAR(13) NOT NULL
);

CREATE TABLE PDO (
    pdo_id INTEGER PRIMARY KEY,
    pdo_name VARCHAR(100) UNIQUE NOT NULL,
    capital_town VARCHAR(100) NOT NULL,
    climate VARCHAR(50) NOT NULL,
    region VARCHAR(100) NOT NULL
);

CREATE TABLE WINE (
    wine_id SERIAL PRIMARY KEY,
    wine_name VARCHAR(100) NOT NULL,
    vintage INTEGER NOT NULL,
    alcohol_content DECIMAL(4,2) NOT NULL,
	category VARCHAR(50) NOT NULL,
    color VARCHAR(20) NOT NULL,
    winery_id INTEGER NOT NULL,
    pdo_id  INTEGER NOT NULL,
    stock INTEGER NOT NULL,
    price DECIMAL(8,2) NOT NULL,
    prizes INTEGER,
    CONSTRAINT CHK_COLOR CHECK (color IN ('red', 'white', 'rosé')),
    CONSTRAINT FK_WINE_PDO FOREIGN KEY (pdo_id) REFERENCES PDO(pdo_id),
    CONSTRAINT CHK_CATEGORY CHECK (category IN ('young', 'aging', 'reserve', 'grand reserve')),
    CONSTRAINT CHK_ALCOHOL_CONTENT CHECK ( 
    	(color = 'white' AND alcohol_content BETWEEN 10 AND 11) OR 
		(color = 'rosé' AND alcohol_content BETWEEN 11 AND 15) OR 
		(color = 'red' AND alcohol_content BETWEEN 13 AND 18)),
    CONSTRAINT FK_WINE_WINERY FOREIGN KEY (winery_id) REFERENCES WINERY(winery_id)
);

CREATE TABLE GRAPE_VARIETY (
    grape_id INTEGER PRIMARY KEY,
    grape_name VARCHAR(50) UNIQUE NOT NULL
);

CREATE TABLE WINE_GRAPE (
    grape_id INTEGER NOT NULL,
    wine_id INTEGER NOT NULL,
    CONSTRAINT PK_WINE_GRAPE PRIMARY KEY (grape_id, wine_id),
    CONSTRAINT FK_WINE_GRAPE_GRAPE_VARIETY FOREIGN KEY (grape_id) REFERENCES GRAPE_VARIETY(grape_id),
    CONSTRAINT FK_WINE_GRAPE_WINE FOREIGN KEY (wine_id) REFERENCES WINE(wine_id)
);

CREATE TABLE CUSTOMER (
	customer_id INTEGER PRIMARY KEY,
	customer_VAT VARCHAR(20) UNIQUE NOT NULL,
	customer_name VARCHAR(100) NOT NULL,
	customer_address VARCHAR(200) NOT NULL,
	customer_phone VARCHAR(20) NOT NULL,
	customer_email VARCHAR(100) NOT NULL,
	customer_status VARCHAR(20) NOT NULL,
	total_amount_cash DECIMAL(8,2) DEFAULT 0,
	CONSTRAINT CHK_STATUS CHECK (customer_status IN ('active', 'inactive'))
);

CREATE TABLE CUSTOMER_ORDER (
	order_id INTEGER PRIMARY KEY,
	customer_id INTEGER NOT NULL,
	order_date DATE NOT NULL,
	order_status VARCHAR(20) NOT NULL,
	order_amount DECIMAL(8,2),
	order_reference CHAR(11),
	shipping_priority VARCHAR(20) DEFAULT 'standard' NOT NULL CHECK (shipping_priority IN ('standard', 'express', 'urgent')),
	CONSTRAINT FK_CUSTOMER_ORDER_CUSTOMER FOREIGN KEY (customer_id) REFERENCES CUSTOMER(customer_id),
	CONSTRAINT CHK_ORDER_DATE CHECK (order_date <= CURRENT_DATE),
	CONSTRAINT CHK_STATUS CHECK (order_status IN ('pending', 'shipped', 'delivered')),
	CONSTRAINT CHK_ORDER_AMOUNT CHECK (order_amount >= 0),
	CONSTRAINT CHK_ORDER_REFERENCE CHECK (order_reference ~ '^[A-Z]{3}-\d{2}-[A-Z]{4}$')
);

CREATE TABLE ORDER_LINE (
	order_id INTEGER NOT NULL,
	order_line_id INTEGER NOT NULL,
	wine_id INTEGER NOT NULL,
	quantity INTEGER NOT NULL,
	CONSTRAINT PK_ORDER_LINE PRIMARY KEY (order_id, order_line_id),
	CONSTRAINT FK_ORDER_LINE_WINE FOREIGN KEY (wine_id) REFERENCES WINE(wine_id),
	CONSTRAINT FK_ORDER_LINE_CUSTOMER_ORDER FOREIGN KEY (order_id) REFERENCES CUSTOMER_ORDER(order_id),
	CONSTRAINT CHK_QUANTITY CHECK (quantity > 0)
);

CREATE TABLE PAYMENT (
    payment_id INT PRIMARY KEY,
    order_id INT NOT NULL,
    payment_date DATE NOT NULL,
    payment_method VARCHAR(30) NOT NULL,
    payment_amount DECIMAL(10, 2) NOT NULL,
    CONSTRAINT chk_payment_method CHECK (payment_method IN ('credit_card', 'bank_transfer', 'cash', 'paypal')),
    CONSTRAINT chk_payment_amount CHECK (payment_amount > 0),
    CONSTRAINT fk_payment_order FOREIGN KEY (order_id) REFERENCES CUSTOMER_ORDER(order_id)
);

CREATE TABLE REPORT_WINERY (
    winery_id SMALLINT NOT NULL,
    winery_name VARCHAR(100) NOT NULL,
	established_year INTEGER DEFAULT NULL,
	total_wines INTEGER DEFAULT 0,
	total_pdos INTEGER DEFAULT 0,
    total_grapes INTEGER DEFAULT 0,
	specialty_pdo_name VARCHAR(100) DEFAULT NULL,
	specialty_grape_name VARCHAR(50) DEFAULT NULL,
    CONSTRAINT PK_REPORT_CUSTOMER PRIMARY KEY (winery_id)
);

CREATE TYPE REPORT_WINERY_TYPE AS (
    t_winery_id SMALLINT,
    t_winery_name VARCHAR(100),
	t_established_year INTEGER,
	t_total_wines INTEGER,
	t_total_pdos INTEGER,
    t_total_grapes INTEGER,
	t_specialty_pdo_name VARCHAR(100),
	t_specialty_grape_name VARCHAR(50)
);
COMMIT;
