<%--
    Document   : jeuMaitre
    Created on : 23 avr. 2020, 18:10:17
    Author     : amalou
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
        <h1>les loups-garous vs les humains</h1>
        <form method="post" action="Deconnexion">
            <input type="submit" value="Deconnexion" class="Deconnexion" />
            <span class="erreur">${form.erreurs['Deconnexion']}</span>
            <p class="${empty form.erreurs ? 'succes' : 'erreur'}">${form.resultat}</p>
        </form>
        <div class="information">
            <p>Vous êtes le maitre du jeu</p>
            <p>Période : "${periode}"</p>
        </div>

        <br><br>
        <c:if test="${mort!=null}">
        <div class="notification">
        
            <div class="new-death">
                <h3>Urgent !!! <mark>"${mort.pseudonyme}"</mark> est trouvé(e) mort(e).</h3>
            </div>
        </div>
        </c:if>
        <br><br>
        
        <div class="same">
        <div class="item">
        <c:choose>
            <c:when test="${maitrejeu == '1'&& periode eq'Jour'}">
                <form method="post" action = "GestionPartie">
                    <input type="submit" value="Nuit" class="Nuit"/>
                    <input type="hidden" name="action" value="passernuit" />
                </form>
                <form method="post" action = "GestionPartie">
                    <input type="submit" value="Jour" class="Jour" disabled/>
                    <input type="hidden" name="action" value="passeraujour" />
                </form>
            </c:when>
            <c:when test="${maitrejeu == '1' && periode == 'Nuit'}">
                <form method="post" action = "GestionPartie">
                    <input type="submit" value="Nuit" class="Nuit" disabled/>
                    <input type="hidden" name="action" value="passernuit" />
                </form>
                <form method="post" action = "GestionPartie">
                    <input type="submit" value="Jour" class="Jour" />
                    <input type="hidden" name="action" value="passeraujour" />
                </form>
            </c:when>
        </c:choose>
        <br>
        <br>
        <br>
        <c:if test="${proposed!=null && proposed.size()!=0}">
            <table>
            <tr>
                <th> Villageois Proposés </th>
                <th> Nombre de votes </th>
            </tr>
            <c:forEach items="${proposed}" var="proposed">
            <tr>
                <td> ${proposed.pseudonyme} </td>
                <td> ${proposed.getNbVote()} </td>
            </tr>
            </c:forEach>
        </table>
        </c:if>
        <c:if test="${proposed==null || proposed.size()==0}">
            <h3> Aucun joueur n'est proposé pour le moment.</h3>
        </c:if>
        </div>
        <div class="item">
        <div class="ListJoueurs ">
        <u><p>Liste des joueurs </p></u>
        <c:forEach items="${joueurs}" var="joueur">
        <c:choose>
        <c:when test="${joueur.elimine}">
        <del>
            <p>${joueur.pseudonyme} (éliminé) </p>
        </del>
        </c:when>
        <c:otherwise>
            <p>${joueur.pseudonyme}</p>
        </c:otherwise>
        </c:choose>
        </c:forEach>
        </div>
        </div>
            </div>
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

    </body>
</html>
