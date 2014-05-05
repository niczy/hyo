CREATE TABLE restaurant (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(512) NOT NULL,
    deleted TINYINT(1) DEFAULT 0,
    time_created TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    time_updated TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS users (
    id INT PRIMARY KEY AUTO_INCREMENT,
    time_created TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    time_updated TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    deleted TINYINT(1) DEFAULT 0,
    name VARCHAR(512) NOT NULL,
    password VARCHAR(512) NOT NULL,
    type INT NOT NULL,
    real_name VARCHAR(512),
    comment VARCHAR(512),
    restaurant_id INT
);