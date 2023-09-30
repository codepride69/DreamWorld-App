package com.codepride.dreamworld;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

public class chooseleague extends AppCompatActivity {
    private InterstitialAd mInterstitialAd;
    private Button goPagebutton21;
    private Button goPagebutton22;
    private Button goPagebutton23;
    private Button goPagebutton24;
    private Button goPagebutton25;
    private Button goPagebutton26;
    private Button goPagebutton27;
    private Button goPagebutton28;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chooseleague);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {



            }
        });

        showads();

        goPagebutton21 = findViewById(R.id.button21);
        goPagebutton22 = findViewById(R.id.button22);
        goPagebutton23 = findViewById(R.id.button23);
        goPagebutton24 = findViewById(R.id.button24);
        goPagebutton25 = findViewById(R.id.button25);
        goPagebutton26 = findViewById(R.id.button26);
        goPagebutton27 = findViewById(R.id.button27);
        goPagebutton28 = findViewById(R.id.btn28);

        goPagebutton21.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(chooseleague.this, stream.class);
                startActivity(intent);
            }
        });
        goPagebutton22.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(chooseleague.this, stream.class);
                startActivity(intent);
            }
        });
        goPagebutton23.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(chooseleague.this, stream.class);
                startActivity(intent);
            }
        });
        goPagebutton24.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(chooseleague.this, stream.class);
                startActivity(intent);
            }
        });
        goPagebutton25.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(chooseleague.this, stream.class);
                startActivity(intent);
            }
        });
        goPagebutton26.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(chooseleague.this, stream.class);
                startActivity(intent);
            }
        });
        goPagebutton27.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(chooseleague.this, stream.class);
                startActivity(intent);
            }
        });
        goPagebutton28.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(chooseleague.this, menu.class);
                startActivity(intent);
            }
        });
    }
    private void showads() {
        AdRequest adRequest = new AdRequest.Builder().build();

        InterstitialAd.load(this,"ca-app-pub-3940256099942544/1033173712", adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        // The mInterstitialAd reference will be null until
                        // an ad is loaded.
                        mInterstitialAd = interstitialAd;
                        String TAG = "";
                        Log.i(TAG, "onAdLoaded");
                        mInterstitialAd.show(chooseleague.this);
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