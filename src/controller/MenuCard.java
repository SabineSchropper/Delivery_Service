package controller;

import databaseConnector.DatabaseConnector;
import models.Ingredient;
import models.Menu;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class MenuCard {
    String url = "jdbc:mysql://localhost:3306/gastro?user=root";
    DatabaseConnector databaseConnector = new DatabaseConnector(url);
    ArrayList<Menu> menus = new ArrayList<>();
    String sql = "";
    Menu menu;

    public void fetchMenus() {
        databaseConnector.buildConnection();
        sql = "SELECT * FROM menu";
        ResultSet rs = databaseConnector.fetchData(sql);
        try {
            while ((rs.next())) {
                String name = rs.getString("name");
                int number = rs.getInt("number");
                double price = rs.getDouble("price");
                menu = new Menu(name,number,price);
                menus.add(menu);
            }
            databaseConnector.closeConnection();
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println("Could not get menus");
        }

    }
    public void showMenuCard(){
        fetchMenus();
        fetchIngredients();
        for(Menu menu : menus){
            System.out.println("Nr. " + menu.number + " " + menu.name + ", " + menu.price + " €");
        }
    }
    public void searchMenuInArrayList(int menuNumber){
        for(Menu menu : menus){
            if(menu.number == menuNumber){
                this.menu = menu;
            }
        }
    }
    public void fetchIngredients(){
        //fetch ingredients and assign it to the right menu
        for(Menu menu : menus) {
            int number = 0;
            String name = "";
            try {
                databaseConnector.buildConnection();
                sql = "SELECT ingredient.name, ingredient.number, ingredient.consumed from menu_ingredient " +
                        "INNER JOIN ingredient ON menu_ingredient.ingredient_number = ingredient.number " +
                        "WHERE menu_number = " + menu.number + "";
                ResultSet rs = databaseConnector.fetchData(sql);
                while (rs.next()) {
                    number = rs.getInt("number");
                    name = rs.getString("name");
                    Ingredient ingredient = new Ingredient(name, number);
                    menu.ingredients.add(ingredient);
                }
                databaseConnector.closeConnection();
            } catch (SQLException ex) {
                ex.fillInStackTrace();
            }
        }
    }
    public void showIngredients(int menuNumber){
        searchMenuInArrayList(menuNumber);
        System.out.println(menu.name +" :");
        for(Ingredient ingredient:menu.ingredients){
            System.out.println("Zutat: " + ingredient.name + " Nummer: " + ingredient.number);
        }
    }
    public String getMenuName() {
        return menu.name;
    }
}
