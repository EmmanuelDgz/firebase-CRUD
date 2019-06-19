package app.m.firebaexample;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.List;

public class ProductList extends ArrayAdapter<Product> {
    private Activity context;

    List<Product> products;

    public ProductList(Activity context, List<Product> products) {
        super(context, R.layout.activity_layout_user_list, products);
        this.context = context;
        this.products = products;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();

        View listViewItem = inflater.inflate(R.layout.activity_layout_user_list, null, true);

        TextView textViewName = (TextView) listViewItem.findViewById
                (R.id.textViewName);
        TextView textviewemail = (TextView) listViewItem.findViewById
                (R.id.textviewemail);
        TextView textviewnumber = (TextView) listViewItem.findViewById
                (R.id.textviewnumber);

        Product Product = products.get(position);

        textViewName.setText(Product.getProductName());

        textviewemail.setText(Product.getProductDescription());

        textviewnumber.setText(Product.getProductPrice());

        return listViewItem;
    }
}