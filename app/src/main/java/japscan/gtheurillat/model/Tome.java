package japscan.gtheurillat.model;

import java.util.ArrayList;

/**
 * Created by gtheurillat on 10/07/2018.
 */

public class Tome {

    private String title;
    private ArrayList<Chapitre> lstChapitres;
    private String url;

    public Tome(String title) {
        this.title = title;

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


}
