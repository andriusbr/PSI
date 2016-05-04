package com.example.andrius.kurjeriuapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.andrius.kurjeriuapp.Classes.Items;
import com.example.andrius.kurjeriuapp.Classes.Order;

import java.util.List;

/**
 * Created by Andrius on 2016-04-02.
 */
public class ListOrdersCourierAdapter extends ArrayAdapter<Order> {
    

    public ListOrdersCourierAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    public ListOrdersCourierAdapter(Context context, int resource, List<Order> items) {
        super(context, resource, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.row_layout_orders_courier, null);
        }


        TextView tv1 = (TextView) v.findViewById(R.id.row_orders_courier_id);
        TextView tv2 = (TextView) v.findViewById(R.id.row_orders_courier_address);
        TextView tv3 = (TextView) v.findViewById(R.id.row_orders_courier_items);
        TextView tv4 = (TextView) v.findViewById(R.id.row_orders_courier_name);
        TextView tv5 = (TextView) v.findViewById(R.id.row_orders_courier_price);

        if(position==0){
            tv1.setText("ID"); tv2.setText("Address"); tv3.setText("Products"); tv4.setText("Name"); tv5.setText("Price");
        }else {
            final Order o = getItem(position);

            if (o != null) {
                if (tv1 != null) {
                    tv1.setText(String.valueOf(o.getId()));
                }

                if (tv2 != null) {
                    tv2.setText(o.getAddress());
                }

                if (tv3 != null) {
                    Items items=o.getItems();
                    String itemsString="";
                    for(int i=0;i<items.getSize();i++){
                        itemsString+=items.get(i).getName();
                        if(i<items.getSize()-1){
                            itemsString+=",\n";
                        }
                    }
                    tv3.setText(itemsString);
                }

                if (tv4 != null) {
                    String name = o.getFirstName() + " " + o.getLastName();
                    tv4.setText(name);
                }

                if (tv5 != null) {
                    tv5.setText(String.valueOf(o.getPrice()));
                }


            }
        }

        return v;
    }


}
