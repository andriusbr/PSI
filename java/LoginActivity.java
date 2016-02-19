package com.example.andrius.kurjeriuapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class LoginActivity extends AppCompatActivity {

    private EditText user, pass;
    private Button buttonLogin;
    // Progress Dialog
    private ProgressDialog pDialog;
    // JSON parser class
    JSONParser jsonParser = new JSONParser();
    private static final String LOGIN_URL = "http://kurjeriu-programele.netne.net/login.php";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonLogin=(Button)findViewById(R.id.button_sign_in);
        user=(EditText)findViewById(R.id.edit_text_username);
        pass=(EditText)findViewById(R.id.edit_text_password);

        buttonLogin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                if(isNetworkAvailable()) {
                    String username = user.getText().toString();
                    String password = pass.getText().toString();
                    new AttemptLogin().execute(username, password);
                }else{
                    Toast.makeText(LoginActivity.this, "No Internet Connection", Toast.LENGTH_LONG).show();
                }

            }
        });

    }


    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


    class AttemptLogin extends AsyncTask<String, String, String> {
        /**
         * Before starting background thread Show Progress Dialog
         */
        boolean failure = false;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(LoginActivity.this);
            pDialog.setMessage("Attempting for login...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... args) {
            // TODO Auto-generated method stub
            // here Check for success tag
            int success;
            String username = args[0];
            String password = args[1];
            try {

                HashMap<String, String> params = new HashMap<>();
                params.put("username", args[0]);
                params.put("password", args[1]);

                Log.d("request!", "starting");

                JSONObject json = jsonParser.makeHttpRequest(
                        LOGIN_URL, params);

                if(json!=null) {
                    // checking  log for json response
                    Log.d("Login attempt", json.toString());

                    // success tag for json
                    success = json.getInt(TAG_SUCCESS);
                    if (success == 1) {
                        Log.d("Successfully Login!", json.toString());

                        Intent ii = new Intent(LoginActivity.this, UserInterface.class);
                        finish();
                        // this finish() method is used to tell android os that we are done with current //activity now! Moving to other activity
                        startActivity(ii);
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
                Toast.makeText(LoginActivity.this, message, Toast.LENGTH_LONG).show();
            }
        }
    }


    }
