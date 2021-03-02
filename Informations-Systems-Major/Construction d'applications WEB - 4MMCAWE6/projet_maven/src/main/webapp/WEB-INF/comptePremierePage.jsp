 <%-- 
    Document   : comptePremierePage
    Created on : Apr 11, 2020, 3:02:04 AM
    Author     : nadir
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix='c' uri='http://java.sun.com/jsp/jstl/core' %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link type="text/css" rel="stylesheet" href="style.css" />
        <title>Page d'accueil compte</title>    
    </head>
    <body>
       <form method="post" action="Deconnexion">
            <input type="submit" value="Deconnexion" class="Deconnexion" />
            <span class="erreur">${form.erreurs['Deconnexion']}</span>
            <p class="${empty form.erreurs ? 'succes' : 'erreur'}">${form.resultat}</p>
        </form>
        <br><br><br><br><br><br>
        <h1>Bienvenue dans votre compte  ${sessionScope.sessionUtilisateur.nom} </h1>
        <br><br>
        <h2>Que voulez vous faire ? </h2>
        <br><br>
        <p align="center">
            <c:choose> 
            <c:when test="${partieEnCours == '0'}">
               <a href="configurationpartie" class="button">Nouvelle Partie</a>
               <a href="Jeu" class="button">Accéder à l'archive de la partie précedente</a>
            </c:when>
            <c:when test="${participe == '0' && partieEnCours == '1' && maitrejeu == '0'}">
            <h3>Une partie créée par <mark>${partieC.maitre}</mark> est actuellement en cours ...</h3>
            <h3> Il reste encore <mark>${nombre}</mark> villageois vivants.</h3>
            <h3>Veuillez attendre la fin de cette partie.</h3>
                   <br><br>
            </c:when>
            <c:when test="${participe == '1' && partieEnCours == '1'}">
              <br></br>
              <a href="Jeu" class="button">Accéder à la partie en cours </a>
            </c:when>
            <c:when test="${maitrejeu == '1' && partieEnCours == '1'}">
              <br></br>
              <a href="Jeu?maitrejeu=1" class="button">Gérer la partie en cours </a>
            </c:when> 
          </c:choose>
               
          
        </p>
    </body>
</html>
