package japscan.gtheurillat.japscan;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import japscan.gtheurillat.adapter.FavorisListAdapter;
import japscan.gtheurillat.adapter.NouveautesExpandableListAdapter;
import japscan.gtheurillat.db.dao.FavorisDAO;
import japscan.gtheurillat.db.model.Favoris;
import japscan.gtheurillat.model.Chapitre;
import japscan.gtheurillat.model.Nouveaute;
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

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_news:
                    //mTextMessage.setText(R.string.title_home);
                    Intent intent_news = new Intent(FavorisActivity.this, NewsActivity.class);
                    startActivity(intent_news);
                    return true;
                case R.id.navigation_tops:
                    //mTextMessage.setText(R.string.title_dashboard);
                    Intent intent_tops = new Intent(FavorisActivity.this, TopsActivity.class);
                    startActivity(intent_tops);
                    return true;
                case R.id.navigation_mangas:
                    //mTextMessage.setText(R.string.title_notifications);
                    Intent intent_mangas = new Intent(FavorisActivity.this, MangasActivity.class);
                    startActivity(intent_mangas);
                    return true;
                case R.id.navigation_favoris:

                    return true;
                case R.id.navigation_bookmark:
                    Intent intent_bookmark = new Intent(FavorisActivity.this, BookmarkActivity.class);
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

                return true;
            case R.id.menu_bookmark:
                Intent intent_bookmark = new Intent(FavorisActivity.this, BookmarkActivity.class);
                startActivity(intent_bookmark);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favoris);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.navigation_favoris);

        mainContext = this;

        listView = (ListView) findViewById(R.id.lst_favoris);
        mainContext = this;

        favDAO = new FavorisDAO(this);

        new Tops().execute();
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

            if (listDetail.size() > 0) {
                listAdapter = new FavorisListAdapter(mainContext, listDetail);
                listView.setAdapter(listAdapter);

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Object listItem = listDetail.get(position);

                        Intent intent_seriedetail = new Intent(FavorisActivity.this, SerieDetailsActivity.class);
                        intent_seriedetail.putExtra("SERIE_TITLE", listDetail.get(position).getName());
                        intent_seriedetail.putExtra("SERIE_URL", listDetail.get(position).getUrl());
                        startActivity(intent_seriedetail);
                    }
                });
            } else {
                Toast.makeText(
                        getApplicationContext(),
                        "Aucun favoris!", Toast.LENGTH_LONG
                ).show();
            }

            mProgressDialog.dismiss();
        }
    }
}
