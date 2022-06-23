package com.projectbyaniket.chatx.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.projectbyaniket.chatx.Activity.HomeActivity;
import com.projectbyaniket.chatx.Activity.chatActivity;
import com.projectbyaniket.chatx.R;
import com.projectbyaniket.chatx.ModelClass.Users;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder>{

    Context homeActivity;
    ArrayList<Users> usersArrayList;


    public UserAdapter(HomeActivity homeActivity, ArrayList<Users> usersArrayList) {
        this.homeActivity = homeActivity;
        this.usersArrayList = usersArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view = LayoutInflater.from(homeActivity).inflate(R.layout.itemuserrow,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Users users = usersArrayList.get(position);

        
        if (FirebaseAuth.getInstance().getCurrentUser().getUid().equals(users.getUid())){
            holder.itemView.setVisibility(View.GONE);
        }

        holder.userName.setText(users.name);
        holder.userStatus.setText(users.status);
        Picasso.get().load(users.imageuri).into(holder.userProfile);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(homeActivity, chatActivity.class);
                intent.putExtra("name",users.getName());
                intent.putExtra("ReciverImageUri",users.getImageuri());
                intent.putExtra("uid",users.getUid());
                homeActivity.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return usersArrayList.size();
    }
    class ViewHolder extends RecyclerView.ViewHolder{

        CircleImageView userProfile;
        TextView userName;
        TextView userStatus;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            userName= itemView.findViewById(R.id.UserName);
            userStatus = itemView.findViewById(R.id.UserStatus);
            userProfile = itemView.findViewById(R.id.Userprofile_image);
        }
    }
}
