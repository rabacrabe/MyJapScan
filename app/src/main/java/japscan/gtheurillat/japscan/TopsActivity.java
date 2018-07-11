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
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import japscan.gtheurillat.adapter.NouveautesExpandableListAdapter;
import japscan.gtheurillat.adapter.TopsListAdapter;
import japscan.gtheurillat.model.Chapitre;
import japscan.gtheurillat.model.Nouveaute;
import japscan.gtheurillat.model.Serie;
import japscan.gtheurillat.util.JapScanProxy;


public class TopsActivity extends AppCompatActivity {

    ListView listView;
    ListAdapter listAdapter;
    List<Serie> listTitle;
    List<Serie> listDetail;
    ProgressDialog mProgressDialog;
    Context mainContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tops);

        //setTitle("Nouveautés");

        listView = (ListView) findViewById(R.id.lst_tops_semaine);
        mainContext = this;

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
            }
        });

    }

    // Title AsyncTask
    private class Tops extends AsyncTask<Void, Void, Void> {
        String title;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(TopsActivity.this);
            mProgressDialog.setTitle("Tops de la semaine");
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                JapScanProxy proxy = new JapScanProxy();
                listDetail = proxy.getTops();
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


            listAdapter = new TopsListAdapter(mainContext, listDetail);
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
                Intent intent_main = new Intent(TopsActivity.this, MainActivity.class);
                startActivity(intent_main);
            case R.id.menu_tops:
                return true;
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

