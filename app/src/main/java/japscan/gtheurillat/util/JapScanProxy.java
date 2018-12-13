package japscan.gtheurillat.util;

import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import io.github.kelvao.cloudflarescrape.CloudflareScrape;
import japscan.gtheurillat.model.Chapitre;
import japscan.gtheurillat.model.Nouveaute;
import japscan.gtheurillat.model.Page;
import japscan.gtheurillat.model.Serie;
import japscan.gtheurillat.model.Tome;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Created by gtheurillat on 10/07/2018.
 */

public class JapScanProxy {

    private String urlRoot = "https://www.japscan.to/";
    private String urlCatalogue = "https://www.japscan.to/mangas/";
    private Map<String, String> cookies;


    public void setCookies(Map<String, String> cookies) {
        this.cookies = cookies;
    }


    public JapScanProxy() {

    }


    public String getUrlRoot() {
        return this.urlRoot;
    }

    public ArrayList<Nouveaute> getNouveautes(){
        ArrayList<Nouveaute> lstNouveautes = new ArrayList<Nouveaute>();

        try {
           // Document doc = Jsoup.connect(this.urlRoot).get();
            String userAgent = "Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.87 Safari/537.36";


            Log.i("URL", this.urlRoot);
            //Document doc = Jsoup.connect(this.urlRoot).userAgent(userAgent).get();

            Document doc = Jsoup.connect(this.urlRoot)
                                .cookies(this.cookies)
                                .userAgent(userAgent)
                                .ignoreHttpErrors(true)
                                .followRedirects(true)
                                .timeout(100000)
                                .ignoreContentType(true)
                                .get();

            Log.i("GET", "JSOUP \n"+ doc.text());

            Element nouveautes_node = doc.select("#chapters").first();


            Nouveaute new_nouveaute = null;
            Serie newSerie = null;

            Elements lst_dates = nouveautes_node.children();
            for (Element dateNode : lst_dates) {
                Elements lst_nouveautes = dateNode.children();

                //Log.e("CLASS", elementNode.attr("class"));
                if (new_nouveaute != null) {
                    lstNouveautes.add(new_nouveaute);
                }
                new_nouveaute = new Nouveaute(dateNode.attr("id"));

                Log.e("DATE", dateNode.attr("id"));

                for (Element elementNode : lst_nouveautes) {
                    if (elementNode.tagName() == "div") {
                        String className = elementNode.attr("class");
                        if (className.equals("chapter_list")) {

                            Elements chapters_nodes = elementNode.select("a");
                            for (Element chapterNode : chapters_nodes) {
                                Log.e("CHAPITRE", chapterNode.text());
                                Chapitre newChapitre = new Chapitre(chapterNode.text(), this.urlRoot + chapterNode.attr("href"));
                                newSerie.addChapitre(newChapitre);
                            }
                        }
                    }
                    else if (elementNode.tagName() == "h3") {
                        Element serieNode = elementNode.select("a").first();



                        Log.e("SERIE", serieNode.text());

                        newSerie = new Serie(serieNode.text(), this.urlRoot + serieNode.attr("href").toString());

                        new_nouveaute.addSerie(newSerie);
                    }
            }



/*
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

*/
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


            //Log.e("TITLE", serieTitle);
            Log.e("URL", serieUrl);
            Document doc = Jsoup.connect(serieUrl).userAgent(userAgent).get();
            Log.e("GET", "JSOUP \n"+ doc.text());

            Element row_table_node = doc.select("div.row").first();

            Elements lst_table_rows = row_table_node.children();

            serie.setAuteur(lst_table_rows.get(0).text());
            serie.setDate_sortie(lst_table_rows.get(1).text());
            serie.setGenre(lst_table_rows.get(2).text());

            if (lst_table_rows.size() > 4) {
                serie.setFansub(lst_table_rows.get(3).text());
                serie.setStatus(lst_table_rows.get(4).text());
            } else {
                serie.setFansub("");
                serie.setStatus(lst_table_rows.get(3).text());
            }

            Element synopsis_node = doc.select("#synopsis").first();
            serie.setSynopsis(synopsis_node.text());

            Log.e("AUTEUR", serie.getAuteur());
            Log.e("DATE", serie.getDate_sortie());
            Log.e("GENRE", serie.getGenre());
            Log.e("FANSUB", serie.getFansub());
            Log.e("STATUS", serie.getStatus());
            Log.e("SYNOPSIS", serie.getSynopsis());

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

                        Chapitre newChapitre = new Chapitre(chapitreNode.text(), this.urlRoot+chapitreNode.attr("href").toString());

                        if (newTome == null) {
                            newTome = new Tome("PLUS RECENT");
                        }

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

        Log.e("NB CHAPITITRES", String.valueOf(serie.getLstChapitres().size()));

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


    public Serie getLecteurInfos(String title, String url){
        Serie serie = null;
        Chapitre chapitre = new Chapitre(title, url);


        try {
            // Document doc = Jsoup.connect(this.urlRoot).get();
            String userAgent = "Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.87 Safari/537.36";


            Log.e("URL", url);
            Document doc = Jsoup.connect(url).userAgent(userAgent).get();
            Log.e("GET", "JSOUP \n"+ doc.text());

            Element img_node = doc.select("img#image").first();

            Element prec_chapter_node = doc.select("a#back_chapter").first();
            Chapitre precChapitre = null;
            if (prec_chapter_node != null) {
                precChapitre = new Chapitre(prec_chapter_node.text(), this.urlRoot + prec_chapter_node.attr("href"));
            }

            Element next_chapter_node = doc.select("a#next_chapter").first();
            Chapitre nextChapitre = null;
            if (next_chapter_node != null) {
                nextChapitre = new Chapitre(next_chapter_node.text(), this.urlRoot + next_chapter_node.attr("href"));
            }

            Element pages_node = doc.select("select#pages").first();

            for (Element pageItem : pages_node.children()) {
                //Log.e("PAGE", pageItem.text() + " -> " + this.urlRoot + pageItem.attr("value"));
                Page newPage = new Page(pageItem.text(), this.urlRoot + pageItem.attr("value"));


                String selected =pageItem.attr("selected");
                if (selected.equals("selected")) {
                    newPage.setTitle(img_node.attr("alt"));
                    newPage.setImgUrl(img_node.attr("src"));

                    Log.e("IMG", img_node.attr("src"));

                    newPage.setSelected(true);
                }

                chapitre.addPage(newPage);
            }


             /*

            Elements details_serie_node = doc.select("tbody > tr");


            Element manga_detail_manga = details_serie_node.get(0).select("td").last();
            Element manga_detail_chapitre = details_serie_node.get(1).select("td").last();
            Element manga_detail_titre = details_serie_node.get(2).select("td").last();
            Element manga_detail_team = details_serie_node.get(3).select("td").last();
            Element manga_detail_date = details_serie_node.get(4).select("td").last();

            serie = new Serie(manga_detail_titre.text(), this.urlRoot + manga_detail_manga.child(0).attr("href"));
            serie.setFansub(manga_detail_team.text());
            serie.setDate_sortie(manga_detail_date.text());

            */
            serie = new Serie("TEST", "");
            Element serie_name_node = doc.select("tbody > tr > td > a").first();
            if (serie_name_node != null) {
                serie.setTitle(serie_name_node.text());
                serie.setUrl(this.urlRoot + serie_name_node.attr("href") );


            }


/*
            ArrayList<Chapitre> tmpLstChapitre = new ArrayList<Chapitre>();

            Element chapitres_node = doc.select("select#chapitres").first();

            while(chapitres_node.children().size() == 0) {

            }

            for (Element chapitreItem : chapitres_node.children()) {
                Log.e("PAGE", chapitreItem.text() + " -> " + this.urlRoot + chapitreItem.attr("value"));
                Chapitre newChapitre = new Chapitre(chapitreItem.text(), this.urlRoot + chapitreItem.attr("value"));
                tmpLstChapitre.add(newChapitre);
                serie.addChapitre(newChapitre);
            }
            //Collections.reverse(tmpLstChapitre);
            //serie.setLstChapitres(tmpLstChapitre);
            serie.setIdxCurrentChapitre(Integer.parseInt(chapitres_node.attr("data-uri"))-1);
*/


            serie.addChapitre(precChapitre);
            serie.addChapitre(chapitre);
            serie.addChapitre(nextChapitre);

            serie.setIdxCurrentChapitre(1);

        }  catch (IOException e) {
            e.printStackTrace();
        }

        return serie;
    }


}

