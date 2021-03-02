/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.ensimag.ima.pseudocode;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.tools.Tuple;
import java.util.LinkedList;
import java.util.Map.Entry;
import java.util.Stack;

/**
 *
 * @author benjellh
 */
public class InitRegisterManager {
    public static void initManager(DecacCompiler compiler)
    {
        RegisterManager.setCompiler(compiler);
        LinkedList<Integer> list = new LinkedList<Integer>();
        LinkedList<Entry<Integer, Integer>> l = new LinkedList<Entry<Integer, Integer>>();
        Stack<Integer> stack = new Stack<Integer>();
        int Rmax = (compiler == null ? 15 : compiler.getCompilerOptions().getRmax());
        for(int i = 0; i < Rmax; i ++){
            list.add(i);
            l.add(new Tuple(i, 0));
        }
        RegisterManager.setListDispo(list);
        RegisterManager.setListPush(l);
        RegisterManager.setStackPush(stack);
    }
}
