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
name CHAR(32) NOT NULL,
address VARCHAR(50) NOT NULL,
phone VARCHAR(20) NOT NULL
);

CREATE TABLE customer(
	id INT PRIMARY KEY,
	name CHAR(32) NOT NULL,
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
name CHAR(32) NOT NULL,
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
start_time CHAR(20) NOT NULL,
end_time CHAR(20) NOT NULL,
cstaff_id INT,
sstaff_id INT,
fdstaff_id INT NOT NULL,
customer_id INT NOT NULL,
CONSTRAINT checkin_cstaff_id_fk FOREIGN KEY(cstaff_id) REFERENCES catering_staff(id) ON DELETE CASCADE,
CONSTRAINT checkin_sstaff_id_fk FOREIGN KEY(sstaff_id) REFERENCES service_staff(id) ON DELETE CASCADE,
CONSTRAINT checkin_fdstaff_id_fk FOREIGN KEY(fdstaff_id) REFERENCES frontdesk_staff(id) ON DELETE CASCADE,
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
cstaff_id INT NOT NULL,
CONSTRAINT restaurant_cstaff_id_fk FOREIGN KEY(cstaff_id) REFERENCES catering_staff(id) ON DELETE CASCADE,
CONSTRAINT restaurant_record_id_fk FOREIGN KEY(id) REFERENCES service_record(id) ON DELETE CASCADE
);

CREATE TABLE phone_record(
id INT PRIMARY KEY,
sstaff_id INT NOT NULL,
CONSTRAINT phone_sstaff_id_fk FOREIGN KEY(sstaff_id) REFERENCES service_staff(id) ON DELETE CASCADE,
CONSTRAINT phone_record_id_fk FOREIGN KEY(id) REFERENCES service_record(id) ON DELETE CASCADE
);

CREATE TABLE laundry_record(
id INT PRIMARY KEY,
sstaff_id INT NOT NULL,
CONSTRAINT laundry_sstaff_id_fk FOREIGN KEY(sstaff_id) REFERENCES service_staff(id) ON DELETE CASCADE,
CONSTRAINT laundry_record_id_fk FOREIGN KEY(id) REFERENCES service_record(id) ON DELETE CASCADE
);

CREATE TABLE billing(
id INT PRIMARY KEY,
billing_addr VARCHAR(50) NOT NULL,
payment_method CHAR(4) NOT NULL,
card_number VARCHAR(16),
customer_id INT NOT NULL UNIQUE,
bstaff_id INT NOT NULL,
CONSTRAINT billing_customer_id_fk FOREIGN KEY(customer_id) REFERENCES customer(id) ON DELETE CASCADE,
CONSTRAINT bstaff_id_fk FOREIGN KEY(bstaff_id) REFERENCES billing_staff(id) ON DELETE CASCADE
);

CREATE TABLE reserve_for(
hotel_id INT,
room_number INT,
checkin_id INT PRIMARY KEY,
CONSTRAINT reserve_room_number_fk FOREIGN KEY(room_number, hotel_id) REFERENCES room(room_number, hotel_id) ON DELETE CASCADE,
CONSTRAINT reserve_checkin_id_fk FOREIGN KEY(checkin_id) REFERENCES checkin(id) ON DELETE CASCADE
);

INSERT INTO hotel(id, name, address, phone) VALUES (hotel_seq.nextval, '4 Seasons', '1010 Laney Dr., Raleigh, NC 27612', '909-909-9090');
INSERT INTO hotel(id, name, address, phone) VALUES (hotel_seq.nextval, 'Hyatt', '2020 Drivey Ln., San Francisco, CA 94102', '808-808-8080');
INSERT INTO hotel(id, name, address, phone) VALUES (hotel_seq.nextval, 'Marriot', '3030 Boulevard Circle, Boston, MA 02108', '707-707-7070');
INSERT INTO hotel(id, name, address, phone) VALUES (hotel_seq.nextval, 'Microhotel', '4040 Circle Blvd., Seattle, WA 98101', '606-606-6060');

INSERT INTO room_type(category, occupancy, nightly_rate) VALUES ('Economy', 2, 80);
INSERT INTO room_type(category, occupancy, nightly_rate) VALUES ('Economy', 1, 60);
INSERT INTO room_type(category, occupancy, nightly_rate) VALUES ('Presidential Suite', 8, 300);
INSERT INTO room_type(category, occupancy, nightly_rate) VALUES ('Deluxe', 4, 120);
INSERT INTO room_type(category, occupancy, nightly_rate) VALUES ('Executive Suite', 4, 200);

INSERT INTO customer(id, name, ssn, gender, phone, email, address) VALUES (cutomer_seq.nextval, 'Gurgen Schneider', '111111111', 'm', '121-121-1212', 'gurgen.schneider@gmail.com', 'USA 1');
INSERT INTO customer(id, name, ssn, gender, phone, email, address) VALUES (cutomer_seq.nextval, 'Alena Ng', '222222222', 'f', '131-131-1313', 'alena.ng@gmail.com', 'USA 2');
INSERT INTO customer(id, name, ssn, gender, phone, email, address) VALUES (cutomer_seq.nextval, 'Matt Mutton', '333333333', 'm', '141-141-1414', 'matt.mutton@gmail.com', 'USA 3');
INSERT INTO customer(id, name, ssn, gender, phone, email, address) VALUES (cutomer_seq.nextval, 'Tobias Cantu', '444444444', 'm', '151-151-1515', 'tobias.cantu@gmail.com', 'USA 4');
INSERT INTO customer(id, name, ssn, gender, phone, email, address) VALUES (cutomer_seq.nextval, 'Sarah Rademakers', '555555555', 'f', '161-161-1616', 'sara.rademakers@gmail.com', 'USA 5');
INSERT INTO customer(id, name, ssn, gender, phone, email, address) VALUES (cutomer_seq.nextval, 'Hilda Milligan', '666666666', 'm', '171-171-1717', 'hilda.milligan@gmail.com', 'USA 6');

INSERT INTO room(room_number, category, occupancy, availability, hotel_id) VALUES (301, 'Economy', 2, 0, 0);
INSERT INTO room(room_number, category, occupancy, availability, hotel_id) VALUES (401, 'Deluxe', 4, 0, 1);
INSERT INTO room(room_number, category, occupancy, availability, hotel_id) VALUES (804, 'Presidential Suite', 8, 1, 2);
INSERT INTO room(room_number, category, occupancy, availability, hotel_id) VALUES (601, 'Executive Suite', 4, 1, 3);

INSERT INTO staff(id, ssn, name, age, gender, title, department, phone, address, hotel_id) VALUES (staff_seq.nextval, '123456789', 'Jane Doe', 45, 'f', 'sous chef', 'catering', '555-555-5555', '3414 Lane Rd., Raleigh, NC 27606', 0);
INSERT INTO staff(id, ssn, name, age, gender, title, department, phone, address, hotel_id) VALUES (staff_seq.nextval, '987654321', 'Joe Smith', 32, 'm', 'front desk staff', 'front desk', '444-444-4444', '8765 Lane Rd., Raleigh, NC 27606', 0);
INSERT INTO staff(id, ssn, name, age, gender, title, department, phone, address, hotel_id) VALUES (staff_seq.nextval, '121234345', 'Mary Lamb', 89, 'f', 'billing staff', 'billing', '333-333-3333', '5678 Road Ln., San Francisco, CA 94102', 1);
INSERT INTO staff(id, ssn, name, age, gender, title, department, phone, address, hotel_id) VALUES (staff_seq.nextval, '212143435', 'Muffin Man', 76, 'm', 'service staff', 'service', '222-222-2222', '9090 Road Ln., San Francisco, CA 94102', 1);
INSERT INTO staff(id, ssn, name, age, gender, title, department, phone, address, hotel_id) VALUES (staff_seq.nextval, '676789890', 'Shoe Lady', 53, 'f', 'manager', 'management', '111-111-1111', '7384 Creek Dr., Boston, MA 02108', 0);
INSERT INTO staff(id, ssn, name, age, gender, title, department, phone, address, hotel_id) VALUES (staff_seq.nextval, '767698980', 'Peter Piper', 19, 'm', 'manager', 'management', '999-999-9999', '4567 Creek Dr., Boston, MA 02108', 1);
INSERT INTO staff(id, ssn, name, age, gender, title, department, phone, address, hotel_id) VALUES (staff_seq.nextval, '192837465', 'Black Sheep', 61, 'f', 'manager', 'management', '888-834-8888', '9190 Circle Blvd., Seattle, WA 98101', 2);
INSERT INTO staff(id, ssn, name, age, gender, title, department, phone, address, hotel_id) VALUES (staff_seq.nextval, '918273645', 'Bob Sheep', 62, 'm', 'manager', 'management', '888-888-8888', '9190 Circle Blvd., Seattle, WA 98101', 3);

INSERT INTO staff(id, ssn, name, age, gender, title, department, phone, address, hotel_id) VALUES (staff_seq.nextval, '918542345', 'Peter Sheep', 34, 'm', 'fish chef', 'catering', '888-888-8888', '9190 Circle Blvd., Seattle, WA 98101', 0);
INSERT INTO staff(id, ssn, name, age, gender, title, department, phone, address, hotel_id) VALUES (staff_seq.nextval, '912363645', 'Shu Sheep', 23, 'f', 'chicken chef', 'catering', '888-123-8888', '9190 Circle Blvd., Seattle, NA 98101', 3);
INSERT INTO staff(id, ssn, name, age, gender, title, department, phone, address, hotel_id) VALUES (staff_seq.nextval, '910000645', 'Taylor Win', 41, 'm', 'pork chef', 'catering', '888-888-8888', '9190 Circle Blvd., Seattle, WA 98101', 1);

INSERT INTO staff(id, ssn, name, age, gender, title, department, phone, address, hotel_id) VALUES (staff_seq.nextval, '435673645', 'Taylor Winner', 31, 'm', 'billing staff', 'billing', '888-888-8888', '9190 Circle Blvd., Seattle, WA 98101', 0);
INSERT INTO staff(id, ssn, name, age, gender, title, department, phone, address, hotel_id) VALUES (staff_seq.nextval, '939473645', 'Taylor Winson', 21, 'm', 'billing staff', 'billing', '888-888-8888', '9190 Circle Blvd., Seattle, WA 98101', 2);
INSERT INTO staff(id, ssn, name, age, gender, title, department, phone, address, hotel_id) VALUES (staff_seq.nextval, '444473645', 'Taylor Winsy', 26, 'f', 'billing staff', 'billing', '888-888-8888', '9190 Circle Blvd., Seattle, WA 98101', 3);

INSERT INTO staff(id, ssn, name, age, gender, title, department, phone, address, hotel_id) VALUES (staff_seq.nextval, '049583645', 'Taylor Winton', 24, 'f', 'service', 'service staff', '888-888-8888', '9190 Circle Blvd., Seattle, WA 98101', 2);
INSERT INTO staff(id, ssn, name, age, gender, title, department, phone, address, hotel_id) VALUES (staff_seq.nextval, '986753645', 'Taylor Sam', 33, 'm', 'service', 'service staff', '888-888-8888', '9190 Circle Blvd., Seattle, WA 98101', 1);
INSERT INTO staff(id, ssn, name, age, gender, title, department, phone, address, hotel_id) VALUES (staff_seq.nextval, '993343645', 'Taylor Tom', 30, 'm', 'service', 'service staff', '888-888-8888', '9190 Circle Blvd., Seattle, WA 98101', 3);

INSERT INTO staff(id, ssn, name, age, gender, title, department, phone, address, hotel_id) VALUES (staff_seq.nextval, '172383645', 'Taylor Yeh', 22, 'm', 'front desk staff', 'front desk', '888-888-8888', '9190 Circle Blvd., Seattle, WA 98101', 0);
INSERT INTO staff(id, ssn, name, age, gender, title, department, phone, address, hotel_id) VALUES (staff_seq.nextval, '203943645', 'Taylor Zhang', 35, 'f', 'front desk staff', 'front desk', '888-888-8888', '9190 Circle Blvd., Seattle, WA 98101', 1);
INSERT INTO staff(id, ssn, name, age, gender, title, department, phone, address, hotel_id) VALUES (staff_seq.nextval, '758393645', 'Taylor Yi', 29, 'm', 'front desk staff', 'front desk', '888-888-8888', '9190 Circle Blvd., Seattle, WA 98101', 3);


INSERT INTO catering_staff(id) VALUES (0);
INSERT INTO catering_staff(id) VALUES (8);
INSERT INTO catering_staff(id) VALUES (9);
INSERT INTO catering_staff(id) VALUES (10);
INSERT INTO frontdesk_staff(id) VALUES (1);
INSERT INTO frontdesk_staff(id) VALUES (17);
INSERT INTO frontdesk_staff(id) VALUES (18);
INSERT INTO frontdesk_staff(id) VALUES (19);
INSERT INTO billing_staff(id) VALUES (2);
INSERT INTO billing_staff(id) VALUES (11);
INSERT INTO billing_staff(id) VALUES (12);
INSERT INTO billing_staff(id) VALUES (13);
INSERT INTO service_staff(id) VALUES (3);
INSERT INTO service_staff(id) VALUES (14);
INSERT INTO service_staff(id) VALUES (15);
INSERT INTO service_staff(id) VALUES (16);
INSERT INTO manager(id, hotel_id) VALUES (4, 0);
INSERT INTO manager(id, hotel_id) VALUES (5, 1);
INSERT INTO manager(id, hotel_id) VALUES (6, 2);
INSERT INTO manager(id, hotel_id) VALUES (7, 3);

INSERT INTO checkin(id, current_occupancy, start_date, end_date, start_time, end_time, cstaff_id, sstaff_id, fdstaff_id, customer_id) VALUES (checkin_seq.nextval, 2, '05-OCT-2016', '07-OCT-2016', '12:12:12', '12:12:12', null, null, 1, 0);
INSERT INTO checkin(id, current_occupancy, start_date, end_date, start_time, end_time, cstaff_id, sstaff_id, fdstaff_id, customer_id) VALUES (checkin_seq.nextval, 8, '20-NOV-2016', '25-NOV-2016', '12:12:13', '12:12:14', 0, 3, 1, 1);
INSERT INTO checkin(id, current_occupancy, start_date, end_date, start_time, end_time, cstaff_id, sstaff_id, fdstaff_id, customer_id) VALUES (checkin_seq.nextval, 4, '24-NOV-2016', '26-NOV-2016', '12:12:13', '12:12:14', null, null, 1, 2);
INSERT INTO checkin(id, current_occupancy, start_date, end_date, start_time, end_time, cstaff_id, sstaff_id, fdstaff_id, customer_id) VALUES (checkin_seq.nextval, 4, '25-NOV-2016', '28-NOV-2016', '12:12:13', '12:12:14', null, null, 1, 3);

INSERT INTO billing(id, billing_addr, payment_method, card_number, customer_id, bstaff_id) VALUES (billing_seq.nextval, 'USA 1', 'CARD', '1234123412341234', 0, 2);
INSERT INTO billing(id, billing_addr, payment_method, card_number, customer_id, bstaff_id) VALUES (billing_seq.nextval, 'USA 2', 'CASH', NULL, 1, 2);
INSERT INTO billing(id, billing_addr, payment_method, card_number, customer_id, bstaff_id) VALUES (billing_seq.nextval, 'USA 3', 'CARD', '2345234523452345', 2, 2);
INSERT INTO billing(id, billing_addr, payment_method, card_number, customer_id, bstaff_id) VALUES (billing_seq.nextval, 'USA 4', 'CASH', NULL, 3, 2);
INSERT INTO billing(id, billing_addr, payment_method, card_number, customer_id, bstaff_id) VALUES (billing_seq.nextval, 'USA 5', 'CASH', NULL, 4, 2);
INSERT INTO billing(id, billing_addr, payment_method, card_number, customer_id, bstaff_id) VALUES (billing_seq.nextval, 'USA 6', 'CASH', NULL, 5, 2);

INSERT INTO reserve_for(hotel_id, room_number, checkin_id) VALUES (0, 301, 0);
INSERT INTO reserve_for(hotel_id, room_number, checkin_id) VALUES (1, 401, 2);
INSERT INTO reserve_for(hotel_id, room_number, checkin_id) VALUES (2, 804, 1);
INSERT INTO reserve_for(hotel_id, room_number, checkin_id) VALUES (3, 601, 3);

INSERT INTO service_record(id, type, amount, checkin_id) VALUES (service_record_seq.nextval, 'phone', 32, 0);
INSERT INTO service_record(id, type, amount, checkin_id) VALUES (service_record_seq.nextval, 'restaurant', 12, 1);
INSERT INTO service_record(id, type, amount, checkin_id) VALUES (service_record_seq.nextval, 'laundry', 20, 0);
INSERT INTO service_record(id, type, amount, checkin_id) VALUES (service_record_seq.nextval, 'phone', 14, 1);
INSERT INTO service_record(id, type, amount, checkin_id) VALUES (service_record_seq.nextval, 'restaurant', 24, 3);
INSERT INTO service_record(id, type, amount, checkin_id) VALUES (service_record_seq.nextval, 'restaurant', 16, 2);
INSERT INTO service_record(id, type, amount, checkin_id) VALUES (service_record_seq.nextval, 'restaurant', 90, 1);
INSERT INTO service_record(id, type, amount, checkin_id) VALUES (service_record_seq.nextval, 'phone', 23, 2);
INSERT INTO service_record(id, type, amount, checkin_id) VALUES (service_record_seq.nextval, 'phone', 5, 3);
INSERT INTO service_record(id, type, amount, checkin_id) VALUES (service_record_seq.nextval, 'laundry', 5, 3);
INSERT INTO service_record(id, type, amount, checkin_id) VALUES (service_record_seq.nextval, 'laundry', 9, 1);
INSERT INTO service_record(id, type, amount, checkin_id) VALUES (service_record_seq.nextval, 'laundry', 15, 2);

INSERT INTO restaurant_record(id, cstaff_id) VALUES (1, 0);
INSERT INTO restaurant_record(id, cstaff_id) VALUES (4, 0);
INSERT INTO restaurant_record(id, cstaff_id) VALUES (5, 0);
INSERT INTO restaurant_record(id, cstaff_id) VALUES (6, 0);

INSERT INTO phone_record(id, sstaff_id) VALUES (0, 3);
INSERT INTO phone_record(id, sstaff_id) VALUES (3, 3);
INSERT INTO phone_record(id, sstaff_id) VALUES (7, 3);
INSERT INTO phone_record(id, sstaff_id) VALUES (8, 3);

INSERT INTO laundry_record(id, sstaff_id) VALUES (2, 3);
INSERT INTO laundry_record(id, sstaff_id) VALUES (9, 3);
INSERT INTO laundry_record(id, sstaff_id) VALUES (10, 3);
INSERT INTO laundry_record(id, sstaff_id) VALUES (11, 3);

