package com.codepride.dreamworld;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class Assets extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private TextView emailTextView;
    private GridView gridView;
    private List<String> itemsList;
    private List<String> colorsList;
    private ProgressBar progressBar;
    private Button logoutButton;
    private InterstitialAd mInterstitialAd;

    private Handler handler = new Handler();
    private Runnable refreshRunnable = new Runnable() {
        @Override
        public void run() {
            loadItems();
            handler.postDelayed(this, 3000000); // Refresh every 1 second
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assets);

        emailTextView = findViewById(R.id.emailTextView);
        gridView = findViewById(R.id.gridView);
        progressBar = findViewById(R.id.progressBar);
        logoutButton = findViewById(R.id.logoutButton);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
                // Initialization complete
            }
        });

        showads();

        // Check if the user is logged in
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            // User is logged in
            String email = currentUser.getEmail();
            emailTextView.setText(email);
            logoutButton.setVisibility(View.GONE); // Show logout button

            // Handle logout button click
            logoutButton.setOnClickListener(v -> {
                mAuth.signOut();
                startActivity(new Intent(Assets.this, Assets.class));
                finish();
            });
        } else {
            // User is not logged in
            emailTextView.setText("Guest"); // Set a default text for non-logged in users
            logoutButton.setVisibility(View.GONE); // Hide logout button
        }

        // Back button
        Button backButton = findViewById(R.id.button2);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Assets.this, menu.class);
                startActivity(intent);
                finish(); // Optional: Close the current activity if needed
            }
        });

        // VIP button
        Button vipButton = findViewById(R.id.button3);
        vipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Assets.this, vip.class);
                startActivity(intent);
                finish(); // Optional: Close the current activity if needed
            }
        });

        // Show logout button and set its click listener
        emailTextView.setOnClickListener(v -> {
            if (logoutButton.getVisibility() == View.VISIBLE) {
                logoutButton.setVisibility(View.GONE); // Hide the logout button
            } else {
                logoutButton.setVisibility(View.VISIBLE);
            }
        });

        // Load items from Firestore
        loadItems();

        // Set item click listener for the GridView
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = itemsList.get(position);
                fetchAndDisplayPrice(selectedItem);
            }
        });

        // Start auto-refreshing
        handler.postDelayed(refreshRunnable, 10000);
    }

    private void loadItems() {
        itemsList = new ArrayList<>();
        colorsList = new ArrayList<>();

        // Replace "items" with the name of the collection in your Firestore database
        db.collection("item")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        itemsList.clear(); // Clear the lists before adding new items
                        colorsList.clear();

                        for (DocumentChange document : task.getResult().getDocumentChanges()) {
                            String item = document.getDocument().getString("name");
                            String color = document.getDocument().getString("color");
                            itemsList.add(item);
                            colorsList.add(color);
                        }

                        // Display the items in the GridView using a custom adapter
                        CustomGridAdapter adapter = new CustomGridAdapter(itemsList, colorsList);
                        gridView.setAdapter(adapter);
                    } else {
                        Log.e("Assets", "Error loading items: " + task.getException());
                    }
                });
    }

    private void fetchAndDisplayPrice(String selectedItem) {
        // Show the progress bar while fetching the price
        progressBar.setVisibility(View.VISIBLE);

        db.collection("item")
                .whereEqualTo("name", selectedItem) // Adjust this condition based on your Firestore structure
                .get()
                .addOnCompleteListener(task -> {
                    // Hide the progress bar once the task is complete
                    progressBar.setVisibility(View.GONE);

                    if (task.isSuccessful() && !task.getResult().isEmpty()) {
                        String price = task.getResult().getDocuments().get(0).getString("price");
                        if (price != null) {
                            // Display the price in a dialog
                            showPriceDialog(selectedItem, price);
                        } else {
                            Log.e("Assets", "Price is null for item: " + selectedItem);
                        }
                    } else {
                        Log.e("Assets", "Firestore query failed: " + task.getException());
                    }
                });
    }

    private void showPriceDialog(String itemName, String price) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(itemName)
                .setMessage("Start time: " + price)
                .setPositiveButton("OK", (dialog, which) -> {
                    // Show an interstitial ad here
                    showads();
                    dialog.dismiss();
                })
                .show();
    }

    // Custom adapter for displaying items with names and colors
    private class CustomGridAdapter extends BaseAdapter {

        private List<String> items;
        private List<String> colors;

        public CustomGridAdapter(List<String> items, List<String> colors) {
            this.items = items;
            this.colors = colors;
        }

        @Override
        public int getCount() {
            return items.size();
        }

        @Override
        public Object getItem(int position) {
            return items.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(Assets.this).inflate(R.layout.custom_grid_item_layout, parent, false);
            }
            convertView.setBackgroundResource(R.drawable.item_background_selector);

            TextView itemNameTextView = convertView.findViewById(R.id.itemNameTextView);
            TextView itemColorTextView = convertView.findViewById(R.id.itemColorTextView);

            String itemName = items.get(position);
            String itemColor = colors.get(position);

            itemNameTextView.setText(itemName);
            itemColorTextView.setText(itemColor);

            return convertView;
        }
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
                        mInterstitialAd.show(Assets.this);
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
