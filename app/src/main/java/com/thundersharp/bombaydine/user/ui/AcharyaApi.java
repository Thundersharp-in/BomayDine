package com.thundersharp.bombaydine.user.ui;

import android.util.ArrayMap;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.Headers;

public class AcharyaApi {

    LoginInteractor loginInteractor;
    String userName,pass;


    public static AcharyaApi initialize(){
        return new AcharyaApi();
    }

    public AcharyaApi addUsername(String userName){
        this.userName = userName;
        LoginModel.userName = userName;
        return this;
    }
    public AcharyaApi attachLoginListener(LoginInteractor loginInteractor){
        this.loginInteractor = loginInteractor;

        return this;
    }

    public AcharyaApi setPassword(String pass){
        this.pass = pass;
        LoginModel.pass = pass;
        init();
        return this;
    }

    public static Retrofit init() {
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient = new OkHttpClient.Builder().addInterceptor(httpLoggingInterceptor).build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://www.acharyainstitutes.in")
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();
        return retrofit;
    }

    public static LoginService getLoginService(){
        LoginService loginService = init().create(LoginService.class);
        return loginService;
    }

    interface LoginService{

        @FormUrlEncoded
        @Headers({ "Content-Type: application/json;charset=UTF-8"})
        @HTTP(method = "GET", path = "/index.php?r=student%2Flogin", hasBody = true)
        Call<JsonObject> performLogin(@Body JSONObject loginModel);
    }

    interface LoginInteractor{
        void OnLoginSuccess(String data);
        void OnLoginFailure(Exception e);
    }

    static class LoginModel{

        LoginModel(){}

        public static String userName,pass;


        public JSONObject getRequestBody(){
            JSONObject data = new JSONObject();
            try {
                data.put("StudentLoginForm[username]",userName);
                data.put("StudentLoginForm[password]",pass);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return data;
        }


    }

}
