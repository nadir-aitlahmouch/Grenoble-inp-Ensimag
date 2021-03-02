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
import beans.Partie;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import dao.PartieDao;
import dao.JoueurDao;
import beans.Joueur;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import beans.Role;
import beans.Pouvoir;
import java.lang.Math;
import dao.MessageDao;
import dao.ExercerPouvoirDao;
import javax.sql.DataSource;

public class PartieForm {
    
    private static final String CHAMP_PROBABILITE    = "probabilite";
    private static final String CHAMP_LOUPGAROU    = "loupgarou";
    private static final String CHAMP_JOUEURS = "joueurs";
    
    private String resultat;
    private Map<String, String> erreurs  = new HashMap<String, String>();
   

    
    public String getResultat() {
        return resultat;
    }

    public Map<String, String> getErreurs() {
        return erreurs;
    }
    
    
    public Partie configurerPartie( HttpServletRequest request , PartieDao partieDao, 
            List<Joueur> joueurs, JoueurDao joueurDao, DataSource ds) {
        
        String maitre = request.getParameter("maitre");
        double probabilite = getValeurChampProbabilite( request, CHAMP_PROBABILITE );
        double loupgarou = getValeurChampLoupGarou( request, CHAMP_LOUPGAROU );
        

        Partie partie = new Partie();
        
        partie.setMaitre( maitre );
        
        try {
            validationProbabilite( probabilite );
        } catch ( Exception e ) {
            setErreur( CHAMP_PROBABILITE, e.getMessage() );
        }
        partie.setProba( probabilite );

        try {
            validationLoupGarou( loupgarou );
        } catch ( Exception e ) {
            setErreur( CHAMP_LOUPGAROU, e.getMessage() );
        }
        partie.setProbaLoupGarou( loupgarou );

        try {
            validateJoueurs(joueurs);
        } catch (Exception e){
            setErreur(CHAMP_JOUEURS, e.getMessage());
        }

        if ( erreurs.isEmpty() ) {
           
            /* delete joueurs and messages belong to previous partie*/
            (new MessageDao(ds)).deleteMessages();
            (new JoueurDao(ds)).deleteJoueurs();
            (new ExercerPouvoirDao(ds)).deletePouvoirs();
            partieDao.creerPartie(maitre, probabilite, loupgarou);
            attributionDesRoles(joueurs,loupgarou);
            attributionDesPouvoirs(joueurs,probabilite);
            joueurDao.addJoueurs(joueurs);
            resultat = "Succès de la configuration.";
        } else {
            resultat = "Échec de la configuration.";
        }
        
        return partie;
    }
    
    private void attributionDesRoles(List<Joueur> joueurs, double loupgarou ){
        
        /** Récuperer le nombre total des joueur **/
        int NbParticipant = joueurs.size();
        int NbLoupGarou = (int) Math.round(Math.rint(NbParticipant*loupgarou));
        int NbHumain = NbParticipant - NbLoupGarou;
        List<Joueur> loupsGarous = new ArrayList<Joueur>();
        /** choisir aléatoirement les loupGarou **/
        for (int i = 0; i < NbLoupGarou; i++){
            Random r = new Random();
            int index = r.nextInt(NbParticipant);
            joueurs.get(index).setRole(Role.loupGarou);
            loupsGarous.add(joueurs.get(index));
            
        }
        /** le resrte sont des humains **/
        for (Joueur j : joueurs){
            /** tous les joueurs ne sont pas éliminé au départ **/
            j.setElimine(false);
            if (!loupsGarous.contains(j)){
                j.setRole(Role.humain);
            }
        }
    }
    
    private void attributionDesPouvoirs(List<Joueur> joueurs, double probabilite){
        
        List<Joueur> joueursSansPv = new ArrayList<Joueur>();
        for (Joueur j: joueurs){
            joueursSansPv.add(j);
        }
        double aucun = 1 - probabilite;
        double voyance = probabilite/2;
        Random r = new Random();
        while (!joueursSansPv.isEmpty()){
            /** Récuperer la liste des loups garou **/
            List<Joueur> loupsGarous = new ArrayList<Joueur>();
            for (Joueur j : joueursSansPv){
                if (j.getRole() == Role.loupGarou){
                    loupsGarous.add(j);
                }
            }
            Random rInt = new Random();
            double proba = r.nextDouble();
            if (proba <= aucun){
                int index = rInt.nextInt((joueursSansPv.size()));
                joueursSansPv.get(index).setPouvoir(Pouvoir.aucun);
                joueursSansPv.remove(index);
            }else if (proba <= voyance + aucun){
                int index = rInt.nextInt((joueursSansPv.size()));
                joueursSansPv.get(index).setPouvoir(Pouvoir.voyance);
                joueursSansPv.remove(index);
            } else {
                /* pouvoir de contamination pour les loups garou **/
                if (!loupsGarous.isEmpty()){
                     int index = rInt.nextInt((loupsGarous.size()));
                loupsGarous.get(index).setPouvoir(Pouvoir.contamination);
                joueursSansPv.remove(loupsGarous.get(index));
                }
            }
        }
        
    }
    private void validationProbabilite( double probabilite) throws Exception {
        if (probabilite > 1 || probabilite < 0 ){
             throw new Exception("La probabilité doit être entre 0 et 1");
        } 
    }

    private void validationLoupGarou( double loupgarou) throws Exception {
        if (loupgarou > 0.33 || loupgarou < 0 ){
             throw new Exception("La proportion de loup garou doit être entre 0 et 0.33");
        } 
    }
    
    private void validateJoueurs(List<Joueur> joueurs) throws Exception {
        if (joueurs.size() < 5){
            throw new Exception("Il faut au moins avoir 5 joueurs pour lancer une partie de jeu");
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
    private static double getValeurChampLoupGarou( HttpServletRequest request, String nomChamp ) {
        String valeur = request.getParameter( nomChamp );
        if ( valeur == null ) {
            return 0.33;
        } else {
            return Double.valueOf(valeur);
        }
    }
    
        private static double getValeurChampProbabilite( HttpServletRequest request, String nomChamp ) {
        String valeur = request.getParameter( nomChamp );
        if ( valeur == null ) {
            return 0;
        } else {
            return Double.valueOf(valeur);
        }
    }


}
