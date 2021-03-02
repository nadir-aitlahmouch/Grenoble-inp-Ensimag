<%-- 
    Document   : configuration
    Created on : Apr 11, 2020, 5:02:32 AM
    Author     : nadir
--%>

<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf-8" />
        <link type="text/css" rel="stylesheet" href="style.css" />
        <title>Inscription</title>
    </head>
    <body>
        <form method="post" action="configurationpartie">
            <fieldset>
                
                <legend>Configuration de la partie</legend>
                <p>Commencez la configuration de la partie</p>
                <br/>
                <p> Selectionnez les utilisateurs qui vont jouer dans la partie ,au minimum 5 joueurs</p>
                <br/>
                <table>
                <tr>
                    <th><!--Utilisateur--></th>
                    <th><!-- ajouter --></th>
                </tr>
                <c:forEach items="${utilisateur}" var="utilisateur">
                    <tr>
                        <td>${utilisateur.nom}</td>
                        <td><a href="configurationpartie?action=addUser&name=${utilisateur.nom}">ajouter</a></td>

                    </tr>
                </c:forEach>
                </table>
                
                <input type=hidden id="maitre" name="maitre" value= "${sessionScope.sessionUtilisateur.nom}" size="20" maxlength="20" />
                <br />

                <label for="probabilite">Probabilité d'attribution des pouvoirs <span class="requis">*</span></label>
                <input type="number" id="probabilite" name="probabilite"  value="0" step="0.01" min="0" max="1" />
                <span class="erreur">${partieform.erreurs['probabilite']}</span>
                <br />

                <label for="loupgarou">Proportion des loups garous <span class="requis">*</span></label>
                <input type="number" id="loupgarou" name="loupgarou" value="0.33" step="0.01" min="0" max="0.33" />
                <span class="erreur">${partieform.erreurs['loupgarou']}</span>
                <br />
                
            </fieldset>
  
               
                
                <input type="hidden" name="action" value="LancerPartie"/>
                <input type="submit" value="Lancez la partie" class="sansLabel" />
                <br />

                <p class="${empty form.erreurs ? 'succes' : 'erreur'}">${form.resultat}</p>
                
        </form>
    </body>
</html>