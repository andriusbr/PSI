package com.example.andrius.kurjeriuapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;

import com.example.andrius.kurjeriuapp.R;

/**
 * Created by Andrius on 2016-02-12.
 */
public class UserInterface extends ActionBarActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitivity_user_interface);

        Button mapButton=(Button)findViewById(R.id.button_view_map);
        mapButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent i=new Intent(UserInterface.this, Map.class);
                startActivity(i);
            }
        });
    }
}
