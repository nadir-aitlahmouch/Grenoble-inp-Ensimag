/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

/**
 * Cette classe contient les informations principale d'un utilisateur de l'application
 * @author Equipe 9
 */
public class Utilisateur {
    
    private long idUser;
    private String email;
    private String motDePasse;
    private String nom;

    /**
     * Définir l'identifiant de l'utilisateur
     * @param id 
     */
    public void setIdUser(long id){
        this.idUser = id;
    }
    
    /**
     * Définir l'e-mail de l'utilisateur
     * @param email 
     */
    public void setEmail(String email) {
	this.email = email;
    }
    
    /**
     * Renvoie l'e-mail de l'utilisateur
     * @return 
     */
    public String getEmail() {
	return email;
    }
    
    /**
     * Définir le mot de passe de l'utilisateur
     * @param motDePasse 
     */
    public void setMotDePasse(String motDePasse) {
	this.motDePasse = motDePasse;
    }
    
    /**
     * Renvoie le mot de passe de l'utilisateur
     * @return motDePasse
     */
    public String getMotDePasse() {
	return motDePasse;
    }

    /**
     * Définir le pseudonyme de l'utilisateur
     * @param nom 
     */
    public void setNom(String nom) {
	this.nom = nom;
    }
    
    /**
     * Renvoie le le pseudonyme de l'utilisateur
     * @return 
     */
    public String getNom() {
	return nom;
    }
}
