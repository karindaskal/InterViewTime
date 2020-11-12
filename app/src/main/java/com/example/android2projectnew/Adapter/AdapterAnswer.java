package com.example.android2projectnew.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.android2projectnew.Module.Comment;
import com.example.android2projectnew.Module.UserProfileData;
import com.example.android2projectnew.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterAnswer extends  RecyclerView.Adapter<AdapterAnswer.MyHolder>{

    public Context context;
    public List<Comment> commentsList;
    Comment comment;

    public AdapterAnswer(List<Comment> commentsList,Context context) {
        this.commentsList = commentsList;
        this.context =context;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_card,parent,false);

        MyHolder holder = new MyHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyHolder holder, int position) {
        DatabaseReference myRef;
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getInstance().getReference().child("user");
        comment = commentsList.get(position);


        //holder.timeTn.setText(java.text.DateFormat.getDateTimeInstance().format(comment.getTimpeStamp()));
        holder.timeTn.setText(java.text.DateFormat.getDateTimeInstance().format(comment.getTimpeStamp()));


        holder.answerTv.setText(comment.getComment());
        holder.nameTv.setText(comment.getuName());

        myRef.child(comment.getuId()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                UserProfileData mission = snapshot.getValue(UserProfileData.class);

                if (snapshot.exists()) {
                    Glide
                            .with(context)
                            .load(mission.getImageURL())
                            .centerCrop()
                            .into(holder.circleImageView);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Toast.makeText(getActivity(), "failed", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return commentsList.size();
    }

    public class  MyHolder extends RecyclerView.ViewHolder{
        CircleImageView circleImageView;
        TextView nameTv,answerTv,timeTn;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            nameTv =itemView.findViewById(R.id.nameTv);
            answerTv=itemView.findViewById(R.id.comment);
            timeTn=itemView.findViewById(R.id.time);
            circleImageView = itemView.findViewById(R.id.image_card);

        }
    }
}
