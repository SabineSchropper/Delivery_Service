package controller;

import databaseConnector.DatabaseConnector;
import models.Customer;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class CustomerFile {
    String url = "jdbc:mysql://localhost:3306/gastro?user=root";
    DatabaseConnector dbConnector = new DatabaseConnector(url);
    String sql = "";
    ArrayList<Customer> customers = new ArrayList <>();
    Customer customer;

    public void fetchCustomer() {
        String name = "";
        String location = "";
        try {
            sql = "SELECT * from customer";
            ResultSet rs = dbConnector.fetchData(sql);
            while(rs.next()) {
                name = rs.getString("name");
                location = rs.getString("location");
                customer = new Customer(name,location);
                customer.id = rs.getInt("customer_id");
                customer.zoneNr = rs.getInt("zone_nr");
                customers.add(customer);
            }
            dbConnector.closeConnection();
        } catch (SQLException ex) {
            ex.fillInStackTrace();
            System.out.println("Could not get customers");
        }
    }

    public int addCustomerAndGetId(String name, String location) {
        Customer customer = new Customer(name,location);
        this.customer = customer;
        int id = 0;
        try {
            sql = "INSERT INTO `customer`(`name`, `location`, `zone_nr`) VALUES " +
                    "('" + name + "','" + location + "', (SELECT deliver_zone FROM location WHERE name = '"+location+"'))";
            dbConnector.insertData(sql);
            sql = "SELECT max(customer_id) from customer";
            ResultSet rs = dbConnector.fetchData(sql);
            if(rs != null) {
                rs.next();
                id = rs.getInt(1);
                customer.id = id;
            }
            else{
                System.out.println("Could not get customer Id");
            }
        } catch (SQLException ex) {
            ex.fillInStackTrace();
        }
        finally {
            dbConnector.closeConnection();
        }
        return id;
    }
    public String checkCustomerAndGetName(int id){
        //get all existing customers from database
        fetchCustomer();
        //search for the right customer from ArrayList
        for (Customer customer:customers){
            if(customer.id == id){
                this.customer.name = customer.name;
                this.customer.location = customer.location;
                this.customer.id = customer.id;
                this.customer.zoneNr = customer.zoneNr;
            }
        }
        return customer.name;
    }
}
