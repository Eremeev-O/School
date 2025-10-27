ALTER TABLE student
	ADD CONSTRAINT age_constraint CHECK (age >= 16),
	ALTER COLUMN age SET default 20;

ALTER TABLE student
    ADD CONSTRAINT name_unique unique (name),
    ALTER COLUMN name SET NOT null;

ALTER TABLE faculty
ADD CONSTRAINT color_name_unique UNIQUE (color, name);