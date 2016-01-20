package me.marcooliveira.smartbuy;

import android.app.Dialog;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.koushikdutta.ion.Ion;

import java.io.File;
import java.io.FileOutputStream;

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
    RelativeLayout description_container;
    RelativeLayout default_container;

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

        description_container = (RelativeLayout) view.findViewById(R.id.description_container);
        default_container = (RelativeLayout) view.findViewById(R.id.default_container);
        description_container.setVisibility(View.GONE);

        return view;
    }

    public void updateInfo(Product product) {
        description_container.setVisibility(View.VISIBLE);
        default_container.setVisibility(View.GONE);

        name.setText(product.getName());
        manufacturer.setText("by " + product.getManufacturer());
        price.setText("$" + product.getSalePrice().toString());
        description.setText(product.getLongDescription());
        width.setText(product.getWidth());
        height.setText(product.getHeight());
        weight.setText(product.getWeight());

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

    public void share() {
        try {
            File sdCard = Environment.getExternalStorageDirectory();
            File appDir = new File(sdCard + "/me.marcooliveira.smartbuy");
            appDir.mkdirs();
            File file = new File(appDir, "share.jpg");
            FileOutputStream fos = new FileOutputStream(file);
            Bitmap bitmap = Ion.with(image).getBitmap();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 95, fos);
            startSharingIntent(file.getPath(), "Hey, look what I found with SmartBUY!");
        }
        catch(Exception e) {
            Log.d("SmartBUY", e.toString());
        }
    }

    private void startSharingIntent(String imagePath,String text) {

        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("image/jpeg");
        share.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(imagePath)));
        share.putExtra(Intent.EXTRA_SUBJECT, text);
        share.putExtra(Intent.EXTRA_TEXT,text);
        startActivity(Intent.createChooser(share, "Share Image"));
    }

    public void showDefaultFragment() {
        default_container.setVisibility(View.VISIBLE);
        description_container.setVisibility(View.GONE);
    }
}
