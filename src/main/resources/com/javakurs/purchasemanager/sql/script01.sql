CREATE TABLE `User` (
	`id` INT NOT NULL AUTO_INCREMENT , 
	`login` VARCHAR(255) NOT NULL UNIQUE, 
	`password` VARCHAR(255) NOT NULL , 
	`firstName` VARCHAR(255) NOT NULL , 
	`lastName` VARCHAR(255) NOT NULL , 
	`isActive` BOOLEAN NOT NULL DEFAULT TRUE , 
	`insertDate` TIMESTAMP NOT NULL , 
	`insertUserId` INT NOT NULL ,
	`modifyDate` TIMESTAMP NULL, 
	`modifyUserId` INT NULL ,
	PRIMARY KEY (`id`));

INSERT INTO User (login, password, firstName, lastName, insertDate, insertUserId) VALUES ('mkrzyzowski', 'enN1aXJhbTE1', 'Mariusz', 'Krzyżowski', now(), 0);
INSERT INTO User (login, password, firstName, lastName, insertDate, insertUserId) VALUES ('adik08pl', 'YWRyaWFubzEyMyEhSGVoZQ=', 'Adrian', 'Stempień', now(), 0);
INSERT INTO User (login, password, firstName, lastName, insertDate, insertUserId) VALUES ('hyperekk', 'YmFydGVrTWF0bWEzMjE=', 'Bartek', 'Tołaj', now(), 0);


