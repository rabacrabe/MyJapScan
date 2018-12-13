package japscan.gtheurillat.japscan;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import java.net.HttpCookie;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.github.kelvao.cloudflarescrape.CloudflareScrape;
import japscan.gtheurillat.adapter.NouveautesExpandableListAdapter;
import japscan.gtheurillat.model.Chapitre;
import japscan.gtheurillat.model.Nouveaute;
import japscan.gtheurillat.model.Serie;
import japscan.gtheurillat.util.JapScanProxy;
import japscan.gtheurillat.util.cloudflare.CloudFlareProxy;

public class NewsActivity extends AppCompatActivity {

    ExpandableListView expandableListView;
    ExpandableListAdapter expandableListAdapter;
    List<Serie> expandableListTitle;
    HashMap<Serie, List<Chapitre>> expandableListDetail;
    ProgressDialog mProgressDialog;
    Context mainContext;

    private HashMap<String, String> cookies;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_news:
                    //mTextMessage.setText(R.string.title_home);
                    return true;
                case R.id.navigation_tops:
                    //mTextMessage.setText(R.string.title_dashboard);
                    Intent intent_tops = new Intent(NewsActivity.this, TopsActivity.class);
                    startActivity(intent_tops);
                    return true;
                case R.id.navigation_mangas:
                    //mTextMessage.setText(R.string.title_notifications);
                    Intent intent_mangas = new Intent(NewsActivity.this, MangasActivity.class);
                    startActivity(intent_mangas);
                    return true;
                case R.id.navigation_favoris:
                    Intent intent_favoris = new Intent(NewsActivity.this, FavorisActivity.class);
                    startActivity(intent_favoris);
                    return true;
                case R.id.navigation_bookmark:
                    Intent intent_bookmark = new Intent(NewsActivity.this, BookmarkActivity.class);
                    startActivity(intent_bookmark);
                    return true;
            }
            return false;
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.navigation_top, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.menu_favoris:
                Intent intent_favoris = new Intent(NewsActivity.this, FavorisActivity.class);
                startActivity(intent_favoris);
                return true;
            case R.id.menu_bookmark:
                Intent intent_bookmark = new Intent(NewsActivity.this, BookmarkActivity.class);
                startActivity(intent_bookmark);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.navigation_news);


        mainContext = this;

        expandableListView = (ExpandableListView) findViewById(R.id.lst_dernieres_sorties);
        new NewsActivity.Nouveautes().execute();



        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {
            /*
            Toast.makeText(getApplicationContext(),
                    expandableListTitle.get(groupPosition) + " List Expanded.",
                    Toast.LENGTH_SHORT).show();
                    */
            }
        });

        expandableListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {
            /*
            Toast.makeText(getApplicationContext(),
                    expandableListTitle.get(groupPosition) + " List Collapsed.",
                    Toast.LENGTH_SHORT).show();
*/
            }
        });

        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                String chapitre_title = expandableListDetail.get(
                        expandableListTitle.get(groupPosition)).get(
                        childPosition).getTitle();
                String chapitre_url = expandableListDetail.get(
                        expandableListTitle.get(groupPosition)).get(
                        childPosition).getUrl();

/*
                Toast.makeText(
                        getApplicationContext(),
                        expandableListTitle.get(groupPosition)
                                + " -> "
                                + expandableListDetail.get(
                                expandableListTitle.get(groupPosition)).get(
                                childPosition).getUrl(), Toast.LENGTH_SHORT
                ).show();
*/



                Intent intent_lecteur = new Intent(NewsActivity.this, LecteurActivity.class);
                intent_lecteur.putExtra("SERIE_TITLE", "");
                intent_lecteur.putExtra("SERIE_URL", "");
                intent_lecteur.putExtra("CHAPITRE_TITLE", chapitre_title);
                intent_lecteur.putExtra("CHAPITRE_URL", chapitre_url);
                startActivity(intent_lecteur);

                return false;
            }
        });

    }


    // Title AsyncTask
    public class Nouveautes extends AsyncTask<Void, Void, Void> {
        String title;
        private HashMap<String, String> cookies;
        private JapScanProxy proxy;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            proxy = new JapScanProxy();


            mProgressDialog = new ProgressDialog(NewsActivity.this);
            mProgressDialog.setTitle("Dernières Sorties");
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.show();
        }






        @Override
        protected Void doInBackground(Void... params) {



            CloudFlareProxy cf = new CloudFlareProxy(proxy.getUrlRoot());
            cf.setUser_agent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10.11; rv:49.0) Gecko/20100101 Firefox/49.0");
            cf.getCookies(new CloudFlareProxy.cfCallback() {
                @Override
                public void onSuccess(List<HttpCookie> cookieList) {
                    Log.i("CLOUDFLARE", "SUCCES");



                    proxy.setCookies(CloudFlareProxy.List2Map(cookieList));

                    try {
                        //CloudFlareProxy cloudflareproxy = new CloudFlareProxy();
                        //cloudflareproxy.getPage(proxy.getUrlRoot());
                        Thread.currentThread();
                        Thread.sleep(10000);

                        ArrayList<Nouveaute> lstNouveautes = proxy.getNouveautes();


                        expandableListDetail = new HashMap<Serie, List<Chapitre>>();
                        expandableListTitle = new ArrayList<Serie>();
                        for (Nouveaute nouveaute : lstNouveautes) {
                            //expandableListDetail.put(new Serie(nouveaute.getDate(), null), new ArrayList<Chapitre>());

                            for (Serie serie : nouveaute.getLstSeries()) {
                                List<Chapitre> lst_chapitres = new ArrayList<Chapitre>();

                                for (Chapitre chapitre : serie.getLstChapitres()) {
                                    chapitre.setDate_sortie(nouveaute.getDate());
                                    lst_chapitres.add(chapitre);
                                }

                                expandableListDetail.put(serie, lst_chapitres);
                                expandableListTitle.add(serie);
                            }
                        }

                        expandableListAdapter = new NouveautesExpandableListAdapter(mainContext, expandableListTitle, expandableListDetail);
                        expandableListView.setAdapter(expandableListAdapter);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFail() {
                    Log.i("CLOUDFLARE", "FAIL");
                }
            });



            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

            //expandableListAdapter = new NouveautesExpandableListAdapter(mainContext, expandableListTitle, expandableListDetail);
            //expandableListView.setAdapter(expandableListAdapter);

            mProgressDialog.dismiss();
        }


    }


}
