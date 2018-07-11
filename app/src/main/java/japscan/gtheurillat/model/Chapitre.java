package japscan.gtheurillat.model;

/**
 * Created by gtheurillat on 10/07/2018.
 */

public class Chapitre {

    private String title;
    private String url;


    public Chapitre(String title, String url) {
        this.title = title;
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }



}
