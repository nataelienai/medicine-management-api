CREATE TABLE medicine (
  registration_number TEXT PRIMARY KEY,
  name TEXT NOT NULL,
  expiration_date DATE NOT NULL,
  customer_service_phone TEXT NOT NULL,
  price NUMERIC(16, 2) NOT NULL,
  amount_of_pills SMALLINT NOT NULL,
  manufacturer_id INT NOT NULL REFERENCES manufacturer(id) ON UPDATE CASCADE ON DELETE RESTRICT
);
