package com.company.model.model;

public class Customer {
    public int id;
    public String name;
    public String location;
    public int zoneNr;

    public Customer(String name, String location){
        this.name = name; this.location = location;
    }
}
