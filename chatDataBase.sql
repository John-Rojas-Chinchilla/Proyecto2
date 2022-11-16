create database chatApp;
use chatApp;

create table users (
	id varchar (15), 
    clave varchar (15),
    nombre varchar (15),
    primary Key(id)
);

create table messages (
    iter integer unsigned auto_increment,
    message varchar(20),
    sender varchar(10),
    receiver varchar(10),
    primary Key(iter)
);

ALTER TABLE messages ADD Foreign KEY (sender) REFERENCES users (id);
ALTER TABLE messages ADD Foreign KEY (receiver) REFERENCES users (id);