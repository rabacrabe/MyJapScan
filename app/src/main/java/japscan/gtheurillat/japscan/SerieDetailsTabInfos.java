package japscan.gtheurillat.japscan;

import android.app.AlertDialog;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import japscan.gtheurillat.adapter.SerieDetailsExpandableListAdapter;
import japscan.gtheurillat.db.dao.FavorisDAO;
import japscan.gtheurillat.db.model.Favoris;
import japscan.gtheurillat.model.Chapitre;
import japscan.gtheurillat.model.Serie;
import japscan.gtheurillat.model.Tome;
import japscan.gtheurillat.util.JapScanProxy;
import japscan.gtheurillat.util.picasso.ResizeTransformation;


public class SerieDetailsTabInfos extends Fragment {

    private String mTitre;
    private String mAuteur;
    private String mDate_sortie;
    private String mGenre;
    private String mFansub;
    private String mSynopsis;
    private String mStatus;
    private String mUrl;
    Picasso picasso;


    ImageView imgFavoris;
    ImageView imgSerie;
    FavorisDAO favDAO;
    Favoris favoris;
    Boolean isFavoris;

    public static SerieDetailsTabInfos newInstance(Serie serie) {
        Bundle args = new Bundle();
        args.putString("TITRE", serie.getTitle());
        args.putString("AUTEUR", serie.getAuteur());
        args.putString("DATE_SORTIE", serie.getDate_sortie());
        args.putString("GENRE", serie.getGenre());
        args.putString("FANSUB", serie.getFansub());
        args.putString("SYNOPSIS", serie.getSynopsis());
        args.putString("STATUS", serie.getStatus());
        args.putString("URL", serie.getUrl());

        SerieDetailsTabInfos fragment = new SerieDetailsTabInfos();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTitre = getArguments().getString("TITRE");
        mAuteur = getArguments().getString("AUTEUR");
        mDate_sortie = getArguments().getString("DATE_SORTIE");
        mGenre = getArguments().getString("GENRE");
        mFansub = getArguments().getString("FANSUB");
        mStatus = getArguments().getString("STATUS");
        mSynopsis = getArguments().getString("SYNOPSIS");
        mUrl = getArguments().getString("URL");


        picasso = Picasso.with(getActivity());
        favDAO = new FavorisDAO(getActivity());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab_seriedetails_infos, container, false);

        TextView titreTextView = (TextView) view.findViewById(R.id.textMainTitle);
        titreTextView.setText(mTitre);

        TextView auteurTextView = (TextView) view.findViewById(R.id.textDetailAuteur);
        auteurTextView.setText(mAuteur);

        TextView dateTextView = (TextView) view.findViewById(R.id.textDetailDate);
        dateTextView.setText(mDate_sortie);

        TextView genreTextView = (TextView) view.findViewById(R.id.textDetailGenre);
        genreTextView.setText(mGenre);

        TextView fansubTextView = (TextView) view.findViewById(R.id.textDetailFansub);
        fansubTextView.setText(mFansub);

        TextView statusTextView = (TextView) view.findViewById(R.id.textDetailStatus);
        statusTextView.setText(mStatus);

        TextView synopsisTextView = (TextView) view.findViewById(R.id.textDetailSynopsis);
        synopsisTextView.setText(mSynopsis);
        synopsisTextView.setMovementMethod(new ScrollingMovementMethod());

        imgSerie = (ImageView) view.findViewById(R.id.imgSerie);
        String imgSerieUrl = "https://cdn.japscan.cc/img/mangas/" + mUrl.split("/")[5] + ".jpg";

        Log.e("getSeriefos", "Recuperation de l'image " + imgSerieUrl);

        picasso.load(imgSerieUrl)
                //.transform(new ResizeTransformation(500,"all"))
                .into(imgSerie);


        imgFavoris = (ImageView) view.findViewById(R.id.imgFavoris);

        isFavoris = false;
        favoris = favDAO.selectionner(mUrl);
        if (favoris != null) {
            imgFavoris.setImageResource(android.R.drawable.btn_star_big_on);
            isFavoris = true;
        }

        imgFavoris.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (isFavoris == true) {
                    favDAO.supprimer(favoris);

                    imgFavoris.setImageResource(android.R.drawable.btn_star_big_off);
                    isFavoris=false;

                    Toast.makeText(getActivity().getApplicationContext(),"Supprimé des series favorites", Toast.LENGTH_LONG).show();
                }
                else {
                    favoris = new Favoris(mTitre, mUrl, mGenre, mStatus);
                    favDAO.ajouter(favoris);

                    imgFavoris.setImageResource(android.R.drawable.btn_star_big_on);
                    isFavoris=true;

                    Toast.makeText(getActivity().getApplicationContext(),"Ajouté aux series favorites", Toast.LENGTH_LONG).show();

                }
            }
        });



        return view;
    }

}

