package me.marcooliveira.smartbuy;

import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    FragmentManager fm = getFragmentManager();
    ProductAdapter productAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final ArrayList<Product> products = new ArrayList<>();

        products.add(new Product("000", "iPhone 6S", "Apple", "img", 699.00));
        products.add(new Product("001", "iPhone 6", "Apple", "img", 599.00));
        products.add(new Product("002", "iPhone 5S", "Apple", "img", 499.00));
        products.add(new Product("003", "iPhone 5", "Apple", "img", 399.00));
        products.add(new Product("004", "iPhone 4S", "Apple", "img", 299.00));


        productAdapter = new ProductAdapter(this, products);
        ListView productList = (ListView) findViewById(R.id.product_list_view);
        productList.setAdapter(productAdapter);

        productList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Product p = (Product) parent.getItemAtPosition(position);
                DetailFragment detailFragment = (DetailFragment) getFragmentManager().findFragmentById(R.id.details_fragment);

                if(detailFragment == null || !detailFragment.isInLayout()) {
                    Intent intent = new Intent(getApplicationContext(), DetailActivity.class);
                    intent.putExtra("product", p);
                    startActivity(intent);
                }
                else {
                    detailFragment.updateInfo(p);
                }

            }
        });


//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
