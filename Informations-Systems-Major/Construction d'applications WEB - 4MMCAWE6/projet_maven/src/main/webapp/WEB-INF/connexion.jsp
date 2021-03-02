<%-- 
    Document   : connexion
    Created on : 7 avr. 2020, 13:16:09
    Author     : amalou
--%>

<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
    <head>
        <meta charset="utf-8" />
        <title>Connexion</title>
        <link type="text/css" rel="stylesheet" href="style.css" />
        <script src = "javascript/test.js" ></script>
    </head>
    <body>
        <form method="post" action="connexion">
            <fieldset>
                <legend>Connexion</legend>
                <p>Vous pouvez vous connecter via ce formulaire.</p>
                <br/>
                
                
                <label for="nom">Pseudonyme <span class="requis">*</span></label>
                <input type="text" id="nom" name="nom" value="<c:out value="${utilisateur.nom}"/>" size="20" maxlength="60" />
                <span class="erreur">${form.erreurs['nom']}</span>
                
                <br/>
                <label for="motdepasse">Mot de passe <span class="requis">*</span></label>
                <input type="password" id="motdepasse" name="motdepasse" value="" size="20" maxlength="60" />
                <span class="erreur">${form.erreurs['motdepasse']}</span>

                <br/>
                <input type="submit" value="Connexion" class="sansLabel" />
                
                <span class="erreur">${form.erreurs['connexion']}</span>
                <p class="${empty form.erreurs ? 'succes' : 'erreur'}">${form.resultat}</p>
            </fieldset>
        <fieldset>
            <legend> Créer un compte </legend>
            <p>C’est rapide et facile </p>
            <td><a href="connexion?action=inscription">Inscription</a></td>
        </fieldset>
                
        </form>
    </body>
</html>
