package japscan.gtheurillat.japscan;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import japscan.gtheurillat.adapter.NouveautesExpandableListAdapter;
import japscan.gtheurillat.adapter.SerieDetailsExpandableListAdapter;
import japscan.gtheurillat.model.Chapitre;
import japscan.gtheurillat.model.Nouveaute;
import japscan.gtheurillat.model.Serie;
import japscan.gtheurillat.model.Tome;
import japscan.gtheurillat.util.JapScanProxy;


public class SerieDetailsActivity extends AppCompatActivity {

    ExpandableListView expandableListView;
    ExpandableListAdapter expandableListAdapter;
    List<Tome> expandableListTitle;
    HashMap<Tome, List<Chapitre>> expandableListDetail;
    ProgressDialog mProgressDialog;
    Context mainContext;
    String title;
    String url;
    Serie serie;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seriedetails);

        //setTitle("Nouveautés");

        mainContext = this;

        title = getIntent().getStringExtra("SERIE_TITLE");
        url = getIntent().getStringExtra("SERIE_URL");

        TextView titleTextView = (TextView)findViewById(R.id.textMainTitle);
        titleTextView.setText(title);

        expandableListView = (ExpandableListView) findViewById(R.id.lst_dernieres_sorties);
        new SerieDetails().execute();



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
                Toast.makeText(
                        getApplicationContext(),
                        expandableListTitle.get(groupPosition)
                                + " -> "
                                + expandableListDetail.get(
                                expandableListTitle.get(groupPosition)).get(
                                childPosition).getUrl(), Toast.LENGTH_SHORT
                ).show();
                return false;
            }
        });
    }

    // Title AsyncTask
    private class SerieDetails extends AsyncTask<Void, Void, Void> {
        String title;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(SerieDetailsActivity.this);
            mProgressDialog.setTitle(this.title);
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                JapScanProxy proxy = new JapScanProxy();
                serie = proxy.getSerieDetails(title, url);


                expandableListDetail = new HashMap<Tome, List<Chapitre>>();

                for (Tome tome : serie.getLstTomes()) {
                    expandableListDetail.put(tome, tome.getLstChapitres());
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


            TextView auteurTextView = (TextView)findViewById(R.id.textDetailAuteur);
            auteurTextView.setText(serie.getAuteur());

            TextView dateTextView = (TextView)findViewById(R.id.textDetailDate);
            dateTextView.setText(serie.getDate_sortie());

            TextView genreTextView = (TextView)findViewById(R.id.textDetailGenre);
            genreTextView.setText(serie.getGenre());

            TextView fansubTextView = (TextView)findViewById(R.id.textDetailFansub);
            fansubTextView.setText(serie.getFansub());

            TextView statusTextView = (TextView)findViewById(R.id.textDetailStatus);
            statusTextView.setText(serie.getStatus());

            TextView synopsisTextView = (TextView)findViewById(R.id.textDetailSynopsis);
            synopsisTextView.setText(serie.getSynopsis());

            expandableListTitle = new ArrayList<Tome>(expandableListDetail.keySet());
            expandableListAdapter = new SerieDetailsExpandableListAdapter(mainContext, expandableListTitle, expandableListDetail);
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
                Intent intent_main = new Intent(SerieDetailsActivity.this, MainActivity.class);
                startActivity(intent_main);
            case R.id.menu_tops:
                Intent intent_tops = new Intent(SerieDetailsActivity.this, TopsActivity.class);
                startActivity(intent_tops);
            case R.id.menu_list_mangas:
                Intent intent_mangas = new Intent(SerieDetailsActivity.this, MangasActivity.class);
                startActivity(intent_mangas);
            case R.id.menu_favoris:
                // Comportement du bouton "Recherche"
                return true;
            case R.id.menu_settings:
                // Comportement du bouton "Paramètres"
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}

