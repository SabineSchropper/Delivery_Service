package com.company.view;

import com.company.model.model.Change;
import com.company.model.model.Menu;
import com.company.model.model.Order;

public class OrderView extends View{
    public int askForMenuToOrder() {
        System.out.println("Welches Gericht möchten Sie bestellen?");
        System.out.println("Sie schließen die Bestellung mit 0 ab.");
        int menuNumber = scanIntMethod(numberScanner);
        return menuNumber;
    }
    public int askWhatToRemove(){
        System.out.println("Welche Zutat möchten Sie entfernen? Bitte Zahl eingeben.");
        int ingredientNumber = scanIntMethod(numberScanner);
        return ingredientNumber;
    }
    public int askWhatToAdd(){
        System.out.println("Welche Zutat möchten sie hinzufügen? Bitte Zahl eingeben.");
        int ingredientNumber = scanIntMethod(numberScanner);
        return ingredientNumber;
    }
    public void showBill(Order order, boolean isThereADiscount, double discount){
        for(Menu menu : order.orderedMenus) {
            System.out.println(menu.name + " " + menu.price + " €");
        }
        for(Change change : order.changes){
            if(change.added){
                System.out.println("+ "+ change.ingredient.name +" " + change.ingredient.priceIfAdded+" €");
            }
            else if(change.removed){
                System.out.println("- "+ change.ingredient.name);
            }
        }
        if(isThereADiscount){
            System.out.println("Rabatt für jede zehnte Bestellung: - 10 Prozent");
            System.out.println("Rabatt:\t\t" + discount +" €");
        }
        System.out.println("Zustellgebühr:\t"+ order.deliverFee + " €");
        System.out.println("-------------------------------");
        System.out.println("Gesamtpreis:\t" + Math.round(100.0*order.totalPrice)/100.0 + " €");
    }

}
