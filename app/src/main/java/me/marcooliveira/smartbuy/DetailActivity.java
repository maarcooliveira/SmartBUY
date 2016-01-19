package me.marcooliveira.smartbuy;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.koushikdutta.ion.Ion;

import java.io.File;
import java.io.FileOutputStream;

import uk.co.senab.photoview.PhotoViewAttacher;

public class DetailActivity extends AppCompatActivity {
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
    DetailFragment detailFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        p = (Product) intent.getSerializableExtra("product");

        detailFragment = (DetailFragment) getFragmentManager().findFragmentById(R.id.details_fragment);

        if (detailFragment == null || !detailFragment.isInLayout()) {
            Log.d("SmartBUY", "***FRAGMENT NOT FOUND!!");
        } else {
            detailFragment.updateInfo(p);
            Log.d("SmartBUY", "**FRAGMENT FOUND!!");
        }

        name = (TextView) findViewById(R.id.product_detail_name);
        manufacturer = (TextView) findViewById(R.id.product_detail_manufacturer);
        price = (TextView) findViewById(R.id.product_detail_price);
        description = (TextView) findViewById(R.id.product_detail_description);
        width = (TextView) findViewById(R.id.product_detail_width);
        height = (TextView) findViewById(R.id.product_detail_height);
        weight = (TextView) findViewById(R.id.product_detail_weight);
        image = (ImageView) findViewById(R.id.product_detail_image);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveToShare();
            }
        });

        updateInfo(p);
    }

    public void updateInfo(Product product) {
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
    }

    public void showZoomFragment(View v) {
        dialog = new Dialog(DetailActivity.this, android.R.style.Theme_Light_NoTitleBar_Fullscreen);
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

    private void saveToShare() {
        try {
            File sdCard = Environment.getExternalStorageDirectory();
            File appDir = new File(sdCard + "/me.marcooliveira.smartbuy");
            appDir.mkdirs();
            File file = new File(appDir, "share.jpg");
            FileOutputStream fos = new FileOutputStream(file);
            Bitmap bitmap = Ion.with(image).getBitmap();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 95, fos);
            share(file.getPath(),"Hey, look what I found with SmartBUY!");
        }
        catch(Exception e) {
            Log.d("SmartBUY", e.toString());
        }
    }

    private void share(String imagePath,String text) {

        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("image/jpeg");
        share.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(imagePath)));
        share.putExtra(Intent.EXTRA_SUBJECT, text);
        share.putExtra(Intent.EXTRA_TEXT,text);
        startActivity(Intent.createChooser(share, "Share Image"));
    }

}
