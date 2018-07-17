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

import japscan.gtheurillat.adapter.FavorisListAdapter;
import japscan.gtheurillat.adapter.TopsListAdapter;
import japscan.gtheurillat.db.dao.FavorisDAO;
import japscan.gtheurillat.db.model.Favoris;
import japscan.gtheurillat.model.Serie;
import japscan.gtheurillat.util.JapScanProxy;


public class FavorisActivity extends AppCompatActivity {

    ListView listView;
    ListAdapter listAdapter;
    List<Favoris> listTitle;
    List<Favoris> listDetail;
    ProgressDialog mProgressDialog;
    Context mainContext;
    FavorisDAO favDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favoris);

        //setTitle("Nouveautés");

        listView = (ListView) findViewById(R.id.lst_favoris);
        mainContext = this;

        favDAO = new FavorisDAO(this);

        new Tops().execute();

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
                Intent intent_seriedetail = new Intent(FavorisActivity.this, SerieDetailsActivity.class);
                intent_seriedetail.putExtra("SERIE_TITLE", listDetail.get(position).getName());
                intent_seriedetail.putExtra("SERIE_URL", listDetail.get(position).getUrl());
                startActivity(intent_seriedetail);
            }
        });

    }

    // Title AsyncTask
    private class Tops extends AsyncTask<Void, Void, Void> {
        String title;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(FavorisActivity.this);
            mProgressDialog.setTitle("Favoris");
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                listDetail = favDAO.selectionnerAll();
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


            listAdapter = new FavorisListAdapter(mainContext, listDetail);
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
                Intent intent_main = new Intent(FavorisActivity.this, MainActivity.class);
                startActivity(intent_main);
                return true;
            case R.id.menu_tops:
                Intent intent_tops = new Intent(FavorisActivity.this, TopsActivity.class);
                startActivity(intent_tops);
                return true;
            case R.id.menu_list_mangas:
                Intent intent_mangas = new Intent(FavorisActivity.this, MangasActivity.class);
                startActivity(intent_mangas);
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

