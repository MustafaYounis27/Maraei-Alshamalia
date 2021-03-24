
package com.mustafa.maraeialshamalia.Models.Cart;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Cart {

    @SerializedName("key")
    @Expose
    private String key;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("data")
    @Expose
    private Data data;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

}
