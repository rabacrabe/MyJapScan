package japscan.gtheurillat.adapter;

/**
 * Created by gtheurillat on 10/07/2018.
 */

import android.content.Context;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import japscan.gtheurillat.japscan.R;
import japscan.gtheurillat.model.Serie;

// Custom list item class for menu items
public class TopsListAdapter extends BaseAdapter {

    private List<Serie> items;
    private Context context;

    public TopsListAdapter(Context context, final List<Serie> items) {
        this.items = items;
        this.context = context;

    }

    public int getCount() {
        return items.size();
    }

    public Serie getItem(int position) {
        return items.get(position);
    }

    public long getItemId(int position) {
        return getItem(position).getIdxCurrentChapitre();
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        // Get the current list item
        final Serie item = items.get(position);
        // Get the layout for the list item
        final LinearLayout itemLayout = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.list_top_item, parent, false);


        // Set the text label as defined in our list item
        TextView txtNumber = (TextView) itemLayout.findViewById(R.id.topNumber);
        txtNumber.setText(item.getNumber());

        TextView txtLabel = (TextView) itemLayout.findViewById(R.id.topLibelle);
        txtLabel.setText(item.getTitle());

        return itemLayout;
    }

}