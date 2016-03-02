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
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by Andrius on 2016-02-21.
 */
public class ChangePasswordActivity extends AppCompatActivity {

    private EditText password, newPassword1, newPassword2;
    private Button buttonChange;
    // Progress Dialog
    private ProgressDialog pDialog;
    // JSON parser class
    JSONParser jsonParser = new JSONParser();
    private static final String LOGIN_URL = "http://kurjeriu-programele.netne.net/change_password.php";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        buttonChange=(Button)findViewById(R.id.button_change_password_confirm);
        password=(EditText)findViewById(R.id.edit_text_old_password);
        newPassword1=(EditText)findViewById(R.id.edit_text_new_password1);
        newPassword2=(EditText)findViewById(R.id.edit_text_new_password2);

        newPassword2.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View view, int keyCode, KeyEvent keyevent) {
                //If the keyevent is a key-down event on the "enter" button
                if ((keyevent.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    buttonClickActions();
                    return true;
                }
                return false;
            }
        });

        buttonChange.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                buttonClickActions();
            }
        });

    }

    private void buttonClickActions(){
        if(isNetworkAvailable()) {
            String oldPass = password.getText().toString();
            String newPass1 = newPassword1.getText().toString();
            String newPass2 = newPassword2.getText().toString();
            if(newPass1.equals(newPass2) && newPass1.length()>=5) {
                new AttemptChangePassword().execute(getUsername(), oldPass, newPass1);
            }else if(oldPass.equals("") || newPass1.equals("") || newPass2.equals("")) {
                Toast.makeText(ChangePasswordActivity.this, "One or more fields are empty.", Toast.LENGTH_LONG).show();
            }else if(!newPass1.equals(newPass2)){
                Toast.makeText(ChangePasswordActivity.this, "Passwords don't match", Toast.LENGTH_LONG).show();
            }else if(newPass1.length()<5){
                Toast.makeText(ChangePasswordActivity.this, "Password must be 5 characters or longer", Toast.LENGTH_LONG).show();
            }
        }else{
            Toast.makeText(ChangePasswordActivity.this, "No Internet Connection", Toast.LENGTH_LONG).show();
        }
    }


    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private String getUsername() {
        String username;
        Bundle extras = getIntent().getExtras();
        if(extras == null) {
            username = "";
        } else {
            username = extras.getString("username");
        }
        return username;
    }






    class AttemptChangePassword extends AsyncTask<String, String, String> {
        /**
         * Before starting background thread Show Progress Dialog
         */
        boolean failure = false;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ChangePasswordActivity.this);
            pDialog.setMessage("Changing password...");
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
                params.put("username", args[0]);
                params.put("old_password", args[1]);
                params.put("new_password", args[2]);

                Log.d("request!", "starting");

                JSONObject json = jsonParser.makeHttpRequest(
                        LOGIN_URL, params);

                if(json!=null) {
                    // checking  log for json response
                    Log.d("Change password attempt", json.toString());

                    // success tag for json
                    success = json.getInt(TAG_SUCCESS);
                    if (success == 1) {
                        Log.d("Password changed!", json.toString());

                        Intent ii = new Intent(ChangePasswordActivity.this, UserInterfaceActivity.class);
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
                Toast.makeText(ChangePasswordActivity.this, message, Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onBackPressed() {

        Bundle extras = getIntent().getExtras();
        if(extras == null) {
            Intent i=new Intent(ChangePasswordActivity.this, UserInterfaceActivity.class);
            finish();
            startActivity(i);
        } else {
            boolean isAdmin=extras.getBoolean("isAdmin");
            if(isAdmin){
                Intent i=new Intent(ChangePasswordActivity.this, AdminInterfaceActivity.class);
                finish();
                startActivity(i);
            }else{
                Intent i=new Intent(ChangePasswordActivity.this, UserInterfaceActivity.class);
                finish();
                startActivity(i);
            }
        }
    }
}
