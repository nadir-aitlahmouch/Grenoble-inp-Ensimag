/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;


/**
 * Cette classe represente les messages que circulent les utilisateurs dans la page du discussion
 * @author Equipe 9
 */
public class Message {
    
    private String date;
    private String nameUtilisateur;
    private String contenu;
    private String periode;
    
    /**
     * Un message est identifé par son Émetteur , la date d'émission , son contenu 
     * et la période où il a été envoyé : jour ou nuit
     * @param date
     * @param nameUtilisateur
     * @param contenu
     * @param periode 
     */
    public Message(String date, String nameUtilisateur, String contenu, String periode) {
        this.date = date;
        this.nameUtilisateur = nameUtilisateur;
        this.contenu = contenu;
        this.periode = periode;
    }
    
    /**
     * Renvoie la période à laquelle le message a été envoyé.
     * @return periode
     */
    public String getPeriode() {
        return periode;
    }
    
    /**
     * Définir la période à laquelle le message a été envoyé.
     * @param periode 
     */
    public void setPeriode(String periode) {
        this.periode = periode;
    }
    
    /**
     * Renvoie la date à laquelle le message a été envoyé.
     * @return date
     */
    public String getDate() {
        return date;
    }

    /**
     * Définir la date à laquelle le message a été envoyé.
     * @return date
     */
    public void setDate(String date) {
        this.date = date;
    }

    /**
     * Renvoie le nom de l'émetteur de message
     * @return émetteur
     */
    public String getNameUtilisateur() {
        return nameUtilisateur;
    }
    
    /**
     * Définir le nom de l'émetteur de message.
     * @param nameUtilisateur 
     */
    public void setNameUtilisateur(String nameUtilisateur) {
        this.nameUtilisateur = nameUtilisateur;
    }
    
    /**
     * Renvoie le contenu de message.
     * @return le message
     */
    public String getContenu() {
        return contenu;
    }
    
    /**
     * Renvoie le contenu de message
     * @param contenu 
     */
    public void setContenu(String contenu) {
        this.contenu = contenu;
    }
}
