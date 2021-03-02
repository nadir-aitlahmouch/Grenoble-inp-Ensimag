/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.ensimag.deca.tools;
import java.util.Map.Entry;
import java.util.Objects;

/**
 *
 * @author benjellh
 * @param <X>
 * @param <Y>
 */
    public class Tuple<X, Y> implements Entry<X, Y>{ 
    public  X x; 
    public  Y y; 
    public Tuple(X x, Y y) { 
        this.x = x; 
        this.y = y; 
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + Objects.hashCode(this.x);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Tuple<?, ?> other = (Tuple<?, ?>) obj;
        return true;
    }
    
    public X getKey()
    {
        return x;
    }

    public Y getValue()
    {
        return y;
    }
    
    @Override
    public Y setValue(Y value) {
        this.y = value;
        return y;
    }
}
