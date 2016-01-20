package me.marcooliveira.smartbuy;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ProductAdapter productAdapter;
    DetailFragment detailFragment;
    FloatingActionButton fab;
    SwipeRefreshLayout swipeContainer;
    CoordinatorLayout snackbarCoordinator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        snackbarCoordinator = (CoordinatorLayout)findViewById(R.id.snackbar_coordinator);

        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                GetList updater = new GetList();
                updater.execute("1");
            }
        });

        fab = (FloatingActionButton) findViewById(R.id.share_fab);
        if (fab != null) {
            fab.setVisibility(View.GONE);
        }

        detailFragment = (DetailFragment) getFragmentManager().findFragmentById(R.id.details_fragment);

        final ArrayList<Product> products = new ArrayList<>();
        ListView productList = (ListView) findViewById(R.id.product_list_view);
        productAdapter = new ProductAdapter(this, products);
        productList.setAdapter(productAdapter);

        productList.setOnScrollListener(new InfiniteScrollListener(5) {
            @Override
            public void loadMore(int page, int totalItemsCount) {
                GetList updater = new GetList();
                updater.execute(String.valueOf(page));
            }
        });

        productList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Product p = (Product) parent.getItemAtPosition(position);

                if (detailFragment == null || !detailFragment.isInLayout()) {
                    Intent intent = new Intent(getApplicationContext(), DetailActivity.class);
                    intent.putExtra("product", p);
                    startActivity(intent);
                } else {
                    detailFragment.updateInfo(p);
                    fab.setVisibility(View.VISIBLE);
                    fab.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            detailFragment.share();
                        }
                    });
                }
            }
        });

        swipeContainer.setRefreshing(true);
        GetList updater = new GetList();
        updater.execute("1");
    }


    public void showZoomFragment(View v) {
        detailFragment.showZoomFragment(v);
    }


    public class GetList extends AsyncTask<String, Void, ArrayList<Product>> {
        boolean isUpdate = false;
        Snackbar snack_update;

        @Override
        protected void onPostExecute(ArrayList<Product> products) {

            snack_update.dismiss();

            if(products != null){
                if (isUpdate)
                    productAdapter.clear();
                for(int i = 0; i < products.size(); i++){
                    productAdapter.add(products.get(i));
                }
            }
            else{
                Snackbar snackbar = Snackbar.make(snackbarCoordinator, getResources().getString(R.string.error), Snackbar.LENGTH_INDEFINITE);
                snackbar.setAction("REFRESH", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        GetList updater = new GetList();
                        updater.execute("1");
                    }
                });
                snackbar.show();
            }
            swipeContainer.setRefreshing(false);
        }


        @Override
        protected ArrayList<Product> doInBackground(String... params) {
            if (params[0].equals("1")) {
                isUpdate = true;
            }

            String baseUrl = "http://api.bestbuy.com/v1/products";
            String categories = "pcmcat209400050001,pcmcat156400050037";
            String show = "name,manufacturer,longDescription,sku,salePrice,regularPrice,image," +
                    "largeImage,url,customerReviewAverage,height,width,weight";
            String pageSize = "15";
            String pageNumber = params[0];
            String format = "json";
            String apiKey = "ujp4vduqppje2d6qvegh2hzz";
            ArrayList<Product> response;

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String jsonResponse = null;

            snack_update = Snackbar.make(snackbarCoordinator, getResources().getString(R.string.loading), Snackbar.LENGTH_SHORT);
            snack_update.setAction("OK", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    snack_update.dismiss();
                }
            });
            snack_update.show();

            try {

                URL url = new URL(baseUrl
                        + "(categoryPath.id%20in%20(" + categories + "))"
                        + "?show=" + show
                        + "&pageSize=" + pageSize
                        + "&page=" + pageNumber
                        + "&format=" + format
                        + "&apiKey=" + apiKey);

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();

                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    return null;
                }

                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    return null;
                }

                jsonResponse = buffer.toString();

                /* Parse the JSON data to return an array of Product objects */
                JSONParser parser = new JSONParser();
                response = parser.parseJSONData(jsonResponse);

            } catch (IOException e) {
                Log.e("SmartBUY", "Error parsing the data", e);
                return null;

            } catch (JSONException e) {
                Log.e("SmartBUY", "Error parsing the data", e);
                return null;

            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e("SmartBUY", "Error closing the stream", e);
                    }
                }
            }
            return response;
        }
    }
}
