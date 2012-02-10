CREATE TABLE users (
  id INT NOT NULL GENERATED ALWAYS AS IDENTITY,
  user_name VARCHAR(25),
  password_hash VARCHAR(50),
  PRIMARY KEY (id)
);

CREATE TABLE tasks (
  id INT NOT NULL GENERATED ALWAYS AS IDENTITY,
  user_id INT DEFAULT NULL,
  description VARCHAR(100),
  created TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  completed BOOLEAN DEFAULT FALSE,
  PRIMARY KEY (id),
  CONSTRAINT users_fk FOREIGN KEY (user_id) REFERENCES users (id)
);