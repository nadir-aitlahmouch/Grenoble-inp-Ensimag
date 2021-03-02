/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import java.util.List;

/**
 *  Cette classe contient les informations importantes sur les votes.
 * @author Equipe 9
 */
public class Proposed {
    
    private final String pseudonyme;
    private List<String> quiVotent;
    
    /**
     * propsed s'identifie par le nom du joueur qui a été proposé 
     * durant la nuit ou le jour par un autre joueur.
     * @param pseudonyme 
     */
    public Proposed(String pseudonyme){
        this.pseudonyme = pseudonyme;
    }
    /**
     * Renvoie le nombre du joueurs qui ont voté pour tuer ce joueur.
     * @return nombre de votes
     */
    public int getNbVote(){
        return quiVotent.size();
    }
    
    /**
     * Ajouté un nouvel joueur à la liste des votants 
     * @param vote 
     */
    public void addVote(List<String> vote){
        quiVotent = vote;
    }
    /**
     * Renvoie la liste des votants.
     * @return 
     */
    public List<String> getVote(){
        return quiVotent;
    }
    
    /**
     * Renvoie le nom du joueur qui a été proposé 
     * pour être tuer.
     * @return 
     */
    public String getPseudonyme(){
        return this.pseudonyme;
    }
    
    
}
