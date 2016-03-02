package com.example.andrius.kurjeriuapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by Andrius on 2016-02-12.
 */
public class UserInterfaceActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_interface);

        setLoggedInMessage();

        Button mapButton=(Button)findViewById(R.id.button_view_map);
        mapButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent i = new Intent(UserInterfaceActivity.this, Map.class);
                startActivity(i);
            }
        });

        Button changePasswordButton=(Button)findViewById(R.id.button_change_password);
        changePasswordButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent i=new Intent(UserInterfaceActivity.this, ChangePasswordActivity.class);
                i.putExtra("username",getUsername());
                i.putExtra("isAdmin",true);
                finish();
                startActivity(i);
            }
        });

        Button logoutButton=(Button)findViewById(R.id.button_user_logout);
        logoutButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent i=new Intent(UserInterfaceActivity.this, LoginActivity.class);
                finish();
                startActivity(i);
            }
        });
    }

    private String getUsername() {
        String username;
        Bundle extras = getIntent().getExtras();
        if(extras == null) {
            username = null;
        } else {
            username = extras.getString("username");
        }
        return username;
    }

    private void setLoggedInMessage(){
        TextView message=(TextView)findViewById(R.id.textView_logged_in_as);
        String username=getUsername();
        if(username!=null){
            message.append(getResources().getString(R.string.logged_in_as)+" "+username);
        }
    }
}
