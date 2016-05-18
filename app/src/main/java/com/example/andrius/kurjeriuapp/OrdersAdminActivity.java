package com.example.andrius.kurjeriuapp;

import android.app.ProgressDialog;
import android.content.Intent;
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
public class OrdersAdminActivity extends AppCompatActivity {

    // Variables used to access database
    //---------------------------------------------------------
    // Progress Dialog
    private ProgressDialog pDialog;
    // JSON parser class
    JSONParser jsonParser = new JSONParser();
    private static final String LOGIN_URL_LIST = "http://kurjeriu-programele.netne.net/get_order_list.php";
    private static final String CONNECTION_URL = "http://kurjeriu-programele.netne.net/generate_orders.php";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    private static final String TAG_IDS="ids";
    //private static final String TAG_QUANTITIES="quantities";
    private static final String TAG_PRODUCTS="products";
    private static final String TAG_PRICES="prices";
    private static final String TAG_COURIERS="couriers";
    private static final String TAG_ADDRESSES="addresses";
    //----------------------------------------------------------

    private Orders orders=new Orders();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders_admin);


        Button generateButton=(Button)findViewById(R.id.button_generate_orders);
        generateButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                new GenerateOrdersAttempt().execute();
            }
        });

        new ListRetrievalAttempt().execute();
    }

    private void setupListView(){
        final ListView usersListView = (ListView) findViewById(R.id.list_orders_admin);
        final ListOrdersAdminAdapter customAdapter = new ListOrdersAdminAdapter(this, R.layout.row_layout_orders_admin, orders.getList());
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                usersListView.setAdapter(customAdapter);
            }
        });
    }


    private void readValuesFromDatabase(JSONObject json)
    {
        ArrayList<Integer> listIds = new ArrayList<>();
        try {
            JSONArray jsonArray = null;
            jsonArray = json.getJSONArray(TAG_IDS);
            if (jsonArray != null) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    listIds.add(jsonArray.getInt(i));
                }
            }
        } catch (JSONException e) { e.printStackTrace(); }
        //ArrayList<String> listQuantities=readArray(json, TAG_QUANTITIES);
        ArrayList<Items> listItems=readItemsArray(json, TAG_PRODUCTS);
        ArrayList<Double> listPrices=readDoubleArray(json, TAG_PRICES);
        ArrayList<String> listCouriers=readArray(json, TAG_COURIERS);
        ArrayList<String> listAddresses=readArray(json, TAG_ADDRESSES);

        // Put values to Orders object
        if(listIds!=null && listItems!=null && listPrices!=null && listCouriers!=null && listAddresses!=null){
            Order order0=new Order();
            orders.add(order0);
            for(int i=0;i<listIds.size();i++){
                Order order=new Order(listIds.get(i),listAddresses.get(i),
                        listItems.get(i),listPrices.get(i),listCouriers.get(i));
                orders.add(order);
            }
        }
    }

    private ArrayList<String> readArray(JSONObject json, String tag)
    {
        ArrayList<String> list = new ArrayList<>();
        try {
            JSONArray jsonArray = null;
            jsonArray = json.getJSONArray(tag);
            if (jsonArray != null) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    list.add(jsonArray.get(i).toString());
                }
            }
        } catch (JSONException e) { e.printStackTrace(); }
        return list;
    }

    private ArrayList<Double> readDoubleArray(JSONObject json, String tag)
    {
        ArrayList<Double> list = new ArrayList<>();
        try {
            JSONArray jsonArray = null;
            jsonArray = json.getJSONArray(tag);
            if (jsonArray != null) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    list.add(jsonArray.getDouble(i));
                }
            }
        } catch (JSONException e) { e.printStackTrace(); }
        return list;
    }

    private ArrayList<Items> readItemsArray(JSONObject json, String tag) {
        ArrayList<Items> list=new ArrayList<>();
        try {
            JSONArray jsonArray = null;
            jsonArray = json.getJSONArray(tag);
            if (jsonArray != null) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONArray jsonItemArray=jsonArray.getJSONArray(i);
                    Items items=new Items();
                    for (int j = 0; j < jsonItemArray.length(); j++) {
                        Item item=new Item();
                        item.setName(jsonItemArray.get(j).toString());
                        items.add(item);
                    }
                    list.add(items);
                }
            }
        } catch (JSONException e) { e.printStackTrace(); }

        return list;
    }










    //------------------------------------------------------------------------------------------------------------------

    class ListRetrievalAttempt extends AsyncTask<String, String, String> {
        /**
         * Before starting background thread Show Progress Dialog
         */

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(OrdersAdminActivity.this);
            pDialog.setMessage("Loading...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... args) {
            int success;
            try {
                Log.d("request!", "starting");

                JSONObject json = jsonParser.makeHttpRequest(
                        LOGIN_URL_LIST, null);

                if (json != null) {
                    // checking  log for json response
                    Log.d("List retrieval attempt", json.toString());

                    // success tag for json
                    success = json.getInt(TAG_SUCCESS);
                    if (success == 1) {

                        readValuesFromDatabase(json);
                        setupListView();

                        Log.d("Success!", json.toString());
                        return json.getString(TAG_MESSAGE);
                    } else {
                        Log.d("Error loading data", json.toString());
                        return json.getString(TAG_MESSAGE);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        /**
         * Once the background process is done we need to  Dismiss the progress dialog asap
         **/
        protected void onPostExecute(String message) {

            pDialog.dismiss();
        }
    }


    class GenerateOrdersAttempt extends AsyncTask<String, String, String> {
        /**
         * Before starting background thread Show Progress Dialog
         */

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(OrdersAdminActivity.this);
            pDialog.setMessage("Loading...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... args) {
            int success;
            try {
                Log.d("request!", "starting");

                JSONObject json = jsonParser.makeHttpRequest(
                        CONNECTION_URL, null);

                if (json != null) {
                    // checking  log for json response
                    Log.d("Attempting to generate", json.toString());

                    // success tag for json
                    success = json.getInt(TAG_SUCCESS);
                    if (success == 1) {

                        Intent intent = getIntent();
                        finish();
                        startActivity(intent);

                        Log.d("Success!", json.toString());
                        return json.getString(TAG_MESSAGE);
                    } else {
                        Log.d("Error generating orders", json.toString());
                        return json.getString(TAG_MESSAGE);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        /**
         * Once the background process is done we need to  Dismiss the progress dialog asap
         **/
        protected void onPostExecute(String message) {

            pDialog.dismiss();
        }
    }
}
