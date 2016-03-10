package me.marcooliveira.smartbuy.fragments;

import android.app.Dialog;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.koushikdutta.ion.Ion;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.io.File;
import java.io.FileOutputStream;

import me.marcooliveira.smartbuy.model.Product;
import me.marcooliveira.smartbuy.R;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Fragment to show details of products
 */
@EFragment(R.layout.fragment_detail)
public class DetailFragment extends Fragment {

    Dialog dialog;
    Product p;

    public DetailFragment() {
    }

    @ViewById
    TextView productDetailName;
    @ViewById
    TextView productDetailManufacturer;
    @ViewById
    TextView productDetailPrice;
    @ViewById
    TextView productDetailDescription;
    @ViewById
    TextView productDetailWidth;
    @ViewById
    TextView productDetailHeight;
    @ViewById
    TextView productDetailWeight;
    @ViewById
    ImageView productDetailImage;
    @ViewById
    RelativeLayout descriptionContainer;
    @ViewById
    RelativeLayout defaultContainer;
    PhotoViewAttacher mAttacher;


    @AfterViews
    protected void hideDescriptionContainer() {
        descriptionContainer.setVisibility(View.GONE);
    }


    // Update the product info when a new product is selected from the list
    public void updateInfo(Product product) {

        descriptionContainer.setVisibility(View.VISIBLE);
        defaultContainer.setVisibility(View.GONE);

        productDetailName.setText(product.getName());
        productDetailManufacturer.setText("by " + product.getManufacturer());
        productDetailPrice.setText("$" + product.getSalePrice().toString());
        productDetailDescription.setText(product.getLongDescription());
        productDetailWidth.setText(product.getWidth());
        productDetailHeight.setText(product.getHeight());
        productDetailWeight.setText(product.getWeight());

        // Ion library to load the image from cache
        Ion.with(productDetailImage)
                .placeholder(R.mipmap.smartphone)
                .error(R.mipmap.smartphone)
                .load(product.getImage());
        this.p = product;
    }

    // Show the image zoom fragment as a dialog
    public void showZoomFragment(View v) {

        dialog = new Dialog(v.getContext(), android.R.style.Theme_Light_NoTitleBar_Fullscreen);
        dialog.setContentView(R.layout.image_zoom);
        dialog.show();

        ImageView zoomImage = (ImageView) dialog.findViewById(R.id.zoom_image);

        Ion.with(zoomImage)
                .placeholder(R.mipmap.smartphone)
                .error(R.mipmap.smartphone)
                .load(p.getImage());

        mAttacher = new PhotoViewAttacher(zoomImage);

        TextView back = (TextView) dialog.findViewById(R.id.zoom_back_button);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    // Save the selected image to the phone storage and start sharing intent
    public void share() {
        try {
            File sdCard = Environment.getExternalStorageDirectory();
            File appDir = new File(sdCard + "/me.marcooliveira.smartbuy");
            appDir.mkdirs();
            File file = new File(appDir, "share.jpg");
            FileOutputStream fos = new FileOutputStream(file);
            Bitmap bitmap = Ion.with(productDetailImage).getBitmap();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 95, fos);
            startSharingIntent(file.getPath(), "Hey, look what I found with SmartBUY!");
        }
        catch(Exception e) {
            Log.d("SmartBUY", e.toString());
        }
    }

    // Start intent to choose application to share image
    private void startSharingIntent(String imagePath,String text) {

        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("image/jpeg");
        share.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(imagePath)));
        share.putExtra(Intent.EXTRA_SUBJECT, text);
        share.putExtra(Intent.EXTRA_TEXT,text);
        startActivity(Intent.createChooser(share, "Share Image"));
    }

    // Show a blank detail fragment when no product is selected
    public void showDefaultFragment() {
        defaultContainer.setVisibility(View.VISIBLE);
        descriptionContainer.setVisibility(View.GONE);
    }
}
