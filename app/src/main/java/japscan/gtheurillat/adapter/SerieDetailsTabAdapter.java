package japscan.gtheurillat.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import japscan.gtheurillat.japscan.SerieDetailsTabChapitres;
import japscan.gtheurillat.japscan.SerieDetailsTabInfos;
import japscan.gtheurillat.model.Serie;

public class SerieDetailsTabAdapter extends FragmentStatePagerAdapter {
    //integer to count number of tabs
    int tabCount;
    String title;
    String url;
    Serie serie;
    private String tabTitles[] = new String[] { "Infos", "Chapitres"};

    //Constructor to the class
    public SerieDetailsTabAdapter(FragmentManager fm, int tabCount, Serie serie) {
        super(fm);
        //Initializing tab count
        this.serie = serie;
        this.tabCount= tabCount;
        this.title = serie.getTitle();
        this.url = serie.getUrl();
    }

    //Overriding method getItem
    @Override
    public Fragment getItem(int position) {
        //Returning the current tabs
        switch (position) {
            case 0:
                return SerieDetailsTabInfos.newInstance(this.serie);

            case 1:
                return SerieDetailsTabChapitres.newInstance(this.serie);
            default:
                return null;
        }
    }

    //Overriden method getCount to get the number of tabs
    @Override
    public int getCount() {
        return tabCount;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        return tabTitles[position];
    }
}
