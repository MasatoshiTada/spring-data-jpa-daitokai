CREATE SEQUENCE seq_vendor_id START WITH 100 INCREMENT BY 1;
CREATE SEQUENCE seq_category_id START WITH 100 INCREMENT BY 1;
CREATE SEQUENCE seq_product_id START WITH 100 INCREMENT BY 1;
CREATE SEQUENCE seq_order_summary_id START WITH 100 INCREMENT BY 1;
CREATE SEQUENCE seq_order_detail_id START WITH 100 INCREMENT BY 1;
CREATE SEQUENCE seq_customer_id START WITH 100 INCREMENT BY 1;


CREATE TABLE vendor(
  id INTEGER PRIMARY KEY ,
  name VARCHAR(64)
);

CREATE TABLE category(
  id INTEGER PRIMARY KEY ,
  name VARCHAR(64)
);

CREATE TABLE product(
  id INTEGER PRIMARY KEY ,
  name VARCHAR(128),
  price DECIMAL,
  vendor_id INTEGER REFERENCES vendor(id),
  category_id INTEGER REFERENCES category(id),
  created_by VARCHAR(64),
  created_date TIMESTAMP,
  updated_by VARCHAR(64),
  updated_date TIMESTAMP
);

CREATE TABLE customer(
  id INTEGER PRIMARY KEY ,
  name VARCHAR(128),
  tel CHAR(13),
  address VARCHAR(256)
);

CREATE TABLE order_summary(
  id INTEGER PRIMARY KEY ,
  order_timestamp TIMESTAMP,
  customer_id INTEGER REFERENCES customer(id)
);

CREATE TABLE order_detail(
  id INTEGER PRIMARY KEY ,
  summary_id INTEGER REFERENCES order_summary(id),
  product_id INTEGER REFERENCES product(id),
  amount INTEGER
);
