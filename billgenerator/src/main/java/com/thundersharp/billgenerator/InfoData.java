package com.thundersharp.billgenerator;

public class InfoData {

    public static InfoData setData(int logo, String clientName, String clientPhone, String clientAddress, String orderId, String terms, String promoName, double discAmt){
        return new InfoData(logo,clientName,clientPhone,clientAddress,orderId,terms,promoName,discAmt);
    }

    public InfoData(){}

    private int logo;
    private String clientName,clientPhone,clientAddress,orderId,terms;
    private String promoName;
    double discAmt;

    public InfoData(int logo, String clientName, String clientPhone, String clientAddress, String orderId, String terms, String promoName, double discAmt) {
        this.logo = logo;
        this.clientName = clientName;
        this.clientPhone = clientPhone;
        this.clientAddress = clientAddress;
        this.orderId = orderId;
        this.terms = terms;

        this.promoName = promoName;
        this.discAmt = discAmt;
    }

    public int getLogo() {
        return logo;
    }

    public String getClientName() {
        return clientName;
    }

    public String getClientPhone() {
        return clientPhone;
    }

    public String getClientAddress() {
        return clientAddress;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getTerms() {
        return terms;
    }

    public String getPromoName() {
        return promoName;
    }

    public double getDiscAmt() {
        return discAmt;
    }
}
