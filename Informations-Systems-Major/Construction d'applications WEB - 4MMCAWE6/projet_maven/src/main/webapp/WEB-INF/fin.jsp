<%-- 
    Document   : fin
    Created on : 25-Apr-2020, 21:30:07
    Author     : benjelloun
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <link type="text/css" rel="stylesheet" href="style_2.css" />
        <title>Fin partie</title>
    </head>
    <body>
        <h1> La partie est terminée</h1>
        <form method="post" action="Deconnexion">
            <input type="submit" value="Deconnexion" class="Deconnexion" />
            <span class="erreur">${form.erreurs['Deconnexion']}</span>
            <p class="${empty form.erreurs ? 'succes' : 'erreur'}">${form.resultat}</p>
        </form>
        <h2> les ${gagnant}s ont gagné la partie</h2>
        <h3>vous pouvez acceder aux archives du jeu et aux rôles des différents joueurs :</h3>
        <div class="container">
        <div class="chat-container">
        <c:forEach items="${messages}" var="message">
            <div class="message">
            <div class="datetime">${message.date}</div>
            <div class="pseudonyme">${message.nameUtilisateur}</div>
            <p>${message.contenu}</p>
            </div>
        </c:forEach>
        </div>  
    <form method="post" action="Jeu">
    <input type="text" name="contenu" value = "" placeholder="Your message">
            <input type="submit" value="Send" class="sansLabel" disabled/>
    <input type="hidden" name="action" value="SendMess"/>
    </form>
</div>
        
        <h4> rôles des différents joueurs :</h4>
        <c:forEach items="${joueurs}" var="joueur">
            <p> le joueur ${joueur.pseudonyme} est un ${joueur.role} 
            <c:choose>
            <c:when test= "${not (joueur.pouvoir eq 'aucun')}">
                qui a comme pouvoir : ${joueur.pouvoir} 
            </c:when>
            </c:choose>
                , il est
            <c:choose>
                <c:when test= "${joueur.elimine}">
                    eliminé
                </c:when>
                <c:otherwise>
                    n'est pas eliminé
                </c:otherwise>
            </c:choose>
            </p>
        </c:forEach>
    </body>
</html>
