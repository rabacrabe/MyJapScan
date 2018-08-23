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


public class SerieDetailsTabChapitres extends Fragment {
    //Overriden method onCreateView
    private String mTitre;
    private String mUrl;
    private ArrayList<Chapitre> mLstChapitres;
    private ArrayList<Tome> mLstTomes;
    ExpandableListView expandableListView;
    ExpandableListAdapter expandableListAdapter;
    List<Tome> expandableListTitle;
    HashMap<Tome, List<Chapitre>> expandableListDetail;



    public static SerieDetailsTabChapitres newInstance(Serie serie) {
        Bundle args = new Bundle();

        args.putString("TITRE", serie.getTitle());
        args.putString("URL", serie.getUrl());
        args.putSerializable("LIST_CHAPITRES", serie.getLstChapitres());
        args.putSerializable("LIST_TOMES", serie.getLstTomes());

        //args.putParcelableArrayList("LIST_CHAPITRES", serie.getLstChapitres());
        //args.putParcelableArrayList("LIST_TOMES", serie.getLstTomes());

        SerieDetailsTabChapitres fragment = new SerieDetailsTabChapitres();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTitre = getArguments().getString("TITRE");
        mUrl = getArguments().getString("URL");
        mLstTomes = (ArrayList<Tome>)getArguments().getSerializable("LIST_TOMES");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab_seriedetails_chapitres, container, false);

        TextView titreTextView = (TextView) view.findViewById(R.id.textMainTitle);
        titreTextView.setText(mTitre);


        expandableListView = (ExpandableListView) view.findViewById(R.id.lst_dernieres_sorties);

        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {
            /*
            Toast.makeText(getApplicationContext(),
                    expandableListTitle.get(groupPosition) + " List Expanded.",
                    Toast.LENGTH_SHORT).show();
                    */
            }
        });

        expandableListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {
            /*
            Toast.makeText(getApplicationContext(),
                    expandableListTitle.get(groupPosition) + " List Collapsed.",
                    Toast.LENGTH_SHORT).show();
                    */

            }
        });

        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {

                String chapitre_title = expandableListDetail.get(
                                                                expandableListTitle.get(groupPosition)).get(
                                                                childPosition).getTitle();
                String chapitre_url = expandableListDetail.get(
                                                                expandableListTitle.get(groupPosition)).get(
                                                                childPosition).getUrl();


                Toast.makeText(
                        getActivity().getApplicationContext(),
                        chapitre_title
                                + " -> "
                                + chapitre_url, Toast.LENGTH_SHORT
                ).show();

                //on clear tout les  arguments
                setArguments(null);

                Intent intent_lecteur = new Intent(getActivity(), LecteurActivity.class);
                intent_lecteur.putExtra("SERIE_TITLE", mTitre);
                intent_lecteur.putExtra("SERIE_URL", mUrl);
                intent_lecteur.putExtra("CHAPITRE_TITLE", chapitre_title);
                intent_lecteur.putExtra("CHAPITRE_URL", chapitre_url);
                getActivity().startActivity(intent_lecteur);

                return true;
            }
        });


        expandableListDetail = new HashMap<Tome, List<Chapitre>>();
        expandableListTitle = new ArrayList<Tome>();
        for (Tome tome : mLstTomes) {
            expandableListDetail.put(tome, tome.getLstChapitres());
            expandableListTitle.add(tome);
        }
        expandableListAdapter = new SerieDetailsExpandableListAdapter(getActivity(), expandableListTitle, expandableListDetail);
        expandableListView.setAdapter(expandableListAdapter);

        return view;
    }
}

