
package com.mustafa.maraeialshamalia.Models.Notifications;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data {

    @SerializedName("data")
    @Expose
    private List<NotifyData> data = null;
    @SerializedName("pagination")
    @Expose
    private Pagination pagination;

    public List<NotifyData> getData() {
        return data;
    }

    public void setData(List<NotifyData> data) {
        this.data = data;
    }

    public Pagination getPagination() {
        return pagination;
    }

    public void setPagination(Pagination pagination) {
        this.pagination = pagination;
    }

}
