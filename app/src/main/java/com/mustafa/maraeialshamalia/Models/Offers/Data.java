
package com.mustafa.maraeialshamalia.Models.Offers;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data {

    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("product_id")
    @Expose
    private Integer productId;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

}
