/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package forms;

/**
 *
 * @author Equipe 9
 */

import java.util.HashMap;
import java.util.Map;
import dao.UtilisateurDao;
import javax.servlet.http.HttpServletRequest;
import beans.Utilisateur;



public class InscriptionForm {
    
    private static final String CHAMP_EMAIL  = "email";
    private static final String CHAMP_PASS   = "motdepasse";
    private static final String CHAMP_CONF   = "confirmation";
    private static final String CHAMP_NOM    = "nom";
    private String resultat;
    private Map<String, String> erreurs  = new HashMap<String, String>();
   

    
    public String getResultat() {
        return resultat;
    }

    public Map<String, String> getErreurs() {
        return erreurs;
    }
    
    
    public Utilisateur inscrireUtilisateur( HttpServletRequest request , UtilisateurDao userDao) {
        
        String email = getValeurChamp( request, CHAMP_EMAIL );
        String motDePasse = getValeurChamp( request, CHAMP_PASS );
        String confirmation = getValeurChamp( request, CHAMP_CONF );
        String nom = getValeurChamp( request, CHAMP_NOM );

        Utilisateur utilisateur = new Utilisateur();

        try {
            validationEmail( email ,userDao);
        } catch ( Exception e ) {
            setErreur( CHAMP_EMAIL, e.getMessage() );
        }
        utilisateur.setEmail( email );

        try {
            validationMotsDePasse( motDePasse, confirmation );
            
        } catch ( Exception e ) {
            setErreur( CHAMP_PASS, e.getMessage() );
            setErreur( CHAMP_CONF, null );
        }
     

        try {
            validationNom( nom ,userDao);
        } catch ( Exception e ) {
            setErreur( CHAMP_NOM, e.getMessage() );
        }
        utilisateur.setNom( nom );

        if ( erreurs.isEmpty() ) {
            String hashPass = userDao.hashPassword(motDePasse);
            utilisateur.setMotDePasse( hashPass );
            userDao.creerUtilisateur(nom, hashPass, email);
            resultat = "Succès de l'inscription.";
        } else {
            resultat = "Échec de l'inscription.";
        }
        
        return utilisateur;
    }
    
    private void validationEmail( String email ,UtilisateurDao userDao) throws Exception {
        // TODO unicité d'email
        if (userDao.verifyUniqueEmail(email)){
             throw new Exception("Cet email est utilisé par un autre utilisateur");
        } else if ( email != null ) {
            if ( !email.matches( "([^.@]+)(\\.[^.@]+)*@([^.@]+\\.)+([^.@]+)" ) ) {
                throw new Exception( "Merci de saisir une adresse mail valide." );
            }
        } else {
            throw new Exception( "Merci de saisir une adresse mail." );
        }
    }

    private void validationMotsDePasse( String motDePasse, String confirmation ) throws Exception {
        if ( motDePasse != null && confirmation != null ) {
            if ( !motDePasse.equals( confirmation ) ) {
                throw new Exception( "Les mots de passe entrés sont différents, merci de les saisir à nouveau." );
            } else if ( motDePasse.length() < 3 ) {
                throw new Exception( "Les mots de passe doivent contenir au moins 3 caractères." );
            }
        } else {
            throw new Exception( "Merci de saisir et confirmer votre mot de passe." );
        }
    }
    
    
    private void validationNom( String nom ,UtilisateurDao userDao) throws Exception {
        if (userDao.verifyPseudonyme(nom)){
            throw new Exception( "Ce pseudonyme est utilisé par un autre utilisateur , veuillez saisir un nouveau" );
        }
        if (nom == null){
            throw new Exception( "Merci de saisir un pseudonyme" );
        } else if (nom.length() < 3 ) {
            throw new Exception( "Le pseudonyme doit contenir au moins 6 caractères." );
        }
    }

/*
 * Ajoute un message correspondant au champ spécifié à la map des erreurs.
 */
    private void setErreur( String champ, String message ) {
        erreurs.put( champ, message );
    }

/*
 * Méthode utilitaire qui retourne null si un champ est vide, et son contenu
 * sinon.
 */
    private static String getValeurChamp( HttpServletRequest request, String nomChamp ) {
        String valeur = request.getParameter( nomChamp );
        if ( valeur == null || valeur.trim().length() == 0 ) {
            return null;
        } else {
            return valeur.trim();
        }
    }

}
