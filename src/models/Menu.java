package models;

import java.util.ArrayList;

public class Menu {
    public String name;
    public int number;
    public double price;
    public ArrayList <Ingredient> ingredients = new ArrayList<>();

    public Menu(String name, int number, double price){
        this.name = name;
        this.number = number;
        this.price = price;
    }


}
