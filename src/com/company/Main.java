package com.company;

import com.company.controller.CustomerController;
import com.company.controller.IngredientController;
import com.company.controller.MenuController;
import com.company.controller.OrderController;
import com.company.model.model.Change;
import com.company.model.model.Customer;
import com.company.model.model.Menu;
import com.company.model.model.Order;

import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        CustomerController customerController = new CustomerController();
        IngredientController ingredientController = new IngredientController();
        Scanner scan = new Scanner (System.in);
        boolean isRegistrationInProgress = true;
        boolean wantsToSeeDetails = true;
        boolean isOrderInProgress = true;
        boolean isThereAChange = false;
        boolean getsAdded;
        boolean getsRemoved;
        String name = "";
        String choice = "";
        int customerId = 0;
        int menuNumber = 0;
        Menu menu;
        Customer customer = null;

        while(isRegistrationInProgress) {
            choice = customerController.start();
            if(choice.equalsIgnoreCase("1")){
                customer = customerController.createAccount();
                System.out.println("Sie haben ein Kundenkonto erstellt. Ihre Kundennummer lautet: "+customer.id);
                isRegistrationInProgress = false;
            }
            else if(choice.equalsIgnoreCase("2")){
                customer = customerController.checkCustomerAndGetName();
                if(customer != null) {
                    System.out.println("Die Anmeldung war erfolgreich, " + customer.name);
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
        //now we have customerId we can create an orderController and an Order
        OrderController orderController = new OrderController(customer);
        MenuController menuController = new MenuController();
        menuController.showMenuCard();

        while(wantsToSeeDetails) {
            wantsToSeeDetails = menuController.askIfWantToSeeDetailsAndShowThem();
        }
        while(isOrderInProgress){
            menuNumber = orderController.askForMenuToOrder();
            if(menuNumber == 0) {
                orderController.createOrder(customer);
                orderController.calculateTotalPriceAndShowBill();
                orderController.addOrderToDatabase();
                isOrderInProgress = false;
            }
            else {
                menu = menuController.showIngredients(menuNumber);
                System.out.println("\nBestellen: 1 \nZutaten verändern: 2 \nZurück: 3");
                choice = scan.nextLine();
                if(choice.equalsIgnoreCase("1")){
                    isThereAChange = false;
                    orderController.addToOrder(menu);
                }
                else if(choice.equalsIgnoreCase("2")){
                    isThereAChange = true;
                    while(isThereAChange) {
                        System.out.println("\nZutat entfernen: 1 \nZutat hinzufügen: 2 \nZurück: 3 \nBestellen : 4");
                        choice = scan.nextLine();

                        if (choice.equalsIgnoreCase("1")) {
                            getsRemoved = true;
                            getsAdded = false;
                            orderController.handleRemovingIngredients(menuNumber,getsRemoved,getsAdded);

                        } else if (choice.equalsIgnoreCase("2")) {
                            getsRemoved = false;
                            getsAdded = true;
                            ingredientController.showAllIngredients();
                            orderController.handleAddingIngredients(menuNumber,getsRemoved,getsAdded);

                        } else if (choice.equalsIgnoreCase("3")) {
                            //when customer returns to last point the changes should be deleted
                            orderController.deleteActualChanges(menuNumber);
                            break;
                        } else if (choice.equalsIgnoreCase("4")) {
                            orderController.addToOrder(menu);
                            orderController.saveChanges();
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
    }
}
