package com.company;

import java.sql.*;

public class Restaurant {
    String url = "jdbc:mysql://localhost:3306/gastro?user=root";
    Connection connection;
    Statement statement;
    String sql = "";
    boolean isItFirstOrder = true;
    int orderNr = 0;
    double priceForAddedIngredient = 0.9;

    public int addCustomerAndGetId(String name, String location) {
        int id = 0;
        try {
            connection = DriverManager.getConnection(url);
            statement = connection.createStatement();
            sql = "INSERT INTO `customer`(`name`, `location`, `zone_nr`) VALUES " +
                    "('" + name + "','" + location + "', (SELECT deliver_zone FROM location WHERE name = '"+location+"'))";
            statement.executeUpdate(sql);
            sql = "SELECT max(customer_id) from customer";
            ResultSet rs = statement.executeQuery(sql);
            rs.next();
            id = rs.getInt(1);
            connection.close();
        } catch (SQLException ex) {
            ex.fillInStackTrace();
        }
        return id;
    }

    public String getCustomerName(int customerId) {
        String name = "";
        try {
            connection = DriverManager.getConnection(url);
            statement = connection.createStatement();
            sql = "SELECT name from customer WHERE customer_id = " + customerId + "";
            ResultSet rs = statement.executeQuery(sql);
            rs.next();
            name = rs.getString("name");
            connection.close();
        } catch (SQLException ex) {
            ex.fillInStackTrace();
        }
        return name;
    }

    public void showMenuCard() {
        try {
            connection = DriverManager.getConnection(url);
            Statement statement = connection.createStatement();
            sql = "SELECT * FROM menu";
            ResultSet rs = statement.executeQuery(sql);
            System.out.println("\nDiese Gerichte stehen zur Auswahl:\n");
            while ((rs.next())) {
                String name = rs.getString("name");
                int number = rs.getInt("number");
                double price = rs.getDouble("price");
                System.out.println("Nr. " + number + " " + name + ", " + price + " €");
            }
            connection.close();
        } catch (SQLException ex) {
            ex.fillInStackTrace();
        }
    }

    public void showIngredients(int menuNumber) {
        int ingredientNumber = 0;
        String ingredient = "";
        try {
            connection = DriverManager.getConnection(url);
            statement = connection.createStatement();
            sql = "SELECT ingredient.name, ingredient.number from menu_ingredient " +
                    "INNER JOIN ingredient ON menu_ingredient.ingredient_number = ingredient.number " +
                    "WHERE menu_number = " + menuNumber + "";
            ResultSet rs = statement.executeQuery(sql);
            while (rs.next()) {
                ingredientNumber = rs.getInt("number");
                ingredient = rs.getString("name");
                System.out.println("Zutat: " + ingredient + " Nummer: " + ingredientNumber);
            }
            connection.close();
        } catch (SQLException ex) {
            ex.fillInStackTrace();
        }
    }

    public String getMenuName(int menuNumber) {
        String menuName = "";
        try {
            connection = DriverManager.getConnection(url);
            statement = connection.createStatement();
            sql = "SELECT * FROM menu WHERE number = " + menuNumber + "";
            ResultSet rs = statement.executeQuery(sql);
            rs.next();
            menuName = rs.getString("name");
            connection.close();
        } catch (SQLException ex) {
            ex.fillInStackTrace();
        }
        return menuName;
    }

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
                connection.close();
            } catch (SQLException ex) {
                ex.fillInStackTrace();
            }
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
    public void calculateTotalPrice(){
        try {
            connection = DriverManager.getConnection(url);
            statement = connection.createStatement();
            sql = "UPDATE orders SET total_price =((SELECT sum(order_details.price) from order_details " +
                    "WHERE order_nr = "+ orderNr +") + (SELECT orders.deliver_fee from orders " +
                    "WHERE order_nr = "+ orderNr +")) WHERE order_nr = " + orderNr +"";
            statement.executeUpdate(sql);
            connection.close();
        } catch (SQLException ex) {
            ex.fillInStackTrace();
        }
    }
    public void showBill() {
        String menuName = "";
        double menuPrice = 0;
        double totalPrice = 0;
        double deliverFee = 0;
        int addedCount = 0;
        try {
            connection = DriverManager.getConnection(url);
            statement = connection.createStatement();
            sql = "SELECT menu.name, menu.price " +
                    "FROM order_details " +
                    "INNER JOIN menu ON order_details.menu_nr = menu.number " +
                    "WHERE order_nr = " + orderNr + "";
            ResultSet rs = statement.executeQuery(sql);
            System.out.println("Ihre Bestellung:");
            while (rs.next()) {
                menuName = rs.getString("name");
                menuPrice = rs.getDouble("price");
                System.out.println(menuName + " " + menuPrice + " €");
            }
            showChanges();
            //at the end of Method show Changes connection is closed
            connection = DriverManager.getConnection(url);
            statement = connection.createStatement();
            sql = "SELECT count(changes.added) FROM `changes` " +
                    "WHERE added = 1 AND order_nr = " + orderNr + "";
            rs = statement.executeQuery(sql);
            rs.next();
            addedCount = rs.getInt(1);
            System.out.println(addedCount +" * "+ priceForAddedIngredient + " = " + (addedCount*priceForAddedIngredient)+" €");
            sql = "SELECT total_price, deliver_fee from orders WHERE order_nr = "+ orderNr +"";
            rs = statement.executeQuery(sql);
            while(rs.next()){
                totalPrice = rs.getDouble("total_price");
                deliverFee = rs.getDouble("deliver_fee");
            }
            System.out.println("Zustellgebühr:\t"+deliverFee + " €");
            System.out.println("-------------------------------");
            System.out.println("Gesamtpreis:\t" +totalPrice + " €");
            connection.close();
        } catch (SQLException ex) {
            ex.fillInStackTrace();
        }
    }
    public void createOrder(int customerId) {
        try {
            connection = DriverManager.getConnection(url);
            statement = connection.createStatement();
            //insert the customerId and get the order_nr
            sql = "INSERT INTO `orders`(`customer_id`, `deliver_fee`) VALUES (" + customerId + "," +
                    "(SELECT deliver_fee.price from deliver_fee " +
                    "INNER JOIN customer ON deliver_fee.zone_nr = customer.zone_nr " +
                    "WHERE customer.customer_id = "+ customerId +"))";
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
