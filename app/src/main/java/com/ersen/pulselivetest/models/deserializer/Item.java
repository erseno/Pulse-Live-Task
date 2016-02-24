package com.ersen.pulselivetest.models.deserializer;

import com.google.gson.annotations.SerializedName;

/** Used for GSON deserializer */
public class Item<T> {
    @SerializedName("item")
    private T mItem;

    public T getItem() {
        return mItem;
    }
}
