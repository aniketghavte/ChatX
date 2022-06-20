package com.projectbyaniket.chatx.Adapter;

import static com.projectbyaniket.chatx.Activity.chatActivity.RImage;
import static com.projectbyaniket.chatx.Activity.chatActivity.SImage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.projectbyaniket.chatx.ModelClass.Massages;
import com.projectbyaniket.chatx.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class massagesAdapter extends RecyclerView.Adapter {

    Context context;
    ArrayList<Massages> massagesArrayList;

    public massagesAdapter(Context context, ArrayList<Massages> massagesArrayList) {
        this.context = context;
        this.massagesArrayList = massagesArrayList;
    }

    int ITEM_SEND = 1;
    int ITEM_RECIVE =2;
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType== ITEM_SEND){
            View view = LayoutInflater.from(context).inflate(R.layout.sender_layout,parent,false);
            return new SenderViewHolder(view);
        }else {
            View view = LayoutInflater.from(context).inflate(R.layout.reciver_layout,parent,false);
            return new ReciverViewHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        Massages massages = massagesArrayList.get(position);
        if (holder.getClass()==SenderViewHolder.class){
            SenderViewHolder viewHolder = (SenderViewHolder) holder;
            viewHolder.txtMassages.setText((massages.getMassage()));
            Picasso.get().load(SImage).into(viewHolder.circleImageView);
        }else {
            ReciverViewHolder viewHolder = (ReciverViewHolder) holder;
            viewHolder.txtMassages.setText((massages.getMassage()));
            Picasso.get().load(RImage).into(viewHolder.circleImageView);

        }

    }

    @Override
    public int getItemCount() {
        return massagesArrayList.size();
    }

    @Override
    public int getItemViewType(int position) {
        Massages massages = massagesArrayList.get(position);
        if (FirebaseAuth.getInstance().getCurrentUser().getUid().equals(massages.getSenderId())){
            return ITEM_SEND;
        }else {
            return ITEM_RECIVE;
        }
    }

    class SenderViewHolder extends RecyclerView.ViewHolder{

        CircleImageView circleImageView;
        TextView txtMassages;
        public SenderViewHolder(@NonNull View itemView) {
            super(itemView);

            circleImageView = itemView.findViewById(R.id.profile_imageInSender);
            txtMassages = itemView.findViewById(R.id.idtxtMassageInSender);
        }
    }
    class ReciverViewHolder extends RecyclerView.ViewHolder{
        CircleImageView circleImageView;
        TextView txtMassages;
        public ReciverViewHolder(@NonNull View itemView) {
            super(itemView);

            circleImageView = itemView.findViewById(R.id.profile_imageInReciver);
            txtMassages = itemView.findViewById(R.id.idtxtMassageInReciver);
        }
    }
}
