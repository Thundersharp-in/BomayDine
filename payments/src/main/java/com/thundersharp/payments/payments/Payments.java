package com.thundersharp.payments.payments;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.razorpay.Checkout;
import com.razorpay.CheckoutActivity;
import com.razorpay.PaymentData;
import com.razorpay.PaymentResultListener;
import com.razorpay.PaymentResultWithDataListener;
import com.thundersharp.payments.R;

import org.json.JSONObject;

public class Payments {

    private static Activity context;
    private Checkout checkout;
    private PaymentObserver paymentObserver;
    private static Payments payments;
    private String orderid;

    private Payments(Activity contextMain) {
        context = contextMain;
        Checkout.preload(contextMain);
        //checkout.setKeyID("rzp_test_DZNbcClq06ahsT");
    }

    public static Payments initialize(Activity context) {
         payments = new Payments(context);
         return payments;
    }

    public Payments startPayment(String description, double amount,String customerEmail,String customerPhone){
        pay(description, amount, customerEmail, customerPhone);
        return payments;
    }

    public Payments startPayment(String description,String orderid, double amount,String customerEmail,String customerPhone){
        pay(description, amount, customerEmail, customerPhone);
        this.orderid = orderid;
        return payments;
    }

    public void attachObserver(PaymentResultListener paymentObserver){

    }

    private void pay(String description, double amount,String customerEmail,String customerPhone) {

        Checkout checkout = new Checkout();
        checkout.setKeyID("rzp_test_qhH5vsTc8VCjpr");
        /**
         * Instantiate Checkout
         */


        /**
         * Set your logo here
         */
        checkout.setImage(R.drawable.ic_launcher);

        /**
         * Reference to current activity
         */
        final Activity activity = context;

        /**



         * Pass your payment options to the Razorpay Checkout as a JSONObject
         */
        try {
            JSONObject options = new JSONObject();

            options.put("name","Bombay Dine Restaurant");
            options.put("description", description);
            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png");
            //if (orderid != null)
            //options.put("order_id", "order_"+orderid);//from response of step 3.

            options.put("theme.color", "#3399cc");
            options.put("currency", "INR");
            options.put("amount", String.valueOf((amount * 100))); //pass amount in currency subunits
            options.put("prefill.email", customerEmail);
            options.put("prefill.contact", customerPhone);
            options.put("send_sms_hash", true);
            JSONObject retryObj = new JSONObject();
            retryObj.put("enabled", true);
            retryObj.put("max_count", 2);
            options.put("retry", retryObj);

            checkout.open(activity, options);

        } catch (Exception e) {
            if (paymentObserver != null){
                paymentObserver.OnPaymentFailed(0,e.getMessage(),null);
            }
            Log.e("TAG", "Error in starting Razorpay Checkout", e);
        }
    }


/*    @Override
    public void onPaymentSuccess(String s) {
        if (paymentObserver != null){
            paymentObserver.OnPaymentSuccess(s, null);
        }
    }

    @Override
    public void onPaymentError(int i, String s) {
        if (paymentObserver != null){
            paymentObserver.OnPaymentFailed(i, s, null);
        }
    }*/
}

