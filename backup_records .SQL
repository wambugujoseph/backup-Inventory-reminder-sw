CREATE DATABASE backup_record;

use backup_record;

CREATE TABLE back_client(
  server_name varchar(252) NOT NULL,
  logical_path varchar(255) NOT NULL,
  CONSTRAINT BCPK
  PRIMARY KEY (server_name)
);
CREATE TABLE file_record (
  file_name varchar(255) NOT NULL,
  server_name varchar(255) NOT NULL,
  date_modified varchar(255) NOT NULL,
  size bigint(20) DEFAULT NULL,
  backup_type varchar(50) DEFAULT NULL,
  CONSTRAINT FRPK
  PRIMARY KEY(file_name),
  FOREIGN KEY(server_name) REFERENCES back_client(server_name) ON DELETE CASCADE  ON UPDATE CASCADE
);

CREATE TABLE client_emails(
	active varchar(164) NOT NULL,
	equipment_user varchar(255) NOT NULL,
	email_address varchar(255) NOT NULL ,
	client_machine varchar(255) NOT NULL,
	execution_time varchar(64) NOT NULL,
    CONSTRAINT CLPK
    PRIMARY KEY(email_address),
    CONSTRAINT CLBCFK
	FOREIGN KEY(client_machine) REFERENCES back_client(server_name) ON DELETE CASCADE  ON UPDATE CASCADE
);

CREATE TABLE mail_messsage(
	message_id varchar(32) NOT NULL,
	CONSTRAINT MMPK
	 PRIMARY KEY(message_id),
	subject varchar(255) NOT NULL,
	content blob NOT NULL
);
CREATE TABLE reminder_notification(
	status varchar(164) NOT NULL,
	reminder_occurrence varchar(128) NOT NULL,
	inventory_occurrence varchar(255) NOT NULL,
	message varchar(32) NOT NULL,
	recipient varchar(255)  NOT NULL,
	
	CONSTRAINT RMNPK
	PRIMARY KEY(message_id,recipient),
	CONSTRAINT RMNMSGFK
	FOREIGN KEY(message) REFERENCES mail_messsage(message_id) ON DELETE CASCADE  ON UPDATE CASCADE,
	CONSTRAINT RMNCEFK
	FOREIGN KEY(recipient) REFERENCES client_emails(email_address) ON DELETE NO ACTION ON UPDATE CASCADE
);
CREATE TABLE mail_logs(
	status varchar(164) NOT NULL,
	date_time varchar(64) NOT NULL,
	log_level varchar(64) NOT NULL,
	recipient varchar(255) NOT NULL,
	client_machine varchar(255) NOT NULL,
	subject varchar(255) NOT NULL,
	message_id varchar(32) NOT NULL,
	inventory varchar(64) NOT NULL,
	CONSTRAINT MLPK
	PRIMARY KEY(date_time),
	CONSTRAINT MLMSFK
	FOREIGN KEY(message_id) REFERENCES mail_messsage(message_id) ON DELETE CASCADE  ON UPDATE CASCADE,
	CONSTRAINT MLFRFK
	FOREIGN KEY(client_machine) REFERENCES back_client(server_name) ON DELETE CASCADE  ON UPDATE CASCADE
);
CREATE TABLE adminTable(
	id int auto_increment,
	userName varchar(64) NOT NULL,
	 CONSTRAINT ATPK
	 PRIMARY KEY(id),
	adminPassword varchar(32) NOT NULL);