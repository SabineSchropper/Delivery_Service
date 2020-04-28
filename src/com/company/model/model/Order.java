package com.company.model.model;

import com.company.model.model.Change;
import com.company.model.model.Menu;

import java.util.ArrayList;

public class Order {
    public Customer customer;
    public int orderNr;
    public double totalPrice;
    public double deliverFee;
    public ArrayList <Menu> orderedMenus = new ArrayList<>();
    public ArrayList <Change> changes = new ArrayList<>();

    public Order(Customer customer){
        this.customer = customer;
    }
}
