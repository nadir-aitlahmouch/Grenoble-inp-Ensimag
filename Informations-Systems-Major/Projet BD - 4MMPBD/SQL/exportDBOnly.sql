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
--------------------------------------------------------
--  DDL for Index HORAIRESPECTACLE_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "HORAIRESPECTACLE_PK" ON "HORAIRESPECTACLE" ("DATEDEBUT", "HEUREDEBUT")
--------------------------------------------------------
--  DDL for Index PK_APOURPSEUDO
--------------------------------------------------------

  CREATE UNIQUE INDEX "PK_APOURPSEUDO" ON "APOURPSEUDO" ("PSEUDO", "IDARTISTE")
--------------------------------------------------------
--  DDL for Index PK_CIRQUE
--------------------------------------------------------

  CREATE UNIQUE INDEX "PK_CIRQUE" ON "ESTDUCIRQUE" ("CODECIRQUE", "CODEN")
--------------------------------------------------------
--  DDL for Index PK_ESTPRESENT
--------------------------------------------------------

  CREATE UNIQUE INDEX "PK_ESTPRESENT" ON "ESTPRESENT" ("CODEN", "IDARTISTE")
--------------------------------------------------------
--  DDL for Index PK_EVAL
--------------------------------------------------------

  CREATE UNIQUE INDEX "PK_EVAL" ON "EVALUATION" ("CODEN", "IDARTISTE")
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
