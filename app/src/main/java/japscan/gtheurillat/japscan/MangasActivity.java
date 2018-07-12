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
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import japscan.gtheurillat.adapter.CatalogueListAdapter;
import japscan.gtheurillat.model.Serie;
import japscan.gtheurillat.util.JapScanProxy;
import japscan.gtheurillat.widget.IndexableListView;


public class MangasActivity extends AppCompatActivity {

    //ListView listView;
    IndexableListView listView;

    ListAdapter listAdapter;
    List<Serie> listTitle;
    List<Serie> listDetail;
    ProgressDialog mProgressDialog;
    Context mainContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mangas);

        //setTitle("Nouveautés");


        mainContext = this;

        //listView = (ListView) findViewById(R.id.lst_mangas);
        listView = (IndexableListView) findViewById(R.id.lst_mangas);
        listView.setFastScrollEnabled(true);

        new Catalogue().execute();


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object listItem = listDetail.get(position);
                Toast.makeText(
                        getApplicationContext(),
                        listDetail.get(position)
                                + " -> "
                                + listDetail.get(position).getUrl(), Toast.LENGTH_SHORT
                ).show();
                Intent intent_seriedetail = new Intent(MangasActivity.this, SerieDetailsActivity.class);
                intent_seriedetail.putExtra("SERIE_TITLE", listDetail.get(position).getTitle());
                intent_seriedetail.putExtra("SERIE_URL", listDetail.get(position).getUrl());
                startActivity(intent_seriedetail);
            }
        });

    }

    // Title AsyncTask
    private class Catalogue extends AsyncTask<Void, Void, Void> {
        String title;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(MangasActivity.this);
            mProgressDialog.setTitle("Toutes les BDs");
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                JapScanProxy proxy = new JapScanProxy();
                listDetail = proxy.getCatalogue();
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


            listAdapter = new CatalogueListAdapter(mainContext, android.R.layout.simple_list_item_1, listDetail);

            listView.setAdapter(listAdapter);


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
                Intent intent_main = new Intent(MangasActivity.this, MainActivity.class);
                startActivity(intent_main);
            case R.id.menu_tops:
                Intent intent_top = new Intent(MangasActivity.this, TopsActivity.class);
                startActivity(intent_top);
            case R.id.menu_list_mangas:
                // Comportement du bouton "Rafraichir"
                return true;
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

