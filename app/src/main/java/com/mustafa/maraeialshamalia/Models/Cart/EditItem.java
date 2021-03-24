package com.mustafa.maraeialshamalia.Models.Cart;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.Map;

public class EditItem
{
    @SerializedName("data")
    @Expose
    private Map<String,String> data;

    public EditItem(String id, String quantity)
    {
        data = new HashMap<> ();
        data.put ( "id",id );
        data.put ( "quantity",quantity );
    }

    public Map<String, String> getData() {
        return data;
    }

    public void setData(Map<String, String> data) {
        this.data = data;
    }
}
