package com.company.view;

import com.company.model.model.Ingredient;
import com.company.model.model.Menu;

import java.util.ArrayList;

public class MenuView extends View {

    public void viewMenu(ArrayList<Menu> menus) {
        for (Menu menu : menus) {
            System.out.println("Nr. " + menu.number + " " + menu.name + ", " + menu.price + " €");
        }
    }
    public int askIfwantToSeeDetails() {
        System.out.println("\nMöchten Sie Zutaten ansehen? Geben Sie die Zahl neben dem Gericht ein.");
        System.out.println("Sie können Zutaten während der Bestellung verändern.");
        System.out.println("Zur Bestellung kommen Sie mit 0");
        int menuNumber = scanIntMethod(numberScanner);
        return menuNumber;
    }
    public void viewIngredients(Menu menu) {
        System.out.println(menu.name + " :");
        for (Ingredient ingredient : menu.ingredients) {
            System.out.println("Zutat: " + ingredient.name + " Nummer: " + ingredient.number);
        }
    }
}
