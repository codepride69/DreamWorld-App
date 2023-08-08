package com.codepride.dreamworld;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class Assets extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private TextView emailTextView;
    private GridView gridView;
    private List<String> itemsList;
    private List<String> colorsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assets);

        emailTextView = findViewById(R.id.emailTextView);
        gridView = findViewById(R.id.gridView);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Check if the user is logged in
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String email = currentUser.getEmail();
            emailTextView.setText(email);

            // Load items from Firestore
            loadItems();
        }
    }

    private void loadItems() {
        itemsList = new ArrayList<>();
        colorsList = new ArrayList<>();

        // Replace "items" with the name of the collection in your Firestore database
        db.collection("item")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (DocumentChange document : task.getResult().getDocumentChanges()) {
                            String item = document.getDocument().getString("name");
                            String color = document.getDocument().getString("color");
                            itemsList.add(item);
                            colorsList.add(color);
                        }

                        // Display the items in the GridView using a custom adapter
                        CustomGridAdapter adapter = new CustomGridAdapter(itemsList, colorsList);
                        gridView.setAdapter(adapter);
                    }
                });
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
}
