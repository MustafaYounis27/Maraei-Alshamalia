
package com.mustafa.maraeialshamalia.Models.Home;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Section {

    @SerializedName("section_id")
    @Expose
    private Integer sectionId;
    @SerializedName("section_name")
    @Expose
    private String sectionName;
    @SerializedName("products")
    @Expose
    private List<HomeProduct> homeProducts = null;

    public Integer getSectionId() {
        return sectionId;
    }

    public void setSectionId(Integer sectionId) {
        this.sectionId = sectionId;
    }

    public String getSectionName() {
        return sectionName;
    }

    public void setSectionName(String sectionName) {
        this.sectionName = sectionName;
    }

    public List<HomeProduct> getHomeProducts() {
        return homeProducts;
    }

    public void setHomeProducts(List<HomeProduct> homeProducts) {
        this.homeProducts = homeProducts;
    }

}
