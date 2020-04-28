package com.company.view;

import java.util.InputMismatchException;
import java.util.Scanner;

public class CustomerView extends View {
    Scanner scan = new Scanner(System.in);

    public String startRegistration(){
        System.out.println("Legen Sie ein Kundenkonto an (1)\noder melden Sie sich mit Ihrer Kundennummer an (2)");
        String choice = scan.nextLine();
        return choice;
    }
    public String askName() {
        System.out.println("Geben Sie bitte Ihren Namen ein:");
        String name = scan.nextLine();
        return name;
    }
    public String askLocation(){
        System.out.println("Geben Sie bitte Ihren Wohnort ein:");
        String location = scan.nextLine();
        return location;
    }
    public int askForCustomerId(){
        System.out.println("Geben Sie bitte Ihre Kundennummer ein:");
        int customerId = scanIntMethod(numberScanner);
        return customerId;
    }

}
