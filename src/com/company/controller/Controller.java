package com.company.controller;

import com.company.model.model.Ingredient;
import com.company.model.repository.IngredientRepository;

import java.util.ArrayList;

public abstract class Controller {
    IngredientRepository ingredientRepository = new IngredientRepository();
    ArrayList<Ingredient> allIngredients;
    Ingredient searchedIngredient;

    public void getAllIngredients(){
        allIngredients = ingredientRepository.getAllIngredients();
    }

    public Ingredient searchIngredientInArrayList(int ingredientNumber) {
        for (Ingredient ingredient : allIngredients) {
            if (ingredient.number == ingredientNumber) {
                searchedIngredient = ingredient;
            }
        }
        return searchedIngredient;
    }
}

