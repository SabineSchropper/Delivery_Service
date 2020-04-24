package com.company;

import controller.CustomerFile;
import controller.MenuCard;
import models.Customer;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Restaurant restaurant = new Restaurant();
        CustomerFile customerFile = new CustomerFile();
        MenuCard menuCard = new MenuCard();
        Scanner scan = new Scanner (System.in);
        Scanner numberScanner = new Scanner (System.in);
        boolean isRegistrationInProgress = true;
        boolean wantsToSeeDetails = true;
        boolean isOrderInProgress = true;
        boolean isThereAChange = false;
        boolean getsAdded;
        boolean getsRemoved;
        String name = "";
        String location = "";
        String choice = "";
        String menuName = "";
        int customerId = 0;
        int menuNumber = 0;
        int ingredientNumber = 0;

        while(isRegistrationInProgress) {
            System.out.println("Legen Sie ein Kundenkonto an (1)\noder melden Sie sich mit Ihrer Kundennummer an (2)");
            choice = scan.nextLine();

            if(choice.equalsIgnoreCase("1")){
                System.out.println("Geben Sie bitte Ihren Namen ein:");
                name = scan.nextLine();
                System.out.println("Geben Sie bitte Ihren Wohnort ein:");
                location = scan.nextLine();
                customerId = customerFile.addCustomerAndGetId(name,location);
                System.out.println("Sie haben ein Kundenkonto erstellt. Ihre Kundennummer lautet: "+customerId);
                isRegistrationInProgress = false;
            }
            else if(choice.equalsIgnoreCase("2")){

                System.out.println("Geben Sie bitte Ihre Kundennummer ein:");
                customerId = scanIntMethod(numberScanner);
                name = customerFile.checkCustomerAndGetName(customerId);

                if(!name.equalsIgnoreCase("")) {
                    System.out.println("Die Anmeldung war erfolgreich, " + name);
                    isRegistrationInProgress = false;
                }
                else{
                    System.out.println("Die Kundennummer wurde nicht gefunden.");
                }
            }
            else{
                System.out.println("Versuchen Sie es bitte noch einmal.");
            }
        }
        menuCard.showMenuCard();

        while(wantsToSeeDetails) {
            System.out.println("\nMöchten Sie Zutaten ansehen? Geben Sie die Zahl neben dem Gericht ein.");
            System.out.println("Sie können Zutaten während der Bestellung verändern.");
            System.out.println("Zur Bestellung kommen Sie mit 0");
            menuNumber = scanIntMethod(numberScanner);
            if(menuNumber == 0){
                wantsToSeeDetails = false;
            }
            else {
                menuCard.showIngredients(menuNumber);
            }
        }
        while(isOrderInProgress){
            System.out.println("Welches Gericht möchten Sie bestellen?");
            System.out.println("Sie schließen die Bestellung mit 0 ab.");
            menuNumber = scanIntMethod(numberScanner);
            if(menuNumber == 0) {
                restaurant.calculateTotalPrice();
                isOrderInProgress = false;
            }
            else {
                menuCard.showIngredients(menuNumber);
                System.out.println("\nBestellen: 1 \nZutaten verändern: 2 \nZurück: 3");
                choice = scan.nextLine();
                if(choice.equalsIgnoreCase("1")){
                    isThereAChange = false;
                    restaurant.addToOrder(customerId, isThereAChange, menuNumber);
                }
                else if(choice.equalsIgnoreCase("2")){
                    isThereAChange = true;
                    while(isThereAChange) {
                        System.out.println("\nZutat entfernen: 1 \nZutat hinzufügen: 2 \nZurück: 3 \nBestellen : 4");
                        choice = scan.nextLine();
                        if (choice.equalsIgnoreCase("1")) {
                            getsRemoved = true;
                            getsAdded = false;
                            System.out.println("Welche Zutat möchten Sie entfernen? Bitte Zahl eingeben.");
                            ingredientNumber = scanIntMethod(numberScanner);
                            restaurant.addToChanges(ingredientNumber, menuNumber, customerId, getsRemoved, getsAdded);
                            restaurant.showChanges();
                        } else if (choice.equalsIgnoreCase("2")) {
                            getsRemoved = false;
                            getsAdded = true;
                            restaurant.showAllIngredients();
                            System.out.println("Welche Zutat möchten sie hinzufügen? Bitte Zahl eingeben.");
                            ingredientNumber = scanIntMethod(numberScanner);
                            restaurant.addToChanges(ingredientNumber, menuNumber, customerId, getsRemoved, getsAdded);
                            restaurant.showChanges();
                        } else if (choice.equalsIgnoreCase("3")) {
                            //when customer returns to last point the changes should be deleted
                            restaurant.deleteThisChanges(menuNumber);
                            break;
                        } else if (choice.equalsIgnoreCase("4")) {
                            restaurant.addToOrder(customerId,isThereAChange,menuNumber);
                            isThereAChange = false;
                        } else {
                            System.out.println("Die Eingabe war nicht eindeutig.");
                        }
                    }
                }
                else if(choice.equalsIgnoreCase("3")){
                    continue;
                }
                else{
                    System.out.println("Die Eingabe war nicht eindeutig.");
                }
            }
        }
        restaurant.showBill(customerId);
    }
    public static int scanIntMethod(Scanner numberScanner){
        int number = 0;
        try {
            numberScanner = new Scanner(System.in);
            number = numberScanner.nextInt();
        } catch (InputMismatchException ex) {
            System.out.println("Die Eingabe muss eine Zahl sein.");
        }
        return number;
    }
}
