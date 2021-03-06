   	drop table APPOINTMENT if exists;
    drop table AUTO_USER if exists;
    drop table SERVICES if exists;

    create table APPOINTMENT (
        APPOINTMENT_ID bigint generated by default as identity,
        APPOINTMENT_DT timestamp,
        MAKE varchar(255),
        MODEL varchar(255),
        YEAR smallint,
        USER_ID bigint,
        STATUS varchar(255),
        primary key (APPOINTMENT_ID)
    );

    create table AUTO_USER (
        autoUserId bigint generated by default as identity,
        EMAIL varchar(255),
        FIRST_NAME varchar(255),
        LAST_NAME varchar(255),
        PASSWORD varchar(255),
        USERNAME varchar(255),
        primary key (autoUserId)
    );

    create table SERVICES (
        APPOINTMENT_ID bigint not null,
        NAME varchar(255)
    );

    alter table APPOINTMENT 
        add constraint FK_hjr0wl1nbp09iulymmarqjj08 
        foreign key (USER_ID) 
        references AUTO_USER;

    alter table SERVICES 
        add constraint FK_e1e9lupq7o7mwsgtbkwd6uivn 
        foreign key (APPOINTMENT_ID) 
        references APPOINTMENT;


insert into AUTO_USER (autoUserId, EMAIL, FIRST_NAME, LAST_NAME, PASSWORD, USERNAME) values (null, 'Ivan.m.Garcia@gmail.com', 'Ivan', 'Garcia', 'password', 'ivgarcia');
insert into AUTO_USER (autoUserId, EMAIL, FIRST_NAME, LAST_NAME, PASSWORD, USERNAME) values (null, 'johndoe@gmail.com', 'John', 'Doe', 'password', 'ivgarcia');
insert into AUTO_USER (autoUserId, EMAIL, FIRST_NAME, LAST_NAME, PASSWORD, USERNAME) values (null, 'janedoe@gmail.com', 'Jane', 'Doe', 'password', 'ivgarcia');
insert into AUTO_USER (autoUserId, EMAIL, FIRST_NAME, LAST_NAME, PASSWORD, USERNAME) values (null, 'juniordoe@gmail.com', 'Junior', 'Doe', 'password', 'ivgarcia');

insert into APPOINTMENT (APPOINTMENT_ID, APPOINTMENT_DT, MAKE, MODEL, YEAR, USER_ID, STATUS) values (1,'2015-08-12 00:00:00', 'Subaru', 'Impreza', '2015', 1, 'Initial');
insert into APPOINTMENT (APPOINTMENT_ID, APPOINTMENT_DT, MAKE, MODEL, YEAR, USER_ID, STATUS) values (2,'2015-08-12 00:00:00', 'Subaru', 'Legacy', '2015', 2, 'Initial');

insert into SERVICES (APPOINTMENT_ID, NAME)  values (1, 'Oil Change');
insert into SERVICES (APPOINTMENT_ID, NAME)  values (1, 'Tire Change');
insert into SERVICES (APPOINTMENT_ID, NAME)  values (2, 'Antifreeze Change');
insert into SERVICES (APPOINTMENT_ID, NAME)  values (2, 'Bulb Change');

;