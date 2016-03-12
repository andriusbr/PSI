package com.example.andrius.kurjeriuapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.andrius.kurjeriuapp.Classes.Courier;
import com.example.andrius.kurjeriuapp.Classes.Couriers;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Andrius on 2016-03-12.
 */
public class ListAdapter extends ArrayAdapter<Courier> {

    // Progress Dialog
    private ProgressDialog pDialog;
    // JSON parser class
    JSONParser jsonParser = new JSONParser();
    private static final String LOGIN_URL = "http://kurjeriu-programele.netne.net/remove_user.php";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    //---------------------------------------------------------


    public ListAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    public ListAdapter(Context context, int resource, List<Courier> items) {
        super(context, resource, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.row_layout_manage_users, null);
        }


        TextView tv1 = (TextView) v.findViewById(R.id.row_id);
        TextView tv2 = (TextView) v.findViewById(R.id.row_username);
        TextView tv3 = (TextView) v.findViewById(R.id.row_password);
        TextView tv4 = (TextView) v.findViewById(R.id.row_first_name);
        TextView tv5 = (TextView) v.findViewById(R.id.row_last_name);
        Button button=(Button)v.findViewById(R.id.row_button_remove);

        if(position==0){
            tv1.setText("ID"); tv2.setText("Username"); tv3.setText("Password"); tv4.setText("Firstname"); tv5.setText("Lastname");
            button.setBackgroundColor(Color.TRANSPARENT);
            button.setOnClickListener(new Button.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });
        }else {
            final Courier c = getItem(position);
            if (c != null) {
                if (tv1 != null) {
                    tv1.setText(String.valueOf(c.getId()));
                }

                if (tv2 != null) {
                    tv2.setText(c.getUsername());
                }

                if (tv3 != null) {
                    tv3.setText(c.getPassword());
                }

                if (tv4 != null) {
                    tv4.setText(c.getFirstName());
                }

                if (tv5 != null) {
                    tv5.setText(c.getLastName());
                }
                if (button != null) {
                    if(c.getId()==1 && c.getUsername().equals("admin")){
                        button.setBackgroundColor(Color.TRANSPARENT);
                        button.setOnClickListener(new Button.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                            }
                        });
                    }else{
                        button.setBackgroundResource(R.drawable.remove_button);

                        button.setOnClickListener(new Button.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                new AlertDialog.Builder(getContext())
                                        .setTitle("Delete entry")
                                        .setMessage("Are you sure you want to delete this entry?")
                                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                String id = String.valueOf(c.getId());
                                                new RemoveAttempt().execute(id);
                                            }
                                        })
                                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                // do nothing
                                            }
                                        })
                                        .setIcon(android.R.drawable.ic_dialog_alert)
                                        .show();
                            }
                        });
                    }

                }
            }
        }

        return v;
    }





    //-------------------------------------------------------------------------------------------------------


    class RemoveAttempt extends AsyncTask<String, String, String> {
        /**
         * Before starting background thread Show Progress Dialog
         */

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(getContext());
            pDialog.setMessage("Removing...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... args) {
            int success;
            try {

                HashMap<String, String> params = new HashMap<>();
                int id=Integer.parseInt(args[0]);
                params.put("id", args[0]);

                Log.d("request!", "starting");

                JSONObject json = jsonParser.makeHttpRequest(
                        LOGIN_URL, params);

                if (json != null) {
                    // checking  log for json response
                    Log.d("Remove attempt", json.toString());

                    // success tag for json
                    success = json.getInt(TAG_SUCCESS);
                    if (success == 1) {
                        Log.d("Successfully removed!", json.toString());

                        Intent intent = ((Activity)getContext()).getIntent();
                        ((Activity)getContext()).finish();
                        ((Activity)getContext()).startActivity(intent);

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
        }
    }
}
