package com.example.adminshop;

public class Order {
    private String id;
     private String phoneNumber;
     private Float cost;
    private String date;

    public Order() {
        // Default constructor required for calls to DataSnapshot.getValue(Order.class)
    }

    public Order(String phoneNumber) {
         this.phoneNumber=phoneNumber;
    }

    public Order(String phoneNumber, Float cost, String date) {
        this.phoneNumber = phoneNumber;
        this.cost = cost;
        this.date = date;
    }


    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public Float getCost() {
        return cost;
    }

    public void setCost(Float cost) {
        this.cost = cost;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

}