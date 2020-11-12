package com.example.android2projectnew.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android2projectnew.Adapter.UserAdapter;
import com.example.android2projectnew.Module.Chat;
import com.example.android2projectnew.Module.UserProfileData;
import com.example.android2projectnew.Notifications.Token;
import com.example.android2projectnew.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;
import java.util.List;

public class ChatFragment extends Fragment {

    private RecyclerView recyclerView;

    private UserAdapter userAdapter;
    private ArrayList<UserProfileData> mUsers;

    ImageButton bactBtn;

    FirebaseUser fuser;
    DatabaseReference reference;

    String CHAT_FRAGMENT_TAG = "chat_fragment_tag";

    private List<String> usersList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.chats_list_fragment, container, false);

        recyclerView = view.findViewById(R.id.chat_list_recycler_view);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        fuser = FirebaseAuth.getInstance().getCurrentUser();

        usersList = new ArrayList<>();
        mUsers = new ArrayList<>();

        userAdapter = new UserAdapter(getContext(),mUsers,true);

        bactBtn = view.findViewById(R.id.back_chat);
        bactBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack();

            }
        });


        reference = FirebaseDatabase.getInstance().getReference("Chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                usersList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Chat chat = snapshot.getValue(Chat.class);

                    if(chat.getSender().equals(fuser.getUid())){
                        usersList.add(chat.getReceiver());
                    }
                    if(chat.getReceiver().equals(fuser.getUid())){
                        usersList.add(chat.getSender());
                    }
                }

                readChats();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        updateToken(FirebaseInstanceId.getInstance().getToken());

        return view;
    }


    private void updateToken(String token){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Tokens");
        Token token1 = new Token(token);
        reference.child(fuser.getUid()).setValue(token1);
    }

    private void readChats(){
        mUsers = new ArrayList<>();

        reference = FirebaseDatabase.getInstance().getReference("user");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                mUsers.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){

                    UserProfileData userProfileData = snapshot.getValue(UserProfileData.class);
                    for(String id : usersList){
                        if(!(userProfileData.getUserName() == null)) {
                            if (userProfileData.getId().equals(id)) {
                                if (mUsers.size() != 0) {
                                    for (int i = 0; i < mUsers.size(); i++) {
                                        if (!userProfileData.getId().equals(mUsers.get(i).getId())) {
                                            mUsers.add(userProfileData);
                                        }
                                    }

                                } else {
                                    mUsers.add(userProfileData);
                                }
                            }
                        }
                    }
                }
                userAdapter = new UserAdapter(getContext(),mUsers,true);
                recyclerView.setAdapter(userAdapter);

                userAdapter.setListener(new UserAdapter.onChatUserListener() {
                    @Override
                    public void onChatUserClick(int position, View view) {

                        MessageFragment messageFragment = MessageFragment.newInstance(mUsers.get(position).getId());
                        FragmentManager fragmentManager = getFragmentManager();
                        FragmentTransaction transaction = fragmentManager.beginTransaction();
                        transaction.add(R.id.drawer_layout,messageFragment ,CHAT_FRAGMENT_TAG);
                        transaction.addToBackStack(null);
                        transaction.commit();
                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
