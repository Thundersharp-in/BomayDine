package com.thundersharp.admin.core.Model;

import java.io.Serializable;

public class OrederBasicDetails implements Serializable {

    public OrederBasicDetails(){}

    private String delivery_address;
    private String deliveryCoOrdinates;
    private String promocodeNameNdiscount;
    private String itemsMain;
    private String delivery_charge;
    private String totalamt;
    private String deliveryNameData;
    private String orderID;
    private String uid;
    private String status;
    private String paymentid;

    public String getPaymentid() {
        return paymentid;
    }

    public void setPaymentid(String paymentid) {
        this.paymentid = paymentid;
    }

    public OrederBasicDetails(String delivery_address, String deliveryCoOrdinates, String promocodeNameNdiscount, String itemsMain, String delivery_charge, String totalamt, String deliveryNameData, String orderID, String paymentid) {
        this.delivery_address = delivery_address;
        this.deliveryCoOrdinates = deliveryCoOrdinates;
        this.promocodeNameNdiscount = promocodeNameNdiscount;
        this.itemsMain = itemsMain;
        this.delivery_charge = delivery_charge;
        this.totalamt = totalamt;
        this.deliveryNameData = deliveryNameData;
        this.orderID = orderID;
        this.paymentid =paymentid;

    }

    public OrederBasicDetails(String delivery_address, String deliveryCoOrdinates, String promocodeNameNdiscount, String itemsMain, String delivery_charge, String totalamt, String deliveryNameData, String orderID, String uid, String status, String paymentid) {
        this.delivery_address = delivery_address;
        this.deliveryCoOrdinates = deliveryCoOrdinates;
        this.promocodeNameNdiscount = promocodeNameNdiscount;
        this.itemsMain = itemsMain;
        this.delivery_charge = delivery_charge;
        this.totalamt = totalamt;
        this.deliveryNameData = deliveryNameData;
        this.orderID = orderID;
        this.uid = uid;
        this.status = status;
        this.paymentid = paymentid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDelivery_address() {
        return delivery_address;
    }

    public void setDelivery_address(String delivery_address) {
        this.delivery_address = delivery_address;
    }

    public String getDeliveryCoOrdinates() {
        return deliveryCoOrdinates;
    }

    public void setDeliveryCoOrdinates(String deliveryCoOrdinates) {
        this.deliveryCoOrdinates = deliveryCoOrdinates;
    }

    public String getPromocodeNameNdiscount() {
        return promocodeNameNdiscount;
    }

    public void setPromocodeNameNdiscount(String promocodeNameNdiscount) {
        this.promocodeNameNdiscount = promocodeNameNdiscount;
    }

    public String getItemsMain() {
        return itemsMain;
    }

    public void setItemsMain(String itemsMain) {
        this.itemsMain = itemsMain;
    }

    public String getDelivery_charge() {
        return delivery_charge;
    }

    public void setDelivery_charge(String delivery_charge) {
        this.delivery_charge = delivery_charge;
    }

    public String getTotalamt() {
        return totalamt;
    }

    public void setTotalamt(String totalamt) {
        this.totalamt = totalamt;
    }

    public String getDeliveryNameData() {
        return deliveryNameData;
    }

    public void setDeliveryNameData(String deliveryNameData) {
        this.deliveryNameData = deliveryNameData;
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
