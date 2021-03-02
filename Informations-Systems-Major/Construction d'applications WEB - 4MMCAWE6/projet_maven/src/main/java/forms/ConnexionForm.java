/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package forms;
import java.util.HashMap;
import java.util.Map;
import dao.UtilisateurDao;
import javax.servlet.http.HttpServletRequest;
import beans.Utilisateur;

/**
 *
 * @author Equipe 9
 */
public class ConnexionForm {
    
    private static final String CHAMP_NOM    = "nom";
    private static final String CHAMP_CONX  = "connexion";
    private static final String CHAMP_PASS   = "motdepasse";

    private String resultat;
    private Map<String, String> erreurs  = new HashMap<String, String>();

    public String getResultat() {
        return resultat;
    }

    public Map<String, String> getErreurs() {
        return erreurs;
    }

    public Utilisateur connecterUtilisateur( HttpServletRequest request , UtilisateurDao userDao) {
        /* Récupération des champs du formulaire */

        String pseudonyme = getValeurChamp( request, CHAMP_NOM );
        String motDePasse = getValeurChamp( request, CHAMP_PASS );
        
        /** Hashage de mot de passe **/
      
        Utilisateur utilisateur = new Utilisateur();

        /* Validation du champ email. */
        try {
            validationNom( pseudonyme );
        } catch ( Exception e ) {
            setErreur( CHAMP_NOM, e.getMessage() );
        }
        

        /* Validation du champ mot de passe. */
        try {
            validationMotDePasse(motDePasse);
        } catch ( Exception e ) {
            setErreur( CHAMP_PASS, e.getMessage() );
        }
        

        
        if ( erreurs.isEmpty() ) {
            try{
                utilisateur.setNom( pseudonyme );
                String hashPass = userDao.hashPassword(motDePasse);
                utilisateur.setMotDePasse( hashPass );
                connexionUser(utilisateur,userDao);
                resultat = "Succès de la connexion.";
            } catch (Exception e) {
                setErreur(CHAMP_CONX, e.getMessage());
            }
        } else {
            resultat = "Échec de la connexion.";
        }
        return utilisateur;
    }

    
    private void connexionUser(Utilisateur user, UtilisateurDao userDao) throws Exception{
        if(!userDao.connexion(user)){
            throw new Exception("Pseudonyme ou mot de passe incorrecte");
        }
    }
    private void validationNom( String nom) throws Exception {
        
        if (nom == null){
            throw new Exception( "Merci de saisir un pseudonyme" );
        } 
    }
    /**
     * Valide le mot de passe saisi.
     */
    private void validationMotDePasse(String motDePasse) throws Exception {
        if ( motDePasse == null ) {
            throw new Exception( "Merci de saisir votre mot de passe." );
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
            return valeur;
        }
    }
    
    
}
