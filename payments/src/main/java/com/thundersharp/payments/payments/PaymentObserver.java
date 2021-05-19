package com.thundersharp.payments.payments;

import com.razorpay.PaymentData;

public interface PaymentObserver {

    void OnPaymentSuccess(String s, PaymentData paymentData);

    void OnPaymentFailed(int i, String s, PaymentData paymentData);

}
