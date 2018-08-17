package japscan.gtheurillat.japscan;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
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

import japscan.gtheurillat.adapter.BookmarkListAdapter;
import japscan.gtheurillat.adapter.FavorisListAdapter;
import japscan.gtheurillat.adapter.NouveautesExpandableListAdapter;
import japscan.gtheurillat.db.dao.BookmarkDAO;
import japscan.gtheurillat.db.dao.FavorisDAO;
import japscan.gtheurillat.db.model.Bookmark;
import japscan.gtheurillat.db.model.Favoris;
import japscan.gtheurillat.model.Chapitre;
import japscan.gtheurillat.model.Nouveaute;
import japscan.gtheurillat.model.Serie;
import japscan.gtheurillat.util.JapScanProxy;

public class BookmarkActivity extends AppCompatActivity {

    ListView listView;
    BookmarkListAdapter listAdapter;
    List<Bookmark> listTitle;
    ArrayList<Bookmark> listDetail;
    ProgressDialog mProgressDialog;
    Context mainContext;
    BookmarkDAO bmDAO;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_news:
                    //mTextMessage.setText(R.string.title_home);
                    Intent intent_news = new Intent(BookmarkActivity.this, NewsActivity.class);
                    startActivity(intent_news);
                    return true;
                case R.id.navigation_tops:
                    //mTextMessage.setText(R.string.title_dashboard);
                    Intent intent_tops = new Intent(BookmarkActivity.this, TopsActivity.class);
                    startActivity(intent_tops);
                    return true;
                case R.id.navigation_mangas:
                    //mTextMessage.setText(R.string.title_notifications);
                    Intent intent_mangas = new Intent(BookmarkActivity.this, MangasActivity.class);
                    startActivity(intent_mangas);
                    return true;
                case R.id.navigation_favoris:
                    Intent intent_favoris = new Intent(BookmarkActivity.this, FavorisActivity.class);
                    startActivity(intent_favoris);
                    return true;
                case R.id.navigation_bookmark:
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
                Intent intent_bookmark = new Intent(BookmarkActivity.this, BookmarkActivity.class);
                startActivity(intent_bookmark);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmark);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.navigation_bookmark);

        mainContext = this;

        listView = (ListView) findViewById(R.id.lst_bookmark);
        mainContext = this;

        bmDAO = new BookmarkDAO(this);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent_lecteur = new Intent(BookmarkActivity.this, LecteurActivity.class);
                /*
                intent_lecteur.putExtra("SERIE_TITLE", listDetail.get(position).getSerieName());
                intent_lecteur.putExtra("SERIE_URL", "");
                intent_lecteur.putExtra("CHAPITRE_TITLE", listDetail.get(position).getChapterName());
                intent_lecteur.putExtra("CHAPITRE_URL", listDetail.get(position).getPageUrl());
                */

                intent_lecteur.putExtra("SERIE_TITLE", listAdapter.getItem(position).getSerieName());
                intent_lecteur.putExtra("SERIE_URL", "");
                intent_lecteur.putExtra("CHAPITRE_TITLE", listAdapter.getItem(position).getChapterName());
                intent_lecteur.putExtra("CHAPITRE_URL", listAdapter.getItem(position).getPageUrl());

                startActivity(intent_lecteur);

                //return true;
            }
        });





        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            int pos;

            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which){
                        case DialogInterface.BUTTON_POSITIVE:
                            Bookmark bookm = bmDAO.selectionnerFromSerie(listDetail.get(pos).getSerieName());
                            bmDAO.supprimer(bookm.getId());
                            Toast.makeText(
                                    getApplicationContext(), "Bookmark supprimé!", Toast.LENGTH_SHORT
                            ).show();

                            //MAJ listview
                            listAdapter.remove(pos);
                            listAdapter.notifyDataSetChanged();

                            break;

                        case DialogInterface.BUTTON_NEGATIVE:
                            //No button clicked
                            break;
                    }
                }
            };
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int position, long id) {
                pos=position;

                AlertDialog.Builder builder = new AlertDialog.Builder(mainContext);
                builder.setMessage("Supprimer le marque page pour la serie " + listDetail.get(position).getSerieName()).setPositiveButton("Oui", dialogClickListener)
                        .setNegativeButton("Non", dialogClickListener).show();
                return true;
            }
        });

        new Bookmarks().execute();
    }


    // Title AsyncTask
    private class Bookmarks extends AsyncTask<Void, Void, Void> {
        String title;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(BookmarkActivity.this);
            mProgressDialog.setTitle("Marques pages");
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                listDetail = bmDAO.selectionnerAll();
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

                listAdapter = new BookmarkListAdapter(mainContext, listDetail);
                listView.setAdapter(listAdapter);

            }
            else{
                Toast.makeText(
                        getApplicationContext(),
                        "Aucun marque page!", Toast.LENGTH_LONG
                ).show();
            }


            mProgressDialog.dismiss();
        }
    }

}
