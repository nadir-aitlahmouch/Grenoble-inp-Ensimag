/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.ensimag.ima.pseudocode;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.tools.Tuple;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.POP;
import fr.ensimag.ima.pseudocode.instructions.PUSH;
import java.util.LinkedList;
import java.util.Map.Entry;
import java.util.Stack;

/**
 *
 * @author benjellh
 */
public class RegisterManager {
    
    static DecacCompiler compiler;
    private static LinkedList<Integer> ListDispo;
    private static LinkedList<Entry<Integer, Integer>> ListPush;
    private static Stack<Integer> StackPush;
    private static int compteur = 0;
    private static int nbrPush = 0;

    public static void setCompiler(DecacCompiler compiler) {
        RegisterManager.compiler = compiler;
    }

    public static void setStackPush(Stack<Integer> StackPush) {
        RegisterManager.StackPush = StackPush;
    }

    public static void setListDispo(LinkedList<Integer> ListDispo) {
        RegisterManager.ListDispo = ListDispo;
    }

    public static void setListPush(LinkedList<Entry<Integer, Integer>> ListPush) {
        RegisterManager.ListPush = ListPush;
    }
    
    public static DecacCompiler getCompiler() {
        return compiler;
    }
    static public LinkedList<Integer> getListDispo() {
        return ListDispo;
    }
    public static LinkedList<Entry<Integer, Integer>> getListPush() {
        return ListPush;
    }

    public static Stack<Integer> getStackPush() {
        return StackPush;
    }

    public static int getNbrPush() {
        return nbrPush;
    }
    
    static public void incrementerCompteur()
    {
        int Rmax = (compiler == null ? 15 : compiler.getCompilerOptions().getRmax());
        compteur = (compteur + 1)%Rmax;
        if(compteur == 1)
        {
            compteur = 2; 
        }
    }
    static public GPRegister getFreeRegister()
    {
        if(!ListDispo.isEmpty())
        {
            return Register.getR(ListDispo.pop());
        }
        else
        {
            ListPush.get(compteur).setValue(ListPush.get(compteur).getValue() + 1);
            StackPush.push(compteur);
            compiler.addInstruction(new PUSH(GPRegister.getR(compteur)));
            nbrPush += 1;
            int valeurRetour = compteur;
            incrementerCompteur();
            return Register.getR(valeurRetour);
        }
    }
    
    
    static public void Refresh(DVal right)
    {
        if(!ListPush.get(((GPRegister)right).getNumber()).getValue().equals(0))
        {
            ListPush.get(((GPRegister)right).getNumber()).setValue(ListPush.get(((GPRegister)right).getNumber()).getValue() - 1);
            StackPush.pop();
            compiler.addInstruction(new POP(((GPRegister)right)));
        }
        else
        {
            ListDispo.push(((GPRegister)right).getNumber());
        }
    }

    public static GPRegister findother(GPRegister left) {
        GPRegister right = getFreeRegister();
        compiler.addInstruction(new LOAD(left, right));
        Refresh(left);
        return right;
    }
    
    public static void reinit() {
        ListDispo = new LinkedList<>();
        ListPush = new LinkedList<>();
        StackPush = new Stack<Integer>();
        compteur = 0;
        nbrPush = 0;
        for(int i = 0; i < (compiler == null? 15 : compiler.getCompilerOptions().getRmax()); i ++){
            ListDispo.add(i);
            ListPush.add(new Tuple(i, 0));
        }
    }
    
    public static void AllUsed(){
        ListDispo = new LinkedList<>();
        ListPush = new LinkedList<>();
        StackPush = new Stack<Integer>();
        compteur = 2;
        for(int i = 0; i < (compiler == null? 15 : compiler.getCompilerOptions().getRmax()); i ++){
            ListPush.add(new Tuple(i, 0));
        }
    }

    public static GPRegister get(int i) {
        if(ListDispo.contains(new Integer(i)))
        {
            ListDispo.remove(new Integer(i));
            return GPRegister.getR(i);
        }
        else
        {
            ListPush.get(i).setValue(ListPush.get(i).getValue() + 1);
            StackPush.push(i);
            compiler.addInstruction(new PUSH(GPRegister.getR(i)));
            nbrPush += 1;
            return GPRegister.getR(i);   
        }
    }

    public static void PopAllUsed() {
        while(!StackPush.empty()){
            Integer popValue = StackPush.pop();
            compiler.addInstruction(new POP(GPRegister.getR(popValue)));
            ListPush.get(popValue).setValue(ListPush.get(popValue).getValue() - 1);
        }
    nbrPush = 0;
 }
}
