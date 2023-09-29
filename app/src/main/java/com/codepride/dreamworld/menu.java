package com.codepride.dreamworld;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class menu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        // Get references to your buttons
        View footballGalleryButton = findViewById(R.id.btn1);
        View footballPredictionsButton = findViewById(R.id.btn3);
        View footballLivestreamButton = findViewById(R.id.btn12);

        // Set click listeners for the buttons
        footballGalleryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Handle the click event for "FOOTBALL GALLERY" button
                // Example: Start a new activity when this button is clicked.
                Intent intent = new Intent(menu.this, home.class);
                startActivity(intent);
            }
        });

        footballPredictionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Handle the click event for "FOOTBALL PREDICTIONS" button
                // Example: Start a new activity when this button is clicked.
                Intent intent = new Intent(menu.this, Assets.class);
                startActivity(intent);
            }
        });

        footballLivestreamButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Handle the click event for "FOOTBALL LIVESTREAM" button
                // Example: Start a new activity when this button is clicked.
                Intent intent = new Intent(menu.this, Assets.class);
                startActivity(intent);
            }
        });
    }
}
