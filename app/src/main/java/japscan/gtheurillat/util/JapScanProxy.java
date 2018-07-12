package japscan.gtheurillat.util;

import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import japscan.gtheurillat.model.Chapitre;
import japscan.gtheurillat.model.Nouveaute;
import japscan.gtheurillat.model.Serie;
import japscan.gtheurillat.model.Tome;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Created by gtheurillat on 10/07/2018.
 */

public class JapScanProxy {

    private String urlRoot = "https://www.japscan.cc/";
    private String urlCatalogue = "https://www.japscan.cc/mangas/";


    public JapScanProxy() {

    }

    public ArrayList<Nouveaute> getNouveautes(){
        ArrayList<Nouveaute> lstNouveautes = new ArrayList<Nouveaute>();

        try {
           // Document doc = Jsoup.connect(this.urlRoot).get();
            String userAgent = "Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.87 Safari/537.36";


            Log.e("URL", this.urlRoot);
            Document doc = Jsoup.connect(this.urlRoot).userAgent(userAgent).get();
            Log.e("GET", "JSOUP \n"+ doc.text());

            Element nouveautes_node = doc.select("#dernieres_sorties").first();

            Nouveaute new_nouveaute = null;
            Serie newSerie = null;

            Elements lst_nouveautes = nouveautes_node.children();

            for (Element elementNode : lst_nouveautes) {

                if (elementNode.tagName() == "div") {
                    //Log.e("CLASS", elementNode.attr("class"));
                    String className = elementNode.attr("class");
                    if (className.equals("date")) {
                        Log.e("DATE", elementNode.text());

                        if (new_nouveaute != null) {
                            lstNouveautes.add(new_nouveaute);
                        }
                        new_nouveaute = new Nouveaute(elementNode.text());
                    }
                    else if(className.equals("manga")) {
                        Element serieNode = elementNode.select("a").first();

                        Log.e("SERIE", serieNode.text());

                        newSerie = new Serie(serieNode.text(), this.urlRoot + serieNode.attr("href").toString());

                        new_nouveaute.addSerie(newSerie);
                    }

                    else if (className.equals("hot")) {
                        Log.e("HOT", "*");
                        newSerie.setHot(true);
                    }

                }

                else if (elementNode.tagName() == "ul") {
                    Elements chapterNodes = elementNode.select("li a");

                    for (Element chapterElement : chapterNodes) {
                        Log.e("CHAPITRE", chapterElement.attr("title"));
                        Chapitre newChapitre = new Chapitre(chapterElement.attr("title"), this.urlRoot + chapterElement.attr("href"));
                        newSerie.addChapitre(newChapitre);
                    }
                }


            }
            if (new_nouveaute != null) {
                lstNouveautes.add(new_nouveaute);
            }

        }  catch (IOException e) {
            e.printStackTrace();
        }

        return lstNouveautes;
    }

    public ArrayList<Serie> getTops(){
        ArrayList<Serie> lstTops = new ArrayList<Serie>();

        try {
            // Document doc = Jsoup.connect(this.urlRoot).get();
            String userAgent = "Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.87 Safari/537.36";


            Log.e("URL", this.urlRoot);
            Document doc = Jsoup.connect(this.urlRoot).userAgent(userAgent).get();
            Log.e("GET", "JSOUP \n"+ doc.text());

            Element tops_node = doc.select("ul.increment-list").first();

            Elements lst_tops = tops_node.children();

            Integer number = 1;
            for (Element elementNode : lst_tops) {

                if (elementNode.tagName() == "li") {
                    Element serieNode = elementNode.select("a").first();

                    Log.e("SERIE", serieNode.text());

                    Serie newSerie = new Serie(serieNode.text(), this.urlRoot + serieNode.attr("href").toString());
                    newSerie.setNumber(String.format("%02d", Integer.parseInt(number.toString())));

                    lstTops.add(newSerie);

                    number += 1;
                }
            }
        }  catch (IOException e) {
            e.printStackTrace();
        }

        return lstTops;
    }


    public Serie getSerieDetails(String serieTitle, String serieUrl){
        Serie serie = new Serie(serieTitle, serieUrl);

        try {
            String userAgent = "Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.87 Safari/537.36";


            Log.e("URL", serieUrl);
            Document doc = Jsoup.connect(serieUrl).userAgent(userAgent).get();
            Log.e("GET", "JSOUP \n"+ doc.text());

            Element row_table_node = doc.select("div.row").first();

            Elements lst_table_rows = row_table_node.children();

            serie.setAuteur(lst_table_rows.get(0).text());
            serie.setDate_sortie(lst_table_rows.get(1).text());
            serie.setGenre(lst_table_rows.get(2).text());
            serie.setFansub(lst_table_rows.get(3).text());
            serie.setStatus(lst_table_rows.get(4).text());

            Element synopsis_node = doc.select("#synopsis").first();
            serie.setSynopsis(synopsis_node.text());

            Log.e("SYNOPSIS", synopsis_node.text());

            Element chapitres_node = doc.select("#liste_chapitres").first();

            Tome newTome = null;
            for (Element elementNode : chapitres_node.children()) {
                if (elementNode.tagName() == "h2") {
                    Log.e("TOME", elementNode.text());
                    if (newTome != null) {
                        serie.addTome(newTome);
                    }
                    newTome = new Tome(elementNode.text());
                }
                if (elementNode.tagName() == "ul") {

                    for (Element subElementNode : elementNode.children()){
                        Element chapitreNode = subElementNode.select("a").first();

                        Log.e("CHAPITRE", chapitreNode.text());

                        Chapitre newChapitre = new Chapitre(chapitreNode.text(), "https:"+chapitreNode.attr("href").toString());

                        newTome.addChapitre(newChapitre);
                    }
                }
            }
            if (newTome != null) {
                serie.addTome(newTome);
            }
        }  catch (IOException e) {
            e.printStackTrace();
        }

        return serie;
    }


    public ArrayList<Serie> getCatalogue(){
        ArrayList<Serie> lstMangas = new ArrayList<Serie>();

        try {
            // Document doc = Jsoup.connect(this.urlRoot).get();
            String userAgent = "Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.87 Safari/537.36";


            Log.e("URL", this.urlCatalogue);
            Document doc = Jsoup.connect(this.urlCatalogue).userAgent(userAgent).get();
            Log.e("GET", "JSOUP \n"+ doc.text());

            Elements manga_node = doc.select("div#liste_mangas div.row");


            for (Element elementNode : manga_node) {
                Element serieNode = elementNode.select("div > a").first();
                Log.e("SERIE", serieNode.text());

                Serie newSerie = new Serie(serieNode.text(), this.urlRoot + serieNode.attr("href").toString());

                Element genreNode = serieNode.parent().nextElementSibling();
                Log.e("Element", genreNode.text());
                newSerie.setGenre(genreNode.text());

                Element statusNode = genreNode.nextElementSibling();
                newSerie.setStatus(statusNode.text());


                lstMangas.add(newSerie);

            }
        }  catch (IOException e) {
            e.printStackTrace();
        }

        return lstMangas;
    }


}

