/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

/**
 * cette classe contient les informations principales des joueurs qui ont exercé leurs pouvoir durant la nuit
 * @author Equipe 9
 */
public class ExercerPouvoir {
    
    private String exercerPar;
    private String exercerSur;
    
    /**
     * Définir le joueur qui a exercé le pouvoir
     * @param name 
     */
    public void setExercerPar(String name){
        this.exercerPar = name;
    }
    
    /**
     * Définir le joueur sur lequel le pouvoir a été  exercé 
     * @param name 
     */
    public void setExercerSur(String name){
        this.exercerSur = name;
    }
    
    /**
     * Renvoie le nom du joueur qui a exercé le pouvoir
     * @return name
     */
    public String getExercerPar(){
        return this.exercerPar;
    }
    
    /**
     * Renvoie le nom du joueur sur lequel le pouvoir a été  exercé 
     * @return 
     */
    public String getExercerSur(){
        return this.exercerSur;
    }
}
