
package com.mustafa.maraeialshamalia.Models.Orders;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data {

    @SerializedName("data")
    @Expose
    private List<OrdersItem> data = null;
    @SerializedName("pagination")
    @Expose
    private Pagination pagination;
    @SerializedName("order_id")
    @Expose
    private int order_id;
    @SerializedName("value")
    @Expose
    private double value;
    @SerializedName("pay_type")
    @Expose
    private String pay_type;

    public String getPay_type() {
        return pay_type;
    }

    public void setPay_type(String pay_type) {
        this.pay_type = pay_type;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public int getOrder_id() {
        return order_id;
    }

    public void setOrder_id(int order_id) {
        this.order_id = order_id;
    }

    public List<OrdersItem> getData() {
        return data;
    }

    public void setData(List<OrdersItem> data) {
        this.data = data;
    }

    public Pagination getPagination() {
        return pagination;
    }

    public void setPagination(Pagination pagination) {
        this.pagination = pagination;
    }

}
