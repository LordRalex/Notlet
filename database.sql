CREATE DATABASE IF NOT EXISTS `notlet`;
USE `notlet`;

CREATE TABLE IF NOT EXISTS `auth` (
	`username` VARCHAR(16) NOT NULL,
	`password` VARBINARY(256) NOT NULL,
	PRIMARY KEY (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE IF NOT EXISTS `scores` (
	`username` VARCHAR(16) NOT NULL,
	`seed` INT(11) NOT NULL,
	`score` INT(11) NOT NULL,
	PRIMARY KEY (`username`, `seed`),
	CONSTRAINT `FK_scores_auth` FOREIGN KEY (`username`) REFERENCES `auth` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;