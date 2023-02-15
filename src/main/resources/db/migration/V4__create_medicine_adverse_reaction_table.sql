CREATE TABLE medicine_adverse_reaction (
  medicine_registration_number TEXT REFERENCES medicine(registration_number) ON UPDATE CASCADE ON DELETE CASCADE,
  adverse_reaction_id INT REFERENCES adverse_reaction(id) ON UPDATE CASCADE ON DELETE RESTRICT,
  PRIMARY KEY (medicine_registration_number, adverse_reaction_id)
);
