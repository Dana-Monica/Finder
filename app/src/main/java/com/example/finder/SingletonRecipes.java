package com.example.finder;

import java.util.ArrayList;
import java.util.List;

public class SingletonRecipes {

    private static SingletonRecipes s;
    private static List<Recipe> elements;

    private SingletonRecipes(){
        elements = new ArrayList<Recipe>();
    }

    public static SingletonRecipes getInstance() {
        if (s == null)
            s = new SingletonRecipes();
        return s;
    }

    public List<Recipe> getElements() {
        return elements;
    }

    public void addElement(Recipe s) {
        Boolean contains = false;
        for (Recipe a : elements) {
            if(a.getName().equals(s.getName()))
                contains = true;
        }
        if(contains == false)
            elements.add(s);
    }

    public void removeElement(String s){
        for (Recipe r: elements) {
            if(r.getName().equals(s))
            {
                elements.remove(s);
                break;
            }
        }
    }
}
