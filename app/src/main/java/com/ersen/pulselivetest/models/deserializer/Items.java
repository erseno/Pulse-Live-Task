package com.ersen.pulselivetest.models.deserializer;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/** Used for GSON deserializer */
public class Items<T> {
    @SerializedName("items")
    private ArrayList<T> mItems;

    public ArrayList<T> getItems() {
        return mItems;
    }
}
