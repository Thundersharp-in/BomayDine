package com.thundersharp.bombaydine.user.core.Adapters;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.thundersharp.bombaydine.R;
import com.thundersharp.bombaydine.user.core.Model.CartItemModel;
import com.thundersharp.bombaydine.user.core.Model.OfferModel;
import com.thundersharp.bombaydine.user.core.Model.OrederBasicDetails;
import com.thundersharp.bombaydine.user.core.offer.OfferCode;
import com.thundersharp.bombaydine.user.core.payments.PrePayment;
import com.thundersharp.bombaydine.user.core.payments.parePayListener;
import com.thundersharp.bombaydine.user.core.utils.Resturant;
import com.thundersharp.bombaydine.user.ui.login.LoginActivity;
import com.thundersharp.bombaydine.user.ui.menu.AllItemsActivity;
import com.thundersharp.bombaydine.user.ui.offers.AllOffersActivity;
import com.thundersharp.bombaydine.user.ui.orders.ConfirmPhoneName;
import com.thundersharp.payments.payments.Payments;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AllOfferAdapters extends RecyclerView.Adapter<AllOfferAdapters.View> {

    private static int adapterType= 0;

    public static AllOfferAdapters getInstance(Context context, List<Object> data,int AdapterType){
        adapterType=AdapterType;
        return new AllOfferAdapters(context,data);
    }
    public static AllOfferAdapters getInstance(Context context, List<Object> data,int AdapterType, OfferCode offerCode){
        adapterType=AdapterType;
        return new AllOfferAdapters(context,data, offerCode);
    }


    private AllOfferAdapters(Context context, List<Object> objects) {
        this.context = context;
        this.objects = objects;
    }

    private AllOfferAdapters(Context context, List<Object> objects, OfferCode offerCode) {
        this.context = context;
        this.objects = objects;
        this.offerCode = offerCode;
    }

    private final Context context;
    private final List<Object> objects;
    private OfferCode offerCode;

    @NonNull
    @Override
    public View onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (adapterType == 0)
            return new View(LayoutInflater.from(context).inflate(R.layout.item_offer,parent,false));
        else return new View(LayoutInflater.from(context).inflate(R.layout.item_offer_all,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull View holder, int position) {
        OfferModel offerModel = (OfferModel) objects.get(position);
        if (adapterType == 0){

            holder.offer_code.setText("USE CODE "+offerModel.getCODE());
            holder.offer_desc.setText(offerModel.getDESC());
            holder.itemView.setOnClickListener(w->ShowOfferCart(offerModel));

        }else {

            holder.offer_text_amt.setOnClickListener(view->{
                ShowOfferCart(offerModel);
            });
            holder.offer_apply.setOnClickListener(view ->{
                offerCode.getOfferCode(offerModel.getCODE()+"#"+offerModel.getPERCENT()+"$"+offerModel.getUPTO());
            });
            holder.offer_desc.setText(offerModel.getDESC());
            holder.offer_text_amt.setText("Get instant discount of "+offerModel.getPERCENT() +"% Off upto Rs. "+offerModel.getUPTO()+" on your delisious orders."+ context.getString(com.thundersharp.admin.R.string.offer_view_detail));
            holder.offer_code.setText(offerModel.getCODE());


        }

    }

    private void ShowOfferCart(OfferModel offerModel){

        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);
        bottomSheetDialog.setContentView(R.layout.item_offer_desc);

        TextView offer_name = bottomSheetDialog.findViewById(R.id.offer_name);
        TextView offer_desc = bottomSheetDialog.findViewById(R.id.offer_desc);
        TextView amount = bottomSheetDialog.findViewById(R.id.amount);
        TextView offer_code = bottomSheetDialog.findViewById(R.id.offer_code);
        TextView copy = bottomSheetDialog.findViewById(R.id.copy);
        RecyclerView tandc = bottomSheetDialog.findViewById(R.id.tandc);

        String[] list_items = offerModel.getTNC().split("\\.");
        List<String> list = new ArrayList<>(Arrays.asList(list_items));

        offer_name.setText("BOMBAY DINE");
        offer_desc.setText(offerModel.getDESC());
        amount.setText("Get "+offerModel.getPERCENT()+"% off upto Rs. "+offerModel.getUPTO());
        offer_code.setText(offerModel.getCODE());

        tandc.setAdapter(new OfferDescAdapter(context,list));

        copy.setOnClickListener(view -> {
            ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("code", offerModel.getCODE());
            clipboard.setPrimaryClip(clip);
            Toast.makeText(context, "Code Copied!", Toast.LENGTH_SHORT).show();
            bottomSheetDialog.dismiss();
        });

        bottomSheetDialog.show();

        /*
        android.view.View bottomview = LayoutInflater.from(context).inflate(R.layout.item_offer_desc, findViewById(R.id.botomcontainer));

        bottomSheetDialog.setContentView(bottomview);
        bottomSheetDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        rec1 = bottomview.findViewById(R.id.rec1);
        rec1.setAdapter(CartItemAdapter.initializeAdapter(offlineDataProvider.returnDataFromString(offlineDataProvider.fetchitemfromStorage()), this, 2));

        TextView shoe_offers = bottomview.findViewById(R.id.shoe_offers);
        TextView delevering_to_address = bottomview.findViewById(R.id.delevering_to_address);
        TextView name_phone = bottomview.findViewById(R.id.name_phone);
        TextView changeName = bottomview.findViewById(R.id.change_Name);

        itemtotal = bottomview.findViewById(R.id.item_tot);
        delehevry = bottomview.findViewById(R.id.del_charges);
        promoamt = bottomview.findViewById(R.id.promotot);
        grandtot = bottomview.findViewById(R.id.grand_tot);
        pay = bottomview.findViewById(R.id.paybtn);

        changeName.setOnClickListener(vv -> startActivityForResult(new Intent(AllItemsActivity.this, ConfirmPhoneName.class), 1008));

        if (sharedPrefHelper != null) {
            if (sharedPrefHelper.getNamePhoneData().getName().isEmpty() || sharedPrefHelper.getNamePhoneData().getPhone().isEmpty()) {
                if (FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber() != null || !FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber().isEmpty()) {
                    name_phone.setText(FirebaseAuth.getInstance().getCurrentUser().getDisplayName() + "," + FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber());
                    sharedPrefHelper.saveNamePhoneData(FirebaseAuth.getInstance().getCurrentUser().getDisplayName(), FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber());
                } else {
                    name_phone.setText("Update your phone no in profile first .");
                    pay.setEnabled(false);
                }

            } else {
                name_phone.setText(sharedPrefHelper.getNamePhoneData().getName() + "," + sharedPrefHelper.getNamePhoneData().getPhone());
            }
        }

        List<CartItemModel> data = updateCartData();

        delevering_to_address.setText("Delivering to :" + sharedPrefHelper.getSavedHomeLocationData().getADDRESS_LINE1());

        shoe_offers.setOnClickListener(viewk -> startActivityForResult(new Intent(this, AllOffersActivity.class), 001));

         */

       /* pay.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View view) {
                if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                    StringBuilder stringBuilder = new StringBuilder();
                    for (int q = 0; q < data.size(); q++) {
                        if (q == data.size() - 1) {
                            stringBuilder
                                    .append(data.get(q).getQUANTITY())
                                    .append(" X ")
                                    .append(data.get(q).getNAME());

                        } else {
                            stringBuilder
                                    .append(data.get(q).getQUANTITY())
                                    .append(" X ")
                                    .append(data.get(q).getNAME())
                                    .append(", ");
                        }
                    }
                    orederBasicDetails = new OrederBasicDetails(
                            sharedPrefHelper.getSavedHomeLocationData().getADDRESS_LINE1(),
                            sharedPrefHelper.getSavedHomeLocationData().getLAT_LONG(),
                            "",
                            stringBuilder.toString(),
                            delehevry.getText().toString().replace("\u20B9", ""),
                            grandtot.getText().toString().replace("\u20B9", ""),
                            "",
                            String.valueOf(System.currentTimeMillis()),
                            "");

                    Resturant.isOpen(new com.thundersharp.conversation.utils.Resturant.Resturantopen() {
                        @Override
                        public void isOpen(boolean isOpen) {
                            if (isOpen) {
                                PrePayment
                                        .getInstance()
                                        .setDadaistListener(new parePayListener() {
                                            @Override
                                            public void addSuccess() {
                                                Payments
                                                        .initialize(AllItemsActivity.this)
                                                        .startPayment("ORDER #" + orederBasicDetails.getOrderID(), Double.parseDouble(orederBasicDetails.getTotalamt()), "support@thundersharp.in", "7301694135");
                                            }

                                            @Override
                                            public void addFailure(Exception exception) {
                                                Toast.makeText(AllItemsActivity.this, "Payment cannot be initialized cause :" + exception.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        }).setOrderToDatabase(data, orederBasicDetails);

                            } else
                                Toast.makeText(AllItemsActivity.this, "Resturant not open", Toast.LENGTH_SHORT).show();
                        }
                    });


                } else {
                    Toast.makeText(AllItemsActivity.this, "Log in first", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(AllItemsActivity.this, LoginActivity.class));
                }

            }
        });


        */

        bottomSheetDialog.setOnDismissListener(dialog -> {
            // Instructions on bottomSheetDialog Dismiss
        });
    }
    @Override
    public int getItemCount() {
        if (objects != null) return objects.size();else return 0;
    }

    class View extends RecyclerView.ViewHolder {
        ImageView offerBy;
        TextView offer_desc, offer_text_amt, offer_code, offer_apply;

        public View(@NonNull android.view.View itemView) {
            super(itemView);
            if (adapterType == 0){
                offer_code = itemView.findViewById(R.id.text3);
                offer_desc = itemView.findViewById(R.id.text2);
            }else {
                offerBy = itemView.findViewById(R.id.offerBy);
                offer_desc = itemView.findViewById(R.id.offer_desc);
                offer_text_amt = itemView.findViewById(R.id.offer_text_amt);
                offer_code = itemView.findViewById(R.id.offer_code);
                offer_apply = itemView.findViewById(R.id.offer_apply);
            }
        }
    }
}
