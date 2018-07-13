package japscan.gtheurillat.model;

import java.util.ArrayList;

/**
 * Created by gtheurillat on 10/07/2018.
 */

public class Chapitre {

    private String title;
    private String url;
    private ArrayList<Page> lstPage;


    public Chapitre(String title, String url) {
        this.title = title;
        this.url = url;

        lstPage = new ArrayList<Page>();
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

    public ArrayList<Page> getLstPage() {
        return lstPage;
    }

    public void setLstPage(ArrayList<Page> lstPage) {
        this.lstPage = lstPage;
    }

    public void addPage(Page page) {
        this.lstPage.add(page);
    }

}
