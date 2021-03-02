/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

/**
 * Cette classe contient les informations d'une partie de jeu
 * @author Equipe 9
 */
public class Partie {

    private String maitre;
    private String periode;
    private double probabilite;
    private double probaLoupGarou;
    private int nbJoueurs;

    /**
     * La période peut être soit le jour soit la nuit.
     * @return periode
     */
    public String getPeriode() {
        return periode;
    }

    /**
     * Définir la période actuelle de la partie de jeu
     * @param periode 
     */
    public void setPeriode(String periode) {
        this.periode = periode;
    }

    /**
     * Renvoie le nombre de joueurs dans la partie de jeu
     * @return nombre de joueurs
     */
    public int getNbJoueurs(){
        return this.nbJoueurs;
    }
    
    /**
     * Définir le nombre de joueurs
     * @param nb 
     */
    public void setNbJoueurs(int nb){
        this.nbJoueurs = nb;
    }
    
    /**
     * Définir la probabilité d'attribution des pouvoirs spéciaux dans la partie de jeu
     * @param proba 
     */
    public void setProba(double proba) {
        this.probabilite = probabilite;
    }

    /**
     * Définir la proportion initiale des loups-garous dans la partie de jeu
     * @param loupGarou 
     */
    public void setProbaLoupGarou(double loupGarou) {
        this.probaLoupGarou = probaLoupGarou;
    }

    /**
     * Renvoie la probabilité d'attribution des pouvoirs spéciaux
     * @return proba
     */
    public double getProba() {
        return this.probaLoupGarou;
    }
    
    /**
     * Renvoie la proportion initiale des loups-garous dans la partie de jeu
     * @return 
     */
    public double getProbaLoupGarou() {
        return this.probaLoupGarou;
    }

    /**
     * Renvoie le maitre de jeu qui a lancé la partie de jeu.
     * @return 
     */
    public String getMaitre() {
        return maitre;
    }

    /**
     * Définir le maitre de jeu de la partie.
     * @param maitre 
     */
    public void setMaitre(String maitre) {
        this.maitre = maitre;
    }
}
