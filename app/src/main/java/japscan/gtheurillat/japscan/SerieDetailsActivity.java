package japscan.gtheurillat.japscan;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import japscan.gtheurillat.adapter.SerieDetailsExpandableListAdapter;
import japscan.gtheurillat.adapter.SerieDetailsTabAdapter;
import japscan.gtheurillat.db.dao.FavorisDAO;
import japscan.gtheurillat.db.model.Favoris;
import japscan.gtheurillat.model.Chapitre;
import japscan.gtheurillat.model.Serie;
import japscan.gtheurillat.model.Tome;
import japscan.gtheurillat.db.DbFavorisBaseHelper;
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
    ImageView imgFavoris;
    FavorisDAO favDAO;
    Favoris favoris;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    SerieDetailsTabAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seriedetails);

        //setTitle("Nouveautés");

        mainContext = this;



        title = getIntent().getStringExtra("SERIE_TITLE");
        url = getIntent().getStringExtra("SERIE_URL");

        favDAO = new FavorisDAO(this);



        new SerieDetails().execute();


    }

    // Title AsyncTask
    private class SerieDetails extends AsyncTask<Void, Void, Void> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(SerieDetailsActivity.this);
            mProgressDialog.setTitle(title);
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                JapScanProxy proxy = new JapScanProxy();
                serie = proxy.getSerieDetails(title, url);


            } catch (Exception e) {
                e.printStackTrace();
                showError(e.toString());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // Set title into TextView
            //TextView txttitle = (TextView) findViewById(R.id.titletxt);
            //txttitle.setText(title);

            try {
                //Initializing the tablayout
                tabLayout = (TabLayout) findViewById(R.id.tabLayout_seriedetails);

                //Adding the tabs using addTab() method
                tabLayout.addTab(tabLayout.newTab().setText("Infos"));
                tabLayout.addTab(tabLayout.newTab().setText("Chapitres"));

                tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

                //Initializing viewPager
                viewPager = (ViewPager) findViewById(R.id.pager);

                //Creating our pager adapter
                adapter = new SerieDetailsTabAdapter(getSupportFragmentManager(), tabLayout.getTabCount(), serie);

                //Adding adapter to pager
                viewPager.setAdapter(adapter);

                // Give the TabLayout the ViewPager
                TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayout_seriedetails);
                tabLayout.setupWithViewPager(viewPager);
            } catch (Exception e) {
                showError(e.toString());
            }
            mProgressDialog.dismiss();

        }
    }


    public void showError(String message) {
        AlertDialog.Builder alertDialogBuilder;
        alertDialogBuilder = new AlertDialog.Builder(mainContext);

        alertDialogBuilder.setTitle("Erreur");

        // set dialog message
        alertDialogBuilder
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton("Ok",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // if this button is clicked, close
                        // current activity
                        SerieDetailsActivity.this.finish();
                    }
                });
        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }

}

