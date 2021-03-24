
package com.mustafa.maraeialshamalia.Models.OrderDetails;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data {

    @SerializedName("order_data")
    @Expose
    private OrderData orderData;
    @SerializedName("products_data")
    @Expose
    private List<ProductsData> productsData = null;

    public OrderData getOrderData() {
        return orderData;
    }

    public void setOrderData(OrderData orderData) {
        this.orderData = orderData;
    }

    public List<ProductsData> getProductsData() {
        return productsData;
    }

    public void setProductsData(List<ProductsData> productsData) {
        this.productsData = productsData;
    }

}
