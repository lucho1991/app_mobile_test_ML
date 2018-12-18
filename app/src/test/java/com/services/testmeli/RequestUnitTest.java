package com.services.testmeli;

import android.util.Log;

import com.services.testmeli.Api.ApiInterface;
import com.services.testmeli.Api.ApiServices;
import com.services.testmeli.Model.Item;

import org.junit.Test;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

import retrofit2.Call;
import retrofit2.Response;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class RequestUnitTest {
    @Test
    public void login_Success() {

        ApiInterface apiInterface= ApiServices.getApliClient().create(ApiInterface.class);

        Call<Item> call = apiInterface.getItemDetail("MLA747312077");

        try {
            Response<Item> response = call.execute();
            Item item = response.body();

            assertTrue(response.isSuccessful() && item.getTitle().startsWith("Ipod "));

        } catch (IOException e) {
            e.printStackTrace();
        }

    }



}