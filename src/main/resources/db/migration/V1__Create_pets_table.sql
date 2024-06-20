-- Create sequences for primary keys
CREATE SEQUENCE pet_owner_id_seq START WITH 1 INCREMENT BY 1 NO MINVALUE NO MAXVALUE CACHE 1;
CREATE SEQUENCE pet_id_seq START WITH 1 INCREMENT BY 1 NO MINVALUE NO MAXVALUE CACHE 1;

-- Create pet_owners table
CREATE TABLE pet_owner
(
    id           INT DEFAULT nextval('pet_owner_id_seq') PRIMARY KEY,
    name         VARCHAR(100),
    contact_info VARCHAR(100)
);

-- Create pets table
CREATE TABLE pet
(
    id             INT DEFAULT nextval('pet_id_seq') PRIMARY KEY,
    name           VARCHAR(100),
    weight         DECIMAL,
    breed          VARCHAR(100),
    vaccinated     BOOLEAN,
    training_level INT,
    pet_owner_id   INT,
    CONSTRAINT fk_pet_owner FOREIGN KEY (pet_owner_id) REFERENCES pet_owner (id)
);

-- Create indices
CREATE INDEX idx_pet_weight ON pet (weight);
CREATE INDEX idx_pet_breed ON pet (breed);
CREATE INDEX idx_pet_vaccinated ON pet (vaccinated);
CREATE INDEX idx_pet_training_level ON pet (training_level);
