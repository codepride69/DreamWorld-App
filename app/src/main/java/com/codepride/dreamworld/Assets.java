package com.codepride.dreamworld;

import android.os.Bundle;
import android.widget.ArrayAdapter;
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

                        // Display the items in the GridView
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                                Assets.this,
                                android.R.layout.simple_list_item_1,
                                itemsList
                        );
                        gridView.setAdapter(adapter);

                        // You can use colorsList to customize the appearance of GridView items
                        // For example, you can set colors or backgrounds based on each item's color field.
                    }
                });
    }
}
