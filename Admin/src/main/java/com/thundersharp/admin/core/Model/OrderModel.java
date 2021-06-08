package com.thundersharp.admin.core.Model;

public class OrderModel {

    private double amount;
    private int food_TYPE;
    private String name;
    private int quantity;

    public OrderModel() {
    }

    public OrderModel(double amount, int food_TYPE, String name, int quantity) {
        this.amount = amount;
        this.food_TYPE = food_TYPE;
        this.name = name;
        this.quantity = quantity;
    }

    public double getAmount() {
        return amount;
    }

    public int getFood_TYPE() {
        return food_TYPE;
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }
}
