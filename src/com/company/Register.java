package com.company;

import java.sql.*;

public class Register {
    String url = "jdbc:mysql://localhost:3306/gastro?user=root";
    Connection connection;
    Statement statement;
    String sql = "";
    Restaurant restaurant;

    public Register(Restaurant restaurant){
        this.restaurant = restaurant;
    }

    public void calculateTotalPrice(int orderNr){
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
    public void showBill(int orderNr, double priceForAddedIngredient, int customerId) {
        double discount = 0;
        boolean isThereADiscount = false;
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
            restaurant.showChanges();
            //at the end of Method show Changes connection is closed
            connection = DriverManager.getConnection(url);
            statement = connection.createStatement();
            sql = "SELECT count(changes.added) FROM `changes` " +
                    "WHERE added = 1 AND order_nr = " + orderNr + "";
            rs = statement.executeQuery(sql);
            rs.next();
            addedCount = rs.getInt(1);
            if(addedCount  != 0) {
                System.out.println(addedCount + " * " + priceForAddedIngredient + " = " + (addedCount * priceForAddedIngredient) + " €");
            }
            sql = "SELECT total_price, deliver_fee from orders WHERE order_nr = "+ orderNr +"";
            rs = statement.executeQuery(sql);
            while(rs.next()){
                totalPrice = rs.getDouble("total_price");
                deliverFee = rs.getDouble("deliver_fee");
            }
            isThereADiscount = checkIfDiscount(customerId);
            if(isThereADiscount){
                System.out.println("Rabatt für jede zehnte Bestellung: - 10 Prozent");
                discount = (totalPrice - deliverFee)/10;
                System.out.println("Rabatt:\t\t" + discount +" €");
                totalPrice = totalPrice - discount;
                setTotalPriceIfDiscount(totalPrice,orderNr);
            }
            System.out.println("Zustellgebühr:\t"+deliverFee + " €");
            System.out.println("-------------------------------");
            System.out.println("Gesamtpreis:\t" +totalPrice + " €");
            connection.close();
        } catch (SQLException ex) {
            ex.fillInStackTrace();
        }
    }
    public boolean checkIfDiscount(int customerId){
        int numberOfOrders = 0;
        boolean isThereADiscount = false;
        try {
            connection = DriverManager.getConnection(url);
            statement = connection.createStatement();
            sql = "SELECT count(order_nr) FROM orders WHERE customer_id = "+ customerId +"";
            ResultSet rs = statement.executeQuery(sql);
            rs.next();
            numberOfOrders = rs.getInt(1);
            connection.close();
        } catch (SQLException ex) {
            ex.fillInStackTrace();
        }
        if(numberOfOrders%10 == 0){
            isThereADiscount = true;
        }
        return isThereADiscount;
    }
    public void setTotalPriceIfDiscount(double totalPrice, int orderNr){
        try {
            connection = DriverManager.getConnection(url);
            statement = connection.createStatement();
            sql = "UPDATE orders SET total_price = " + totalPrice + " WHERE order_nr = "+ orderNr +"";
            statement.executeUpdate(sql);
            connection.close();
        } catch (SQLException ex) {
            ex.fillInStackTrace();
        }
    }
}

