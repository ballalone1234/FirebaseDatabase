package com.example.firebasedatabase;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;

public class Add_product extends AppCompatActivity {

    EditText name, description;
    TextView userEmail;

    FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = FirebaseFirestore.getInstance();
        setContentView(R.layout.activity_addproduct);
        ImageButton btnImport = findViewById(R.id.image_btn);
        btnImport.setOnClickListener(v -> importImage());
        ImageButton btnCamera = findViewById(R.id.camera_btn);
        btnCamera.setOnClickListener(v -> openCamera());
        Button btnSave = findViewById(R.id.add_btn);
        btnSave.setOnClickListener(v -> saveImage());
        name = findViewById(R.id.nameP);
        description = findViewById(R.id.DetailP);
        ActionBar actionBar;
        actionBar = getSupportActionBar();
        ColorDrawable colorDrawable
                = new ColorDrawable(Color.parseColor("#5bf03a"));
// Set BackgroundDrawable
        actionBar.setBackgroundDrawable(colorDrawable);
        userEmail = findViewById(R.id.user_email);
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String email = auth.getCurrentUser().getEmail();
        userEmail.setText("Login by : " + email);

    }

    //create method for import image file from gallery
    private void importImage(){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, 1);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        setTitle("Add Product B6301095");
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);

        MenuItem item = menu.findItem(R.id.add_btn);
        MenuItem item1 = menu.findItem(R.id.menu_list);
        MenuItem item2 = menu.findItem(R.id.logout);
        item1.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent intent = new Intent(Add_product.this, List_product.class);
                startActivity(intent);
                return false;
            }
        });
        item2.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent intent = new Intent(Add_product.this, Login.class);
                startActivity(intent);
                return false;
            }
        });

        return true;
    }
    Uri filePath;
    Bitmap bitmap;
    ImageView imageView;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
           super.onActivityResult(requestCode, resultCode, data);

             imageView = findViewById(R.id.imagePreView);
            if(requestCode == 1 && resultCode == RESULT_OK){
                imageView.setImageURI(data.getData());
                filePath = data.getData();
            }
            else if(requestCode == 0 && resultCode == RESULT_OK){
                bitmap = (Bitmap) data.getExtras().get("data");
                imageView.setImageBitmap(bitmap);


            }

        }

        //save data to firebase firestore
        private void saveData(String uri){
            String nameP = name.getText().toString();
            String descriptionP = description.getText().toString();
            Product product = new Product(nameP, descriptionP , uri);
            ProgressBar progressBar = findViewById(R.id.progressBarProduct);
            progressBar.setVisibility(ProgressBar.VISIBLE);
            db.collection("products")
                    .add(product)
                    .addOnSuccessListener(documentReference -> {
                        Toast.makeText(Add_product.this, "Success", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(ProgressBar.GONE);
                        //success
                    })
                    .addOnFailureListener(e -> {
                        //error
                    });
        }


    //save image to firebase storage
        private void saveImage(){
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        StorageReference riversRef = storageRef.child("images/"+ Timestamp.now());
            if (filePath != null) {
                riversRef.putFile(filePath)
                        .addOnSuccessListener(taskSnapshot -> {
                            // Get a URL to the uploaded content
                            //get url from firebase storage
                            riversRef.getDownloadUrl().addOnSuccessListener(uri -> {
                                Log.i("url", uri.toString());
                                saveData(uri.toString());
                            });

                        })
                        .addOnFailureListener(exception -> {
                            // Handle unsuccessful uploads
                            // ...
                        });
            } else {
                Log.i("file", "file not found");
                // Handle the case where the file doesn't exist at the specified location
            }
    }


    //method save data to firebase

    //create method for open camera and take picture
    private void openCamera(){
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        startActivityForResult(intent, 0);
    }

}