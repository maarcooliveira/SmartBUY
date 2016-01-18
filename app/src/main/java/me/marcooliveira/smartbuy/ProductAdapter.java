package me.marcooliveira.smartbuy;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by marco on 1/18/16.
 */
public class ProductAdapter extends ArrayAdapter<Product> {
    private Activity context;
    private ArrayList<Product> data;

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

        //TODO: insert image into layout
        name.setText(data.get(position).getName());
        manufacturer.setText(data.get(position).getManufacturer());
        price.setText("$" + data.get(position).getSalePrice().toString());

        return rowView;
    }
}
