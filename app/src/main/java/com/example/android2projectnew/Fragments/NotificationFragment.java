package com.example.android2projectnew.Fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android2projectnew.Adapter.AdapterAnswer;
import com.example.android2projectnew.Adapter.NotificationCommentLikeAdapter;
import com.example.android2projectnew.Module.NotificationCommentLike;
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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class NotificationFragment extends Fragment {

    RecyclerView notificationRecycler;
    private FirebaseAuth firebaseAuth;
    private NotificationCommentLikeAdapter notificationAdapter;

    String timeStampString;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    ImageButton backBtn;
    DatabaseReference reference;

    private List<NotificationCommentLike> notificationList;

    public NotificationFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_notification,container,false);

        notificationRecycler = view.findViewById(R.id.recycler_notif);
        backBtn = view.findViewById(R.id.back_notif);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack();

            }
        });

        firebaseAuth = FirebaseAuth.getInstance();

        getAllNotification();



        return view;
    }

    private void getAllNotification(){

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        notificationRecycler.setLayoutManager(layoutManager);

        notificationList = new ArrayList<>();
      //  notificationAdapter = new NotificationCommentLikeAdapter(getContext(),notificationList);
        notificationAdapter = new NotificationCommentLikeAdapter(getContext(),notificationList);
        notificationRecycler.setAdapter(notificationAdapter);


        notificationAdapter.setListener(new NotificationCommentLikeAdapter.NotificationListener() {
            @Override
            public void onlongNotificationClick(final int position, final View view) {

              //  timeStampString = java.text.DateFormat.getDateTimeInstance().format(notificationList.get(position).getTimeStamp());
                timeStampString = String.valueOf(notificationList.get(position).getTimeStamp().getTime());
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Delete");
                builder.setMessage("Are you sure to delete this notification?");
                builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //delete notif
                        reference = database.getReference("user");
                        reference.child(firebaseAuth.getUid()).child("NotificationCommentLike").child(timeStampString)
                                .removeValue()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {

                                        notificationAdapter.notifyDataSetChanged();
                                       /* notificationRecycler.setAdapter(notificationAdapter);*/

                                        Snackbar.make((getActivity().findViewById(android.R.id.content)), R.string.notif_delete, Snackbar.LENGTH_SHORT)
                                                .show();

                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(getContext(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();
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
                builder.create().show();
            }
        });


        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("user");
        ref.child(firebaseAuth.getUid()).child("NotificationCommentLike")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        notificationList.clear();
                       // if(dataSnapshot.exists()){
                        for(DataSnapshot ds : dataSnapshot.getChildren()){
                             NotificationCommentLike model = ds.getValue(NotificationCommentLike.class);

                             notificationList.add(model);

                        }

                        notificationRecycler.setAdapter(notificationAdapter);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
}
