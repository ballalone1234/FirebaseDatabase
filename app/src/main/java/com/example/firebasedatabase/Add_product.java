package com.example.firebasedatabase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.concurrent.atomic.AtomicMarkableReference;

public class Add_product extends AppCompatActivity {

    EditText name, description;

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

    }

    //create method for import image file from gallery
    private void importImage(){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, 1);
    }
    File file;
    Uri filePath;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
           super.onActivityResult(requestCode, resultCode, data);

            ImageView imageView = findViewById(R.id.imagePreView);
            if(requestCode == 1 && resultCode == RESULT_OK){
                imageView.setImageURI(data.getData());
            }
            else if(requestCode == 0 && resultCode == RESULT_OK){
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                File file = new File(data.getData().getPath());
                imageView.setImageBitmap(bitmap);
            }
            filePath = data.getData();
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
        StorageReference riversRef = storageRef.child("images/"+filePath.getLastPathSegment());
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



    //create method for open camera and take picture
    private void openCamera(){
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        startActivityForResult(intent, 0);
    }

}