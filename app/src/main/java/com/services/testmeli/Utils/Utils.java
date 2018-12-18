package com.services.testmeli.Utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


public class Utils {
    private static Gson gson;

    //Json constructor
    public static Gson getGsonParser() {
        if(null == gson) {
            GsonBuilder builder = new GsonBuilder();
            gson = builder.create();
        }
        return gson;
    }


    public static double calculeForDiscountPrice(String price, String original_price){
        double percentageValueOffPrice = Double.parseDouble(price) * 100 / Double.parseDouble(original_price);
        percentageValueOffPrice = 100-percentageValueOffPrice;
        return Math.round(percentageValueOffPrice);
    }

    //TestConection
    public static boolean isOnline(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isAvailable() && networkInfo.isConnected();
    }



}
