package com.panzer.androidapp;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public class PanzerAPI {

    private static Gson gson = new GsonBuilder().setLenient().create();
    private Retrofit retrofit;

    public PanzerAPI(String baseUrl) {


        this.retrofit = new Retrofit.Builder().baseUrl(baseUrl).addConverterFactory(GsonConverterFactory.create(gson)).build();
    }

    public void setRetrofit(String baseUrl) {
        this.retrofit = new Retrofit.Builder().baseUrl(baseUrl).addConverterFactory(GsonConverterFactory.create(gson)).build();
    }

    private interface CheckConnectionStateAPI {

        @Headers("Content-Type: application/json")
        @GET("index.php")
        Call<String> getConnectionState();
    }

    public void getConnectionState(Callback<String> callback) {

        CheckConnectionStateAPI checkConnectionStateAPI = retrofit.create(CheckConnectionStateAPI.class);
        Call<String> call = checkConnectionStateAPI.getConnectionState();
        call.enqueue(callback);
    }

    private interface ControlPanzerAPI {

        @Headers("Content-Type: application/json")
        @GET("index.php")
        Call<String> controlPanzer(@Query("control") String control);
    }

    public void controlPanzer(String value, Callback<String> callback) {
        ControlPanzerAPI controlPanzerAPI = retrofit.create(ControlPanzerAPI.class);
        Call<String> call = controlPanzerAPI.controlPanzer(value);
        call.enqueue(callback);
    }
}
