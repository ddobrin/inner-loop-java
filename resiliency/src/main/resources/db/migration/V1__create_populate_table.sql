CREATE TABLE IF NOT EXISTS serverless_services(id serial primary key, name varchar(255) not null);

INSERT INTO serverless_services (name) VALUES ('CloudRun'), ('CloudFunctions'), ('AppEngine');