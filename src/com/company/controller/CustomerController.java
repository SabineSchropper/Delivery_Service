package com.company.controller;

import com.company.model.model.Customer;
import com.company.model.repository.CustomerRepository;
import com.company.view.CustomerView;

public class CustomerController {
    CustomerRepository customerRepository = new CustomerRepository();
    CustomerView customerView = new CustomerView();

    public String start() {
        String choice = customerView.startRegistration();
        return choice;
    }
    public Customer createAccount(){
        String name = customerView.askName();
        String location = customerView.askLocation();
        Customer customer = customerRepository.addCustomerAndGetId(name,location);
        return customer;
    }
    public Customer checkCustomerAndGetName(){
        int customerId = customerView.askForCustomerId();
        Customer customer = customerRepository.checkCustomerAndGetName(customerId);
        return customer;
    }
}
