DROP SEQUENCE hotel_seq;
DROP SEQUENCE staff_seq;
DROP SEQUENCE checkin_seq;
DROP SEQUENCE service_record_seq;
DROP SEQUENCE cutomer_seq;
DROP SEQUENCE billing_seq;
CREATE SEQUENCE hotel_seq MINVALUE 0 START WITH 0;
CREATE SEQUENCE staff_seq MINVALUE 0 START WITH 0;
CREATE SEQUENCE checkin_seq MINVALUE 0 START WITH 0;
CREATE SEQUENCE service_record_seq MINVALUE 0 START WITH 0;
CREATE SEQUENCE cutomer_seq MINVALUE 0 START WITH 0;
CREATE SEQUENCE billing_seq MINVALUE 0 START WITH 0;

DROP TABLE hotel CASCADE CONSTRAINTS;
DROP TABLE staff CASCADE CONSTRAINTS;
DROP TABLE manager CASCADE CONSTRAINTS;
DROP TABLE frontdesk_staff CASCADE CONSTRAINTS;
DROP TABLE billing_staff CASCADE CONSTRAINTS;
DROP TABLE catering_staff CASCADE CONSTRAINTS;
DROP TABLE service_staff CASCADE CONSTRAINTS;
DROP TABLE room CASCADE CONSTRAINTS;
DROP TABLE room_type CASCADE CONSTRAINTS;
DROP TABLE checkin CASCADE CONSTRAINTS;
DROP TABLE service_record CASCADE CONSTRAINTS;
DROP TABLE restaurant_record CASCADE CONSTRAINTS;
DROP TABLE phone_record CASCADE CONSTRAINTS;
DROP TABLE laundry_record CASCADE CONSTRAINTS;
DROP TABLE customer CASCADE CONSTRAINTS;
DROP TABLE billing CASCADE CONSTRAINTS;
DROP TABLE reserve_for CASCADE CONSTRAINTS;

CREATE TABLE hotel (
id INT PRIMARY KEY,
name VARCHAR(32) NOT NULL,
address VARCHAR(50) NOT NULL,
phone VARCHAR(20) NOT NULL
);

CREATE TABLE customer(
	id INT PRIMARY KEY,
	name VARCHAR(32) NOT NULL,
	ssn VARCHAR(11) NOT NULL UNIQUE,
	gender CHAR(1) NOT NULL,
	phone VARCHAR(20) NOT NULL,
	email VARCHAR(64) NOT NULL,
	address VARCHAR(50) NOT NULL,
    CHECK(gender IN ('f', 'm'))
);


CREATE TABLE staff (
id INT PRIMARY KEY,
ssn VARCHAR(11) NOT NULL UNIQUE,
name VARCHAR(32) NOT NULL,
age INT NOT NULL,
gender CHAR(1) NOT NULL,
title VARCHAR(32) NOT NULL,
department VARCHAR(32) NOT NULL,
phone VARCHAR(20) NOT NULL,
address VARCHAR(50) NOT NULL,
hotel_id INT NOT NULL,
CHECK(gender IN ('f', 'm')),
CONSTRAINT staff_hotel_id_fk FOREIGN KEY(hotel_id) REFERENCES hotel(id)
ON DELETE CASCADE
);

CREATE TABLE manager (
id INT PRIMARY KEY,
hotel_id INT NOT NULL UNIQUE,
CONSTRAINT manager_fk FOREIGN KEY(id) REFERENCES staff(id) ON DELETE CASCADE,
CONSTRAINT hotel_fk FOREIGN KEY(hotel_id) REFERENCES hotel(id)
);

CREATE TABLE frontdesk_staff (
id INT PRIMARY KEY,
CONSTRAINT frontdesk_staff_fk FOREIGN KEY(id) REFERENCES staff(id) ON DELETE CASCADE
);

CREATE TABLE billing_staff (
id INT PRIMARY KEY,
CONSTRAINT billing_staff_fk FOREIGN KEY(id) REFERENCES staff(id) ON DELETE CASCADE
);

CREATE TABLE catering_staff (
id INT PRIMARY KEY,
CONSTRAINT catering_staff_fk FOREIGN KEY(id) REFERENCES staff(id) ON DELETE CASCADE
);

CREATE TABLE service_staff (
id INT PRIMARY KEY,
CONSTRAINT service_staff_fk FOREIGN KEY(id) REFERENCES staff(id) ON DELETE CASCADE
);

CREATE TABLE room_type(
	category VARCHAR(20) NOT NULL,
	occupancy INT NOT NULL,
	nightly_rate INT NOT NULL,
	PRIMARY KEY (category, occupancy)
);

CREATE TABLE room (
room_number INT,
category VARCHAR(20) NOT NULL,
occupancy INT NOT NULL,
availability INT NOT NULL,
hotel_id INT NOT NULL,
PRIMARY KEY (hotel_id, room_number),
CONSTRAINT room_hotel_id_fk FOREIGN KEY(hotel_id) REFERENCES hotel(id),
CONSTRAINT room_type_fk FOREIGN KEY(category, occupancy) REFERENCES room_type(category, occupancy)
);

CREATE TABLE checkin(
id INT PRIMARY KEY,
current_occupancy INT NOT NULL,
start_date DATE NOT NULL,
end_date DATE NOT NULL,
start_time VARCHAR(20) NOT NULL,
end_time VARCHAR(20) NOT NULL,
cstaff_id INT,
sstaff_id INT,
fdstaff_id INT,
customer_id INT NOT NULL,
CONSTRAINT checkin_cstaff_id_fk FOREIGN KEY(cstaff_id) REFERENCES catering_staff(id) ON DELETE SET NULL,
CONSTRAINT checkin_sstaff_id_fk FOREIGN KEY(sstaff_id) REFERENCES service_staff(id) ON DELETE SET NULL,
CONSTRAINT checkin_fdstaff_id_fk FOREIGN KEY(fdstaff_id) REFERENCES frontdesk_staff(id) ON DELETE SET NULL,
CONSTRAINT checkin_customer_id_fk FOREIGN KEY(customer_id) REFERENCES customer(id) ON DELETE CASCADE
);

CREATE TABLE service_record(
id INT PRIMARY KEY,
type VARCHAR(16) NOT NULL,
amount INT NOT NULL,
checkin_id INT NOT NULL,
CONSTRAINT checkin_id_fk FOREIGN KEY(checkin_id) REFERENCES checkin(id) ON DELETE CASCADE
);

CREATE TABLE restaurant_record(
id INT PRIMARY KEY,
cstaff_id INT,
CONSTRAINT restaurant_cstaff_id_fk FOREIGN KEY(cstaff_id) REFERENCES catering_staff(id) ON DELETE SET NULL,
CONSTRAINT restaurant_record_id_fk FOREIGN KEY(id) REFERENCES service_record(id) ON DELETE CASCADE
);

CREATE TABLE phone_record(
id INT PRIMARY KEY,
sstaff_id INT,
CONSTRAINT phone_sstaff_id_fk FOREIGN KEY(sstaff_id) REFERENCES service_staff(id) ON DELETE SET NULL,
CONSTRAINT phone_record_id_fk FOREIGN KEY(id) REFERENCES service_record(id) ON DELETE CASCADE
);

CREATE TABLE laundry_record(
id INT PRIMARY KEY,
sstaff_id INT,
CONSTRAINT laundry_sstaff_id_fk FOREIGN KEY(sstaff_id) REFERENCES service_staff(id) ON DELETE SET NULL,
CONSTRAINT laundry_record_id_fk FOREIGN KEY(id) REFERENCES service_record(id) ON DELETE CASCADE
);

CREATE TABLE billing(
id INT PRIMARY KEY,
billing_addr VARCHAR(50) NOT NULL,
payment_method CHAR(4) NOT NULL,
card_number VARCHAR(16),
customer_id INT NOT NULL UNIQUE,
bstaff_id INT,
CONSTRAINT billing_customer_id_fk FOREIGN KEY(customer_id) REFERENCES customer(id) ON DELETE CASCADE,
CONSTRAINT bstaff_id_fk FOREIGN KEY(bstaff_id) REFERENCES billing_staff(id) ON DELETE SET NULL
);

CREATE TABLE reserve_for(
hotel_id INT,
room_number INT,
checkin_id INT PRIMARY KEY,
CONSTRAINT reserve_room_number_fk FOREIGN KEY(room_number, hotel_id) REFERENCES room(room_number, hotel_id) ON DELETE CASCADE,
CONSTRAINT reserve_checkin_id_fk FOREIGN KEY(checkin_id) REFERENCES checkin(id) ON DELETE CASCADE
);

INSERT INTO hotel(id, name, address, phone) VALUES (hotel_seq.nextval, 'WolfVilla', '27 Timber Dr, Garner, NC 27529', '976-728-1980');

INSERT INTO room_type(category, occupancy, nightly_rate) VALUES ('Economy', 2, 150);
INSERT INTO room_type(category, occupancy, nightly_rate) VALUES ('Executive Suite', 2, 250);
INSERT INTO room_type(category, occupancy, nightly_rate) VALUES ('Deluxe', 2, 350);

INSERT INTO customer(id, name, ssn, gender, phone, email, address) VALUES (cutomer_seq.nextval, 'Carl T. Ashcraft', '144-54-9090', 'm', '121-121-1212', 'carlashcraft@kmail.us', '881 Java Lane Graniteville, SC 29829');
INSERT INTO customer(id, name, ssn, gender, phone, email, address) VALUES (cutomer_seq.nextval, 'Angela J. Roberts', '678-90-0900', 'f', '131-131-1313', 'angelaroberts@kmail.us', '2697 Stroop Hill Road Atlanta, GA 30342');

INSERT INTO room(room_number, category, occupancy, availability, hotel_id) VALUES (101, 'Economy', 2, 0, 0);
INSERT INTO room(room_number, category, occupancy, availability, hotel_id) VALUES (201, 'Executive Suite', 2, 0, 0);
INSERT INTO room(room_number, category, occupancy, availability, hotel_id) VALUES (301, 'Deluxe', 2, 1, 0);


INSERT INTO staff(id, ssn, name, age, gender, title, department, phone, address, hotel_id) VALUES (staff_seq.nextval, '409-02-1234', 'David D. Clukey', 25, 'm', 'Front Desk representative', 'Administration', '980-131-1238', '106, Cloverdale Ct, Raleigh, NC, 27607', 0);
INSERT INTO staff(id, ssn, name, age, gender, title, department, phone, address, hotel_id) VALUES (staff_seq.nextval, '143-22-9089', 'James M. Gooden', 32, 'm', 'Catering Staff', 'Catering', '980-187-1983', '109, Cloverdale Ct, Raleigh, NC, 27607', 0);
INSERT INTO staff(id, ssn, name, age, gender, title, department, phone, address, hotel_id) VALUES (staff_seq.nextval, '132-67-4793', 'Todd C. Chen', 40, 'm', 'Manager', 'Administration', '976-728-1980', '1048, Avent Ferry Road, Raleigh, NC, 27606', 0);
INSERT INTO staff(id, ssn, name, age, gender, title, department, phone, address, hotel_id) VALUES (staff_seq.nextval, '183-47-1905', 'Donald Chen', 23, 'm', 'Billing staff', 'billing', '919-738-1928', '1910, Entrepreneur Dr, Raleigh, NC, 27606', 0);

INSERT INTO catering_staff(id) VALUES (1);
INSERT INTO frontdesk_staff(id) VALUES (0);
INSERT INTO manager(id, hotel_id) VALUES (2, 0);
INSERT INTO billing_staff(id) VALUES(3);

INSERT INTO checkin(id, current_occupancy, start_date, end_date, start_time, end_time, cstaff_id, sstaff_id, fdstaff_id, customer_id) VALUES (checkin_seq.nextval, 1, '12-NOV-2016', '14-NOV-2016', '12:00:00', '12:00:00', 1, null, 0, 0);
INSERT INTO checkin(id, current_occupancy, start_date, end_date, start_time, end_time, cstaff_id, sstaff_id, fdstaff_id, customer_id) VALUES (checkin_seq.nextval, 1, '14-NOV-2016', '16-NOV-2016', '12:00:00', '12:00:00', 1, null, 0, 1);


INSERT INTO billing(id, billing_addr, payment_method, card_number, customer_id, bstaff_id) VALUES (billing_seq.nextval, '881 Java Lane Graniteville, SC 29829', 'CARD', '5184950505589328', 0, 3);
INSERT INTO billing(id, billing_addr, payment_method, card_number, customer_id, bstaff_id) VALUES (billing_seq.nextval, '2697 Stroop Hill Road Atlanta, GA 30342', 'CARD', '5196591432385020', 1, 3);

INSERT INTO reserve_for(hotel_id, room_number, checkin_id) VALUES (0, 101, 0);
INSERT INTO reserve_for(hotel_id, room_number, checkin_id) VALUES (0, 201, 1);

INSERT INTO service_record(id, type, amount, checkin_id) VALUES (service_record_seq.nextval, 'restaurant', 30, 0);
INSERT INTO service_record(id, type, amount, checkin_id) VALUES (service_record_seq.nextval, 'restaurant', 35, 0);
INSERT INTO service_record(id, type, amount, checkin_id) VALUES (service_record_seq.nextval, 'laundry', 15, 0);
INSERT INTO service_record(id, type, amount, checkin_id) VALUES (service_record_seq.nextval, 'restaurant', 40, 1);
INSERT INTO service_record(id, type, amount, checkin_id) VALUES (service_record_seq.nextval, 'laundry', 15, 1);
INSERT INTO service_record(id, type, amount, checkin_id) VALUES (service_record_seq.nextval, 'laundry', 10, 1);

