CREATE TABLE cars (
	id SMALLSERIAL PRIMARY KEY,
	marka VARCHAR (50),
	model VARCHAR (50),
	cost int4
	);

CREATE TABLE driver (
	id SMALLSERIAL PRIMARY KEY,
	name VARCHAR (100),
	age int2,
	vodprava BOOLEAN default false,
	car_id SMALLSERIAL REFERENCES cars (id)
	);
