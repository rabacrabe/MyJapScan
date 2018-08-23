package japscan.gtheurillat.japscan;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.squareup.picasso.Transformation;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import japscan.gtheurillat.adapter.TopsListAdapter;
import japscan.gtheurillat.db.dao.BookmarkDAO;
import japscan.gtheurillat.db.model.Bookmark;
import japscan.gtheurillat.model.Chapitre;
import japscan.gtheurillat.model.Page;
import japscan.gtheurillat.model.Serie;
import japscan.gtheurillat.util.JapScanProxy;
import japscan.gtheurillat.util.picasso.CropTransformation;

import static java.lang.Thread.sleep;

public class LecteurActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    ProgressDialog mProgressDialog;
    Context mainContext;
    String chapitreTitle;
    String chapitreUrl;
    Serie serie;
    TextView titrePage;
    ImageView img;
    Picasso picasso;
    Integer currentImageindex;
    Chapitre currentChapitre;
    TextView lecteurTitreChapitre;
    TextView lecteurTitreSerie;
    NavigationView navigationView;
    Spinner navigationPagination;
    TextView navigationPaginationSuffix;
    ArrayAdapter<String> paginationAdapter;
    BookmarkDAO bmDAO;
    Bookmark bookmark;
    Integer idx_selected;
    int disply_width;
    String pagePosition = "all";
    boolean isDoublesPages = false;
    View headerView;
    int check = 0;
    Switch swDoublePage;
    boolean isDroiteAGauche = false;
    int onDoublesPageNb = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mainContext = this;


        setContentView(R.layout.activity_lecteur);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        bmDAO = new BookmarkDAO(this);

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        headerView = navigationView.getHeaderView(0);

        titrePage = (TextView) findViewById(R.id.lecteur_titre);
        lecteurTitreChapitre = (TextView) headerView.findViewById(R.id.lecteur_menu_titre_chapitre);
        lecteurTitreSerie = (TextView) headerView.findViewById(R.id.lecteur_menu_titre_serie);

        lecteurTitreSerie.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Intent intent_seriedetail = new Intent(LecteurActivity.this, SerieDetailsActivity.class);
                intent_seriedetail.putExtra("SERIE_TITLE", serie.getTitle());
                intent_seriedetail.putExtra("SERIE_URL", serie.getUrl());
                startActivity(intent_seriedetail);
            }
        });



        img = (ImageView)findViewById(R.id.lecteur_image);

        img.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Bookmark newBookmark = new Bookmark();
                newBookmark.setSerieName(serie.getTitle());
                newBookmark.setChapterName(chapitreTitle);
                newBookmark.setPageNumber(String.valueOf(idx_selected));
                newBookmark.setPageUrl(chapitreUrl);


                bookmark = null;
                bookmark = bmDAO.selectionnerFromSerie(serie.getTitle());

                if (bookmark == null) {
                    bmDAO.ajouter(newBookmark);
                    Toast.makeText(getBaseContext(), "Serie bookmarked (new bookmark)", Toast.LENGTH_SHORT).show();
                }
                else {
                    newBookmark.setId(bookmark.getId());
                    bmDAO.modifier(newBookmark);
                    Toast.makeText(getBaseContext(), "Serie bookmarked (update bookmark)", Toast.LENGTH_SHORT).show();
                }

                return true;
            }
        });


        navigationPagination = (Spinner)headerView.findViewById(R.id.nav_pagination_lst);
        navigationPaginationSuffix = (TextView)headerView.findViewById(R.id.nav_pagination_suffix);


        navigationPagination.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // your code here
                if(++check > 1) {
                    Chapitre currentChapitre = serie.getLstChapitres().get(1);
                    Page selectedPage = currentChapitre.getLstPage().get(position);
                    /*
                    Toast.makeText(
                            getApplicationContext(),
                            "Go to page -> " + selectedPage.getTitle() + " " + selectedPage.getUrl(), Toast.LENGTH_SHORT
                    ).show();*/

                    goToPage(selectedPage.getUrl(), String.valueOf(position+1));
                }
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });



        //serieTitle = getIntent().getStringExtra("SERIE_TITLE");
        //serieUrl = getIntent().getStringExtra("SERIE_URL");
        chapitreTitle = (String)getIntent().getSerializableExtra("CHAPITRE_TITLE");
        chapitreUrl = (String)getIntent().getSerializableExtra("CHAPITRE_URL");


        titrePage.setText(chapitreTitle);
        lecteurTitreChapitre.setText(chapitreTitle);

        picasso = Picasso.with(mainContext);

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        disply_width = size.x;

        new Lecteur().execute();


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                */
                goToNextPage();


            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);



    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

/*
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        getMenuInflater().inflate(R.menu.lecteur, menu);
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
*/
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.lecteur_nav_page_suivant) {
            this.goToNextPage();

        } else if (id == R.id.lecteur_nav_page_precedent) {
           this.goToPreviousPage();

        } else if (id == R.id.lecteur_nav_chapitre_suivant) {
           this.goToNextChapter();

        } else if (id == R.id.lecteur_nav_chapitre_precedent) {
            this.goToPreviousChapter();

        } else if (id == R.id.nav_dp_switch) {

            isDoublesPages = !item.isChecked();
            item.setChecked(isDoublesPages);

            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            Menu menuNav=navigationView.getMenu();
            MenuItem page_sens = menuNav.findItem(R.id.nav_dp_sens);

            if (isDoublesPages == true) {
                item.setTitle("Doubles Pages");
                item.setIcon(ContextCompat.getDrawable(this, R.drawable.ic_border_vertical_black_24dp));
                pagePosition = "left";
                Toast.makeText(
                        getApplicationContext(),
                        "Mode doubles pages activés", Toast.LENGTH_SHORT
                ).show();
                onDoublesPageNb = 1;

                page_sens.setEnabled(true);
                this.reloadPage();
                //@drawable/ic_border_vertical_black_24dp
            }
            else {
                item.setTitle("Simples Pages");
                item.setIcon(ContextCompat.getDrawable(this, R.drawable.ic_border_outer_black_24dp));
                pagePosition = "all";
                Toast.makeText(
                        getApplicationContext(),
                        "Mode simples pages activés", Toast.LENGTH_SHORT
                ).show();

                onDoublesPageNb = 0;

                page_sens.setEnabled(false);
                this.reloadPage();
            }

        }else if (id == R.id.nav_dp_sens) {
            isDroiteAGauche = !item.isChecked();
            item.setChecked(isDroiteAGauche);

            if (isDroiteAGauche == true) {
                item.setTitle("De droite à gauche (<-)");
                item.setIcon(ContextCompat.getDrawable(this, R.drawable.ic_undo_black_24dp));
                pagePosition = "right";
                Toast.makeText(
                        getApplicationContext(),
                        "Lecture de droite à gauche activés", Toast.LENGTH_SHORT
                ).show();

                this.reloadPage();
                //@drawable/ic_border_vertical_black_24dp
            }
            else {
                item.setTitle("De gauche à droite (->)");
                item.setIcon(ContextCompat.getDrawable(this, R.drawable.ic_redo_black_24dp));
                pagePosition = "left";
                Toast.makeText(
                        getApplicationContext(),
                        "Lecture de gauche à droite activés", Toast.LENGTH_SHORT
                ).show();
                this.reloadPage();
            }

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void reloadPage() {
        Toast.makeText(
                getApplicationContext(),
                "Reload page", Toast.LENGTH_SHORT
        ).show();


        Page currentPage = currentChapitre.getLstPage().get(currentImageindex);
        picasso.invalidate(currentPage.getImgUrl());
        picasso.load(currentPage.getImgUrl())
                .transform(new ResizeTransformation(disply_width, pagePosition))
                .into(img);
    }

    public void goToPage(String pageUrl, String pageNumber) {
        Toast.makeText(
                getApplicationContext(),
                "Go page -> " + pageNumber, Toast.LENGTH_SHORT
        ).show();

        if (isDoublesPages == true) {
            if (isDroiteAGauche == true) {
                pagePosition = "right";
            }else {
                pagePosition = "left";
            }
        }

        chapitreUrl = pageUrl;
        new Lecteur().execute();

    }

    public void goToNextPage() {
        if (isDoublesPages == true && onDoublesPageNb == 1) {
            if (pagePosition == "left") {pagePosition = "right";}
            else if (pagePosition == "right") {pagePosition = "left";}

            onDoublesPageNb = 2;

            this.reloadPage();
        }
        else {
            if (isDoublesPages == true) {
                if (pagePosition == "left") {pagePosition = "right";}
                else if (pagePosition == "right") {pagePosition = "left";}
                onDoublesPageNb = 1;
            }

            if (currentImageindex + 1 == currentChapitre.getLstPage().size()) {
                this.goToNextChapter();
            } else {
                Page nexPage = currentChapitre.getLstPage().get(currentImageindex + 1);

                Toast.makeText(
                        getApplicationContext(),
                        "Next page", Toast.LENGTH_SHORT
                ).show();

                chapitreUrl = nexPage.getUrl();
                new Lecteur().execute();
            }
        }
    }

    public void goToPreviousPage() {
        if (isDoublesPages && onDoublesPageNb == 2) {
            if (pagePosition == "left") {pagePosition = "right";}
            else if (pagePosition == "right") {pagePosition = "left";}

            onDoublesPageNb = 1;

            this.reloadPage();
        }
        else {
            if (isDoublesPages == true) {
                if (isDroiteAGauche == true) {pagePosition = "right";}
                else {pagePosition = "left";}

                onDoublesPageNb = 2;
            }

            if (currentImageindex == 0) {
                this.goToPreviousChapter();
            } else {
                Page precPage = currentChapitre.getLstPage().get(currentImageindex - 1);

                Toast.makeText(
                        getApplicationContext(),
                        "Precedent page", Toast.LENGTH_SHORT
                ).show();

                chapitreUrl = precPage.getUrl();
                new Lecteur().execute();
            }
        }
    }

    public void goToNextChapter() {

        Chapitre nextChapitre = serie.getLstChapitres().get(2);

        Toast.makeText(
                getApplicationContext(),
                "Next Chapter", Toast.LENGTH_SHORT
        ).show();

        lecteurTitreChapitre.setText(nextChapitre.getTitle());
        chapitreTitle  = nextChapitre.getTitle();
        titrePage.setText(chapitreTitle);
        chapitreUrl  = nextChapitre.getUrl();

        if (isDoublesPages == true) {
            if (isDroiteAGauche == true) {pagePosition = "right";}
            else {pagePosition = "left";}

            onDoublesPageNb = 1;
        }

        chapitreUrl=nextChapitre.getUrl();
        new Lecteur().execute();
    }

    public void goToPreviousChapter() {
        Chapitre precChapitre = serie.getLstChapitres().get(0);

        if (precChapitre == null) {
            Toast.makeText(
                    getApplicationContext(),
                    "Pas de chapitre precedent", Toast.LENGTH_SHORT
            ).show();

        }
        else {
            Toast.makeText(
                    getApplicationContext(),
                    "Precedent Chapitre -> " + precChapitre.getTitle(), Toast.LENGTH_SHORT
            ).show();

            lecteurTitreChapitre.setText(precChapitre.getTitle());
            chapitreTitle  = precChapitre.getTitle();
            titrePage.setText(chapitreTitle);
            chapitreUrl  = precChapitre.getUrl();

            if (isDoublesPages == true) {
                if (isDroiteAGauche == true) {pagePosition = "right";}
                else {pagePosition = "left";}

                onDoublesPageNb = 1;
            }

            chapitreUrl = precChapitre.getUrl();
            new Lecteur().execute();
        }
    }

    // Title AsyncTask
    public class Lecteur extends AsyncTask<Void, Void, Void> {
        String title;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            check = 0;
            mProgressDialog = new ProgressDialog(LecteurActivity.this);
            mProgressDialog.setTitle(chapitreTitle);
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                JapScanProxy proxy = new JapScanProxy();
                Log.e("getLecteurInfos", "Recuperation des donnes du chapitre " + chapitreTitle + " URL: " + chapitreUrl);
                serie = proxy.getLecteurInfos(chapitreTitle, chapitreUrl);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            //chargement de l'image
            //mise en place des elements dans la page

            if (serie != null) {
                lecteurTitreSerie.setText(serie.getTitle());

                /*
                //0 = precedent chapitre
                //1 = current chapitre
                //2 = next chapitre
                currentChapitre = serie.getLstChapitres().get(1);
                */

                //Log.e("AFFICHAGE", "nb chapitre -> " + serie.getLstChapitres().size() + " ; current=" + serie.getIdxCurrentChapitre());

                currentChapitre = serie.getLstChapitres().get(serie.getIdxCurrentChapitre());

                ArrayList<String> paginationArray = new ArrayList<String>();
                idx_selected = 0;
                Integer idx = 0;
                for (Page page : currentChapitre.getLstPage()) {
                    if (page.isSelected() == true) {

                        Log.e("LOAD IMG", page.getImgUrl());
                        Log.e("POS IMG", pagePosition);

                        currentImageindex = idx;
                        picasso.invalidate(page.getImgUrl());
                        picasso.load(page.getImgUrl())
                                .transform(new ResizeTransformation(disply_width, pagePosition))
                                .into(img);

                        Integer nbpages = currentChapitre.getLstPage().size();

                        Toast.makeText(
                                getApplicationContext(),
                                "Page " + String.valueOf(idx + 1) + " / " + String.valueOf(nbpages), Toast.LENGTH_SHORT
                        ).show();

                        idx_selected = idx;

                        titrePage.setText(chapitreTitle + " : " + String.valueOf(idx + 1) + " / " + String.valueOf(nbpages));

                    }



                    paginationArray.add(String.valueOf(idx + 1));

                    idx += 1;
                }

                paginationAdapter = new ArrayAdapter<String>(LecteurActivity.this, android.R.layout.simple_spinner_item, paginationArray);
                navigationPagination.setAdapter(paginationAdapter);
                navigationPagination.setSelection(idx_selected);

                navigationPaginationSuffix.setText(" / " + String.valueOf(currentChapitre.getLstPage().size()));

            }else
            {
                Toast.makeText(
                        getApplicationContext(),
                        "Impossible de recuperer la page", Toast.LENGTH_SHORT
                ).show();

                //picasso.load().into(img);
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
                Intent intent_main = new Intent(LecteurActivity.this, MainActivity.class);
                startActivity(intent_main);
                return true;
            case R.id.menu_tops:
                Intent intent_tops = new Intent(LecteurActivity.this, TopsActivity.class);
                startActivity(intent_tops);
                return true;
            case R.id.menu_list_mangas:
                Intent intent_mangas = new Intent(LecteurActivity.this, MangasActivity.class);
                startActivity(intent_mangas);
                return true;
            case R.id.menu_favoris:
                Intent intent_favoris = new Intent(LecteurActivity.this, FavorisActivity.class);
                startActivity(intent_favoris);
                return true;
            case R.id.menu_bookmark:
                Intent intent_bookmark = new Intent(LecteurActivity.this, BookmarkActivity.class);
                startActivity(intent_bookmark);
                return true;
            case R.id.menu_settings:
                // Comportement du bouton "Paramètres"
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public class ResizeTransformation implements Transformation {

        //la largeur voulue
        private int targetWidth;
        private String targetPosition;

        public ResizeTransformation(int width, String position) {
            this.targetWidth = width;
            this.targetPosition = position;
        }

        @Override
        public Bitmap transform(Bitmap source) {
            double aspectRatio = (double) source.getHeight() / (double) source.getWidth();
            int targetHeight = (int) (targetWidth * aspectRatio);
            Bitmap resultScale = Bitmap.createScaledBitmap(source, targetWidth, targetHeight, false);

            Bitmap result;

            Log.e("Target position", targetPosition);



            if (targetPosition == "left") {
                result = Bitmap.createBitmap(resultScale, 0,0,targetWidth/2, targetHeight);
            }
            else if (targetPosition == "right") {
                result = Bitmap.createBitmap(resultScale, targetWidth/2,0,targetWidth/2, targetHeight);
            }
            else {
                result = resultScale;
            }


            if (result != source) {
                // Same bitmap is returned if sizes are the same
                source.recycle();
            }
            return result;
        }

        @Override
        public String key() {
            return "ResizeTransformation"+targetWidth;
        }
    }

}
