package com.codepride.dreamworld;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class news extends AppCompatActivity {

    private TextView textView;
    private ImageView imageView;
    private GridView gridView;
    private ProgressBar progressBar;
    private Button homeButton;

    private final Handler handler = new Handler();
    private final int delay = 300000; // 10 seconds in milliseconds
    private Runnable refreshRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        // Initialize views
        textView = findViewById(R.id.textView4);
        imageView = findViewById(R.id.imageView);
        gridView = findViewById(R.id.gridView);
        progressBar = findViewById(R.id.progressBar);
        homeButton = findViewById(R.id.button);

        // Set up the button click listener
        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Handle the HOME button click
                // Start the HomeActivity
                Intent intent = new Intent(news.this, menu.class);
                startActivity(intent);
            }
        });

        // Example: Load data from Firebase Firestore
        loadNewsData();

        // Set up item click listener for the GridView
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Retrieve the selected NewsItem
                NewsItem selectedItem = (NewsItem) parent.getItemAtPosition(position);

                // Display the popup with image, title, and description
                showPopup(selectedItem);
            }
        });

        // Set up a periodic refresh of data
        setupPeriodicRefresh();
    }

    private void loadNewsData() {
        // Access a Cloud Firestore instance
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Assuming you have a "news" collection in your Firestore
        db.collection("news")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<NewsItem> data = new ArrayList<>();

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                // Retrieve data from Firestore document
                                String title = document.getString("title");
                                String imageUrl = document.getString("imageurl");
                                String description = document.getString("description");

                                // Add data to the list
                                data.add(new NewsItem(title, imageUrl, description));
                            }

                            // Set up the GridView with the custom adapter
                            NewsAdapter adapter = new NewsAdapter(news.this, R.layout.grid_item, data);
                            gridView.setAdapter(adapter);

                            // Hide progress bar after data loading is complete
                            progressBar.setVisibility(View.GONE);
                        } else {
                            // Handle errors
                            progressBar.setVisibility(View.GONE);
                            // Show an error message or handle the error as needed
                        }
                    }
                });
    }


    private void setupPeriodicRefresh() {
        refreshRunnable = new Runnable() {
            @Override
            public void run() {
                // Load data from Firebase Firestore
                loadNewsData();

                // Schedule the next refresh after 10 seconds
                handler.postDelayed(this, delay);
            }
        };

        // Schedule the first refresh after 10 seconds
        handler.postDelayed(refreshRunnable, delay);
    }

    // Data model class
    private static class NewsItem {
        String title;
        String imageUrl;
        String description;

        NewsItem(String title, String imageUrl, String description) {
            this.title = title;
            this.imageUrl = imageUrl;
            this.description = description;
        }
    }

    // Custom adapter for GridView
    private static class NewsAdapter extends ArrayAdapter<NewsItem> {
        private final int layoutResource;
        private final LayoutInflater inflater;

        NewsAdapter(news context, int resource, List<NewsItem> objects) {
            super(context, resource, objects);
            layoutResource = resource;
            inflater = LayoutInflater.from(context);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = inflater.inflate(layoutResource, parent, false);
            }

            ImageView imageView = convertView.findViewById(R.id.gridItemImage);
            TextView titleTextView = convertView.findViewById(R.id.gridItemTitle);

            NewsItem newsItem = getItem(position);

            if (newsItem != null) {
                // Use Glide to load the image from the URL and clear any previous image
                Glide.with(getContext())
                        .load(newsItem.imageUrl)
                        .centerCrop()
                        .placeholder(R.mipmap.soccerplayer) // Add a placeholder if needed
                        .into(imageView);

                int desiredHeight = 500; // Set your desired height in pixels

                ViewGroup.LayoutParams layoutParams = imageView.getLayoutParams();
                layoutParams.height = desiredHeight;
                imageView.setLayoutParams(layoutParams);

                titleTextView.setText(newsItem.title);
            }

            return convertView;
        }
    }

        // Method to show the popup with image, title, and description
    private void showPopup(NewsItem selectedItem) {
        // Create an AlertDialog with a custom layout
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View popupView = inflater.inflate(R.layout.popup_layout, null);

        // Set the content of the popup
        ImageView popupImageView = popupView.findViewById(R.id.popupImage);
        TextView popupTitleTextView = popupView.findViewById(R.id.popupTitle);
        TextView popupDescriptionTextView = popupView.findViewById(R.id.popupDescription);

        Glide.with(this)
                .load(selectedItem.imageUrl)
                .into(popupImageView);

        popupTitleTextView.setText(selectedItem.title);
        popupDescriptionTextView.setText(selectedItem.description);

        // Set the custom layout for the AlertDialog
        builder.setView(popupView)
                .setPositiveButton("Back", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Handle OK button click if needed
                    }
                });

        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();

        // Get the background drawable from the popupView
        Drawable popupBackground = popupView.getBackground();

        // Set the background drawable of the AlertDialog window
        alertDialog.getWindow().setBackgroundDrawable(popupBackground);

        // Show the AlertDialog
        alertDialog.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Remove the refresh callbacks when the activity is destroyed
        handler.removeCallbacks(refreshRunnable);
    }
}
