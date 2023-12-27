package com.example.firebasedatabase;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class List_product extends AppCompatActivity {

    ListView listView;
    ArrayAdapter<Product> aa;
    ArrayList<Product> products = new ArrayList<>();

    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = FirebaseFirestore.getInstance();
        setContentView(R.layout.activity_list_product);
        listView = (ListView)this.findViewById(R.id.listView);
        getData();
        aa = new ProductAdapter(this, products);
        listView.setAdapter(aa);
        ActionBar actionBar;
        actionBar = getSupportActionBar();
        actionBar.setTitle("List Product  B6301095");
        ColorDrawable colorDrawable
                = new ColorDrawable(Color.parseColor("#5bf03a"));
// Set BackgroundDrawable
        actionBar.setBackgroundDrawable(colorDrawable);
    }

    private void getData(){
        db.collection("products").get().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                products.clear();
                for(DocumentSnapshot document : task.getResult()){
                    Product product = document.toObject(Product.class);
                    products.add(product);
                }
                aa.notifyDataSetChanged();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);

        MenuItem item = menu.findItem(R.id.add_btn);
        MenuItem item1 = menu.findItem(R.id.menu_list);
        MenuItem item2 = menu.findItem(R.id.logout);



        return true;
    }


}