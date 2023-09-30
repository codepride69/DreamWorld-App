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

public class stream extends AppCompatActivity {
    private InterstitialAd mInterstitialAd;

    private Button goPagebutton51;
    private Button goPagebutton52;
    private Button goPagebutton53;
    private Button goPagebutton54;
    private Button goPagebutton55;
    private Button goPagebutton56;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stream);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {



            }
        });

        showads();

        goPagebutton51 = findViewById(R.id.button51);
        goPagebutton52 = findViewById(R.id.server1);
        goPagebutton53 = findViewById(R.id.server2);
        goPagebutton54 = findViewById(R.id.server3);
        goPagebutton55 = findViewById(R.id.server4);
        goPagebutton56 = findViewById(R.id.server5);

        goPagebutton51.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(stream.this, menu.class);
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
                        mInterstitialAd.show(stream.this);
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