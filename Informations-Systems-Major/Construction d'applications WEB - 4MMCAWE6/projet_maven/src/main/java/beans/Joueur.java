/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

/**
 * Joueur class contient les informations importantes
 * d'un joueur dans une partie de jeu : son pseudonyme, role, pouvoir 
 * et son état de jeu : vivant ou mort.
 * @author Equipe 9
 * 
 */
public class Joueur {
    
    private String pseudonyme;
    private boolean elimine;
    private Role role;
    private Pouvoir pouvoir;
    private String roleSt;
    private String pouvoirSt;
    
    /**
     * Création d'un joueur à partir de pseudonyme de l'utilisateur
     * @param pseudonyme 
     */
    public Joueur(String pseudonyme){
        this.pseudonyme = pseudonyme;
    }
    
    /**
     * Renvoie le pseudonyme de joueur.
     * @return pseudonyme
     */
    public String getPseudonyme(){
        return this.pseudonyme;
    }
    
    /**
     * Changé l'état d'un joueur dans une partie de jeu.
     * Au début de la partie bool est à false ce qui signifie que 
     * le joueur est vivant.
     * @param bool état de jeu
     */
    public void setElimine(boolean bool){
        this.elimine = bool;
    }
    
    /**
     * Préciser le role d'un joueur : humain ou loup-garou
     * @param role 
     */
    public void setRole(Role role){
        this.role = role;
        if (role == Role.humain){
            this.roleSt = "humain";
        }else {
            this.roleSt = "loupGarou";
        }
    }
    
    /**
     * Préciser le pouvoir d'un joueur : contamination , voyance ou aucun pouvoir 
     * @param pouvoir 
     */
    public void setPouvoir(Pouvoir pouvoir){
        this.pouvoir = pouvoir;
        if (pouvoir == Pouvoir.aucun){
            this.pouvoirSt = "aucun";
        } else if (pouvoir == Pouvoir.contamination){
            this.pouvoirSt = "contamination";
        }else {
            this.pouvoirSt = "voyance";
        }
    }
    
    /**
     * Renvoie le role d'un joueur
     * @return 
     */
    public Role getRole(){
        return this.role;
    }
    
     /**
     * Renvoie le pouvoir d'un joueur
     * @return 
     */
    public Pouvoir getPouvoir(){
        return this.pouvoir;
    }
    
     /**
     * Renvoiel'état de jeu  d'un joueur
     * @return 
     */
    public boolean getElimine(){
        return this.elimine;
    }
    
    public String getRoleSt(){
        return this.roleSt;
    }
    
    public String getPouvoirSt(){
        return this.pouvoirSt;
    }
}
