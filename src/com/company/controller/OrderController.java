package com.company.controller;

import com.company.model.model.*;
import com.company.model.repository.OrderRepository;
import com.company.view.OrderView;
import java.util.ArrayList;

public class OrderController extends Controller {
    OrderView orderView = new OrderView();
    OrderRepository orderRepository;
    ArrayList<Change> actualChanges;

    public OrderController(Customer customer){
        orderRepository = new OrderRepository(customer);
    }
    public int askForMenuToOrder(){
        int menuNumber = orderView.askForMenuToOrder();
        return menuNumber;
    }
    public void addToOrder(Menu menu){
        orderRepository.addToOrder(menu);
    }
    public void handleRemovingIngredients(int menuNumber, boolean getsRemoved, boolean getsAdded){
        int ingredientNumber = orderView.askWhatToRemove();
        Ingredient ingredient = searchIngredientInArrayList(ingredientNumber);
        actualChanges = orderRepository.addToActualChanges(ingredient, menuNumber, getsRemoved, getsAdded);
        showChangedIngredients(actualChanges);
    }
    public void handleAddingIngredients(int menuNumber, boolean getsRemoved, boolean getsAdded){
        int ingredientNumber = orderView.askWhatToAdd();
        getAllIngredients();
        Ingredient ingredient = searchIngredientInArrayList(ingredientNumber);
        actualChanges = orderRepository.addToActualChanges(ingredient,menuNumber,getsRemoved,getsAdded);
        showChangedIngredients(actualChanges);
    }
    public void showChangedIngredients(ArrayList<Change> actualChanges) {
        String addedOrRemovedString = "";
        for (Change change : actualChanges) {
            if (change.removed) {
                addedOrRemovedString = "Entfernt: ";
            } else if (change.added) {
                addedOrRemovedString = "Hinzugefügt: ";
            }
            System.out.println(addedOrRemovedString + "Zutat: " + change.ingredient.name + " Nummer: " + change.ingredient.number);
        }
    }
    public void deleteActualChanges(int menuNumber){
        orderRepository.deleteActualChanges(menuNumber);
        System.out.println("Änderungen an diesem Gericht wurden wieder gelöscht.");
    }
    public void saveChanges(){
        orderRepository.saveChanges();
    }
    public void calculateTotalPriceAndShowBill(){
        double totalPrice = 0;
        double priceForChanges = 0;
        double discount = 0;
        Order order = orderRepository.getOrder();
        boolean isThereADiscount = orderRepository.checkIfDiscount(order.customer.id);
        for(Menu menu : order.orderedMenus){
            totalPrice = totalPrice + menu.price;
        }
        for(Change change : order.changes){
            if(change.added){
                priceForChanges = priceForChanges + change.ingredient.priceIfAdded;
            }
        }
        totalPrice = totalPrice + priceForChanges;
        if(isThereADiscount){
            discount = totalPrice/10;
            totalPrice = totalPrice - discount;
        }
        totalPrice = totalPrice + order.deliverFee;
        order.totalPrice = totalPrice;
        orderView.showBill(order, isThereADiscount, discount);
    }
    public void createOrder(Customer customer) {
        orderRepository.createOrder(customer);
    }
    public void addOrderToDatabase(){
        orderRepository.addOrderToDatabase();
    }


}
