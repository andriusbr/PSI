package com.example.andrius.kurjeriuapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

/**
 * Created by Andrius on 2016-02-21.
 */
public class AdminInterfaceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_interface);
        

        Button manageUsersButton=(Button)findViewById(R.id.button_manage_couriers);
        manageUsersButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent i = new Intent(AdminInterfaceActivity.this, ManageCouriers.class);
                startActivity(i);
            }
        });

        Button ordersButton=(Button)findViewById(R.id.button_admin_orders);
        ordersButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent i=new Intent(AdminInterfaceActivity.this, OrdersAdminActivity.class);
                startActivity(i);
            }
        });

        Button changePasswordButton=(Button)findViewById(R.id.button_admin_change_password);
        changePasswordButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent i=new Intent(AdminInterfaceActivity.this, ChangePasswordActivity.class);
                i.putExtra("username",getUsername());
                i.putExtra("isAdmin",true);
                finish();
                startActivity(i);
            }
        });

        Button logoutButton=(Button)findViewById(R.id.button_admin_logout);
        logoutButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent i = new Intent(AdminInterfaceActivity.this, LoginActivity.class);
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
}
