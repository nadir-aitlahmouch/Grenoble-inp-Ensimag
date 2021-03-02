--------------------------------------------------------
--  Fichier créé - lundi-décembre-09-2019   
--------------------------------------------------------
--------------------------------------------------------
--  DDL for Table APOURPSEUDO
--------------------------------------------------------

  CREATE TABLE "APOURPSEUDO" ("PSEUDO" VARCHAR2(20), "IDARTISTE" NUMBER(*,0))
--------------------------------------------------------
--  DDL for Table ARTISTE
--------------------------------------------------------

  CREATE TABLE "ARTISTE" ("IDARTISTE" NUMBER(*,0), "NOM" VARCHAR2(20), "PRENOM" VARCHAR2(20), "DATENAISSANCE" DATE, "CODECIRQUE" NUMBER(*,0), "TEL" VARCHAR2(10))
--------------------------------------------------------
--  DDL for Table CIRQUE
--------------------------------------------------------

  CREATE TABLE "CIRQUE" ("CODECIRQUE" NUMBER(*,0), "NOM" VARCHAR2(20))
--------------------------------------------------------
--  DDL for Table ESTDANSLESPECTACLE
--------------------------------------------------------

  CREATE TABLE "ESTDANSLESPECTACLE" ("CODEN" NUMBER(*,0), "CODESPECTACLE" NUMBER(*,0))
--------------------------------------------------------
--  DDL for Table ESTDESPECIALITE
--------------------------------------------------------

  CREATE TABLE "ESTDESPECIALITE" ("SPECIALITE" VARCHAR2(20), "IDARTISTE" NUMBER(*,0))
--------------------------------------------------------
--  DDL for Table ESTDUCIRQUE
--------------------------------------------------------

  CREATE TABLE "ESTDUCIRQUE" ("CODECIRQUE" NUMBER(*,0), "CODEN" NUMBER(*,0))
--------------------------------------------------------
--  DDL for Table ESTPRESENT
--------------------------------------------------------

  CREATE TABLE "ESTPRESENT" ("CODEN" NUMBER(*,0), "IDARTISTE" NUMBER(*,0))
--------------------------------------------------------
--  DDL for Table EVALUATION
--------------------------------------------------------

  CREATE TABLE "EVALUATION" ("CODEN" NUMBER(*,0), "IDARTISTE" NUMBER(*,0), "NOTE" NUMBER(*,0), "COMMENTAIRE" VARCHAR2(255))
--------------------------------------------------------
--  DDL for Table EXPERT
--------------------------------------------------------

  CREATE TABLE "EXPERT" ("IDARTISTE" NUMBER(*,0), "NOM" VARCHAR2(20), "PRENOM" VARCHAR2(20), "DATENAISSANCE" DATE, "CODECIRQUE" NUMBER(*,0), "TEL" VARCHAR2(10))
--------------------------------------------------------
--  DDL for Table HORAIRESPECTACLE
--------------------------------------------------------

  CREATE TABLE "HORAIRESPECTACLE" ("DATEDEBUT" DATE, "HEUREDEBUT" NUMBER(*,0), "CODESPECTACLE" NUMBER(*,0))
--------------------------------------------------------
--  DDL for Table NUMERO
--------------------------------------------------------

  CREATE TABLE "NUMERO" ("CODEN" NUMBER(*,0), "TITRE" VARCHAR2(20), "RESUME" VARCHAR2(40), "DURÉE" NUMBER(*,0), "NBARTISTE" NUMBER(*,0), "THEME" VARCHAR2(20), "ARTISTEPRINCIPAL" NUMBER(*,0))
--------------------------------------------------------
--  DDL for Table PSEUDONYME
--------------------------------------------------------

  CREATE TABLE "PSEUDONYME" ("PSEUDO" VARCHAR2(20))
--------------------------------------------------------
--  DDL for Table SPECIALITE
--------------------------------------------------------

  CREATE TABLE "SPECIALITE" ("SPECIALITE" VARCHAR2(20))
--------------------------------------------------------
--  DDL for Table SPECTACLE
--------------------------------------------------------

  CREATE TABLE "SPECTACLE" ("CODESPECTACLE" NUMBER(*,0), "PRIXENTREE" NUMBER(*,0), "THEME" VARCHAR2(20), "PRESENTATEUR" NUMBER(*,0))
REM INSERTING into APOURPSEUDO
SET DEFINE OFF;
Insert into APOURPSEUDO (PSEUDO,IDARTISTE) values ('1','1');
Insert into APOURPSEUDO (PSEUDO,IDARTISTE) values ('Adam','10');
Insert into APOURPSEUDO (PSEUDO,IDARTISTE) values ('Adam','11');
Insert into APOURPSEUDO (PSEUDO,IDARTISTE) values ('Alex','1');
Insert into APOURPSEUDO (PSEUDO,IDARTISTE) values ('DE','250');
Insert into APOURPSEUDO (PSEUDO,IDARTISTE) values ('DEUX','1');
Insert into APOURPSEUDO (PSEUDO,IDARTISTE) values ('Le bon','11');
Insert into APOURPSEUDO (PSEUDO,IDARTISTE) values ('LoLo','3');
Insert into APOURPSEUDO (PSEUDO,IDARTISTE) values ('MIOOO','250');
Insert into APOURPSEUDO (PSEUDO,IDARTISTE) values ('Mehdi','1');
Insert into APOURPSEUDO (PSEUDO,IDARTISTE) values ('YOLO','11');
Insert into APOURPSEUDO (PSEUDO,IDARTISTE) values ('jcl','1');
Insert into APOURPSEUDO (PSEUDO,IDARTISTE) values ('jj','1');
Insert into APOURPSEUDO (PSEUDO,IDARTISTE) values ('mama','1');
Insert into APOURPSEUDO (PSEUDO,IDARTISTE) values ('mehdi','1');
Insert into APOURPSEUDO (PSEUDO,IDARTISTE) values ('mehdi','2');
Insert into APOURPSEUDO (PSEUDO,IDARTISTE) values ('mehdi','3');
Insert into APOURPSEUDO (PSEUDO,IDARTISTE) values ('mehdi','7');
Insert into APOURPSEUDO (PSEUDO,IDARTISTE) values ('mehdi','11');
Insert into APOURPSEUDO (PSEUDO,IDARTISTE) values ('mehdi','12');
Insert into APOURPSEUDO (PSEUDO,IDARTISTE) values ('mehdi','100');
Insert into APOURPSEUDO (PSEUDO,IDARTISTE) values ('mimi','1');
Insert into APOURPSEUDO (PSEUDO,IDARTISTE) values ('n''existe pas','1');
Insert into APOURPSEUDO (PSEUDO,IDARTISTE) values ('nABIL','1');
Insert into APOURPSEUDO (PSEUDO,IDARTISTE) values ('test1','1');
Insert into APOURPSEUDO (PSEUDO,IDARTISTE) values ('titi','1');
Insert into APOURPSEUDO (PSEUDO,IDARTISTE) values ('tito','1');
Insert into APOURPSEUDO (PSEUDO,IDARTISTE) values ('toto','1');
Insert into APOURPSEUDO (PSEUDO,IDARTISTE) values ('triche pas :3','12');
Insert into APOURPSEUDO (PSEUDO,IDARTISTE) values ('yo','0');
REM INSERTING into ARTISTE
SET DEFINE OFF;
Insert into ARTISTE (IDARTISTE,NOM,PRENOM,DATENAISSANCE,CODECIRQUE,TEL) values ('1','jeau','claude',to_date('27/10/98','DD/MM/RR'),'0','1010');
Insert into ARTISTE (IDARTISTE,NOM,PRENOM,DATENAISSANCE,CODECIRQUE,TEL) values ('2','jeau','claude',to_date('27/10/98','DD/MM/RR'),'0','1010');
Insert into ARTISTE (IDARTISTE,NOM,PRENOM,DATENAISSANCE,CODECIRQUE,TEL) values ('3','jean','claude',to_date('27/10/98','DD/MM/RR'),'0','1010');
Insert into ARTISTE (IDARTISTE,NOM,PRENOM,DATENAISSANCE,CODECIRQUE,TEL) values ('8','gi','ce',to_date('27/10/98','DD/MM/RR'),'1','1010');
Insert into ARTISTE (IDARTISTE,NOM,PRENOM,DATENAISSANCE,CODECIRQUE,TEL) values ('11','ci','jay',to_date('27/10/98','DD/MM/RR'),'2','1010');
Insert into ARTISTE (IDARTISTE,NOM,PRENOM,DATENAISSANCE,CODECIRQUE,TEL) values ('10','carl','jr',to_date('27/10/98','DD/MM/RR'),'2','1010');
Insert into ARTISTE (IDARTISTE,NOM,PRENOM,DATENAISSANCE,CODECIRQUE,TEL) values ('9','c','j',to_date('27/10/98','DD/MM/RR'),'2','1010');
Insert into ARTISTE (IDARTISTE,NOM,PRENOM,DATENAISSANCE,CODECIRQUE,TEL) values ('12','triche','heure',to_date('11/11/11','DD/MM/RR'),'0','0101');
Insert into ARTISTE (IDARTISTE,NOM,PRENOM,DATENAISSANCE,CODECIRQUE,TEL) values ('13','triche','pas',to_date('22/12/12','DD/MM/RR'),'2','1111');
Insert into ARTISTE (IDARTISTE,NOM,PRENOM,DATENAISSANCE,CODECIRQUE,TEL) values ('15','"h"','"b"',to_date('05/09/00','DD/MM/RR'),'1','"06187989"');
Insert into ARTISTE (IDARTISTE,NOM,PRENOM,DATENAISSANCE,CODECIRQUE,TEL) values ('51261256','benjelloun','hamza',to_date('20/09/98','DD/MM/RR'),'1','0618798900');
Insert into ARTISTE (IDARTISTE,NOM,PRENOM,DATENAISSANCE,CODECIRQUE,TEL) values ('18','Legria','Weld',to_date('08/06/90','DD/MM/RR'),'1','0606060606');
Insert into ARTISTE (IDARTISTE,NOM,PRENOM,DATENAISSANCE,CODECIRQUE,TEL) values ('56','Jay','cee',to_date('10/12/98','DD/MM/RR'),'3','11156');
Insert into ARTISTE (IDARTISTE,NOM,PRENOM,DATENAISSANCE,CODECIRQUE,TEL) values ('445454545','h','b',to_date('20/09/98','DD/MM/RR'),'1','555');
Insert into ARTISTE (IDARTISTE,NOM,PRENOM,DATENAISSANCE,CODECIRQUE,TEL) values ('1510','lola','carmen',to_date('16/05/95','DD/MM/RR'),'3','0666365696');
Insert into ARTISTE (IDARTISTE,NOM,PRENOM,DATENAISSANCE,CODECIRQUE,TEL) values ('300','Depir','Léo',to_date('12/12/96','DD/MM/RR'),'1','0674122558');
Insert into ARTISTE (IDARTISTE,NOM,PRENOM,DATENAISSANCE,CODECIRQUE,TEL) values ('1999','laen','malik',to_date('12/12/96','DD/MM/RR'),'3','0674122559');
Insert into ARTISTE (IDARTISTE,NOM,PRENOM,DATENAISSANCE,CODECIRQUE,TEL) values ('11111111','qfdq','qsdfqsf',to_date('11/11/11','DD/MM/RR'),'3','qsdf');
Insert into ARTISTE (IDARTISTE,NOM,PRENOM,DATENAISSANCE,CODECIRQUE,TEL) values ('11511','qsdfqsd','qsdfqdf',to_date('11/11/11','DD/MM/RR'),null,'qsdfqsfd');
Insert into ARTISTE (IDARTISTE,NOM,PRENOM,DATENAISSANCE,CODECIRQUE,TEL) values ('7','gi','ce',to_date('27/10/98','DD/MM/RR'),'1','1010');
Insert into ARTISTE (IDARTISTE,NOM,PRENOM,DATENAISSANCE,CODECIRQUE,TEL) values ('88','gean','claude',to_date('11/12/11','DD/MM/RR'),'3','784815');
Insert into ARTISTE (IDARTISTE,NOM,PRENOM,DATENAISSANCE,CODECIRQUE,TEL) values ('77','ja','js',to_date('11/11/11','DD/MM/RR'),'2','1');
Insert into ARTISTE (IDARTISTE,NOM,PRENOM,DATENAISSANCE,CODECIRQUE,TEL) values ('99','qsd','qsd',to_date('11/11/11','DD/MM/RR'),'2','1');
Insert into ARTISTE (IDARTISTE,NOM,PRENOM,DATENAISSANCE,CODECIRQUE,TEL) values ('0','idiot','imbecile',to_date('01/04/15','DD/MM/RR'),'0','0101');
Insert into ARTISTE (IDARTISTE,NOM,PRENOM,DATENAISSANCE,CODECIRQUE,TEL) values ('4','j','claude',to_date('27/10/98','DD/MM/RR'),'3','1010');
Insert into ARTISTE (IDARTISTE,NOM,PRENOM,DATENAISSANCE,CODECIRQUE,TEL) values ('5','j','c',to_date('27/10/98','DD/MM/RR'),'3','1010');
Insert into ARTISTE (IDARTISTE,NOM,PRENOM,DATENAISSANCE,CODECIRQUE,TEL) values ('6','jay','cee',to_date('27/10/98','DD/MM/RR'),'1','1010');
Insert into ARTISTE (IDARTISTE,NOM,PRENOM,DATENAISSANCE,CODECIRQUE,TEL) values ('55','Hello','Kenobi',to_date('25/10/97','DD/MM/RR'),'3','1448');
Insert into ARTISTE (IDARTISTE,NOM,PRENOM,DATENAISSANCE,CODECIRQUE,TEL) values ('20','lazaar','fatouma',to_date('22/05/98','DD/MM/RR'),'2','0645467989');
Insert into ARTISTE (IDARTISTE,NOM,PRENOM,DATENAISSANCE,CODECIRQUE,TEL) values ('100','Dupont','Léon',to_date('04/12/96','DD/MM/RR'),'3','0649865223');
Insert into ARTISTE (IDARTISTE,NOM,PRENOM,DATENAISSANCE,CODECIRQUE,TEL) values ('250','jahim','Martin',to_date('12/02/95','DD/MM/RR'),'1','0632233665');
Insert into ARTISTE (IDARTISTE,NOM,PRENOM,DATENAISSANCE,CODECIRQUE,TEL) values ('155155','a','l',to_date('10/10/11','DD/MM/RR'),'1','0');
Insert into ARTISTE (IDARTISTE,NOM,PRENOM,DATENAISSANCE,CODECIRQUE,TEL) values ('489494','jsdfj','sjdfs',to_date('11/11/11','DD/MM/RR'),'1','1');
Insert into ARTISTE (IDARTISTE,NOM,PRENOM,DATENAISSANCE,CODECIRQUE,TEL) values ('7777777','jsdfj','sjdfs',to_date('11/11/11','DD/MM/RR'),'1','1');
Insert into ARTISTE (IDARTISTE,NOM,PRENOM,DATENAISSANCE,CODECIRQUE,TEL) values ('234567','richard','luka',to_date('10/12/00','DD/MM/RR'),'1','0616738660');
Insert into ARTISTE (IDARTISTE,NOM,PRENOM,DATENAISSANCE,CODECIRQUE,TEL) values ('232','omar','benchekroun',to_date('11/12/90','DD/MM/RR'),'2','0655667788');
Insert into ARTISTE (IDARTISTE,NOM,PRENOM,DATENAISSANCE,CODECIRQUE,TEL) values ('5415','youness','luka',to_date('20/12/00','DD/MM/RR'),'2','0612356898');
REM INSERTING into CIRQUE
SET DEFINE OFF;
Insert into CIRQUE (CODECIRQUE,NOM) values ('1','cirque 1');
Insert into CIRQUE (CODECIRQUE,NOM) values ('2','cirque 2');
Insert into CIRQUE (CODECIRQUE,NOM) values ('3','cirque 3');
Insert into CIRQUE (CODECIRQUE,NOM) values ('0','cirque 0');
REM INSERTING into ESTDANSLESPECTACLE
SET DEFINE OFF;
Insert into ESTDANSLESPECTACLE (CODEN,CODESPECTACLE) values ('8749484','123');
REM INSERTING into ESTDESPECIALITE
SET DEFINE OFF;
Insert into ESTDESPECIALITE (SPECIALITE,IDARTISTE) values ('Danse','0');
Insert into ESTDESPECIALITE (SPECIALITE,IDARTISTE) values ('Danse','1');
Insert into ESTDESPECIALITE (SPECIALITE,IDARTISTE) values ('Danse','6');
Insert into ESTDESPECIALITE (SPECIALITE,IDARTISTE) values ('Danse','11');
Insert into ESTDESPECIALITE (SPECIALITE,IDARTISTE) values ('Danse','18');
Insert into ESTDESPECIALITE (SPECIALITE,IDARTISTE) values ('Danse','56');
Insert into ESTDESPECIALITE (SPECIALITE,IDARTISTE) values ('Danse','88');
Insert into ESTDESPECIALITE (SPECIALITE,IDARTISTE) values ('Danse','232');
Insert into ESTDESPECIALITE (SPECIALITE,IDARTISTE) values ('Danse','300');
Insert into ESTDESPECIALITE (SPECIALITE,IDARTISTE) values ('Danse','1999');
Insert into ESTDESPECIALITE (SPECIALITE,IDARTISTE) values ('Danse','5415');
Insert into ESTDESPECIALITE (SPECIALITE,IDARTISTE) values ('Danse','11511');
Insert into ESTDESPECIALITE (SPECIALITE,IDARTISTE) values ('Danse','155155');
Insert into ESTDESPECIALITE (SPECIALITE,IDARTISTE) values ('Danse','234567');
Insert into ESTDESPECIALITE (SPECIALITE,IDARTISTE) values ('Danse','11111111');
Insert into ESTDESPECIALITE (SPECIALITE,IDARTISTE) values ('Magie','99');
Insert into ESTDESPECIALITE (SPECIALITE,IDARTISTE) values ('a','3');
Insert into ESTDESPECIALITE (SPECIALITE,IDARTISTE) values ('a','5');
Insert into ESTDESPECIALITE (SPECIALITE,IDARTISTE) values ('a','7');
Insert into ESTDESPECIALITE (SPECIALITE,IDARTISTE) values ('a','9');
Insert into ESTDESPECIALITE (SPECIALITE,IDARTISTE) values ('a','10');
Insert into ESTDESPECIALITE (SPECIALITE,IDARTISTE) values ('a','12');
Insert into ESTDESPECIALITE (SPECIALITE,IDARTISTE) values ('a','13');
Insert into ESTDESPECIALITE (SPECIALITE,IDARTISTE) values ('a','489494');
Insert into ESTDESPECIALITE (SPECIALITE,IDARTISTE) values ('a','7777777');
Insert into ESTDESPECIALITE (SPECIALITE,IDARTISTE) values ('b','7');
Insert into ESTDESPECIALITE (SPECIALITE,IDARTISTE) values ('clown','99');
Insert into ESTDESPECIALITE (SPECIALITE,IDARTISTE) values ('clown','489494');
Insert into ESTDESPECIALITE (SPECIALITE,IDARTISTE) values ('clown','7777777');
Insert into ESTDESPECIALITE (SPECIALITE,IDARTISTE) values ('dresseur','1999');
Insert into ESTDESPECIALITE (SPECIALITE,IDARTISTE) values ('jongleur','0');
Insert into ESTDESPECIALITE (SPECIALITE,IDARTISTE) values ('jongleur','2');
Insert into ESTDESPECIALITE (SPECIALITE,IDARTISTE) values ('jongleur','15');
Insert into ESTDESPECIALITE (SPECIALITE,IDARTISTE) values ('jongleur','55');
Insert into ESTDESPECIALITE (SPECIALITE,IDARTISTE) values ('jongleur','51261256');
Insert into ESTDESPECIALITE (SPECIALITE,IDARTISTE) values ('lancer de couteaux','4');
Insert into ESTDESPECIALITE (SPECIALITE,IDARTISTE) values ('lancer de couteaux','8');
REM INSERTING into ESTDUCIRQUE
SET DEFINE OFF;
REM INSERTING into ESTPRESENT
SET DEFINE OFF;
Insert into ESTPRESENT (CODEN,IDARTISTE) values ('1','0');
Insert into ESTPRESENT (CODEN,IDARTISTE) values ('1','1');
Insert into ESTPRESENT (CODEN,IDARTISTE) values ('1','2');
Insert into ESTPRESENT (CODEN,IDARTISTE) values ('1','3');
Insert into ESTPRESENT (CODEN,IDARTISTE) values ('1','4');
Insert into ESTPRESENT (CODEN,IDARTISTE) values ('1','5');
Insert into ESTPRESENT (CODEN,IDARTISTE) values ('1','10');
Insert into ESTPRESENT (CODEN,IDARTISTE) values ('1','232');
Insert into ESTPRESENT (CODEN,IDARTISTE) values ('1','234567');
Insert into ESTPRESENT (CODEN,IDARTISTE) values ('55','9');
Insert into ESTPRESENT (CODEN,IDARTISTE) values ('55','1999');
Insert into ESTPRESENT (CODEN,IDARTISTE) values ('55','445454545');
Insert into ESTPRESENT (CODEN,IDARTISTE) values ('1234','2');
Insert into ESTPRESENT (CODEN,IDARTISTE) values ('544541','10');
Insert into ESTPRESENT (CODEN,IDARTISTE) values ('544541','20');
Insert into ESTPRESENT (CODEN,IDARTISTE) values ('8749484','10');
Insert into ESTPRESENT (CODEN,IDARTISTE) values ('8749484','1999');
REM INSERTING into EVALUATION
SET DEFINE OFF;
Insert into EVALUATION (CODEN,IDARTISTE,NOTE,COMMENTAIRE) values ('3','51261256','4',null);
Insert into EVALUATION (CODEN,IDARTISTE,NOTE,COMMENTAIRE) values ('3','55','4',null);
Insert into EVALUATION (CODEN,IDARTISTE,NOTE,COMMENTAIRE) values ('3','15','0',null);
Insert into EVALUATION (CODEN,IDARTISTE,NOTE,COMMENTAIRE) values ('544541','2','0',null);
Insert into EVALUATION (CODEN,IDARTISTE,NOTE,COMMENTAIRE) values ('544541','18','0',null);
Insert into EVALUATION (CODEN,IDARTISTE,NOTE,COMMENTAIRE) values ('3','18','4',null);
Insert into EVALUATION (CODEN,IDARTISTE,NOTE,COMMENTAIRE) values ('3','4','5',null);
Insert into EVALUATION (CODEN,IDARTISTE,NOTE,COMMENTAIRE) values ('544541','234567','0',null);
Insert into EVALUATION (CODEN,IDARTISTE,NOTE,COMMENTAIRE) values ('544541','5','0',null);
Insert into EVALUATION (CODEN,IDARTISTE,NOTE,COMMENTAIRE) values ('544541','56','0',null);
Insert into EVALUATION (CODEN,IDARTISTE,NOTE,COMMENTAIRE) values ('78','489494','3',null);
Insert into EVALUATION (CODEN,IDARTISTE,NOTE,COMMENTAIRE) values ('78','234567','4',null);
Insert into EVALUATION (CODEN,IDARTISTE,NOTE,COMMENTAIRE) values ('78','51261256','0',null);
Insert into EVALUATION (CODEN,IDARTISTE,NOTE,COMMENTAIRE) values ('78','12','10','magnifique');
Insert into EVALUATION (CODEN,IDARTISTE,NOTE,COMMENTAIRE) values ('78','13','7',null);
Insert into EVALUATION (CODEN,IDARTISTE,NOTE,COMMENTAIRE) values ('123','489494','3',null);
Insert into EVALUATION (CODEN,IDARTISTE,NOTE,COMMENTAIRE) values ('123','18','1',null);
Insert into EVALUATION (CODEN,IDARTISTE,NOTE,COMMENTAIRE) values ('123','2','5',null);
Insert into EVALUATION (CODEN,IDARTISTE,NOTE,COMMENTAIRE) values ('4','4','2',null);
Insert into EVALUATION (CODEN,IDARTISTE,NOTE,COMMENTAIRE) values ('4','234567','0',null);
Insert into EVALUATION (CODEN,IDARTISTE,NOTE,COMMENTAIRE) values ('4','51261256','0',null);
Insert into EVALUATION (CODEN,IDARTISTE,NOTE,COMMENTAIRE) values ('4','55','0',null);
Insert into EVALUATION (CODEN,IDARTISTE,NOTE,COMMENTAIRE) values ('4','15','0',null);
Insert into EVALUATION (CODEN,IDARTISTE,NOTE,COMMENTAIRE) values ('123','0','3',null);
Insert into EVALUATION (CODEN,IDARTISTE,NOTE,COMMENTAIRE) values ('123','1','4',null);
REM INSERTING into EXPERT
SET DEFINE OFF;
Insert into EXPERT (IDARTISTE,NOM,PRENOM,DATENAISSANCE,CODECIRQUE,TEL) values ('1','jeau','claude',to_date('27/10/98','DD/MM/RR'),'0','1010');
Insert into EXPERT (IDARTISTE,NOM,PRENOM,DATENAISSANCE,CODECIRQUE,TEL) values ('12','triche','heure',to_date('11/11/11','DD/MM/RR'),'2','0101');
Insert into EXPERT (IDARTISTE,NOM,PRENOM,DATENAISSANCE,CODECIRQUE,TEL) values ('13','triche','pas',to_date('22/12/12','DD/MM/RR'),'1','1111');
Insert into EXPERT (IDARTISTE,NOM,PRENOM,DATENAISSANCE,CODECIRQUE,TEL) values ('4','j','claude',to_date('27/10/98','DD/MM/RR'),'3','1010');
Insert into EXPERT (IDARTISTE,NOM,PRENOM,DATENAISSANCE,CODECIRQUE,TEL) values ('5','j','c',to_date('27/10/98','DD/MM/RR'),'3','1010');
Insert into EXPERT (IDARTISTE,NOM,PRENOM,DATENAISSANCE,CODECIRQUE,TEL) values ('2','jeau','claude',to_date('27/10/98','DD/MM/RR'),'0','1010');
Insert into EXPERT (IDARTISTE,NOM,PRENOM,DATENAISSANCE,CODECIRQUE,TEL) values ('11','ci','jay',to_date('27/10/98','DD/MM/RR'),'2','1010');
Insert into EXPERT (IDARTISTE,NOM,PRENOM,DATENAISSANCE,CODECIRQUE,TEL) values ('15','"h"','"b"',to_date('05/09/00','DD/MM/RR'),'1','"06187989"');
Insert into EXPERT (IDARTISTE,NOM,PRENOM,DATENAISSANCE,CODECIRQUE,TEL) values ('51261256','benjelloun','hamza',to_date('20/09/98','DD/MM/RR'),'1','0618798900');
Insert into EXPERT (IDARTISTE,NOM,PRENOM,DATENAISSANCE,CODECIRQUE,TEL) values ('18','Legria','Weld',to_date('08/06/90','DD/MM/RR'),'1','0606060606');
Insert into EXPERT (IDARTISTE,NOM,PRENOM,DATENAISSANCE,CODECIRQUE,TEL) values ('56','Jay','cee',to_date('10/12/98','DD/MM/RR'),'3','11156');
Insert into EXPERT (IDARTISTE,NOM,PRENOM,DATENAISSANCE,CODECIRQUE,TEL) values ('0','idiot','imbecile',to_date('01/04/15','DD/MM/RR'),'0','0101');
Insert into EXPERT (IDARTISTE,NOM,PRENOM,DATENAISSANCE,CODECIRQUE,TEL) values ('55','Hello','Kenobi',to_date('25/10/97','DD/MM/RR'),'3','1448');
Insert into EXPERT (IDARTISTE,NOM,PRENOM,DATENAISSANCE,CODECIRQUE,TEL) values ('1510','lola','carmen',to_date('16/05/95','DD/MM/RR'),'3','0666365696');
Insert into EXPERT (IDARTISTE,NOM,PRENOM,DATENAISSANCE,CODECIRQUE,TEL) values ('300','Depir','Léo',to_date('12/12/96','DD/MM/RR'),'1','0674122558');
Insert into EXPERT (IDARTISTE,NOM,PRENOM,DATENAISSANCE,CODECIRQUE,TEL) values ('100','Dupont','Léon',to_date('04/12/96','DD/MM/RR'),'3','0649865223');
Insert into EXPERT (IDARTISTE,NOM,PRENOM,DATENAISSANCE,CODECIRQUE,TEL) values ('250','jahim','Martin',to_date('12/02/95','DD/MM/RR'),'1','0632233665');
Insert into EXPERT (IDARTISTE,NOM,PRENOM,DATENAISSANCE,CODECIRQUE,TEL) values ('489494','jsdfj','sjdfs',to_date('11/11/11','DD/MM/RR'),'1','1');
Insert into EXPERT (IDARTISTE,NOM,PRENOM,DATENAISSANCE,CODECIRQUE,TEL) values ('234567','richard','luka',to_date('10/12/00','DD/MM/RR'),'1','0616738660');
Insert into EXPERT (IDARTISTE,NOM,PRENOM,DATENAISSANCE,CODECIRQUE,TEL) values ('232','omar','benchekroun',to_date('11/12/90','DD/MM/RR'),'2','0655667788');
REM INSERTING into HORAIRESPECTACLE
SET DEFINE OFF;
Insert into HORAIRESPECTACLE (DATEDEBUT,HEUREDEBUT,CODESPECTACLE) values (to_date('27/12/19','DD/MM/RR'),'9','123');
Insert into HORAIRESPECTACLE (DATEDEBUT,HEUREDEBUT,CODESPECTACLE) values (to_date('16/12/19','DD/MM/RR'),'9','14');
REM INSERTING into NUMERO
SET DEFINE OFF;
Insert into NUMERO (CODEN,TITRE,RESUME,"DURÉE",NBARTISTE,THEME,ARTISTEPRINCIPAL) values ('8749484','dighsdu','sdhfqsjfh','11','2','Danse','10');
Insert into NUMERO (CODEN,TITRE,RESUME,"DURÉE",NBARTISTE,THEME,ARTISTEPRINCIPAL) values ('4','titre 4','its cool','11','7','jongleur','4');
Insert into NUMERO (CODEN,TITRE,RESUME,"DURÉE",NBARTISTE,THEME,ARTISTEPRINCIPAL) values ('123','Ballet','Ballet classique','20','10','Danse','5');
Insert into NUMERO (CODEN,TITRE,RESUME,"DURÉE",NBARTISTE,THEME,ARTISTEPRINCIPAL) values ('78','qsjdfhj','jsqdflkqjsfk','10','2','a','55');
Insert into NUMERO (CODEN,TITRE,RESUME,"DURÉE",NBARTISTE,THEME,ARTISTEPRINCIPAL) values ('54545','titre5463','cool','10','100','lancer de couteaux','1');
Insert into NUMERO (CODEN,TITRE,RESUME,"DURÉE",NBARTISTE,THEME,ARTISTEPRINCIPAL) values ('1234','Charcot','hypnose avec charcot','20','1','Magie','489494');
Insert into NUMERO (CODEN,TITRE,RESUME,"DURÉE",NBARTISTE,THEME,ARTISTEPRINCIPAL) values ('489449888','Blabla','bbb','11','2','Danse','3');
Insert into NUMERO (CODEN,TITRE,RESUME,"DURÉE",NBARTISTE,THEME,ARTISTEPRINCIPAL) values ('1','j','j','11','1','Danse','10');
Insert into NUMERO (CODEN,TITRE,RESUME,"DURÉE",NBARTISTE,THEME,ARTISTEPRINCIPAL) values ('55','titre68','jouer avec des cartes','26','3','jongleur','9');
Insert into NUMERO (CODEN,TITRE,RESUME,"DURÉE",NBARTISTE,THEME,ARTISTEPRINCIPAL) values ('2','titre 2','its cool','11','1','jongleur','3');
Insert into NUMERO (CODEN,TITRE,RESUME,"DURÉE",NBARTISTE,THEME,ARTISTEPRINCIPAL) values ('3','titre 3','its cool','11','4','jongleur','8');
Insert into NUMERO (CODEN,TITRE,RESUME,"DURÉE",NBARTISTE,THEME,ARTISTEPRINCIPAL) values ('13','TestDeleteCascade','TestDeleteCascade','11','1','jongleur','1');
Insert into NUMERO (CODEN,TITRE,RESUME,"DURÉE",NBARTISTE,THEME,ARTISTEPRINCIPAL) values ('69','Danse test','Un test de dance','14','10000','Danse','1');
Insert into NUMERO (CODEN,TITRE,RESUME,"DURÉE",NBARTISTE,THEME,ARTISTEPRINCIPAL) values ('544541','sdfqsdf','qsdfqsdfd','10','2','Danse','20');
REM INSERTING into PSEUDONYME
SET DEFINE OFF;
Insert into PSEUDONYME (PSEUDO) values ('1');
Insert into PSEUDONYME (PSEUDO) values ('Adam');
Insert into PSEUDONYME (PSEUDO) values ('Alex');
Insert into PSEUDONYME (PSEUDO) values ('DE');
Insert into PSEUDONYME (PSEUDO) values ('DEUX');
Insert into PSEUDONYME (PSEUDO) values ('Le bon');
Insert into PSEUDONYME (PSEUDO) values ('LoLo');
Insert into PSEUDONYME (PSEUDO) values ('MIOOO');
Insert into PSEUDONYME (PSEUDO) values ('Mehdi');
Insert into PSEUDONYME (PSEUDO) values ('YOLO');
Insert into PSEUDONYME (PSEUDO) values ('jcl');
Insert into PSEUDONYME (PSEUDO) values ('jj');
Insert into PSEUDONYME (PSEUDO) values ('mama');
Insert into PSEUDONYME (PSEUDO) values ('mehdi');
Insert into PSEUDONYME (PSEUDO) values ('mimi');
Insert into PSEUDONYME (PSEUDO) values ('n''existe pas');
Insert into PSEUDONYME (PSEUDO) values ('nABIL');
Insert into PSEUDONYME (PSEUDO) values ('test1');
Insert into PSEUDONYME (PSEUDO) values ('titi');
Insert into PSEUDONYME (PSEUDO) values ('tito');
Insert into PSEUDONYME (PSEUDO) values ('toto');
Insert into PSEUDONYME (PSEUDO) values ('triche pas :3');
Insert into PSEUDONYME (PSEUDO) values ('yo');
REM INSERTING into SPECIALITE
SET DEFINE OFF;
Insert into SPECIALITE (SPECIALITE) values ('Danse');
Insert into SPECIALITE (SPECIALITE) values ('Magie');
Insert into SPECIALITE (SPECIALITE) values ('a');
Insert into SPECIALITE (SPECIALITE) values ('b');
Insert into SPECIALITE (SPECIALITE) values ('clown');
Insert into SPECIALITE (SPECIALITE) values ('dresseur');
Insert into SPECIALITE (SPECIALITE) values ('jongleur');
Insert into SPECIALITE (SPECIALITE) values ('lancer de couteaux');
REM INSERTING into SPECTACLE
SET DEFINE OFF;
Insert into SPECTACLE (CODESPECTACLE,PRIXENTREE,THEME,PRESENTATEUR) values ('123','12','Danse','1');
Insert into SPECTACLE (CODESPECTACLE,PRIXENTREE,THEME,PRESENTATEUR) values ('14','12','Danse','1');
--------------------------------------------------------
--  DDL for Index PK_APOURPSEUDO
--------------------------------------------------------

  CREATE UNIQUE INDEX "PK_APOURPSEUDO" ON "APOURPSEUDO" ("PSEUDO", "IDARTISTE")
--------------------------------------------------------
--  DDL for Index PK_EVAL
--------------------------------------------------------

  CREATE UNIQUE INDEX "PK_EVAL" ON "EVALUATION" ("CODEN", "IDARTISTE")
--------------------------------------------------------
--  DDL for Index HORAIRESPECTACLE_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "HORAIRESPECTACLE_PK" ON "HORAIRESPECTACLE" ("DATEDEBUT", "HEUREDEBUT")
--------------------------------------------------------
--  DDL for Index PK_CIRQUE
--------------------------------------------------------

  CREATE UNIQUE INDEX "PK_CIRQUE" ON "ESTDUCIRQUE" ("CODECIRQUE", "CODEN")
--------------------------------------------------------
--  DDL for Index PK_ESTPRESENT
--------------------------------------------------------

  CREATE UNIQUE INDEX "PK_ESTPRESENT" ON "ESTPRESENT" ("CODEN", "IDARTISTE")
--------------------------------------------------------
--  Constraints for Table EVALUATION
--------------------------------------------------------

  ALTER TABLE "EVALUATION" ADD CONSTRAINT "C_NOTE" CHECK (0 <= Note and Note <= 10) ENABLE
  ALTER TABLE "EVALUATION" ADD CONSTRAINT "PK_EVAL" PRIMARY KEY ("CODEN", "IDARTISTE") USING INDEX  ENABLE
  ALTER TABLE "EVALUATION" MODIFY ("NOTE" NOT NULL ENABLE)
--------------------------------------------------------
--  Constraints for Table EXPERT
--------------------------------------------------------

  ALTER TABLE "EXPERT" ADD PRIMARY KEY ("IDARTISTE") USING INDEX  ENABLE
  ALTER TABLE "EXPERT" MODIFY ("NOM" NOT NULL ENABLE)
  ALTER TABLE "EXPERT" MODIFY ("PRENOM" NOT NULL ENABLE)
  ALTER TABLE "EXPERT" MODIFY ("DATENAISSANCE" NOT NULL ENABLE)
  ALTER TABLE "EXPERT" MODIFY ("CODECIRQUE" NOT NULL ENABLE)
  ALTER TABLE "EXPERT" MODIFY ("TEL" NOT NULL ENABLE)
--------------------------------------------------------
--  Constraints for Table ESTDUCIRQUE
--------------------------------------------------------

  ALTER TABLE "ESTDUCIRQUE" ADD CONSTRAINT "PK_CIRQUE" PRIMARY KEY ("CODECIRQUE", "CODEN") USING INDEX  ENABLE
--------------------------------------------------------
--  Constraints for Table PSEUDONYME
--------------------------------------------------------

  ALTER TABLE "PSEUDONYME" ADD PRIMARY KEY ("PSEUDO") USING INDEX  ENABLE
--------------------------------------------------------
--  Constraints for Table SPECTACLE
--------------------------------------------------------

  ALTER TABLE "SPECTACLE" ADD CONSTRAINT "SPECTACLE_PRIX_CHK" CHECK (prixentree >= 0) ENABLE
  ALTER TABLE "SPECTACLE" ADD PRIMARY KEY ("CODESPECTACLE") USING INDEX  ENABLE
  ALTER TABLE "SPECTACLE" MODIFY ("PRIXENTREE" NOT NULL ENABLE)
  ALTER TABLE "SPECTACLE" MODIFY ("THEME" NOT NULL ENABLE)
  ALTER TABLE "SPECTACLE" MODIFY ("PRESENTATEUR" NOT NULL ENABLE)
--------------------------------------------------------
--  Constraints for Table ARTISTE
--------------------------------------------------------

  ALTER TABLE "ARTISTE" ADD PRIMARY KEY ("IDARTISTE") USING INDEX  ENABLE
  ALTER TABLE "ARTISTE" MODIFY ("NOM" NOT NULL ENABLE)
  ALTER TABLE "ARTISTE" MODIFY ("PRENOM" NOT NULL ENABLE)
  ALTER TABLE "ARTISTE" MODIFY ("DATENAISSANCE" NOT NULL ENABLE)
  ALTER TABLE "ARTISTE" MODIFY ("TEL" NOT NULL ENABLE)
--------------------------------------------------------
--  Constraints for Table ESTPRESENT
--------------------------------------------------------

  ALTER TABLE "ESTPRESENT" ADD CONSTRAINT "PK_ESTPRESENT" PRIMARY KEY ("CODEN", "IDARTISTE") USING INDEX  ENABLE
--------------------------------------------------------
--  Constraints for Table APOURPSEUDO
--------------------------------------------------------

  ALTER TABLE "APOURPSEUDO" ADD CONSTRAINT "PK_APOURPSEUDO" PRIMARY KEY ("PSEUDO", "IDARTISTE") USING INDEX  ENABLE
--------------------------------------------------------
--  Constraints for Table ESTDANSLESPECTACLE
--------------------------------------------------------

  ALTER TABLE "ESTDANSLESPECTACLE" ADD PRIMARY KEY ("CODEN") USING INDEX  ENABLE
  ALTER TABLE "ESTDANSLESPECTACLE" MODIFY ("CODESPECTACLE" NOT NULL ENABLE)
--------------------------------------------------------
--  Constraints for Table HORAIRESPECTACLE
--------------------------------------------------------

  ALTER TABLE "HORAIRESPECTACLE" ADD CONSTRAINT "HEURE_CHK" CHECK (heuredebut = 9 OR HEUREDEBUT = 14) ENABLE
  ALTER TABLE "HORAIRESPECTACLE" ADD CONSTRAINT "HORAIRESPECTACLE_PK" PRIMARY KEY ("DATEDEBUT", "HEUREDEBUT") USING INDEX  ENABLE
--------------------------------------------------------
--  Constraints for Table NUMERO
--------------------------------------------------------

  ALTER TABLE "NUMERO" ADD CONSTRAINT "DUREE_VALUE" CHECK (durée <= 30 and durée >= 10) ENABLE
  ALTER TABLE "NUMERO" ADD CONSTRAINT "NBARTISTECHECK" CHECK (nbartiste > 0) ENABLE
  ALTER TABLE "NUMERO" ADD PRIMARY KEY ("CODEN") USING INDEX  ENABLE
  ALTER TABLE "NUMERO" MODIFY ("TITRE" NOT NULL ENABLE)
  ALTER TABLE "NUMERO" MODIFY ("RESUME" NOT NULL ENABLE)
  ALTER TABLE "NUMERO" MODIFY ("DURÉE" NOT NULL ENABLE)
  ALTER TABLE "NUMERO" MODIFY ("NBARTISTE" NOT NULL ENABLE)
  ALTER TABLE "NUMERO" MODIFY ("THEME" NOT NULL ENABLE)
  ALTER TABLE "NUMERO" MODIFY ("ARTISTEPRINCIPAL" NOT NULL ENABLE)
--------------------------------------------------------
--  Constraints for Table CIRQUE
--------------------------------------------------------

  ALTER TABLE "CIRQUE" ADD PRIMARY KEY ("CODECIRQUE") USING INDEX  ENABLE
  ALTER TABLE "CIRQUE" MODIFY ("NOM" NOT NULL ENABLE)
--------------------------------------------------------
--  Constraints for Table SPECIALITE
--------------------------------------------------------

  ALTER TABLE "SPECIALITE" ADD PRIMARY KEY ("SPECIALITE") USING INDEX  ENABLE
--------------------------------------------------------
--  Constraints for Table ESTDESPECIALITE
--------------------------------------------------------

  ALTER TABLE "ESTDESPECIALITE" ADD PRIMARY KEY ("SPECIALITE", "IDARTISTE") USING INDEX  ENABLE
--------------------------------------------------------
--  Ref Constraints for Table APOURPSEUDO
--------------------------------------------------------

  ALTER TABLE "APOURPSEUDO" ADD FOREIGN KEY ("IDARTISTE") REFERENCES "ARTISTE" ("IDARTISTE") ENABLE
  ALTER TABLE "APOURPSEUDO" ADD FOREIGN KEY ("PSEUDO") REFERENCES "PSEUDONYME" ("PSEUDO") ENABLE
--------------------------------------------------------
--  Ref Constraints for Table ARTISTE
--------------------------------------------------------

  ALTER TABLE "ARTISTE" ADD FOREIGN KEY ("CODECIRQUE") REFERENCES "CIRQUE" ("CODECIRQUE") ENABLE
--------------------------------------------------------
--  Ref Constraints for Table ESTDANSLESPECTACLE
--------------------------------------------------------

  ALTER TABLE "ESTDANSLESPECTACLE" ADD CONSTRAINT "FK_SPECTACLE" FOREIGN KEY ("CODESPECTACLE") REFERENCES "SPECTACLE" ("CODESPECTACLE") ON DELETE CASCADE ENABLE
  ALTER TABLE "ESTDANSLESPECTACLE" ADD FOREIGN KEY ("CODEN") REFERENCES "NUMERO" ("CODEN") ENABLE
--------------------------------------------------------
--  Ref Constraints for Table ESTDESPECIALITE
--------------------------------------------------------

  ALTER TABLE "ESTDESPECIALITE" ADD FOREIGN KEY ("IDARTISTE") REFERENCES "ARTISTE" ("IDARTISTE") ENABLE
  ALTER TABLE "ESTDESPECIALITE" ADD FOREIGN KEY ("SPECIALITE") REFERENCES "SPECIALITE" ("SPECIALITE") ENABLE
--------------------------------------------------------
--  Ref Constraints for Table ESTDUCIRQUE
--------------------------------------------------------

  ALTER TABLE "ESTDUCIRQUE" ADD FOREIGN KEY ("CODECIRQUE") REFERENCES "CIRQUE" ("CODECIRQUE") ENABLE
  ALTER TABLE "ESTDUCIRQUE" ADD FOREIGN KEY ("CODEN") REFERENCES "NUMERO" ("CODEN") ENABLE
--------------------------------------------------------
--  Ref Constraints for Table ESTPRESENT
--------------------------------------------------------

  ALTER TABLE "ESTPRESENT" ADD FOREIGN KEY ("CODEN") REFERENCES "NUMERO" ("CODEN") ENABLE
  ALTER TABLE "ESTPRESENT" ADD FOREIGN KEY ("IDARTISTE") REFERENCES "ARTISTE" ("IDARTISTE") ENABLE
--------------------------------------------------------
--  Ref Constraints for Table EVALUATION
--------------------------------------------------------

  ALTER TABLE "EVALUATION" ADD FOREIGN KEY ("CODEN") REFERENCES "NUMERO" ("CODEN") ENABLE
  ALTER TABLE "EVALUATION" ADD FOREIGN KEY ("IDARTISTE") REFERENCES "EXPERT" ("IDARTISTE") ENABLE
--------------------------------------------------------
--  Ref Constraints for Table EXPERT
--------------------------------------------------------

  ALTER TABLE "EXPERT" ADD FOREIGN KEY ("CODECIRQUE") REFERENCES "CIRQUE" ("CODECIRQUE") ENABLE
--------------------------------------------------------
--  Ref Constraints for Table HORAIRESPECTACLE
--------------------------------------------------------

  ALTER TABLE "HORAIRESPECTACLE" ADD FOREIGN KEY ("CODESPECTACLE") REFERENCES "SPECTACLE" ("CODESPECTACLE") ON DELETE CASCADE ENABLE
--------------------------------------------------------
--  Ref Constraints for Table NUMERO
--------------------------------------------------------

  ALTER TABLE "NUMERO" ADD FOREIGN KEY ("THEME") REFERENCES "SPECIALITE" ("SPECIALITE") ENABLE
  ALTER TABLE "NUMERO" ADD FOREIGN KEY ("ARTISTEPRINCIPAL") REFERENCES "ARTISTE" ("IDARTISTE") ENABLE
--------------------------------------------------------
--  Ref Constraints for Table SPECTACLE
--------------------------------------------------------

  ALTER TABLE "SPECTACLE" ADD FOREIGN KEY ("PRESENTATEUR") REFERENCES "ARTISTE" ("IDARTISTE") ENABLE
  ALTER TABLE "SPECTACLE" ADD FOREIGN KEY ("THEME") REFERENCES "SPECIALITE" ("SPECIALITE") ENABLE
