/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 * Author:  Equipe 9
 * Created: 24 mars 2020
 */

CREATE SEQUENCE id_partie;
CREATE SEQUENCE id_user;
CREATE SEQUENCE id_MessageJour;
CREATE SEQUENCE id_MessageNuit;
CREATE SEQUENCE id_Archive;


/**
 requêtes pour créer la base dedonnées

**/

/** Les utilisateurs de l'application **/

create table Utilisateur
(
    idUser number(6) default id_user.nextval,
    pseudonyme NVARCHAR2(20) not null,
    password NVARCHAR2(65) not null,
    email VARCHAR(100) not null,
    primary key(pseudonyme)
);

/** La partie de jeu dans l'application et ses caractéristiques **/

CREATE TABLE Partie (
    maitre NVARCHAR2(20) not null references Utilisateur(pseudonyme),
    probaPouvoir float(10) not null,
    propLoup float(10) not null,
    periode NVARCHAR2(10) not null check (periode in ('Jour', 'Nuit')),
    primary key(maitre)
);

/** La liste des jouers d'une partie et leurs informations **/

create table Joueur (
    pseudonyme NVARCHAR2(20) not null references Utilisateur(pseudonyme),
    elimine NUMBER(1) not null check (elimine in (1,0)),
    role NVARCHAR2(20) not null check (role in ('humain','loupGarou')),
    pouvoir NVARCHAR2(20) not null check (pouvoir in ('aucun', 'voyance', 'contamination')),
    primary key(pseudonyme)

);

/** Les joueurs proposé durant un vote **/

create table Proposed (
    pseudonyme NVARCHAR2(20) not null references Joueur(pseudonyme),
    voter NVARCHAR2(20) not null references Joueur(pseudonyme),
    primary key(pseudonyme, voter)

);
/** Le joueurs qui a été tué après un vote **/

create table Removed (
    pseudonyme NVARCHAR2(20) not null references Joueur(pseudonyme),
    primary key(pseudonyme)

);

/** Les messages de jour envoyés par les joueurs **/

create table MessageJour (
    id_MessageJour number(6) default id_MessageJour.nextval,
    datePub NVARCHAR2(40) not null,
    pseudonyme NVARCHAR2(20) not null references Utilisateur(pseudonyme),
    contenu NVARCHAR2(2000) not null,
    primary key(id_MessageJour)
);

/** Les messages de nuit envoyés par les loups-garous **/

create table MessageNuit (
    id_MessageNuit number(6) default id_MessageNuit.nextval,
    datePub NVARCHAR2(40) not null,
    pseudonyme NVARCHAR2(20) not null references Utilisateur(pseudonyme),
    contenu NVARCHAR2(2000) not null,
    primary key(id_MessageNuit)
);

/** L'archive de la discussion de jeu **/

create table Archive (
    id_Archive number(6) default id_Archive.nextval,
    datePub NVARCHAR2(40) not null,
    pseudonyme NVARCHAR2(20) not null references Utilisateur(pseudonyme),
    contenu NVARCHAR2(2000) not null,
    periode NVARCHAR2(10) not null check (periode in ('Jour', 'Nuit')),
    primary key(id_Archive)
);

/* Une table qui permet de stocker les noms des joueurs qui ont déja exercer
    leur pouvoir pendant la nuit 
*/
create table ExercerPouvoir(
    exercerPar NVARCHAR2(20) not null references Utilisateur(pseudonyme),
    exercerSur NVARCHAR2(20) not null references Utilisateur(pseudonyme),
    primary key(exercerPar)
);


/* peupler la base de donnée*/

/** insertion d'une liste des utilisateurs **/
INSERT INTO Utilisateur (pseudonyme,password,email) VALUES ('naima', '9f86d081884c7d659a2feaa0c55ad015a3bf4f1b2b0b822cd15d6c15b0f00a08', 'naima@gmail.com');
INSERT INTO Utilisateur (pseudonyme,password,email) VALUES ('nabil', '9f86d081884c7d659a2feaa0c55ad015a3bf4f1b2b0b822cd15d6c15b0f00a08', 'nabil@gmail.com');
INSERT INTO Utilisateur (pseudonyme,password,email) VALUES ('hamza', '9f86d081884c7d659a2feaa0c55ad015a3bf4f1b2b0b822cd15d6c15b0f00a08', 'hamza@gmail.com');
INSERT INTO Utilisateur (pseudonyme,password,email) VALUES ('nadir', '9f86d081884c7d659a2feaa0c55ad015a3bf4f1b2b0b822cd15d6c15b0f00a08', 'nadir@gmail.com');
INSERT INTO Utilisateur (pseudonyme,password,email) VALUES ('maria', '9f86d081884c7d659a2feaa0c55ad015a3bf4f1b2b0b822cd15d6c15b0f00a08', 'maria@gmail.com');
INSERT INTO Utilisateur (pseudonyme,password,email) VALUES ('Antoine', '9f86d081884c7d659a2feaa0c55ad015a3bf4f1b2b0b822cd15d6c15b0f00a08', 'Antoine@gmail.com');
INSERT INTO Utilisateur (pseudonyme,password,email) VALUES ('Carla', '9f86d081884c7d659a2feaa0c55ad015a3bf4f1b2b0b822cd15d6c15b0f00a08', 'Carla@gmail.com');
INSERT INTO Utilisateur (pseudonyme,password,email) VALUES ('yidi', '9f86d081884c7d659a2feaa0c55ad015a3bf4f1b2b0b822cd15d6c15b0f00a08', 'yidi@gmail.com');
INSERT INTO Utilisateur (pseudonyme,password,email) VALUES ('Jean', '9f86d081884c7d659a2feaa0c55ad015a3bf4f1b2b0b822cd15d6c15b0f00a08', 'Jean@gmail.com');
INSERT INTO Utilisateur (pseudonyme,password,email) VALUES ('Anne', '9f86d081884c7d659a2feaa0c55ad015a3bf4f1b2b0b822cd15d6c15b0f00a08', 'Anne@gmail.com');
INSERT INTO Utilisateur (pseudonyme,password,email) VALUES ('clément', '9f86d081884c7d659a2feaa0c55ad015a3bf4f1b2b0b822cd15d6c15b0f00a08', 'clément@gmail.com');
INSERT INTO Utilisateur (pseudonyme,password,email) VALUES ('thomas', '9f86d081884c7d659a2feaa0c55ad015a3bf4f1b2b0b822cd15d6c15b0f00a08', 'thomas@gmail.com');
INSERT INTO Utilisateur (pseudonyme,password,email) VALUES ('Catherine', '9f86d081884c7d659a2feaa0c55ad015a3bf4f1b2b0b822cd15d6c15b0f00a08', 'Ctherine@gmail.com');
INSERT INTO Utilisateur (pseudonyme,password,email) VALUES ('Sara', '9f86d081884c7d659a2feaa0c55ad015a3bf4f1b2b0b822cd15d6c15b0f00a08', 'sara@gmail.com');

 
