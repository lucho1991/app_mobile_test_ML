package com.services.testmeli.Api;

import com.services.testmeli.Model.Item;
import com.services.testmeli.Model.ListItems;


import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;


public interface ApiInterface {

    //Declare the URL of the api to consume


    @GET("site/{site_id}/search")
    Call<ListItems> getListItems(
            @Path("site_id") String stringSiteId,
            @Query("item") String searchItem, //aws
            @Query("offset") int offSetPagination);


    @GET("item/{id}")
    Call<Item> getItemDetail(@Path("id") String stringIdSearchItems);

}
