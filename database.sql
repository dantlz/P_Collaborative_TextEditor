DROP DATABASE if exists tianlinz;

CREATE DATABASE tianlinz;

USE tianlinz;

CREATE TABLE Users (
	username varchar(50) primary key not null,
	password varchar(50) not null
);

CREATE TABLE Files (
	fileID int(11) primary key not null auto_increment,
	filename varchar(50) not null,
	fileContent longtext not null 
);

CREATE TABLE Permissions(
	fileID int(11) not null,
    username varchar(50) not null,
    isOwner tinyint(1) not null
);