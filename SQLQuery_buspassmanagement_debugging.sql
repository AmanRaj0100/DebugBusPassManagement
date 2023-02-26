create database buspassdebugdb

create table Users(
	id INT IDENTITY(1,1),
	name NVARCHAR(50) not null,
	phone NVARCHAR(20),
	email NVARCHAR(30) UNIQUE,
	password NVARCHAR(30),
	address NVARCHAR(100),
	department NVARCHAR(30),
	type INT,
	createdOn DATETIME DEFAULT CURRENT_TIMESTAMP,
	PRIMARY KEY(id)
);

create table Route(
	id INT PRIMARY KEY IDENTITY(1,1),
	title VARCHAR(256),
	description VARCHAR(256),
	adminId INT constraint routes_id_fk references Users(id),
	createdOn DATETIME DEFAULT CURRENT_TIMESTAMP,
);

create table Stop(
	id INT PRIMARY KEY IDENTITY(1,1),
	address VARCHAR(256),
	sequenceOrder INT,
	routeId INT constraint stops_routeID_fk references Route(id),
	adminID INT constraint stops_id_fk references Users(id),
	createdOn DATETIME DEFAULT CURRENT_TIMESTAMP
);

create table Vehicle(
	id INT PRIMARY KEY IDENTITY(1,1),
	registrationNumber VARCHAR(256),
	totalSeats INT,
	filledSeats INT,
	type INT,
	vehicleStatus INT,
	startPickUpTime VARCHAR(256),
	startDropOffTime VARCHAR(256),
	routeID INT constraint vehicle_routeID_fk references Route(id),
    adminID INT constraint vehicle_id_fk references Users(id),
	driverID INT,
	createdOn DATETIME DEFAULT CURRENT_TIMESTAMP
);

create table BusPass(
	id INT PRIMARY KEY IDENTITY(1,1),
	uid INT constraint buspass_id_fk references Users(id),
	routeId INT constraint buspass_routeID_fk references Route(id),
	requestedOn DATETIME DEFAULT CURRENT_TIMESTAMP,
	approvedRejectedOn DATETIME,
	validTill DATETIME,
	status INT DEFAULT 1,
	createdOn DATETIME DEFAULT CURRENT_TIMESTAMP,
);

create table Feedback(
	id INT PRIMARY KEY IDENTITY(1,1),
	userId INT constraint feedback_id_fk references Users(id),
	title VARCHAR(256),
	description VARCHAR(2048),
	type INT,
	raisedBy VARCHAR(256),
	createdOn DATETIME DEFAULT CURRENT_TIMESTAMP,
);

------------------------------------------------------------------------------------------------------------
--Testing Implementations:

select * from users;
select * from route;
select * from Stop;
select * from Feedback;
select * from BusPass;
select * from Vehicle;

SELECT * from BusPass where requestedOn BETWEEN '2023-02-14' AND '2023-02-19';

select CASE WHEN count(1) > 0 THEN '1' ELSE '0' END AS XYZ from BusPass where uid = 3 AND routeId=2

DELETE FROM Feedback where id=2

UPDATE BusPass SET routeId = 4 WHERE id= 7


