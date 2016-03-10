package me.marcooliveira.smartbuy.activity;

import android.content.Intent;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.FragmentById;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;

import me.marcooliveira.smartbuy.api.GetProductList;
import me.marcooliveira.smartbuy.fragments.DetailFragment;
import me.marcooliveira.smartbuy.listener.InfiniteScrollListener;
import me.marcooliveira.smartbuy.model.Product;
import me.marcooliveira.smartbuy.adapters.ProductAdapter;
import me.marcooliveira.smartbuy.R;

@EActivity(R.layout.activity_main)
public class MainActivity extends AppCompatActivity {

    ProductAdapter productAdapter;

    @ViewById
    SwipeRefreshLayout swipeContainer;
    @ViewById
    ListView productListView;
    @ViewById
    Toolbar toolbar;
    @ViewById
    CoordinatorLayout snackbarCoordinator;
    @ViewById
    FloatingActionButton shareFab;
    @FragmentById
    DetailFragment detailsFragment;
    Snackbar updateSnack;

    @Bean
    GetProductList getProductList;

    @AfterViews
    void init() {
        initInterface();
        initListView();
        initData();
    }

    void initInterface() {
        setSupportActionBar(toolbar);
        if (shareFab != null) {
            shareFab.setVisibility(View.GONE);
        }

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getProductList.callBestBuyAPI(1);
                if (detailsFragment != null)
                    detailsFragment.showDefaultFragment();
            }
        });
    }

    @ItemClick
    void productListViewItemClicked(Product selectedProduct) {
        /* If the fragment was not found, it is a small/medium device --> start DetailActivity;
         *  Otherwise, update the fragment info and make the share FAB visible */
        if (detailsFragment == null || !detailsFragment.isInLayout()) {
            Intent intent = new Intent(getApplicationContext(), DetailActivity_.class);
            intent.putExtra("product", selectedProduct);
            startActivity(intent);
        } else {
            detailsFragment.updateInfo(selectedProduct);
            shareFab.setVisibility(View.VISIBLE);
        }
    }

    void initListView() {
        final ArrayList<Product> products = new ArrayList<>();

        productAdapter = new ProductAdapter(this, products);
        productListView.setAdapter(productAdapter);
        productListView.setOnScrollListener(new InfiniteScrollListener(5) {
            @Override
            public void loadMore(int page, int totalItemsCount) {
                getProductList.callBestBuyAPI(page);
            }
        });
    }

    @Click(R.id.share_fab)
    void shareFabClick() {
        detailsFragment.share();
    }

    void initData() {
        swipeContainer.setRefreshing(true);
        getProductList.callBestBuyAPI(1);
    }


    public void showZoomFragment(View v) {
        detailsFragment.showZoomFragment(v);
    }

    public void showLoadingSnackbar() {
        updateSnack = Snackbar.make(snackbarCoordinator, getResources().getString(R.string.loading), Snackbar.LENGTH_SHORT);
        updateSnack.setAction("OK", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateSnack.dismiss();
            }
        });
        updateSnack.show();
    }

    public void showErrorSnackbar() {
        Snackbar snackbar = Snackbar.make(snackbarCoordinator, getResources().getString(R.string.error), Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction("REFRESH", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getProductList.callBestBuyAPI(1);
            }
        });
        snackbar.show();
        swipeContainer.setRefreshing(false);
    }

    void dismissLoadingSnackbar() {
        updateSnack.dismiss();
    }

    public void updateProductsList(ArrayList<Product> products, boolean isUpdate) {
        dismissLoadingSnackbar();

        if(products != null) {
            // Empty the adapter if the pull to refresh was used
            if (isUpdate)
                productAdapter.clear();
            for (Product product : products)
                productAdapter.add(product);
        }

        swipeContainer.setRefreshing(false);
    }
}