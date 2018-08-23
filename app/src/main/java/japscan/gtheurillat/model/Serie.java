package japscan.gtheurillat.model;

import java.util.ArrayList;

/**
 * Created by gtheurillat on 10/07/2018.
 */

public class Serie{

    private String title;
    private ArrayList<Chapitre> lstChapitres;
    private ArrayList<Tome> lstTomes;
    private String url;
    private boolean hot;
    private String number;
    private String auteur;
    private String date_sortie;
    private String genre;
    private String fansub;
    private String status;
    private String synopsis;
    private Integer idxCurrentChapitre;
    private Boolean favoris;

    public Serie(String title, String url) {
        this.title = title;
        this.url = url;
        this.favoris = false;

        lstChapitres = new ArrayList<Chapitre>();
        lstTomes = new ArrayList<Tome>();
    }

    public Serie(String title, String url, String genre, String status) {
        this.title = title;
        this.url = url;
        this.genre = genre;
        this.status = status;
        this.favoris = false;

        lstChapitres = new ArrayList<Chapitre>();
        lstTomes = new ArrayList<Tome>();
    }

    public ArrayList<Tome> getLstTomes() {
        return lstTomes;
    }

    public void setLstTomes(ArrayList<Tome> lstTomes) {
        this.lstTomes = lstTomes;
    }

    public void addTome(Tome tome) {
        this.lstTomes.add(tome);
    }

    public String getAuteur() {
        return auteur;
    }

    public void setAuteur(String auteur) {
        this.auteur = auteur;
    }

    public String getDate_sortie() {
        return date_sortie;
    }

    public void setDate_sortie(String date_sortie) {
        this.date_sortie = date_sortie;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getFansub() {
        return fansub;
    }

    public void setFansub(String fansub) {
        this.fansub = fansub;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
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

    public Integer getIdxCurrentChapitre() {
        return idxCurrentChapitre;
    }

    public void setIdxCurrentChapitre(Integer idxCurrentChapitre) {
        this.idxCurrentChapitre = idxCurrentChapitre;
    }

    public Boolean isFavoris() {
        return favoris;
    }

    public void setFavoris(Boolean favoris) {
        this.favoris = favoris;
    }


}
