CREATE TABLE product
(

    id                  SERIAL PRIMARY KEY,
    product_name        VARCHAR(100) NOT NULL,
    product_description varchar(200) not null,
    product_price       int          not null,
    category_id         integer REFERENCES category (id)


);
