package me.marcooliveira.smartbuy;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailFragment extends Fragment {
    TextView detail;

    public DetailFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail, null);
        detail = (TextView) view.findViewById(R.id.detail_text);



        return view;
    }

    public void updateInfo(Product product) {
        detail.setText(product.getName());
    }
}
