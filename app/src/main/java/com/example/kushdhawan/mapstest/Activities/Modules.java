package com.example.kushdhawan.mapstest.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.kushdhawan.mapstest.R;

public class Modules extends AppCompatActivity {

    Button addParking, carPark;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modules);

        addParking = (Button)findViewById(R.id.addParking);
        carPark = (Button)findViewById(R.id.parkCar);

        addParking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                   Intent i = new Intent(Modules.this,AddParking.class);
                    startActivity(i);
            }
        });

        carPark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Modules.this,MapsActivity.class);
                startActivity(i);
            }
        });
    }
}
