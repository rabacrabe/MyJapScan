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

import japscan.gtheurillat.adapter.BookmarkListAdapter;
import japscan.gtheurillat.adapter.FavorisListAdapter;
import japscan.gtheurillat.db.dao.BookmarkDAO;
import japscan.gtheurillat.db.dao.FavorisDAO;
import japscan.gtheurillat.db.model.Bookmark;
import japscan.gtheurillat.db.model.Favoris;


public class BookmarkActivity extends AppCompatActivity {

    ListView listView;
    ListAdapter listAdapter;
    List<Bookmark> listTitle;
    List<Bookmark> listDetail;
    ProgressDialog mProgressDialog;
    Context mainContext;
    BookmarkDAO bmDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmark);

        //setTitle("Nouveautés");

        listView = (ListView) findViewById(R.id.lst_bookmark);
        mainContext = this;

        bmDAO = new BookmarkDAO(this);

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

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Object listItem = listDetail.get(position);
/*
                Toast.makeText(
                        getApplicationContext(),
                        listDetail.get(position)
                                + listDetail.get(position).getSerieName()
                                + " -> "
                                + listDetail.get(position).getPageUrl(), Toast.LENGTH_SHORT
                ).show();
*/
                        Intent intent_lecteur = new Intent(BookmarkActivity.this, LecteurActivity.class);
                        intent_lecteur.putExtra("SERIE_TITLE", listDetail.get(position).getSerieName());
                        intent_lecteur.putExtra("SERIE_URL", "");
                        intent_lecteur.putExtra("CHAPITRE_TITLE", listDetail.get(position).getChapterName());
                        intent_lecteur.putExtra("CHAPITRE_URL", listDetail.get(position).getPageUrl());
                        startActivity(intent_lecteur);
                    }
                });
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_home:
                Intent intent_main = new Intent(BookmarkActivity.this, MainActivity.class);
                startActivity(intent_main);
                return true;
            case R.id.menu_tops:
                Intent intent_tops = new Intent(BookmarkActivity.this, TopsActivity.class);
                startActivity(intent_tops);
                return true;
            case R.id.menu_list_mangas:
                Intent intent_mangas = new Intent(BookmarkActivity.this, MangasActivity.class);
                startActivity(intent_mangas);
                return true;
            case R.id.menu_favoris:
                Intent intent_favoris = new Intent(BookmarkActivity.this, FavorisActivity.class);
                startActivity(intent_favoris);
                return true;
            case R.id.menu_bookmark:
                return true;
            case R.id.menu_settings:
                // Comportement du bouton "Paramètres"
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}

