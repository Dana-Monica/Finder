package com.example.finder;

import java.util.HashMap;
import java.util.List;

public class Recipe {

    private String name;
    private HashMap<String,String> ingredients;
    private HashMap<String,String> instructions;

    public Recipe(){
        this.instructions = new HashMap<String,String>();
        this.ingredients = new HashMap<String,String>();
    }

    public Recipe(String name, HashMap<String,String> ingr, HashMap<String,String> instr){
        this.name = name;
        this.ingredients = ingr;
        this.instructions = instr;
    }

    public String getName() {
        return name;
    }

    public HashMap<String,String> getInstructions() {
        return instructions;
    }

    public HashMap<String,String> getIngredients() {
        return ingredients;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addIngredient(String name, String quantity){
        ingredients.put(name,quantity);
    }

    public void addInstruction(String key, String instr){
        instructions.put(key,instr);
    }

    public String getInstructionsString() {
        String returnString = "";
        for (String value : instructions.values())
        {
            returnString += value + "\n";
        }
        return returnString;
    }

    public String getIngredientsString() {
        String returnString = "";
        for (String value : ingredients.keySet())
        {
            returnString += value + ":" + ingredients.get(value) + "\n";
        }
        return returnString;
    }
}
