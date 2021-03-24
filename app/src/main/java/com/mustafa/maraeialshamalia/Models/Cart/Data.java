
package com.mustafa.maraeialshamalia.Models.Cart;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data {

    @SerializedName("hasNormalItem")
    @Expose
    private Boolean hasNormalItem;
    @SerializedName("delivery_charge")
    @Expose
    private Integer deliveryCharge;

    @SerializedName("product_name")
    @Expose
    private String product_name;
    @SerializedName("total_cart")
    @Expose
    private Integer total_cart;

    @SerializedName("cart_items")
    @Expose
    private List<CartItem> cartItems = null;

    public Boolean getHasNormalItem() {
        return hasNormalItem;
    }

    public void setHasNormalItem(Boolean hasNormalItem) {
        this.hasNormalItem = hasNormalItem;
    }

    public Integer getDeliveryCharge() {
        return deliveryCharge;
    }

    public void setDeliveryCharge(Integer deliveryCharge) {
        this.deliveryCharge = deliveryCharge;
    }

    public List<CartItem> getCartItems() {
        return cartItems;
    }

    public void setCartItems(List<CartItem> cartItems) {
        this.cartItems = cartItems;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public Integer getTotal_cart() {
        return total_cart;
    }

    public void setTotal_cart(Integer total_cart) {
        this.total_cart = total_cart;
    }
}
