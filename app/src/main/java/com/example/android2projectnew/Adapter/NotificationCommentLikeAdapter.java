package com.example.android2projectnew.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.android2projectnew.Fragments.PostDetailFragment;
import com.example.android2projectnew.Module.InterviewQuestions;
import com.example.android2projectnew.Module.NotificationCommentLike;
import com.example.android2projectnew.Module.UserProfileData;
import com.example.android2projectnew.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class NotificationCommentLikeAdapter extends RecyclerView.Adapter<NotificationCommentLikeAdapter.MyHolder> {
    public Context context;
    public List<NotificationCommentLike> notificationList;
    NotificationCommentLike notification;

    String timeStampString;

    final String NOTIF_FRAGMENT_TAG = "tag";

    private FirebaseAuth firebaseAuth;
    NotificationListener listener;

    public NotificationListener getListener() {
        return listener;
    }

    View view;

    public NotificationCommentLikeAdapter(Context context, List<NotificationCommentLike> notificationList) {
        this.notificationList = notificationList;
        this.context = context;
        firebaseAuth = FirebaseAuth.getInstance();
    }

    public void setListener(NotificationListener listener) {
        this.listener = listener;
    }

    public interface NotificationListener{
        void onlongNotificationClick(int position, View view);
       // void onImageOrTextUserNameClicked(int position, View view);
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_notification, parent, false);

        MyHolder holder = new MyHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyHolder holder, final int position) {

        final DatabaseReference myRef;
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getInstance().getReference();


        notification = notificationList.get(position);

        timeStampString = java.text.DateFormat.getDateTimeInstance().format(notification.getTimeStamp());

        //holder.timeTn.setText(java.text.DateFormat.getDateTimeInstance().format(comment.getTimpeStamp()));

        holder.timeTn.setText(timeStampString);
        holder.notificationTv.setText(notification.getNotification());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //InterviewQuestions question = questionsList.get(position);

                myRef.child("list/Questions").child(notificationList.get(position).getpId())
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot1) {
                              //  InterviewQuestions mission = snapshot.getValue(InterviewQuestions.class);
                                InterviewQuestions mission= new InterviewQuestions();
                                mission.setAnswer(snapshot1.child("answer").getValue(String.class));
                                mission.setCompany(snapshot1.child("company").getValue(String.class));
                                mission.setDate(snapshot1.child("date").getValue(String.class));
                                mission.setId(snapshot1.child("id").getValue(String.class));
                                mission.setIdQ(snapshot1.child("idQ").getValue(Integer.class));
                                mission.setJob(snapshot1.child("job").getValue(String.class));
                                mission.setLikes(snapshot1.child("likes").getValue(Integer.class));
                                mission.setpComment(snapshot1.child("pComment").getValue(String.class));
                                mission.setProses(snapshot1.child("proses").getValue(String.class));
                                mission.setQuestion(snapshot1.child("question").getValue(String.class));
                                mission.setUserName(snapshot1.child("userName").getValue(String.class));
                                mission.setTimeStamp(snapshot1.child("timeStamp").getValue(Date.class));
                                mission.setLongitude(snapshot1.child("longitude").getValue(Double.class));
                                mission.setLatitude(snapshot1.child("latitude").getValue(Double.class));

                                PostDetailFragment postDetailFragment = PostDetailFragment.newInstance(mission);
                                FragmentManager fragmentManager = ((AppCompatActivity)context).getSupportFragmentManager();
                                FragmentTransaction transaction = fragmentManager.beginTransaction();
                                transaction.add(R.id.drawer_layout, postDetailFragment, NOTIF_FRAGMENT_TAG);
                                transaction.addToBackStack(null);
                                transaction.commit();

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });


            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                listener.onlongNotificationClick(position,view);

           /*     AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Delete");
                builder.setMessage("Are you sure to delete this notification?");
                builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //delete notif
                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("user");
                        reference.child(firebaseAuth.getUid()).child("NotificationCommentLike").child(timeStampString)
                                .removeValue().
                                addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Snackbar.make(view, R.string.notif_delete, Snackbar.LENGTH_SHORT)
                                                .show();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(context, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.create().show();*/

                return false;
            }
        });



        //  holder.nameTv.setText(notification.getsName());

        myRef.child("user").child(notification.getsUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                UserProfileData mission = snapshot.getValue(UserProfileData.class);

                if (snapshot.exists()) {

                    holder.nameTv.setText(mission.getUserName());
                    Glide.with(context).load(mission.getImageURL()).centerCrop().into(holder.circleImageView);
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
        return notificationList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        CircleImageView circleImageView;
        TextView nameTv, notificationTv, timeTn;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            nameTv = itemView.findViewById(R.id.nameTv_notif);
            notificationTv = itemView.findViewById(R.id.notif);
            timeTn = itemView.findViewById(R.id.time_notif);
            circleImageView = itemView.findViewById(R.id.image_card_notif);

        }
    }
}
