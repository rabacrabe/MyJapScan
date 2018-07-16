package japscan.gtheurillat.japscan;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import java.util.List;

import japscan.gtheurillat.adapter.CatalogueListAdapter;
import japscan.gtheurillat.model.Serie;
import japscan.gtheurillat.util.JapScanProxy;
import japscan.gtheurillat.widget.IndexableListView;


public class MangasActivity extends AppCompatActivity {

    //ListView listView;
    IndexableListView listView;
    CatalogueListAdapter listAdapter;
    List<Serie> listTitle;
    List<Serie> listDetail;
    ProgressDialog mProgressDialog;
    Context mainContext;
    SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mangas);

        //setTitle("Nouveautés");


        mainContext = this;

        //listView = (ListView) findViewById(R.id.lst_mangas);
        listView = (IndexableListView) findViewById(R.id.lst_mangas);
        listView.setFastScrollEnabled(true);

        searchView = (SearchView)findViewById(R.id.recherche_manga);


        new Catalogue().execute();

        // Get the intent, verify the action and get the query
        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            //doMySearch(query);
        }


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Object listItem = listDetail.get(position);
                Object listItem = listAdapter.getItem(position);
                Toast.makeText(
                        getApplicationContext(),
                        listAdapter.getItem(position)
                                + " -> "
                                + listAdapter.getItem(position).getUrl(), Toast.LENGTH_SHORT
                ).show();
                Intent intent_seriedetail = new Intent(MangasActivity.this, SerieDetailsActivity.class);
                intent_seriedetail.putExtra("SERIE_TITLE", listAdapter.getItem(position).getTitle());
                intent_seriedetail.putExtra("SERIE_URL", listAdapter.getItem(position).getUrl());
                startActivity(intent_seriedetail);
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String arg0) {
                // TODO Auto-generated method stub
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                // TODO Auto-generated method stub

                listAdapter.getFilter().filter(query);

                return false;
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
                return true;
            case R.id.menu_tops:
                Intent intent_top = new Intent(MangasActivity.this, TopsActivity.class);
                startActivity(intent_top);
                return true;
            case R.id.menu_list_mangas:
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

