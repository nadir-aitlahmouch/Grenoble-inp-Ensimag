# Application WEB : Les Loups-Garous vs Humains
  Ce projet contient une application web de fameux jeu : loups-garous vs Humains.

## Développeurs

  * Nadir AIT LAHMOUCH
  * Naima AMALOU
  * Hamza BENJELLOUN
  * Nabil BENSRHIER

## Usage

L'utilisation de l'application nécessite l'installation de Netbeans et maven. Nous avons utilisé maven via Netbeans, donc le lancement de l'application ne peut pas se faire en ligne de commande.

## install_bd.sql

Ce fichier contient tous les requêtes nécessaires pour installer la base de données dont l'application a besoin, ainsi que certaines requêtes qui permettent d'ajouter des utilisateurs dans l'application.

## uninstall.sql

Permet de supprimer toutes les tables installées par install_bd.sql

## projetAcol

Ce dossier contient le projet maven sans les fichiers compilés.

## Les identifants de connexion

  ### pour les utilisateurs de l'application :
    Pour des raisons de simplification et vu que le mot de passe est haché par l'application, les utilisateurs installés dans install_bd.sql ont le même mot de passe qui est  : test


  ### Pour le site adminer.ensimag.fr/:
    username : amaloun
    password : amaloun

## Lancer l'Application

Pour lancer l'application il suffit d'ouvrir le projet maven dans Netbeans, et configurer la connexion à la base de données dans services Database en utilisant les identifiants précédents.
Ou bien dans n'importe quelle base de données , mais il faut exécuter le ficher install_bd.sql avant de lancer l'application. Ensuite il suffit de cliquer sur build project pour compiler les fichiers ensuite sur run project pour lancer l'application.

## JAVADOC
Pour génerer la javadoc  il faut cliquer sur run et generate javadoc  elle est dans le dossier projetAcol/target/site/apidocs/
