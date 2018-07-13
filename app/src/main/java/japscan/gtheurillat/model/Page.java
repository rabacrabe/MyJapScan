package japscan.gtheurillat.model;

import java.util.ArrayList;

/**
 * Created by gtheurillat on 10/07/2018.
 */

public class Page {

    private String title;
    private String url;
    private String imgUrl;
    private Boolean selected;

    public Page(String title, String url) {
        this.url = url;
        this.title = title;
        this.selected = false;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String link) {
        this.url = link;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public Boolean isSelected() {
        return selected;
    }

    public void setSelected(Boolean selected) {
        this.selected = selected;
    }
}
