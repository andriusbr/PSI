package com.example.andrius.kurjeriuapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.example.andrius.kurjeriuapp.Classes.Item;
import com.example.andrius.kurjeriuapp.Classes.Items;
import com.example.andrius.kurjeriuapp.Classes.Order;
import com.example.andrius.kurjeriuapp.Classes.Orders;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by Andrius on 2016-05-01.
 */
public class RetrieveOrders {
    // Progress Dialog
    private ProgressDialog pDialog;
    // JSON parser class
    JSONParser jsonParser = new JSONParser();
    private static final String LOGIN_URL = "http://kurjeriu-programele.netne.net/get_courier_orders.php";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    private static final String TAG_IDS="ids";
    private static final String TAG_PRODUCTS="products";
    private static final String TAG_PRICES="prices";
    private static final String TAG_ADDRESSES="addresses";
    private static final String TAG_FIRST_NAMES="first_names";
    private static final String TAG_LAST_NAME="last_names";

    private RetrievalAttempt retrievalAttempt;

    private Context context;
    private Orders orders=new Orders();

    public RetrieveOrders(String username, Context context){
        this.context=context;
        retrievalAttempt = new RetrievalAttempt();
        try {
            retrievalAttempt.execute(username).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }


    public Orders GetOrders(){
        /*AsyncTask.Status tt=retrievalAttempt.getStatus();
        while(retrievalAttempt.getStatus() != AsyncTask.Status.FINISHED){

        }*/
        return orders;
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

        ArrayList<Items> listItems=readItemsArray(json, TAG_PRODUCTS);
        ArrayList<Double> listPrices=readDoubleArray(json, TAG_PRICES);
        ArrayList<String> listAddresses=readArray(json, TAG_ADDRESSES);
        ArrayList<String> listFirstNames=readArray(json, TAG_FIRST_NAMES);
        ArrayList<String> listLastNames=readArray(json, TAG_LAST_NAME);

        // Put values to Orders object
        if(listIds!=null && listItems!=null && listPrices!=null && listFirstNames!=null && listLastNames!=null && listAddresses!=null){
            for(int i=0;i<listIds.size();i++){
                Order order=new Order(listIds.get(i),listAddresses.get(i),
                        listItems.get(i),listPrices.get(i),listFirstNames.get(i), listLastNames.get(i));
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
        ArrayList<Items> list = new ArrayList<>();
        try {
            JSONArray jsonArray = null;
            jsonArray = json.getJSONArray(tag);
            if (jsonArray != null) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONArray jsonItemArray = jsonArray.getJSONArray(i);
                    Items items = new Items();
                    for (int j = 0; j < jsonItemArray.length(); j++) {
                        Item item = new Item();
                        item.setName(jsonItemArray.get(j).toString());
                        items.add(item);
                    }
                    list.add(items);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }




        class RetrievalAttempt extends AsyncTask<String, String, String> {
        /**
         * Before starting background thread Show Progress Dialog
         */
        //boolean failure = false;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(context);
            pDialog.setMessage("Retrieving data...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... args) {
            // TODO Auto-generated method stub
            // here Check for success tag
            int success;
            try {

                HashMap<String, String> params = new HashMap<>();
                params.put("_username", args[0]);

                Log.d("request!", "starting");

                JSONObject json = jsonParser.makeHttpRequest(
                        LOGIN_URL, params);

                if(json!=null) {
                    // checking  log for json response
                    Log.d("Login attempt", json.toString());

                    // success tag for json
                    success = json.getInt(TAG_SUCCESS);
                    if (success == 1) {
                        Log.d("Successfully retrieved!", json.toString());

                        readValuesFromDatabase(json);
                        return json.getString(TAG_MESSAGE);
                    } else {
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
            /*if (message != null) {
                Toast.makeText(LoginActivity.this, message, Toast.LENGTH_LONG).show();
            }*/
        }
    }

}
