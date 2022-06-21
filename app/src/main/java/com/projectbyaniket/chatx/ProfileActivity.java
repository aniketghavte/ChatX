package com.projectbyaniket.chatx;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.projectbyaniket.chatx.ModelClass.Users;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {

    CircleImageView circleImageView;
    EditText SettingName,SettingStatus;
    FirebaseAuth auth;
    FirebaseDatabase Database;
    FirebaseStorage Storage;
    ImageView save;
    Uri selectedImageUri;
    String email;
    ProgressDialog progressDialog;
    Button resetBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);

        auth = FirebaseAuth.getInstance();
        Database = FirebaseDatabase.getInstance();
        Storage = FirebaseStorage.getInstance();

        SettingName = findViewById(R.id.idSettingName);
        SettingStatus = findViewById(R.id.idSettingStatus);
        circleImageView = findViewById(R.id.profile_imageInSetting);
        save = findViewById(R.id.idSaveBtn);
        resetBtn = findViewById(R.id.idResetPass);

        DatabaseReference reference = Database.getReference().child("user").child(auth.getUid());
        StorageReference storageReference = Storage.getReference().child("Upload").child(auth.getUid());

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                email = snapshot.child("email").getValue().toString();
                String name = snapshot.child("name").getValue().toString();
                String status = snapshot.child("status").getValue().toString();
                String imageUri = snapshot.child("imageuri").getValue().toString();

                SettingName.setText(name);
                SettingStatus.setText(status);
                Picasso.get().load(imageUri).into(circleImageView);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        resetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(ProfileActivity.this, "Password reset link send to "+ email, Toast.LENGTH_SHORT).show();

                        }else {
                            Toast.makeText(ProfileActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
            }
        });

        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 111);
            }
        });


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                progressDialog.show();
                String name = SettingName.getText().toString();
                String status = SettingStatus.getText().toString();
                if (selectedImageUri!= null){
                    storageReference.putFile(selectedImageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String finalImageUri = uri.toString();
                                    Users users = new Users(auth.getUid(),name,email,finalImageUri,status);

                                    reference.setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()){
                                                progressDialog.dismiss();
                                                Toast.makeText(ProfileActivity.this, "Data Succesfully Updated", Toast.LENGTH_SHORT).show();
                                            }else {
                                                progressDialog.dismiss();
                                                Toast.makeText(ProfileActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }
                            });
                        }
                    });
                }else {
                    storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String finalImageUri = uri.toString();
                            Users users = new Users(auth.getUid(),name,email,finalImageUri,status);

                            reference.setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        progressDialog.dismiss();
                                        Toast.makeText(ProfileActivity.this, "Data Succesfully Updated", Toast.LENGTH_SHORT).show();
                                    }else {
                                        progressDialog.dismiss();
                                        Toast.makeText(ProfileActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    });
                }

            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 111){
            if (data != null){
                selectedImageUri = data.getData();
                circleImageView.setImageURI(selectedImageUri);
            }
        }
    }
}