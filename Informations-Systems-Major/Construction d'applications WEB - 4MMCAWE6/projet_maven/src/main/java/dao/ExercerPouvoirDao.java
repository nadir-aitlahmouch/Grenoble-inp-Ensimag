/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;
import beans.Joueur;
import beans.ExercerPouvoir;

/**
 *
 * @author Equipe 9
 */
public class ExercerPouvoirDao extends AbstractDataBaseDAO{
    
    public ExercerPouvoirDao(DataSource ds){
        super(ds);
    }
 /**
 * listé les humains non eliminé. 
 * @return liste des humains non eliminé
 */
    public List<Joueur> getHumains(){
        
        List<Joueur> humains = new ArrayList<>();
        try (
            
            Connection conn = getConn();
            PreparedStatement st = conn.prepareStatement
            ("select * from Joueur where elimine = 0 and role like ?");) {
            st.setString(1, "humain");
            ResultSet resultSet = st.executeQuery();
            while (resultSet.next()){
                Joueur humain = new Joueur(resultSet.getString("pseudonyme"));
                humains.add(humain);
            }
            
        } catch (SQLException e) {
            throw new DAOException("Erreur BD "  +  e.getMessage(), e);
        }
        
        return humains;
    }
 /**
 * cherché le joueur qui a comme pseudonyme name.
 * @param name
 * @return joueur avec le meme name
 */    
    public List<Joueur> getJoeurs(String name){
        List<Joueur> joueurs = new ArrayList<>();
        try (
            
            Connection conn = getConn();
            PreparedStatement st = conn.prepareStatement
            ("select * from Joueur where elimine = 0 and pseudonyme != ?");) {
            st.setString(1, name);
            ResultSet resultSet = st.executeQuery();
            while (resultSet.next()){
                Joueur player = new Joueur(resultSet.getString("pseudonyme"));
                joueurs.add(player);
            }
            
        } catch (SQLException e) {
            throw new DAOException("Erreur BD "  +  e.getMessage(), e);
        }
        
        return joueurs;
    }
 /**
 * exercer le pouvoir d'un loup garoup sur un humain.
 * @param exercerPv
 */
    public void appliqueVoyance(ExercerPouvoir exercerPv){
        
        String par = exercerPv.getExercerPar();
        String sur = exercerPv.getExercerSur();
        
        try (
            
            Connection conn = getConn();
            PreparedStatement st = conn.prepareStatement
            ("insert into ExercerPouvoir (exercerPar,exercerSur) values (?,?)");) {
            st.setString(1, par);
            st.setString(2, sur);
            st.executeQuery();
        } catch (SQLException e) {
            throw new DAOException("Erreur BD "  +  e.getMessage(), e);
        }
    }
    
 /**
 * exercer le pouvoir d'un loup garoup sur un humain.
 * @param exercerPv
 */
    public void appliqueContamination(ExercerPouvoir exercerPv){
        
        String loupGarou = exercerPv.getExercerPar();
        String humain = exercerPv.getExercerSur();
        
        try (
            
            Connection conn = getConn();
            PreparedStatement st = conn.prepareStatement
            ("update joueur set role = 'loupGarou' where pseudonyme like ?");) {
            st.setString(1, humain);
            st.executeQuery();
        } catch (SQLException e) {
            throw new DAOException("Erreur BD "  +  e.getMessage(), e);
        }
        
        try (
            
            Connection conn = getConn();
            PreparedStatement st = conn.prepareStatement
            ("insert into ExercerPouvoir (exercerPar,exercerSur) values (?,?)");) {
            st.setString(1, loupGarou);
            st.setString(2, humain);
            st.executeQuery();
        } catch (SQLException e) {
            throw new DAOException("Erreur BD "  +  e.getMessage(), e);
        }
    }
 /**
 * vide la table exercerPouvoir.
 */
    public void videTable(){
        
        try (
            
            Connection conn = getConn();
            PreparedStatement st = conn.prepareStatement
            ("delete exercerPouvoir");) {
            st.executeQuery();
        } catch (SQLException e) {
            throw new DAOException("Erreur BD "  +  e.getMessage(), e);
        }
        
    }
 /**
 * vide la table exercerPouvoir.
 */
    public void deletePouvoirs() {
            try (
            Connection conn = getConn();  
            PreparedStatement st = conn.prepareStatement
            ("delete from ExercerPouvoir");) {
            st.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("Erreur BD "  +  e.getMessage(), e);
        }
    }
    
}
