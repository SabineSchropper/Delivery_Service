package com.company.controller;

import com.company.model.model.Ingredient;
import com.company.model.repository.IngredientRepository;

import java.util.ArrayList;
//made abstract class controller because I wanted OrderController also to be able to use these Methods
public abstract class Controller {
    IngredientRepository ingredientRepository = new IngredientRepository();
    ArrayList<Ingredient> allIngredients;
    Ingredient searchedIngredient;

    public Ingredient searchIngredientInArrayList(int ingredientNumber) {
        for (Ingredient ingredient : allIngredients) {
            if (ingredient.number == ingredientNumber) {
                searchedIngredient = ingredient;
            }
        }
        return searchedIngredient;
    }
    public void getAllIngredients(){
        allIngredients = ingredientRepository.getAllIngredients();
    }
}

