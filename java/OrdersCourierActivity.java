package com.example.andrius.kurjeriuapp;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.example.andrius.kurjeriuapp.Classes.Item;
import com.example.andrius.kurjeriuapp.Classes.Items;
import com.example.andrius.kurjeriuapp.Classes.Order;
import com.example.andrius.kurjeriuapp.Classes.Orders;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Andrius on 2016-03-31.
 */
public class OrdersCourierActivity extends AppCompatActivity {

    private Orders orders;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders_courier);

        retrieveOrders();
        setupListView();
    }

    public void retrieveOrders(){
        String username;
        Bundle extras = getIntent().getExtras();
        if(extras == null) {
            username = null;
        } else {
            username = extras.getString("username");
        }
        RetrieveOrders retrieveOrders=new RetrieveOrders(username, OrdersCourierActivity.this);
        orders=retrieveOrders.GetOrders();

        orders.add(0, new Order());
    }

    private void setupListView(){
        final ListView ordersListView = (ListView) findViewById(R.id.list_orders_courier);
        final ListOrdersCourierAdapter customAdapter = new ListOrdersCourierAdapter(this, R.layout.row_layout_orders_courier, orders.getList());
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ordersListView.setAdapter(customAdapter);
            }
        });
    }



}
