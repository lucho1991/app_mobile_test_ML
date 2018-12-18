package com.services.testmeli.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;



public class ListItems {

    @SerializedName("results")
    @Expose
    private List<Item> results;

    public ListItems() {
    }

    public ListItems(List<Item> results) {
        this.results = results;
    }

    public List<Item> getResults() {
        return results;
    }

    public void setResults(List<Item> results) {
        this.results = results;
    }

}


