package me.marcooliveira.smartbuy.adapters;

import android.app.Activity;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.koushikdutta.ion.Ion;

import java.util.ArrayList;

import me.marcooliveira.smartbuy.model.Product;
import me.marcooliveira.smartbuy.R;

/**
 * SmartBUY project
 * Created by marco on 1/18/16.
 */
public class ProductAdapter extends ArrayAdapter<Product> {
    private Activity context;
    private ArrayList<Product> data;
    private View lastSelected;
    private int lastSelectedIdx = -1;

    public ProductAdapter(Activity context, ArrayList<Product> data) {
        super(context, R.layout.list_item, data);
        this.context = context;
        this.data = data;
    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.list_item, null, true);

        ImageView image = (ImageView) rowView.findViewById(R.id.product_list_image);
        TextView name = (TextView) rowView.findViewById(R.id.product_list_name);
        TextView manufacturer = (TextView) rowView.findViewById(R.id.product_list_manufacturer);
        TextView price = (TextView) rowView.findViewById(R.id.product_list_price);
        View selected = rowView.findViewById(R.id.product_list_selected);

        // Ion lib for image download and cache - https://github.com/koush/ion
        Ion.with(image)
                .placeholder(R.mipmap.smartphone)
                .error(R.mipmap.smartphone)
                .load(data.get(position).getImage());

        name.setText(data.get(position).getName());
        manufacturer.setText(data.get(position).getManufacturer());
        price.setText("$" + data.get(position).getSalePrice().toString());

        if (lastSelectedIdx == position) {
            selected.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorAccent));
        }
        else {
            selected.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.transparent));
        }

        return rowView;
    }

    public void setLastSelected(View v) {
        this.lastSelected = v;
    }

    public View getLastSelected() {
        return lastSelected;
    }

    public void setLastSelectedIdx(int idx) {
        this.lastSelectedIdx = idx;
    }
}
