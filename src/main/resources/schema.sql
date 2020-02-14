DROP TABLE IF EXISTS calculator_history;

CREATE TABLE calculator_history (
  id INT AUTO_INCREMENT  PRIMARY KEY,
  operation VARCHAR(250) NOT NULL,
  result VARCHAR(250) NOT NULL,
  date TIMESTAMP DEFAULT NULL
);