package com.thundersharp.bombaydine.user.core.Model;

public class PaymentsRequestOptions {

    public static class Builder {

        private PaymentsRequestOptions requestOptions;

        public Builder(PaymentsRequestOptions requestOptions) {
            this.requestOptions = requestOptions;
        }

        public PaymentsRequestOptions getRequestOptions() {
            return requestOptions;
        }
    }

    private String merchantTittle;
    private String CustomerEmail;
    private String CustomerPhone;
    private double transactionAmount;


    /**
     * @see PaymentsRequestOptions
     * @return PaymentRequestOptions
     */
    public static PaymentsRequestOptions initlizeBuilder(){
        return new PaymentsRequestOptions();
    }

    public String getMerchantTittle() {
        return merchantTittle;
    }

    public PaymentsRequestOptions setMerchantTittle(String merchantTittle) {
        this.merchantTittle = merchantTittle;
        return this;
    }

    public String getCustomerEmail() {
        return CustomerEmail;
    }

    public PaymentsRequestOptions setCustomerEmail(String customerEmail) {
        CustomerEmail = customerEmail;
        return this;
    }

    public String getCustomerPhone() {
        return CustomerPhone;
    }

    public PaymentsRequestOptions setCustomerPhone(String customerPhone) {
        CustomerPhone = customerPhone;
        return this;
    }

    public double getTransactionAmount() {
        return transactionAmount;
    }

    public PaymentsRequestOptions setTransactionAmount(double transactionAmount) {
        this.transactionAmount = transactionAmount;
        return this;
    }

    public Builder build(){
        return new Builder(this);
    }
}

