package japscan.gtheurillat.model;

import java.util.ArrayList;

/**
 * Created by gtheurillat on 10/07/2018.
 */

public class Nouveaute {

    private String date;
    ArrayList<Serie> lstSeries;

    public Nouveaute(String date) {
        this.date = date;
        lstSeries =  new ArrayList<Serie>();
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public ArrayList<Serie> getLstSeries() {
        return lstSeries;
    }

    public void setLstSeries(ArrayList<Serie> lstSeries) {
        this.lstSeries = lstSeries;
    }

    public void addSerie(Serie serie) {
        this.lstSeries.add(serie);
    }



}
