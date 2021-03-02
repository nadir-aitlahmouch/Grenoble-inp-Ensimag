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
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;
import beans.Utilisateur;
import java.security.MessageDigest;  
import java.security.NoSuchAlgorithmException;


public class UtilisateurDao extends AbstractDataBaseDAO{
    
    public UtilisateurDao(DataSource ds){
        super(ds);
    }
    
    /**
     *  Cette méthode insert les données de l'utilisateur au cours de l'enregistrement.
     * @param pseudonyme
     * @param password
     * @param email
     */
    public void creerUtilisateur(String pseudonyme,String password, String email){
        try(
                Connection conn = getConn();
                PreparedStatement st = conn.prepareStatement
                ("insert into Utilisateur (pseudonyme,password,email) values (?,?,?)");
            ){
            st.setString(1,pseudonyme);
            st.setString(2,password);
            st.setString(3,email);
            st.executeUpdate();
        } catch (SQLException e){ 
            throw new DAOException("Erreur BD " + e.getMessage(), e);
        }
    }
    
    /**
     * Chercher dans la base de donnée si il y a un utilisateur avec 
     * le même pseudonyme
     * @param pseudonyme 
     */
    public boolean verifyPseudonyme(String pseudonyme){
        try (
                Connection conn = getConn();
                PreparedStatement st = conn.prepareStatement
                ("select idUser,pseudonyme from Utilisateur where pseudonyme=? ");
            ){
            st.setString(1,pseudonyme);
            ResultSet resultSet = st.executeQuery();
            if (resultSet.next()){
                return true;
            } else {
                return false;
            }
        } catch (SQLException e){
            throw new DAOException("Erreur BD " + e.getMessage(), e);
        }
    }
    
    /**
     * elle vérifie s'il existe un utilisateur avec le même email fourni
     * @param email
     * @return boolean indiquant l'etat de cette recherche
     */
    public boolean verifyUniqueEmail(String email){
        try (
                Connection conn = getConn();
                PreparedStatement st = conn.prepareStatement
                ("select idUser,email from Utilisateur where email=? ");
            ){
            st.setString(1,email);
            ResultSet resultSet = st.executeQuery();
            return resultSet.next();
        } catch (SQLException e){
            throw new DAOException("Erreur BD " + e.getMessage(), e);
        }
    }
    
    /**
     *  Vérification de l'utilisateur connecté s'il est déjà en base de donnée
     * @param user
     * @return true si le pseudo et le password sont corrects.
     */
    public boolean connexion(Utilisateur user){
        String pseudonyme = user.getNom();
        String motDePasse = user.getMotDePasse();
        
        
        try (
                Connection conn = getConn();
                PreparedStatement st = conn.prepareStatement
                ("select idUser,pseudonyme,password from Utilisateur where pseudonyme=? ");
            ){
                st.setString(1,pseudonyme);
                ResultSet resultSet = st.executeQuery();
                if (resultSet.next()){
                    String password = resultSet.getString(3);
                    if (!password.equals(motDePasse)){
                        return false;
                    } else {
                        user.setIdUser(resultSet.getInt(1));
                        return true;
                    }
                } else {
                    return false;
                }
        } catch (SQLException e){
            throw new DAOException("Erreur BD " + e.getMessage(), e);
        }
    }
    
    /**
     * Encode le mot de passe afin d'éviter l'identification des  vrais mots de passes
     * @param password
     * @return nouveau mot de passe codé
     */
    public String hashPassword(String password){
        String generatedPass = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(password.getBytes());
            byte[] bytes = md.digest();
            StringBuilder sb = new StringBuilder();
            for(int i=0; i< bytes.length ;i++)
            {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            generatedPass = sb.toString();
         } catch (NoSuchAlgorithmException e) 
            {
                e.printStackTrace();
            }
        return generatedPass;
    }
    
    /**
     * Elle affiche les utilisateur qui n'appartiennent à aucune partie.
     * @param maitre
     * @return listes des utilisateurs qui ne sont pas des joueurs
     */
    public List<Utilisateur> getListeUtilisateurs(String maitre){
         List<Utilisateur> result = new ArrayList<Utilisateur>();
        try (
	     Connection conn = getConn();
             PreparedStatement st = conn.prepareStatement
             /** je n'affiche pas les utilisateurs qui sont déjà des joueurs **/
                ("SELECT * FROM Utilisateur where pseudonyme != ? ");
	     ) {
            st.setString(1,maitre);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                Utilisateur utilisateur = new Utilisateur();
                utilisateur.setIdUser(rs.getInt("idUser"));
                utilisateur.setNom(rs.getString("pseudonyme"));
                result.add(utilisateur);
            }
        } catch (SQLException e) {
            throw new DAOException("Erreur BD " + e.getMessage(), e);
	}
	return result;
    }

    /**
     *  Vérification si un joueur appartient à une partie
     * @param Name
     * @return un boolean indiquant l'etat d'appartenance.
     */
    public boolean participePartie(String Name){
    try (
        Connection conn = getConn();  
        PreparedStatement st = conn.prepareStatement
        ("select * from Joueur where pseudonyme = ?");) {
        st.setString(1, Name);
        ResultSet rs = st.executeQuery();
        return rs.next();
        }
        catch (SQLException e) {
            throw new DAOException("Erreur BD "  +  e.getMessage(), e);
        }
    }

    /**
     *  Vérification du nom du maître du jeu
     * @param Name
     * @return true si Name est bien le pseudo du maître
     */
    public boolean maitrePartie(String Name) {
                try (
            Connection conn = getConn();  
            PreparedStatement st = conn.prepareStatement
            ("select maitre from Partie where maitre = ?");) {
            st.setString(1, Name);
            ResultSet rs = st.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            throw new DAOException("Erreur BD "  +  e.getMessage(), e);
        }
    }
}
