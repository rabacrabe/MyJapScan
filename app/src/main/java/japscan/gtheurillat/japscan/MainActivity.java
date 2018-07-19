package japscan.gtheurillat.japscan;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import japscan.gtheurillat.adapter.NouveautesExpandableListAdapter;
import japscan.gtheurillat.model.Chapitre;
import japscan.gtheurillat.model.Nouveaute;
import japscan.gtheurillat.model.Serie;
import japscan.gtheurillat.util.JapScanProxy;

import android.os.AsyncTask;

import com.squareup.picasso.Picasso;


public class MainActivity extends AppCompatActivity {

    ExpandableListView expandableListView;
    ExpandableListAdapter expandableListAdapter;
    List<Serie> expandableListTitle;
    HashMap<Serie, List<Chapitre>> expandableListDetail;
    ProgressDialog mProgressDialog;
    Context mainContext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //setTitle("Nouveautés");

        mainContext = this;

        expandableListView = (ExpandableListView) findViewById(R.id.lst_dernieres_sorties);
        new Nouveautes().execute();



        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

        @Override
        public void onGroupExpand(int groupPosition) {
            Toast.makeText(getApplicationContext(),
                    expandableListTitle.get(groupPosition) + " List Expanded.",
                    Toast.LENGTH_SHORT).show();
        }
        });

        expandableListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

        @Override
        public void onGroupCollapse(int groupPosition) {
            Toast.makeText(getApplicationContext(),
                    expandableListTitle.get(groupPosition) + " List Collapsed.",
                    Toast.LENGTH_SHORT).show();

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


                Toast.makeText(
                        getApplicationContext(),
                        expandableListTitle.get(groupPosition)
                                + " -> "
                                + expandableListDetail.get(
                                expandableListTitle.get(groupPosition)).get(
                                childPosition).getUrl(), Toast.LENGTH_SHORT
                ).show();




                Intent intent_lecteur = new Intent(MainActivity.this, LecteurActivity.class);
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
    private class Nouveautes extends AsyncTask<Void, Void, Void> {
        String title;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(MainActivity.this);
            mProgressDialog.setTitle("Dernières Sorties");
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {



                JapScanProxy proxy = new JapScanProxy();
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


            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // Set title into TextView
            //TextView txttitle = (TextView) findViewById(R.id.titletxt);
            //txttitle.setText(title);

            //expandableListTitle = new ArrayList<Serie>(expandableListDetail.keySet());

            expandableListAdapter = new NouveautesExpandableListAdapter(mainContext, expandableListTitle, expandableListDetail);
            expandableListView.setAdapter(expandableListAdapter);

            mProgressDialog.dismiss();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_home:
                // Comportement du bouton "A Propos"
                return true;
            case R.id.menu_tops:
                Intent intent_tops = new Intent(MainActivity.this, TopsActivity.class);
                startActivity(intent_tops);
                return true;
            case R.id.menu_list_mangas:
                Intent intent_mangas = new Intent(MainActivity.this, MangasActivity.class);
                startActivity(intent_mangas);
                return true;
            case R.id.menu_favoris:
                Intent intent_favoris = new Intent(MainActivity.this, FavorisActivity.class);
                startActivity(intent_favoris);
                return true;
            case R.id.menu_settings:
                // Comportement du bouton "Paramètres"
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}

