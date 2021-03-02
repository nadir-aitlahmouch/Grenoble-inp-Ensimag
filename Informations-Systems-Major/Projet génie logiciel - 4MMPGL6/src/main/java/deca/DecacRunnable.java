/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.ensimag.deca;

/**
 *
 * @author benjellh
 */
public class DecacRunnable implements Runnable{
    DecacCompiler compiler;

    DecacRunnable(DecacCompiler decacCompiler) {
        this.compiler = decacCompiler;
    }
    @Override
    public void run() 
    {
        compiler.compile();
    }
}
    
