
package com.mustafa.maraeialshamalia.Models.Orders;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OrdersItem {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("order_num")
    @Expose
    private String orderNum;
    @SerializedName("count_products")
    @Expose
    private Integer countProducts;
    @SerializedName("order_status")
    @Expose
    private String orderStatus;
    @SerializedName("order_status_name")
    @Expose
    private String orderStatusName;
    @SerializedName("pay_status")
    @Expose
    private String payStatus;
    @SerializedName("pay_type")
    @Expose
    private String payType;
    @SerializedName("has_transfer")
    @Expose
    private Boolean hasTransfer;
    @SerializedName("order_total")
    @Expose
    private Integer orderTotal;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }

    public Integer getCountProducts() {
        return countProducts;
    }

    public void setCountProducts(Integer countProducts) {
        this.countProducts = countProducts;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getOrderStatusName() {
        return orderStatusName;
    }

    public void setOrderStatusName(String orderStatusName) {
        this.orderStatusName = orderStatusName;
    }

    public String getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(String payStatus) {
        this.payStatus = payStatus;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public Boolean getHasTransfer() {
        return hasTransfer;
    }

    public void setHasTransfer(Boolean hasTransfer) {
        this.hasTransfer = hasTransfer;
    }

    public Integer getOrderTotal() {
        return orderTotal;
    }

    public void setOrderTotal(Integer orderTotal) {
        this.orderTotal = orderTotal;
    }

}
