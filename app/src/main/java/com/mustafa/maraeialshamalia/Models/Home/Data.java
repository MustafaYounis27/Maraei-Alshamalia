
package com.mustafa.maraeialshamalia.Models.Home;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data {

    @SerializedName("unread_notifications")
    @Expose
    private Integer unreadNotifications;
    @SerializedName("slider")
    @Expose
    private List<String> slider = null;
    @SerializedName("sections")
    @Expose
    private List<Section> sections = null;

    public Integer getUnreadNotifications() {
        return unreadNotifications;
    }

    public void setUnreadNotifications(Integer unreadNotifications) {
        this.unreadNotifications = unreadNotifications;
    }

    public List<String> getSlider() {
        return slider;
    }

    public void setSlider(List<String> slider) {
        this.slider = slider;
    }

    public List<Section> getSections() {
        return sections;
    }

    public void setSections(List<Section> sections) {
        this.sections = sections;
    }

}
