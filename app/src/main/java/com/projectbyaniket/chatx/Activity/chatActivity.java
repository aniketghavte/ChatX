package com.projectbyaniket.chatx.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.projectbyaniket.chatx.Adapter.massagesAdapter;
import com.projectbyaniket.chatx.ModelClass.Massages;
import com.projectbyaniket.chatx.R;
import com.squareup.picasso.Picasso;

import java.sql.Date;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class chatActivity extends AppCompatActivity {

    String ReciverImage,ReciverUid,ReciverName,SenderUid;
    CircleImageView profileImage;
    TextView reciverName;
    FirebaseDatabase firebaseDatabase;
    FirebaseAuth firebaseAuth;
    public static String SImage;
    public static String RImage;

    ImageView sendBtn;
    EditText EditMassage;

    String senderRoom , reciverRoom;
    RecyclerView massageAdapter;
    ArrayList<Massages> massagesArrayList;
    massagesAdapter massagesAdapter;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        ReciverName = getIntent().getStringExtra("name");
        ReciverImage = getIntent().getStringExtra("ReciverImageUri");
        ReciverUid = getIntent().getStringExtra("uid");

        profileImage = findViewById(R.id.profile_imageInChat);
        reciverName = findViewById(R.id.ReciverName);

        sendBtn = findViewById(R.id.idSendBtn);
        EditMassage = findViewById(R.id.idEditMassage);

        Picasso.get().load(ReciverImage).into(profileImage);
        reciverName.setText(ReciverName);

        SenderUid = firebaseAuth.getUid();

        senderRoom = SenderUid+ReciverUid;
        reciverRoom = ReciverUid+SenderUid ;

        massageAdapter = findViewById(R.id.massageAdapter);
        massagesArrayList = new ArrayList<>();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        massageAdapter.setLayoutManager(linearLayoutManager);
        massagesAdapter = new massagesAdapter(chatActivity.this,massagesArrayList);
        massageAdapter.setAdapter(massagesAdapter);



        DatabaseReference reference = firebaseDatabase.getReference().child("user").child(firebaseAuth.getUid());
        DatabaseReference chatRef = firebaseDatabase.getReference().child("chats").child(senderRoom).child("messages");


        chatRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){

                    Massages massages = dataSnapshot.getValue(Massages.class);
                    massagesArrayList.add(massages);
                }

                massagesAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
               SImage = snapshot.child("imageuri").getValue().toString();
               RImage = ReciverImage;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Massage = EditMassage.getText().toString();

                massagesArrayList.clear();
                if (Massage.isEmpty()){
                    Toast.makeText(chatActivity.this, "Please Enter valid Text", Toast.LENGTH_SHORT).show();
                    return;
                }
                EditMassage.setText("");
                Date date = new Date(2003,8,24);


                Massages messages = new Massages(Massage,SenderUid,date.getTime());

                firebaseDatabase = FirebaseDatabase.getInstance();
                firebaseDatabase.getReference().child("chats")
                        .child(senderRoom)
                        .child("messages")
                        .push()
                        .setValue(messages)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        firebaseDatabase.getReference().child("chats")
                                .child(reciverRoom)
                                .child("messages")
                                .push()
                                .setValue(messages)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {


                                    }
                                });
                    }

                });
                massagesAdapter.notifyDataSetChanged();

            }
        });



    }
}