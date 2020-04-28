package com.company.model.model;

public class Ingredient {
    public String name;
    public int number;
    public double priceIfAdded;
    public int consumed;

    public Ingredient(String name, int number){
        this.name = name;
        this.number = number;
    }

    public void setPriceIfAdded(double priceIfAdded) {
        this.priceIfAdded = priceIfAdded;
    }

    public void setConsumed(int consumed) {
        this.consumed = consumed;
    }
}
