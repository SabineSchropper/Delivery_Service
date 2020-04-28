package com.company.model.repository;

import com.company.model.DatabaseConnector;
import com.company.model.model.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class OrderRepository {
    String url = "jdbc:mysql://localhost:3306/gastro?user=root";
    DatabaseConnector databaseConnector = new DatabaseConnector(url);
    Order order;
    ArrayList<Change> actualChanges = new ArrayList<>();
    int changeNumber = 1;
    String sql;

    public OrderRepository(Customer customer) {
        Order order = new Order(customer);
        this.order = order;
    }

    public void addToOrder(Menu menu) {
        order.orderedMenus.add(menu);
    }

    public ArrayList addToActualChanges(Ingredient ingredient, int menuNumber, boolean removed, boolean added) {
        Change change = new Change(ingredient, menuNumber, removed, added, changeNumber);
        actualChanges.add(change);
        changeNumber++;
        return actualChanges;
    }

    public Order getOrder() {
        return order;
    }

    public void deleteActualChanges(int menuNumber) {
        for (Change change : actualChanges) {
            if (change.menuNr == menuNumber) {
                actualChanges.remove(change);
            }
        }
    }
    public void saveChanges() {
        for (Change change : actualChanges) {
            order.changes.add(change);
        }
        actualChanges.clear();
        changeNumber = 1;
    }
    public void addOrderToDatabase() {
        setTotalPrice();
        insertIntoTableOrderDetails();
        insertIntoTableChanges();
        setConsumedIngredientsInDatabase();
    }

    public void createOrder(Customer customer) {
        int orderNr;
        double deliverFee;
        try {
            databaseConnector.buildConnection();
            //insert the customerId and get the order_nr
            sql = "INSERT INTO `orders`(`customer_id`, `deliver_fee`) VALUES (" + customer.id + "," +
                    "(SELECT deliver_fee.price from deliver_fee " +
                    "INNER JOIN customer ON deliver_fee.zone_nr = customer.zone_nr " +
                    "WHERE customer.customer_id = " + customer.id + "))";
            databaseConnector.insertData(sql);
            sql = "SELECT max(order_nr), deliver_fee from orders WHERE customer_id = " + customer.id + "";
            ResultSet rs = databaseConnector.fetchData(sql);
            while (rs.next()) {
                orderNr = rs.getInt("max(order_nr)");
                order.orderNr = orderNr;
                deliverFee = rs.getDouble("deliver_fee");
                order.deliverFee = deliverFee;

            }
            databaseConnector.closeConnection();
        } catch (SQLException ex) {
            ex.fillInStackTrace();
        }
    }

    public boolean checkIfDiscount(int customerId) {
        int numberOfOrders = 0;
        boolean isThereADiscount = false;
        try {
            databaseConnector.buildConnection();
            sql = "SELECT count(order_nr) FROM orders WHERE customer_id = " + customerId + "";
            ResultSet rs = databaseConnector.fetchData(sql);
            rs.next();
            numberOfOrders = rs.getInt(1);
            databaseConnector.closeConnection();
        } catch (SQLException ex) {
            ex.fillInStackTrace();
        }
        if (numberOfOrders % 10 == 0) {
            isThereADiscount = true;
        }
        return isThereADiscount;
    }

    public void setTotalPrice() {
        databaseConnector.buildConnection();
        sql = "UPDATE orders SET total_price = " + order.totalPrice + " WHERE order_nr = " + order.orderNr + "";
        databaseConnector.updateData(sql);
    }

    public void insertIntoTableOrderDetails() {
        for (Menu menu : order.orderedMenus) {
            databaseConnector.buildConnection();
            sql = "INSERT INTO `order_details`(`order_nr`, `menu_nr`) " +
                    "VALUES (" + order.orderNr + "," + menu.number + ")";
            databaseConnector.insertData(sql);
        }
    }

    public void insertIntoTableChanges() {

        for (Change change : order.changes){
            databaseConnector.buildConnection();
            sql = "INSERT INTO `changes`(`order_nr`, `menu_nr`, `ingredient_nr`, `added`, `removed`, `change_nr`) " +
                    "VALUES ("+ order.orderNr +","+ change.menuNr +","+ change.ingredient.number + "," +
                    change.added +"," + change.removed + "," + change.changeNumber+")";
            databaseConnector.insertData(sql);
        }
    }
    public void setConsumedIngredientsInDatabase() {
        for (Change change : order.changes) {
            databaseConnector.buildConnection();
            if (change.added) {
                sql = "UPDATE ingredient set consumed = (consumed+1) WHERE ingredient.number = " + change.ingredient.number + "";
                databaseConnector.updateData(sql);
            } else if (change.removed) {
                sql = "UPDATE ingredient set consumed = (consumed-1) WHERE ingredient.number = " + change.ingredient.number + "";
                databaseConnector.updateData(sql);
            }
        }
        for (Menu menu : order.orderedMenus) {
            for(Ingredient ingredient : menu.ingredients) {
                databaseConnector.buildConnection();
                sql = "UPDATE ingredient set consumed = (consumed+1) WHERE" +
                        " ingredient.number = " + ingredient.number + "";
                databaseConnector.updateData(sql);
            }
        }
    }


}
