package com.company.model.model;

public class Change {
    public int menuNr;
    public Ingredient ingredient;
    public int changeNumber;
    public boolean added;
    public boolean removed;

    public Change(Ingredient ingredient, int menuNr, boolean removed, boolean added, int changeNumber){
        this.menuNr = menuNr;
        this.ingredient = ingredient;
        this.added = added;
        this.removed = removed;
        this.changeNumber = changeNumber;
    }
}
