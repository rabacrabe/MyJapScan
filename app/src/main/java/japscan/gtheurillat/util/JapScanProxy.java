package japscan.gtheurillat.util;

import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import japscan.gtheurillat.model.Chapitre;
import japscan.gtheurillat.model.Nouveaute;
import japscan.gtheurillat.model.Serie;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Created by gtheurillat on 10/07/2018.
 */

public class JapScanProxy {

    private String urlRoot = "https://www.japscan.cc/";


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


}

