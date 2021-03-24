
package com.mustafa.maraeialshamalia.Models.OrderDetails;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OrderData {

    @SerializedName("order_id")
    @Expose
    private Integer orderId;
    @SerializedName("order_num")
    @Expose
    private String orderNum;
    @SerializedName("order_total")
    @Expose
    private Integer orderTotal;
    @SerializedName("delivery_date")
    @Expose
    private String deliveryDate;
    @SerializedName("notes")
    @Expose
    private String notes;
    @SerializedName("pay_type")
    @Expose
    private String payType;
    @SerializedName("pay_status")
    @Expose
    private String payStatus;
    @SerializedName("has_transfer")
    @Expose
    private Boolean hasTransfer;
    @SerializedName("pay_type_name")
    @Expose
    private String payTypeName;
    @SerializedName("order_status")
    @Expose
    private String orderStatus;
    @SerializedName("order_status_name")
    @Expose
    private String orderStatusName;

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public String getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }

    public Integer getOrderTotal() {
        return orderTotal;
    }

    public void setOrderTotal(Integer orderTotal) {
        this.orderTotal = orderTotal;
    }

    public String getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(String deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public String getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(String payStatus) {
        this.payStatus = payStatus;
    }

    public Boolean getHasTransfer() {
        return hasTransfer;
    }

    public void setHasTransfer(Boolean hasTransfer) {
        this.hasTransfer = hasTransfer;
    }

    public String getPayTypeName() {
        return payTypeName;
    }

    public void setPayTypeName(String payTypeName) {
        this.payTypeName = payTypeName;
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

}
