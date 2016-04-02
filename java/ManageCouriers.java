package com.example.andrius.kurjeriuapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.example.andrius.kurjeriuapp.Classes.Courier;
import com.example.andrius.kurjeriuapp.Classes.Couriers;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Handler;

/**
 * Created by Andrius on 2016-03-10.
 */
public class ManageCouriers extends AppCompatActivity {

    // Variables used to access database
    //---------------------------------------------------------
    // Add user
    // Progress Dialog
    private ProgressDialog pDialog;
    // JSON parser class
    JSONParser jsonParser = new JSONParser();
    private static final String LOGIN_URL = "http://kurjeriu-programele.netne.net/add_new_user.php";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    //---------------------------------------------------------
    // Get user list
    //JSONParser jsonParser = new JSONParser();
    private static final String LOGIN_URL_LIST = "http://kurjeriu-programele.netne.net/get_courier_list.php";
    private static final String TAG_IDS="ids";
    private static final String TAG_USERNAMES="usernames";
    private static final String TAG_PASSWORDS="passwords";
    private static final String TAG_FIRST_NAMES="first_names";
    private static final String TAG_LAST_NAMES="last_names";
    //----------------------------------------------------------


    //PopupWindow
    private PopupWindow pwAdd;
    private View pwLayout;
    //private boolean popUpOpen=false;
    private static final double PW_SCREEN_HEIGHT_RATIO=0.45;
    private static final double PW_SCREEN_WIDTH_RATIO=0.85;

    //Buttons
    private Button button_add;

    //EditText
    private EditText firstname_edit;
    private EditText lastname_edit;
    private EditText login_edit;

    //Couriers object
    Couriers couriers=new Couriers();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_couriers);


        button_add = (Button) findViewById(R.id.button_add_courier);
        button_add.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View v) {
                inflatePopUpAdd();
            }
        });

        new ListRetrievalAttempt().execute();

    }


    private void setupListView(){
        final ListView usersListView = (ListView) findViewById(R.id.list_users);
        final ListManageCouriersAdapter customAdapter = new ListManageCouriersAdapter(this, R.layout.row_layout_manage_users, couriers.getList());
        runOnUiThread(new Runnable() {
                          @Override
                          public void run() {
                              usersListView.setAdapter(customAdapter);
                          }
                      });
    }

    private void inflatePopUpAdd(){
        LayoutInflater inflater = (LayoutInflater) ManageCouriers.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        pwLayout = inflater.inflate(R.layout.popup_add_user, null);

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        int pw_height= (int) (metrics.heightPixels * PW_SCREEN_HEIGHT_RATIO);
        int pw_width= (int) (metrics.widthPixels * PW_SCREEN_WIDTH_RATIO);

        pwAdd = new PopupWindow(pwLayout, pw_width, pw_height, true);
        pwAdd.showAtLocation(pwLayout, Gravity.CENTER, 0, 0);

        initVariables(pwLayout);


        ImageButton btnDismiss = (ImageButton) pwLayout.findViewById(R.id.buttonPWClose);
        btnDismiss.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View v) {
                pwAdd.dismiss();
            }
        });

        Button submitRequest = (Button) pwLayout.findViewById(R.id.button_add_new_user);
        submitRequest.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View v) {
                attemptToAddUser();
            }
        });
    }

    private void initVariables(View layout){
        firstname_edit=(EditText)layout.findViewById(R.id.edit_add_first_name);
        lastname_edit=(EditText)layout.findViewById(R.id.edit_add_last_name);
        login_edit=(EditText)layout.findViewById(R.id.edit_login);

        login_edit.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View view, int keyCode, KeyEvent keyevent) {
                //If the keyevent is a key-down event on the "enter" button
                if ((keyevent.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    attemptToAddUser();
                    return true;
                }
                return false;
            }
        });
    }

    private void attemptToAddUser(){
        if(isNetworkAvailable()) {
            String firstname = firstname_edit.getText().toString();
            String lastname = lastname_edit.getText().toString();
            String login = login_edit.getText().toString();
            if(firstname.length()<=0 || lastname.length()<=0 || login.length()<=0){
                Toast.makeText(ManageCouriers.this, " One or more fields are empty ", Toast.LENGTH_LONG).show();
            }else if(firstname.length()<5 || lastname.length()<5 || login.length()<5){
                Toast.makeText(ManageCouriers.this, "Some of the fields are too short. Minimum 5 characters", Toast.LENGTH_LONG).show();
            }else if(firstname.length()>15 || lastname.length()>15 || login.length()>15){
                Toast.makeText(ManageCouriers.this, "Some of the fields are too long. Maximum 15 characters", Toast.LENGTH_LONG).show();
            }else {
                new AddAttempt().execute(firstname, lastname, login);
            }
        }else{
            Toast.makeText(ManageCouriers.this, "No Internet Connection", Toast.LENGTH_LONG).show();
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }



    private void readValuesFromDatabase(JSONObject json)
    {
        ArrayList<Integer> listIds = new ArrayList<>();
        try {
            JSONArray jsonArray = null;
            jsonArray = (JSONArray) json.getJSONArray("ids");
            if (jsonArray != null) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    listIds.add((Integer) jsonArray.getInt(i));
                }
            }
        } catch (JSONException e) { e.printStackTrace(); }

        ArrayList<String> listUsernames=readArray(json, TAG_USERNAMES);
        ArrayList<String> listPasswords=readArray(json, TAG_PASSWORDS);
        ArrayList<String> listFirstNames=readArray(json, TAG_FIRST_NAMES);
        ArrayList<String> listLastNames=readArray(json, TAG_LAST_NAMES);

        // Put values to Couriers object
        if(listIds!=null && listUsernames !=null && listPasswords!=null && listFirstNames!=null && listLastNames!=null){
            Courier courier0=new Courier(0,"","","","");
            couriers.add(courier0);
            for(int i=0;i<listIds.size();i++){
                Courier courier=new Courier(listIds.get(i),listUsernames.get(i),
                        listPasswords.get(i),listFirstNames.get(i),listLastNames.get(i));
                couriers.add(courier);
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





    //--------------------------------------------------------------------------------------------------------------

    class AddAttempt extends AsyncTask<String, String, String> {
        /**
         * Before starting background thread Show Progress Dialog
         */
        boolean failure = false;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ManageCouriers.this);
            pDialog.setMessage("Adding new user...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... args) {
            int success;
            try {

                HashMap<String, String> params = new HashMap<>();
                params.put("firstname", args[0]);
                params.put("lastname", args[1]);
                params.put("login", args[2]);

                Log.d("request!", "starting");

                JSONObject json = jsonParser.makeHttpRequest(
                        LOGIN_URL, params);

                if (json != null) {
                    // checking  log for json response
                    Log.d("Add attempt", json.toString());

                    // success tag for json
                    success = json.getInt(TAG_SUCCESS);
                    if (success == 1) {
                        Log.d("Successfully added!", json.toString());

                        Intent intent = getIntent();
                        finish();
                        startActivity(intent);

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
            if (message != null) {
                Toast.makeText(ManageCouriers.this, message, Toast.LENGTH_LONG).show();
            }
        }
    }




    //------------------------------------------------------------------------------------------------------------------

    class ListRetrievalAttempt extends AsyncTask<String, String, String> {
        /**
         * Before starting background thread Show Progress Dialog
         */

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ManageCouriers.this);
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


}
