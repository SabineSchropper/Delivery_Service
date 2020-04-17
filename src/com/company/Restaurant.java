package com.company;

import java.sql.*;

public class Restaurant {
    String url = "jdbc:mysql://localhost:3306/gastro?user=root";
    Connection connection;
    Statement statement;
    String sql = "";

    public int addCustomerAndGetId(String name, String location) {
        int id = 0;
        try {
            connection = DriverManager.getConnection(url);
            statement = connection.createStatement();
            sql = "INSERT INTO `customer`(`name`, `location`) VALUES ('"+ name +"',+'"+ location +"')";
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
    public String getCustomerName(int customerId){
        String name = "";
        try {
            connection = DriverManager.getConnection(url);
            statement = connection.createStatement();
            sql = "SELECT name from customer WHERE customer_id = "+ customerId +"";
            ResultSet rs = statement.executeQuery(sql);
            rs.next();
            name = rs.getString("name");
            connection.close();
        }
        catch (SQLException ex){
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
            while ((rs.next())){
                String name = rs.getString("name");
                int  number = rs.getInt("number");
                System.out.println(name + " " +number);
            }
            connection.close();
        }
        catch (SQLException ex){
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
        } catch (SQLException ex) {
            ex.fillInStackTrace();
        }
    }
}
