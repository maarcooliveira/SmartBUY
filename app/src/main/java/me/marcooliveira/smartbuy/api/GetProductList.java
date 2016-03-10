package me.marcooliveira.smartbuy.api;

import android.util.Log;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;
import org.androidannotations.annotations.UiThread;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import me.marcooliveira.smartbuy.activity.MainActivity;
import me.marcooliveira.smartbuy.activity.MainActivity_;
import me.marcooliveira.smartbuy.model.Product;
import me.marcooliveira.smartbuy.utils.JSONParser;

/**
 * Created by marco on 10/03/16.
 */
@EBean
public class GetProductList {
    Boolean isUpdate;
    final int LOADING = 1;
    final int ERROR = 2;

    @RootContext
    MainActivity activity;

    @Background
    public void callBestBuyAPI(int page) {
        isUpdate = page == 1 ? true : false;

        // URL configuration for the BestBuy API call
        String baseUrl = "http://api.bestbuy.com/v1/products";
        String categories = "pcmcat209400050001,pcmcat156400050037";
        String show = "name,manufacturer,longDescription,sku,salePrice,regularPrice,image," +
                "largeImage,url,customerReviewAverage,height,width,weight";
        String pageSize = "15";
        String pageNumber = String.valueOf(page);
        String format = "json";
        String apiKey = "ujp4vduqppje2d6qvegh2hzz";
        ArrayList<Product> response;

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String jsonResponse;

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
                showSnackbar(ERROR);
                return;
            }

            reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;

            while ((line = reader.readLine()) != null) {
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                showSnackbar(ERROR);
                return;
            }

            jsonResponse = buffer.toString();

            // Parse the JSON data to return an array of Product objects
            JSONParser parser = new JSONParser();
            response = parser.parseJSONData(jsonResponse);

        } catch (Exception e) {
            Log.e("SmartBUY", "Error parsing the data", e);
            showSnackbar(ERROR);
            return;
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
        showSnackbar(LOADING);
        updateUI(response, isUpdate);
    }

    @UiThread
    void updateUI(ArrayList<Product> products, boolean isUpdate) {
        activity.updateProductsList(products, isUpdate);
    }

    @UiThread
    void showSnackbar (int type) {
        if (type == LOADING) {
            activity.showLoadingSnackbar();
        }
        else if (type == ERROR) {
            activity.showErrorSnackbar();
        }
    }
}
