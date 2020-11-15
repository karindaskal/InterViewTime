package com.example.android2projectnew.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android2projectnew.Adapter.UserAdapter;
import com.example.android2projectnew.R;
import com.example.android2projectnew.Module.UserProfileData;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class UserFragment extends Fragment {

    FirebaseUser firebaseUser;
    DatabaseReference reference;

    private RecyclerView usersRecyclerView;
    private UserAdapter userAdapter;
    private List<UserProfileData> mUsers;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.chats_list_fragment, container, false);

        usersRecyclerView = view.findViewById(R.id.chat_list_recycler_view);
        usersRecyclerView.setHasFixedSize(true);
        usersRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mUsers=new ArrayList<>();

        readUsers();

        return view;
    }

    private void readUsers(){
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("user");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mUsers.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){

                    UserProfileData userProfileData = snapshot.getValue(UserProfileData.class);

                    assert userProfileData!=null;
                    assert  firebaseUser!=null;
                    if(!snapshot.getKey().equals(firebaseUser.getUid())){
                        mUsers.add(userProfileData);
                    }
                }
                userAdapter = new UserAdapter(getContext(), mUsers,true);
                usersRecyclerView.setAdapter(userAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
