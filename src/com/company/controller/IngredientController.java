package com.company.controller;
import com.company.model.model.Ingredient;

public class IngredientController extends Controller {

    public void showAllIngredients(){
        getAllIngredients();
        for(Ingredient ingredient: allIngredients){
            System.out.println("Zutat: "+ ingredient.name + " Nummer: "+ingredient.number);
        }
    }
}
