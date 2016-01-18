package me.marcooliveira.smartbuy;

import java.io.Serializable;

/**
 * Created by marco on 1/18/16.
 */
public class Product implements Serializable {
    private String name;
    private String manufacturer;
    private String longDescription;
    private String sku;
    private Double salePrice;
    private Double regularPrice;
    private String image;
    private String url;
    private String customerReviewAverage;
    private String width;
    private String height;
    private String weight;

    public Product(String sku, String name, String manufacturer, String image, Double salePrice) {
        this.sku = sku;
        this.name = name;
        this.manufacturer = manufacturer;
        this.image = image;
        this.salePrice = salePrice;
    }

    public void addDetails(String longDescription, Double regularPrice, String url,
                           String customerReviewAverage, String width, String height, String weight ) {
        this.longDescription = longDescription;
        this.regularPrice = regularPrice;
        this.url = url;
        this.customerReviewAverage = customerReviewAverage;
        this.width = width;
        this.height = height;
        this.weight = weight;
    }

    public String getName() {
        return name;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public String getLongDescription() {
        return longDescription;
    }

    public String getSku() {
        return sku;
    }

    public Double getSalePrice() {
        return salePrice;
    }

    public Double getRegularPrice() {
        return regularPrice;
    }

    public String getImage() {
        return image;
    }

    public String getUrl() {
        return url;
    }

    public String getCustomerReviewAverage() {
        return customerReviewAverage;
    }

    public String getWidth() {
        return width;
    }

    public String getHeight() {
        return height;
    }

    public String getWeight() {
        return weight;
    }
}
