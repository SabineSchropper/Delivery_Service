package com.company.controller;

import com.company.model.model.Change;
import com.company.model.model.Ingredient;
import com.company.model.model.Order;
import com.company.model.repository.IngredientRepository;

import java.util.ArrayList;

public class IngredientController extends Controller {

    public void showAllIngredients(){
        getAllIngredients();
        for(Ingredient ingredient: allIngredients){
            System.out.println("Zutat: "+ ingredient.name + " Nummer: "+ingredient.number);
        }
    }
}
