package me.marcooliveira.smartbuy;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by marco on 1/18/16.
 *
 * Class to parse a JSON string received from the API to an ArrayList<Product>
 */
public class JSONParser {

    Integer pages;
    Integer current_page;

    public ArrayList<Product> parseJSONData(String jsonStr) throws JSONException {

        final String PRODUCTS = "products";
        final String NAME = "name";
        final String MANUFACTURER = "manufacturer";
        final String DESCRIPTION = "longDescription";
        final String SKU = "sku";
        final String SALE = "salePrice";
        final String REGULAR = "regularPrice";
        final String IMAGE = "image";
        final String URL = "url";
        final String REVIEW_AVG = "customerReviewAverage";
        final String WIDTH = "width";
        final String HEIGHT = "height";
        final String WEIGHT = "weight";
        final String PAGE = "currentPage";
        final String PAGE_TOTAL = "totalPages";


        JSONObject jsonObject = new JSONObject(jsonStr);

        pages = jsonObject.getInt(PAGE_TOTAL);
        current_page = jsonObject.getInt(PAGE);

        JSONArray jsonProductList = jsonObject.getJSONArray(PRODUCTS);

        ArrayList<Product> productList = new ArrayList<Product>();

        for(int i = 0; i < jsonProductList.length(); i++) {
            JSONObject jsonProduct = jsonProductList.getJSONObject(i);

            /* By default, BestBuy puts the manufacturer name before the product name in the "name"
             * field. The manufacturer is removed to show a cleaner interface */
            String name = jsonProduct.getString(NAME);
            String manufacturer = jsonProduct.getString(MANUFACTURER);
            String manufacturer_in_name = manufacturer + " - ";
            name = name.replaceAll(manufacturer_in_name, "");

            Product p = new Product();
            p.setName(name);
            p.setManufacturer(manufacturer);
            p.setLongDescription(jsonProduct.getString(DESCRIPTION));
            p.setSku(jsonProduct.getString(SKU));
            p.setSalePrice(jsonProduct.getDouble(SALE));
            p.setRegularPrice(jsonProduct.getDouble(REGULAR));
            p.setImage(jsonProduct.getString(IMAGE));
            p.setUrl(jsonProduct.getString(URL));
            p.setCustomerReviewAverage(jsonProduct.getString(REVIEW_AVG));
            p.setWidth(jsonProduct.getString(WIDTH));
            p.setHeight(jsonProduct.getString(HEIGHT));
            p.setWeight(jsonProduct.getString(WEIGHT));

            productList.add(p);
        }
        return productList;
    }
}
