package com.thundersharp.bombaydine.user.core.location;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.thundersharp.bombaydine.user.core.Model.DataresponsePojo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PinCodeInteractor implements PinCodeContract.PinccodeInteractor {

    private Context context;
    private PinCodeContract.onPinDatafetchListner onPinDatafetchListner;

    String pinCode;
    private RequestQueue mRequestQueue;

    public PinCodeInteractor(Context context, PinCodeContract.onPinDatafetchListner onPinDatafetchListner) {
        this.context = context;
        this.onPinDatafetchListner = onPinDatafetchListner;
        mRequestQueue = Volley.newRequestQueue(context);
    }

    @Override
    public void getdetailsfromPincode(@NonNull String pinCode) {
        // clearing our cache of request queue.
        mRequestQueue.getCache().clear();

        String url = "https://api.postalpincode.in/pincode/" + pinCode;

        // below line is use to initialize our request queue.
        RequestQueue queue = Volley.newRequestQueue(context);


        JsonArrayRequest objectRequest = new JsonArrayRequest(Request.Method.GET, url, null, response -> {
            try {

                if (response.getJSONObject(0).getString("Status").equals("Error")) {

                    Exception exception = new Exception("Api internal error : DATA NOT FOUND") ;
                    datafailure(exception);

                } else {
                    // if the status is success we are calling this method
                    // in which we are getting data from post office object
                    // here we are calling first object of our json array.
                    JSONObject obj = response.getJSONObject(0).getJSONArray("PostOffice").getJSONObject(0);

                    // inside our json array we are getting district name,
                    // state and country from our data.
                    String district = obj.getString("District");
                    String state = obj.getString("State");
                    String country = obj.getString("Country");

                    setdatasucess(obj);

                }
            } catch (JSONException e) {

                e.printStackTrace();
                datafailure(e);
            }
        }, error -> {

            // below method is called if we get
            // any error while fetching data from API.
            // below line is use to display an error message.
            //Toast.makeText(getActivity(), "Pin code is not valid." + error.getCause().getMessage(), Toast.LENGTH_SHORT).show();
            Log.d("Error", error.getMessage());
            datafailure(error);
            //pinCodeDetailsTV.setText("Pin code is not valid");
        });
        // below line is use for adding object
        // request to our request queue.
        queue.add(objectRequest);
    }

    private void setdatasucess(JSONObject dataresponsePojo){
        onPinDatafetchListner.onDataFetch(dataresponsePojo);
    }

    private void datafailure(Exception e){
        onPinDatafetchListner.onDataFetchFailureListner(e);
    }
}
