package me.marcooliveira.smartbuy.activity;

import android.support.v7.app.AppCompatActivity;
import android.view.View;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.FragmentById;

import me.marcooliveira.smartbuy.fragments.DetailFragment;
import me.marcooliveira.smartbuy.model.Product;
import me.marcooliveira.smartbuy.R;

@EActivity(R.layout.activity_detail)
public class DetailActivity extends AppCompatActivity {

    @FragmentById
    DetailFragment fragment;

    @Extra
    Product product;

    @Click(R.id.fab)
    void fabClicked() {
        fragment.share();
    }

    @AfterViews
    void readyToUpdate() {
        fragment.updateInfo(product);
    }

    public void showZoomFragment(View v) {
        fragment.showZoomFragment(v);
    }

}
