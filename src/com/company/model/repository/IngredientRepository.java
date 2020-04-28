package com.company.model.repository;

import com.company.model.DatabaseConnector;
import com.company.model.model.Ingredient;
import com.company.model.model.Menu;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class IngredientRepository {
    String url = "jdbc:mysql://localhost:3306/gastro?user=root";
    DatabaseConnector databaseConnector = new DatabaseConnector(url);
    ArrayList<Ingredient> allIngredients = new ArrayList<>();
    String sql;

    public IngredientRepository(){
        //with creating an Objekt from IngredientController we fetch the Ingredients from Database and get ArrayList
        fetchAllIngredients();
    }

    public void fetchAllIngredients() {
        int number = 0;
        String name = "";
        int consumed = 0;
        double priceIfAdded = 0;
        try {
            databaseConnector.buildConnection();
            sql = "SELECT * from ingredient";
            ResultSet rs = databaseConnector.fetchData(sql);
            while (rs.next()) {
                number = rs.getInt("number");
                name = rs.getString("name");
                consumed = rs.getInt("consumed");
                priceIfAdded = rs.getDouble("price_if_added");
                Ingredient ingredient = new Ingredient(name, number);
                ingredient.setConsumed(consumed);
                ingredient.setPriceIfAdded(priceIfAdded);
                allIngredients.add(ingredient);
            }
            databaseConnector.closeConnection();
        } catch (SQLException ex) {
            ex.fillInStackTrace();
        }

    }
    public ArrayList<Ingredient> getAllIngredients() {
        return allIngredients;
    }
}
