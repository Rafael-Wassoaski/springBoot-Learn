CREATE TABLE client(
ID INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
NAME VARCHAR(100)
);

CREATE TABLE product(
ID INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
DESCRIPTION VARCHAR(100),
PRICE NUMERIC(20, 2)
);

CREATE TABLE purchase(
ID INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
CLIENT_ID INT REFERENCES client(ID),
DATE_PURCHASE TIMESTAMP,
STATUS varchar(200),
TOTAL NUMERIC(20, 2)
);

CREATE TABLE purchase_product(
ID INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
PURCHASE_ID INT REFERENCES purchase(ID),
PRODUCT_ID INT REFERENCES product(ID),
AMOUNT INT
);

