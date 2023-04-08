DROP TABLE IF EXISTS projects;
DROP TABLE IF EXISTS customers;

CREATE TABLE customers(
  customer_id INT AUTO_INCREMENT NOT NULL,
  customer_name VARCHAR(25) NOT NULL,
  PRIMARY KEY (customer_id)
);

CREATE TABLE projects(
  customer_id INT AUTO_INCREMENT NOT NULL,
  project_id INT NOT NULL,
  gross_price DECIMAL(7,2) NOT NULL,
  system_size_kw DECIMAL(5,3) NOT NULL,
  dealer_fees DECIMAL (7,2),
  adders DECIMAL (7,2),
  installed BOOLEAN NOT NULL,
  install_year INT,
  install_month INT,
  rep_commission DECIMAL(7,2) NOT NULL,
  PRIMARY KEY (customer_id),
  FOREIGN KEY (customer_id) REFERENCES customers (customer_id) ON DELETE CASCADE
);