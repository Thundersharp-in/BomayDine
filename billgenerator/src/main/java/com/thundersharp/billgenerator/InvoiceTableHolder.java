package com.thundersharp.billgenerator;

public class InvoiceTableHolder {

    public InvoiceTableHolder(){
    }

    int qty;
    double unitprice;
    String name;

    public InvoiceTableHolder(int qty, double unitprice, String name) {
        this.qty = qty;
        this.unitprice = unitprice;
        this.name = name;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public double getUnitprice() {
        return unitprice;
    }

    public void setUnitprice(double unitprice) {
        this.unitprice = unitprice;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
