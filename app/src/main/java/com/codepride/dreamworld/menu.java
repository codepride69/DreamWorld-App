package com.codepride.dreamworld;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

public class menu extends AppCompatActivity {

    private InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {



            }
        });

        showads();

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
                Intent intent = new Intent(menu.this, news.class);
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
                Intent intent = new Intent(menu.this, chooseleague.class);
                startActivity(intent);
            }
        });
    }
    private void showads() {
        AdRequest adRequest = new AdRequest.Builder().build();

        InterstitialAd.load(this,"ca-app-pub-3348841996734968/1459942436", adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        // The mInterstitialAd reference will be null until
                        // an ad is loaded.
                        mInterstitialAd = interstitialAd;
                        String TAG = "";
                        Log.i(TAG, "onAdLoaded");
                        mInterstitialAd.show(menu.this);
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error
                        String TAG = "";
                        Log.i(TAG, loadAdError.toString());
                        mInterstitialAd = null;
                    }
                });
    }
}
