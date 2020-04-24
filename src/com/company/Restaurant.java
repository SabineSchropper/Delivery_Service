package com.company;

import databaseConnector.DatabaseConnector;

import java.sql.*;

public class Restaurant {
    String url = "jdbc:mysql://localhost:3306/gastro?user=root";
    DatabaseConnector dbConnector = new DatabaseConnector(url);
    Connection connection;
    Statement statement;
    String sql = "";
    boolean isItFirstOrder = true;
    int orderNr = 0;
    double priceForAddedIngredient = 0.9;
    Register register = new Register(this);

    public void addToOrder(int customerId, boolean isThereAChange, int menuNumber) {

        if (isItFirstOrder) {
            createOrder(customerId);
            isItFirstOrder = false;
        }
        if(!isThereAChange){
            try {
                connection = DriverManager.getConnection(url);
                statement = connection.createStatement();
                sql = "INSERT INTO `order_details`(`order_nr`, `menu_nr`, `price`) VALUES " +
                        "(" + orderNr + "," + menuNumber + ",(SELECT price from menu WHERE number = " + menuNumber + "))";
                statement.executeUpdate(sql);
                addToConsumedIngredients(menuNumber);
                connection.close();
            } catch (SQLException ex) {
                ex.fillInStackTrace();
            }
        }
        if(isThereAChange){
            double price = 0;
            int numberOfAddedIngredients = 0;
            double newPrice = 0;
            try {
                connection = DriverManager.getConnection(url);
                statement = connection.createStatement();
                sql = "SELECT price from menu WHERE number = " + menuNumber +"";
                ResultSet rs = statement.executeQuery(sql);
                rs.next();
                price = rs.getDouble(1);
                sql = "SELECT count(order_nr) FROM `changes` WHERE order_nr = " + orderNr + " AND " +
                        "menu_nr = " + menuNumber + " AND added = 1";
                rs = statement.executeQuery(sql);
                rs.next();
                numberOfAddedIngredients = rs.getInt(1);
                newPrice = price + (numberOfAddedIngredients * priceForAddedIngredient);
                sql = "INSERT INTO `order_details`(`order_nr`, `menu_nr`, `price`) VALUES " +
                        "(" + orderNr + "," + menuNumber + "," + newPrice + ")";
                statement.executeUpdate(sql);
                addToConsumedIngredients(menuNumber);
                connection.close();
            } catch (SQLException ex) {
                ex.fillInStackTrace();
            }
        }
    }
    public void addToConsumedIngredients(int menuNumber){
        //here I insert the amount of ingredients of a menu into table ingredient
        int[] ingredientNumbers = new int[15];
        int i = 0;
        try {
            sql = "SELECT ingredient_number FROM menu_ingredient WHERE menu_number = " + menuNumber + "";
            ResultSet rs = statement.executeQuery(sql);
            while (rs.next()) {
                ingredientNumbers[i] = rs.getInt("ingredient_number");
                i++;
            }
            for(int j = 0; j < ingredientNumbers.length; j++) {
                if(ingredientNumbers[j] != 0) {
                    sql = "UPDATE ingredient set consumed = (consumed+1) WHERE ingredient.number = " + ingredientNumbers[j] + "";
                    statement.executeUpdate(sql);
                }
                else{
                    break;
                }
            }
        }
        catch (SQLException ex){
            ex.printStackTrace();
        }
    }
    public void addToChanges(int ingredientNumber, int menuNumber, int customerId, boolean getsRemoved, boolean getsAdded) {
        if (isItFirstOrder) {
            createOrder(customerId);
            isItFirstOrder = false;
        }
        try {
            connection = DriverManager.getConnection(url);
            statement = connection.createStatement();
            sql = "INSERT INTO `changes`(`order_nr`, `menu_nr`, `ingredient_nr`, `added`, `removed`) VALUES " +
                    "(" + orderNr + "," + menuNumber + "," + ingredientNumber + "," + getsAdded + "," + getsRemoved + ")";
            statement.executeUpdate(sql);
            //here I add or remove the changed ingredient from table ingredient
            if(getsAdded) {
                sql = "UPDATE ingredient set consumed = (consumed+1) WHERE ingredient.number = " + ingredientNumber + "";
                statement.executeUpdate(sql);
            }
            else if(getsRemoved){
                sql = "UPDATE ingredient set consumed = (consumed-1) WHERE ingredient.number = " + ingredientNumber + "";
                statement.executeUpdate(sql);
            }
            connection.close();
        } catch (SQLException ex) {
            ex.fillInStackTrace();
        }
    }
    public void showChanges() {
        String ingredientName = "";
        try {
            connection = DriverManager.getConnection(url);
            statement = connection.createStatement();
            sql = "SELECT ingredient.name FROM changes " +
                    "INNER JOIN ingredient ON changes.ingredient_nr = ingredient.number " +
                    "WHERE added = 1 AND order_nr = " + orderNr + "";
            ResultSet rs = statement.executeQuery(sql);
            while (rs.next()) {
                ingredientName = rs.getString("name");
                System.out.println("Hinzugefügt: " + ingredientName);
            }
            sql = "SELECT ingredient.name FROM changes " +
                    "INNER JOIN ingredient ON changes.ingredient_nr = ingredient.number " +
                    "WHERE removed = 1 AND order_nr = " + orderNr + "";
            rs = statement.executeQuery(sql);
            while (rs.next()) {
                ingredientName = rs.getString("name");
                System.out.println("Entfernt: " + ingredientName);
            }
            connection.close();
        } catch (SQLException ex) {
            ex.fillInStackTrace();
        }
    }
    public void deleteThisChanges(int menuNumber) {
        try {
            connection = DriverManager.getConnection(url);
            statement = connection.createStatement();
            sql = "DELETE FROM `changes` WHERE menu_nr = "+ menuNumber +" AND order_nr = "+orderNr+"";
            statement.executeUpdate(sql);
            connection.close();
        } catch (SQLException ex) {
            ex.fillInStackTrace();
        }
    }
    public void showAllIngredients(){
        String ingredientName = "";
        int ingredientNumber = 0;
        try {
            connection = DriverManager.getConnection(url);
            statement = connection.createStatement();
            sql = "SELECT * FROM ingredient";
            ResultSet rs = statement.executeQuery(sql);
            while (rs.next()){
                ingredientName = rs.getString("name");
                ingredientNumber = rs.getInt("number");
                System.out.println(ingredientName + " Nr. "+ingredientNumber);
            }
            connection.close();
        } catch (SQLException ex) {
            ex.fillInStackTrace();
        }
    }
    public void calculateTotalPrice() {
        register.calculateTotalPrice(orderNr);
    }
    public void showBill(int customerId) {
        register.showBill(orderNr, priceForAddedIngredient, customerId);
    }
    public void createOrder(int customerId) {
        try {
            connection = DriverManager.getConnection(url);
            statement = connection.createStatement();
            //insert the customerId and get the order_nr
            sql = "INSERT INTO `orders`(`customer_id`, `deliver_fee`) VALUES (" + customerId + "," +
                    "(SELECT deliver_fee.price from deliver_fee " +
                    "INNER JOIN customer ON deliver_fee.zone_nr = customer.zone_nr " +
                    "WHERE customer.customer_id = " + customerId + "))";
            statement.executeUpdate(sql);
            sql = "SELECT max(order_nr) from orders";
            ResultSet rs = statement.executeQuery(sql);
            rs.next();
            orderNr = rs.getInt(1);
            connection.close();
        } catch (SQLException ex) {
            ex.fillInStackTrace();
        }
    }
}
