/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

/**
 *
 * @author Equipe 9
 */
import beans.Joueur;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;
import beans.Partie;
import beans.Pouvoir;
import beans.Proposed;
import beans.Role;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class PartieDao extends AbstractDataBaseDAO {

    public PartieDao(DataSource ds) { 
        super(ds);
    }
 /**
 * crée une table partie qui contient les paramétres choisis par le maitre de jeu
     * @param maitre
     * @param probabilite
     * @param loupgarou
 */
    public void creerPartie(String maitre, double probabilite, double loupgarou) {
        try (
            Connection conn = getConn();  
            PreparedStatement st = conn.prepareStatement("insert into Partie (maitre,probaPouvoir,propLoup,periode) values (?,?,?,?)");) {
            st.setString(1, maitre);
            st.setDouble(2, probabilite);
            st.setDouble(3, loupgarou);
            st.setString(4, "Jour");
            st.executeUpdate();
            
        } catch (SQLException e) {
            throw new DAOException("Erreur BD " + e.getMessage(), e);
        }
        
    }
 /**
 * compte le nombre des loups garous non eliminés si onlyLoup sinon elle compte tous les joueurs non eliminés.
 * @param onlyLoup
 * @return nombre de joueurs 
 */
    public int nbJoueurs(boolean onlyLoup){
        String statement = "select count(*) from joueur where elimine = 0 and role=" + "\'loupGarou\'";
        if (!onlyLoup){
            statement = "select count(*) from joueur where elimine = 0";
        }
        try (
            Connection conn = getConn();  
            PreparedStatement st = conn.prepareStatement(statement);) {
            ResultSet resultSet = st.executeQuery();
            if (resultSet.next()){
                 return resultSet.getInt(1);
            }
            return 0;
        } catch (SQLException e) {
            throw new DAOException("Erreur BD " + e.getMessage(), e);
        }
    }
 /**
 * proposer un joueur pour le tuer. 
 * @param pseudo pseudo joueur qui propose un villageois.
 * @param voter  pseudo joueur proposé.
 */
    public void proposerVillageois(String pseudo, String voter){
        try (
            Connection conn = getConn();  
            PreparedStatement st = conn.prepareStatement
            ("insert into Proposed (pseudonyme,voter) values (?,?)");) {
            st.setString(1, pseudo);
            st.setString(2, voter);   
            st.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("Erreur BD "  +  e.getMessage(), e);
        }
    }
 /**
 * retirer le vote de celui qui a proposé.
 * @param pseudo pseudo joueur qui a proposé .
 * @param voter  pseudo joueur proposé.
 */
    public void retirerVote(String pseudo, String voter){
        try (
            Connection conn = getConn();  
            PreparedStatement st = conn.prepareStatement
            ("delete from Proposed where pseudonyme=? and voter=?");) {
            st.setString(1, pseudo);
            st.setString(2, voter);   
            st.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("Erreur BD "  +  e.getMessage(), e);
        }
    }

    /**
     *  Renvoie la liste des joueurs proposés aucours de la partie.
     * @return liste des proposés
     */
    public List<Proposed> getProposed(){
        List<Proposed> result = new ArrayList<>();
        try (
	     Connection conn = getConn();
             PreparedStatement st = conn.prepareStatement
                ("SELECT * FROM PROPOSED");
	     ) {
            Map<String, List<String>> map = new HashMap<>();
            
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                List<String> list = map.get(rs.getString("pseudonyme"));
                if (list != null){
                    list.add(rs.getString("voter"));
                }
                else{
                    list = new ArrayList<>();
                    list.add(rs.getString("voter"));
                }
                map.put(rs.getString("pseudonyme"), list);
            }
            Set<String> keys = map.keySet();
            for (String pseudo:keys){
                Proposed proposed = new Proposed(pseudo);
                proposed.addVote(map.get(pseudo));
                result.add(proposed);
            }
        } catch (SQLException e) {
            throw new DAOException("Erreur BD " + e.getMessage(), e);
	}
	return result;
    }

    /**
     *  Elle insert dans partie le mâtre qui gère et la période du jeu.
     * @param partie
     * @return un boolean indiquant l'existance d'une partie ou pas.
     */
    public boolean partieEnCours(Partie partie){
        try (
            Connection conn = getConn();  
            PreparedStatement st = conn.prepareStatement("select * from Partie");) {
            ResultSet resultSet = st.executeQuery();
            if (resultSet.next()){
                 partie.setMaitre(resultSet.getString("maitre"));
                 partie.setPeriode(resultSet.getString("periode"));
                 partie.setNbJoueurs(nbJoueurs(false));
                return true;
            } else {
                return false;
            }
            
        } catch (SQLException e) {
            throw new DAOException("Erreur BD " + e.getMessage(), e);
        }
    }

    /**
     * Elle vide la table des proposés aucours d'une période.
     */
    public void viderProposed(){
        try (
            Connection conn = getConn();  
            PreparedStatement st = conn.prepareStatement
            ("delete from Proposed");) {   
            st.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("Erreur BD "  +  e.getMessage(), e);
        }
    }

    /**
     *  Elimanation du joueur ratifié
     *  Suppression de la table REMOVED.
     *  AJout de l'éliminé dans la page removed
     * @param removed
     */
    public void changeStatut(String removed){
        try (
            /* changer le statut de l'utilisateur*/
            Connection conn = getConn();
            PreparedStatement st1 = conn.prepareStatement("Update joueur set elimine=1 where pseudonyme=?");){ 
            st1.setString(1, removed);
            st1.executeUpdate();
            /* vider la table du joueur tué*/
            PreparedStatement st2 = conn.prepareStatement
            ("delete from Removed");  
            st2.executeUpdate();
            /* ajouter le nouveau mort*/
            PreparedStatement st3 = conn.prepareStatement("insert into Removed (pseudonyme) values (?)");
            st3.setString(1, removed);
            st3.executeUpdate();
        } catch (SQLException ex) {
            throw new DAOException("Erreur BD "  +  ex.getMessage(), ex);
        }

    }

    /**
     *  Récuperer les informations du nouveau joueur éliminé
     * @return joueur éliminé
     */
    public Joueur nouveauMort(){
        try (
            Connection conn = getConn();  
            PreparedStatement st = conn.prepareStatement("select * from removed");) {
            ResultSet rt = st.executeQuery();
            Joueur joueur = null;
            if (rt.next()){
                joueur = new Joueur(rt.getString("pseudonyme"));
                PreparedStatement st1 = conn.prepareStatement("select * from Joueur where pseudonyme=?");
                st1.setString(1, joueur.getPseudonyme());
                ResultSet resultSet = st1.executeQuery();
                if (resultSet.next()){
                    if (resultSet.getString("pouvoir").equals("aucun")){
                        joueur.setPouvoir(Pouvoir.aucun);
                    } else if (resultSet.getString("pouvoir").equals("voyance")){
                        joueur.setPouvoir(Pouvoir.voyance);
                    } else{
                        joueur.setPouvoir(Pouvoir.contamination);
                    }

                    if (resultSet.getInt("elimine") == 0){
                        joueur.setElimine(false);
                    } else {
                        joueur.setElimine(true);
                    }

                    if (resultSet.getString("role").equals("humain")){
                        joueur.setRole(Role.humain);
                    }else {
                        joueur.setRole(Role.loupGarou);
                    }
                }
            }
            
            return joueur;
        } catch (SQLException ex) {
            throw new DAOException("Erreur BD "  +  ex.getMessage(), ex);
        }
    }

    /**
     *  Passage du jour à nuit et vise versa 
     *  Archiver les messages de chaque période après ce passage
     *  Suppression de la table de messages
     *  Changer la période de la partie.
     * @param periode
     * @param partie
     */
    public void passerPeriode(String periode, Partie partie){
        /*modifier la periode*/
        try (
            Connection conn = getConn();  
            PreparedStatement st = conn.prepareStatement("UPDATE Partie SET periode  = ? WHERE Maitre = ?");) {
            st.setString(1, periode);
            st.setString(2, partie.getMaitre());
            st.executeUpdate();  
        } catch (SQLException e) {
            throw new DAOException("Erreur BD " + e.getMessage(), e);
        }
       String complem;
        if(periode.equals("Nuit")){
            complem = "Jour";
        }
        else{
            complem = "Nuit";
        }
        /*ajouter les messages dans l'archive*/
               try (
            Connection conn = getConn();  
            PreparedStatement st = conn.prepareStatement("select * from Message"+complem);) {
            ResultSet resultSet = st.executeQuery();
            while(resultSet.next()){
                 try (
            PreparedStatement st2 = conn.prepareStatement("insert into Archive (datePub,pseudonyme,contenu, periode) values (?,?,?,?)");) {
            st2.setString(1, resultSet.getString("datePub"));
            st2.setString(2, resultSet.getString("pseudonyme"));
            st2.setString(3, resultSet.getString("contenu"));
            st2.setString(4, complem);
            st2.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("Erreur BD " + e.getMessage(), e);
        }
            }
            
        } catch (SQLException e) {
            throw new DAOException("Erreur BD " + e.getMessage(), e);
        }
        /* enlever les messages de la table messageComplem*/
        try (
            Connection conn = getConn();  
            PreparedStatement st = conn.prepareStatement("delete from Message" + complem);) {
            st.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("Erreur BD " + e.getMessage(), e);
        }
    }

    /**
     * Suppression de la table partie
     */
    public void deletePartie() {
        /*supprimer la partie et tous les elements de la partie*/
        
        
        /*Supprimer la partie*/
        try (
            Connection conn = getConn();  
            PreparedStatement st = conn.prepareStatement
            ("delete from Partie");) {
            st.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("Erreur BD "  +  e.getMessage(), e);
        }
        
        /*Supprimer Removed*/
        try (
            Connection conn = getConn();  
            PreparedStatement st = conn.prepareStatement
            ("delete from Removed");) {
            st.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("Erreur BD "  +  e.getMessage(), e);
        }
    /* supprimer Proposed*/
    try (
            Connection conn = getConn();  
            PreparedStatement st = conn.prepareStatement
            ("delete from Proposed");) {
            st.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("Erreur BD "  +  e.getMessage(), e);
        }
    }
}
