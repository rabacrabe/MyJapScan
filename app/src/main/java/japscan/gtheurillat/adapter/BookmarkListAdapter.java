package japscan.gtheurillat.adapter;

/**
 * Created by gtheurillat on 10/07/2018.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import japscan.gtheurillat.db.model.Bookmark;
import japscan.gtheurillat.db.model.Favoris;
import japscan.gtheurillat.japscan.R;

// Custom list item class for menu items
public class BookmarkListAdapter extends BaseAdapter {

    private List<Bookmark> items;
    private Context context;
    private int numItems = 0;

    public BookmarkListAdapter(Context context, final List<Bookmark> items) {
        this.items = items;
        this.context = context;
        this.numItems = items.size();
    }

    public int getCount() {
        return numItems;
    }

    public Bookmark getItem(int position) {
        return items.get(position);
    }

    public long getItemId(int position) {
        return 0;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        // Get the current list item
        final Bookmark item = items.get(position);
        // Get the layout for the list item
        final LinearLayout itemLayout = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.list_bookmark_item, parent, false);


        TextView txtLibelle = (TextView) itemLayout.findViewById(R.id.mangaLibelle);
        txtLibelle.setText(item.getSerieName());

        TextView txtGenre = (TextView) itemLayout.findViewById(R.id.mangaChapitre);
        txtGenre.setText(item.getChapterName());

        TextView txtStatus = (TextView) itemLayout.findViewById(R.id.mangaPage);
        txtStatus.setText("Page: " + item.getPageNumber());

        return itemLayout;
    }

}