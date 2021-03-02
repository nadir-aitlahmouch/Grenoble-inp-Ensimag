<%-- 
    Document   : JeuArchive
    Created on : 23-Apr-2020, 22:20:11
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
        <title>Jeu</title>    
    </head>
    <body>
        <h1>Archive des messages</h1>
       



        
<div class="container">
  <div class="chat-container">
            <c:forEach items="${messages}" var="message">
                <div class="message">
                <div class="datetime">${message.periode} ${message.date}</div>
                <div class="pseudonyme">${message.nameUtilisateur}</div> 
                <p>${message.contenu}</p>        
                </div>
            </c:forEach>
  </div>
</div>
        <a href="Jeu" class="button">Retour à la partie en cours </a>
</body>
</html>
