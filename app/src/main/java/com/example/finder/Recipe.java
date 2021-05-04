package com.example.finder;

import java.util.HashMap;
import java.util.List;

public class Recipe {

    private String name;
    private HashMap<String,String> ingredients;
    private String instructions;

    public Recipe(){
        this.instructions = "";
        this.ingredients = new HashMap<String,String>();
        name= "";
    }

    public Recipe(String name, HashMap<String,String> ingr, String instr){
        this.name = name;
        this.ingredients = ingr;
        this.instructions = instr;
    }

    public String getName() {
        return name;
    }

    public String getInstructions() {
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

    public void addInstruction(String instr){
        instructions = instr;
    }

    public String getInstructionsString() {
        return instructions;
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
