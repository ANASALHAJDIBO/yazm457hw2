CREATE DATABASE yazm457hw2;

USE yazm457hw2;

CREATE TABLE product_backlog(
    id INT NOT NULL AUTO_INCREMENT,
    backlogId INT NOT NULL,
	taskName varchar(50) NOT NULL,
	priority INT NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE sprint_backlog(
    id INT NOT NULL AUTO_INCREMENT,
	taskId INT NOT NULL UNIQUE,
	taskName VARCHAR(50) NOT NULL ,
	backlogId INT NOT NULL,
	priority INT NOT NULL,
	sprintId INT NOT NULL,
	available INT NOT NULL DEFAULT 1,
    PRIMARY KEY (id),
    INDEX idx_sprintId (sprintId),
    FOREIGN KEY (taskId) REFERENCES product_backlog(id)
);

CREATE TABLE board(
    id INT NOT NULL AUTO_INCREMENT,
	taskId INT NOT NULL UNIQUE,
	taskName VARCHAR(50) NOT NULL,
	backlogId INT NOT NULL,
    priority INT NOT NULL,
	sprintId INT NOT NULL,
	developerName VARCHAR(20) NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (sprintId) REFERENCES sprint_backlog(sprintId),
    FOREIGN KEY (taskId) REFERENCES product_backlog(id)
);
