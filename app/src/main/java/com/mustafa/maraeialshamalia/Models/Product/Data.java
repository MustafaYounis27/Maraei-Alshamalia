
package com.mustafa.maraeialshamalia.Models.Product;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("desc")
    @Expose
    private String desc;
    @SerializedName("avg_rate")
    @Expose
    private Integer avgRate;
    @SerializedName("count_rate")
    @Expose
    private Integer countRate;
    @SerializedName("minced_price")
    @Expose
    private Integer mincedPrice;
    @SerializedName("is_fav")
    @Expose
    private Boolean isFav;
    @SerializedName("in_cart")
    @Expose
    private Boolean inCart;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("images")
    @Expose
    private List<String> images = null;
    @SerializedName("sizes")
    @Expose
    private List<Size> sizes = null;
    @SerializedName("cuts")
    @Expose
    private List<Cut> cuts = null;
    @SerializedName("heads")
    @Expose
    private List<Head> heads = null;
    @SerializedName("encapsulations")
    @Expose
    private List<Encapsulation> encapsulations = null;
    @SerializedName("charities")
    @Expose
    private List<Charity> charities = null;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Integer getAvgRate() {
        return avgRate;
    }

    public void setAvgRate(Integer avgRate) {
        this.avgRate = avgRate;
    }

    public Integer getCountRate() {
        return countRate;
    }

    public void setCountRate(Integer countRate) {
        this.countRate = countRate;
    }

    public Integer getMincedPrice() {
        return mincedPrice;
    }

    public void setMincedPrice(Integer mincedPrice) {
        this.mincedPrice = mincedPrice;
    }

    public Boolean getIsFav() {
        return isFav;
    }

    public void setIsFav(Boolean isFav) {
        this.isFav = isFav;
    }

    public Boolean getInCart() {
        return inCart;
    }

    public void setInCart(Boolean inCart) {
        this.inCart = inCart;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public List<Size> getSizes() {
        return sizes;
    }

    public void setSizes(List<Size> sizes) {
        this.sizes = sizes;
    }

    public List<Cut> getCuts() {
        return cuts;
    }

    public void setCuts(List<Cut> cuts) {
        this.cuts = cuts;
    }

    public List<Head> getHeads() {
        return heads;
    }

    public void setHeads(List<Head> heads) {
        this.heads = heads;
    }

    public List<Encapsulation> getEncapsulations() {
        return encapsulations;
    }

    public void setEncapsulations(List<Encapsulation> encapsulations) {
        this.encapsulations = encapsulations;
    }

    public List<Charity> getCharities() {
        return charities;
    }

    public void setCharities(List<Charity> charities) {
        this.charities = charities;
    }

}
