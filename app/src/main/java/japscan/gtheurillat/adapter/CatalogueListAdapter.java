package japscan.gtheurillat.adapter;

/**
 * Created by gtheurillat on 10/07/2018.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.SectionIndexer;
import android.widget.TextView;

import java.util.List;

import japscan.gtheurillat.japscan.R;
import japscan.gtheurillat.model.Serie;
import japscan.gtheurillat.util.StringMatcher;

// Custom list item class for menu items
public class CatalogueListAdapter extends ArrayAdapter<Serie> implements SectionIndexer {

    private List<Serie> items;
    private Context context;
    private int numItems = 0;
    private String mSections = "#ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    public CatalogueListAdapter(Context context, int textViewResourceId, final List<Serie> items) {
        super(context, textViewResourceId, items);

        this.items = items;
        this.context = context;
        this.numItems = items.size();

    }

    public int getCount() {
        return numItems;
    }

    public Serie getItem(int position) {
        return items.get(position);
    }

    public long getItemId(int position) {
        return 0;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        // Get the current list item
        final Serie item = items.get(position);
        // Get the layout for the list item
        final LinearLayout itemLayout = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.list_manga_item, parent, false);


        // Set the text label as defined in our list item
        //TextView txtNumber = (TextView) itemLayout.findViewById(R.id.topNumber);
        //txtNumber.setText(item.getNumber());

        TextView txtLibelle = (TextView) itemLayout.findViewById(R.id.mangaLibelle);
        txtLibelle.setText(item.getTitle());

        TextView txtGenre = (TextView) itemLayout.findViewById(R.id.mangaGenre);
        txtGenre.setText(item.getGenre());

        TextView txtStatus = (TextView) itemLayout.findViewById(R.id.mangaStatus);
        txtStatus.setText(item.getStatus());

        return itemLayout;
    }

    @Override
    public int getPositionForSection(int section) {
        // If there is no item for current section, previous section will be selected
        for (int i = section; i >= 0; i--) {
            for (int j = 0; j < getCount(); j++) {
                if (i == 0) {
                    // For numeric section
                    for (int k = 0; k <= 9; k++) {
                        if (StringMatcher.match(String.valueOf(getItem(j).getTitle().charAt(0)), String.valueOf(k)))
                            return j;
                    }
                } else {
                    if (StringMatcher.match(String.valueOf(getItem(j).getTitle().charAt(0)), String.valueOf(mSections.charAt(i))))
                        return j;
                }
            }
        }
        return 0;
    }

    @Override
    public int getSectionForPosition(int position) {
        return 0;
    }

    @Override
    public Object[] getSections() {
        String[] sections = new String[mSections.length()];
        for (int i = 0; i < mSections.length(); i++)
            sections[i] = String.valueOf(mSections.charAt(i));
        return sections;
    }
}
