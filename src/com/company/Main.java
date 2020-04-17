package com.company;

import java.sql.*;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/gastro?user=root";
        Restaurant restaurant = new Restaurant();
        Scanner scan = new Scanner (System.in);
        Scanner numberScanner;
        boolean isRegistrationInProgress = true;
        boolean wantsToSeeDetails = true;
        String name = "";
        String location = "";
        String choice = "";
        int customerId = 0;
        int menuNumber = 0;

        while(isRegistrationInProgress) {
            System.out.println("Legen Sie ein Kundenkonto an (1)\noder melden Sie sich mit Ihrer Kundennummer an (2)");
            choice = scan.nextLine();

            if(choice.equalsIgnoreCase("1")){
                System.out.println("Geben Sie bitte Ihren Namen ein:");
                name = scan.nextLine();
                System.out.println("Geben Sie bitte Ihren Wohnort ein:");
                location = scan.nextLine();
                customerId = restaurant.addCustomerAndGetId(name,location);
                System.out.println("Sie haben ein Kundenkonto erstellt. Ihre Kundennummer lautet: "+customerId);
                isRegistrationInProgress = false;
            }
            else if(choice.equalsIgnoreCase("2")){
                System.out.println("Geben Sie bitte Ihre Kundennummer ein:");
                try {
                    numberScanner = new Scanner(System.in);
                    customerId = numberScanner.nextInt();
                }
                catch (InputMismatchException ex){
                    System.out.println("Die Kundennummer muss eine Zahl sein.");
                    continue;
                }
                name = restaurant.getCustomerName(customerId);
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
        restaurant.showMenuCard();
        while(wantsToSeeDetails) {
            System.out.println("\nMöchten Sie Zutaten ansehen? Geben Sie die Zahl neben dem Gericht ein.");
            System.out.println("Zur Bestellung kommen Sie mit 0");
            try {
                numberScanner = new Scanner(System.in);
                menuNumber = numberScanner.nextInt();
            }
            catch (InputMismatchException ex){
                System.out.println("Die Menünummer muss eine Zahl sein.");
                continue;
            }
            restaurant.showIngredients(menuNumber);


        }
    }
}
