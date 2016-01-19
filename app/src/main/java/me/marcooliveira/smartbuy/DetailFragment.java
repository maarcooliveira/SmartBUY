package me.marcooliveira.smartbuy;

import android.app.Dialog;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.koushikdutta.ion.Ion;

import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailFragment extends Fragment {
    TextView name;
    TextView manufacturer;
    TextView price;
    TextView description;
    TextView width;
    TextView height;
    TextView weight;
    ImageView image;
    Dialog dialog;

    Product p;
    PhotoViewAttacher mAttacher;

    public DetailFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail, null);

        name = (TextView) view.findViewById(R.id.product_detail_name);
        manufacturer = (TextView) view.findViewById(R.id.product_detail_manufacturer);
        price = (TextView) view.findViewById(R.id.product_detail_price);
        description = (TextView) view.findViewById(R.id.product_detail_description);
        width = (TextView) view.findViewById(R.id.product_detail_width);
        height = (TextView) view.findViewById(R.id.product_detail_height);
        weight = (TextView) view.findViewById(R.id.product_detail_weight);
        image = (ImageView) view.findViewById(R.id.product_detail_image);

        return view;
    }

    public void updateInfo(Product product) {
        name.setText(product.getName());
        manufacturer.setText("by " + product.getManufacturer());
        price.setText("$" + product.getSalePrice().toString());
        description.setText(product.getLongDescription());

        Ion.with(image)
                .placeholder(R.mipmap.smartphone)
                .error(R.mipmap.smartphone)
                .load(product.getImage());
        this.p = product;
    }

    public void showZoomFragment(View v) {
        dialog = new Dialog(v.getContext(), android.R.style.Theme_Light_NoTitleBar_Fullscreen);
        dialog.setContentView(R.layout.image_zoom);
        dialog.show();

        ImageView imageZoom = (ImageView) dialog.findViewById(R.id.zoom_image);

        Ion.with(imageZoom)
                .placeholder(R.mipmap.smartphone)
                .error(R.mipmap.smartphone)
                .load(p.getImage());
        mAttacher = new PhotoViewAttacher(imageZoom);

        TextView back = (TextView) dialog.findViewById(R.id.zoom_back_button);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }
}
