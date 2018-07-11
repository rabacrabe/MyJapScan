package japscan.gtheurillat.model;

import java.util.ArrayList;

/**
 * Created by gtheurillat on 10/07/2018.
 */

public class Serie {

    private String title;
    private ArrayList<Chapitre> lstChapitres;
    private String url;
    private boolean hot;
    private String number;

    public Serie(String title, String url) {
        this.title = title;
        this.url = url;

        lstChapitres = new ArrayList<Chapitre>();
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public ArrayList<Chapitre> getLstChapitres() {
        return lstChapitres;
    }

    public void setLstChapitres(ArrayList<Chapitre> lstChapitres) {
        this.lstChapitres = lstChapitres;
    }

    public void addChapitre(Chapitre chapitre) {
        this.lstChapitres.add(chapitre);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isHot() {
        return hot;
    }

    public void setHot(boolean hot) {
        this.hot = hot;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

}
