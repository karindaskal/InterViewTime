package com.example.android2projectnew.Adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.android2projectnew.Module.Chat;
import com.example.android2projectnew.Module.UserProfileData;
import com.example.android2projectnew.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    public static  final int MSG_TYPE_LEFT = 0;
    public static  final int MSG_TYPE_RIGHT = 1;

    private Context mContext;
    private List<Chat> mChat;
    private String imageurl;

    public OnMessageListener listener;

    FirebaseUser fuser;

    public MessageAdapter(Context mContext, List<Chat> mChat, String imageurl){
        this.mChat = mChat;
        this.mContext = mContext;
        this.imageurl = imageurl;
    }


    public interface OnMessageListener{
        void onMessageClick(int position, View view);
    }

    public void setListener(OnMessageListener listener){
        this.listener = listener;
    }


    @NonNull
    @Override
    public MessageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == MSG_TYPE_RIGHT) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.chat_item_right, parent, false);
            return new MessageAdapter.ViewHolder(view);
        } else {
            View view = LayoutInflater.from(mContext).inflate(R.layout.chat_item_left, parent, false);
            return new MessageAdapter.ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final MessageAdapter.ViewHolder holder, int position) {
        DatabaseReference myRef;
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getInstance().getReference().child("user");

        Chat chat = mChat.get(position);

        holder.show_message.setText(chat.getMessage());
        myRef.child(chat.getSender()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                UserProfileData mission = snapshot.getValue(UserProfileData.class);

                if (snapshot.exists()) {


                    // holder.userName.setText(mission.getUserName());
                    Glide.with(mContext).load(mission.getImageURL()).centerCrop().into(holder.profile_image);



                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Toast.makeText(getActivity(), "failed", Toast.LENGTH_SHORT).show();
            }
        });

        /*if (imageurl.equals("default")){
            holder.profile_image.setImageResource(R.mipmap.ic_launcher);
        } else {
            Glide.with(mContext).load(imageurl).into(holder.profile_image);
        }*/

        /*if (position == mChat.size()-1){
            if (chat.isIsseen()){
                holder.txt_seen.setText("Seen");
            } else {
                holder.txt_seen.setText("Delivered");
            }
        } else {
            holder.txt_seen.setVisibility(View.GONE);
        }*/

    }

    @Override
    public int getItemCount() {
        return mChat.size();
    }

    public  class ViewHolder extends RecyclerView.ViewHolder{

        TextView show_message;
        ImageView profile_image;
        //public TextView txt_seen;

        public ViewHolder(View itemView) {
            super(itemView);

            show_message = itemView.findViewById(R.id.show_message);
            profile_image = itemView.findViewById(R.id.chat_image_left);
            //txt_seen = itemView.findViewById(R.id.txt_seen);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onMessageClick(getAdapterPosition(),v);

                }
            });
        }


    }

    @Override
    public int getItemViewType(int position) {
        fuser = FirebaseAuth.getInstance().getCurrentUser();
        if (mChat.get(position).getSender().equals(fuser.getUid())){
            return MSG_TYPE_RIGHT;
        } else {
            return MSG_TYPE_LEFT;
        }
    }

}