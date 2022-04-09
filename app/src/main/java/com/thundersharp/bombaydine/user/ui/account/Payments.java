package com.thundersharp.bombaydine.user.ui.account;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.razorpay.PaymentResultListener;
import com.thundersharp.bombaydine.R;
import com.thundersharp.bombaydine.user.core.Model.PaymentsRequestOptions;
import com.thundersharp.bombaydine.user.ui.menu.AllItemsActivity;
import com.thundersharp.conversation.ChatStarter;
import com.thundersharp.payments.payments.PaymentObserver;

public class Payments extends AppCompatActivity implements PaymentResultListener {

    private Context context;
    private static PaymentsRequestOptions.Builder requestOptions;
    public static PaymentObserver paymentObserver;

    public static void initialize(Context context, PaymentsRequestOptions.Builder requestOptions, @Nullable PaymentObserver paymentObserver){

        if (requestOptions != null) {
            Payments.paymentObserver = paymentObserver;
            context.startActivity(new Intent(context, Payments.class));
            Payments.requestOptions = requestOptions;

        }else Toast.makeText(context, "Request Options is null.", Toast.LENGTH_SHORT).show();

        ChatStarter.initializeChat(context).setChatType(ChatStarter.MODE_CHAT_FROM_ORDERS);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payments);

        com.thundersharp.payments.payments.Payments
                .initialize(Payments.this)
                .startPayment("View payments", 1, FirebaseAuth.getInstance().getCurrentUser().getEmail(), FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber());
    }

    @Override
    public void onPaymentSuccess(String s) {
        if (paymentObserver != null) paymentObserver.OnPaymentSuccess(s,null);
        finish();
    }

    @Override
    public void onPaymentError(int i, String s) {
        if (paymentObserver != null) paymentObserver.OnPaymentFailed(i,s,null);
        finish();

    }
}