package com.example.android2projectnew.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.android2projectnew.Module.JobList;
import com.example.android2projectnew.Module.CompanyList;
import com.example.android2projectnew.R;
import com.example.android2projectnew.Module.UserProfileData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class EditProfileFragment extends Fragment {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef;

    EditText editName;
    ImageButton backBtn;

    FirebaseUser user;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_profile,container,false);
        Button addB = view.findViewById(R.id.add_btn_profile);
        user = FirebaseAuth.getInstance().getCurrentUser();
        myRef = database.getReference().child("user/"+user.getUid());

        backBtn = view.findViewById(R.id.back_profile_edit);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack();

            }
        });



        CompanyList companyList = CompanyList.getMySingleton();
        JobList jobList=JobList.getMySingleton();

        editName = view.findViewById(R.id.profile_EditNameDescription);


        final ArrayAdapter<String> jobDescriptionEditAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, jobList.read(getContext()));
        final AutoCompleteTextView jobDescriptionEditProfile = (AutoCompleteTextView) view.findViewById(R.id.profile_EditJobTitleDescription);
        jobDescriptionEditProfile.setAdapter(jobDescriptionEditAdapter);

        ArrayAdapter<String> companyDescriptionEditAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, companyList.read());
        final AutoCompleteTextView companyDescriptionEditProfile = (AutoCompleteTextView) view.findViewById(R.id.profile_EditCompanyDescription);
        companyDescriptionEditProfile.setAdapter(companyDescriptionEditAdapter);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                UserProfileData mission = snapshot.getValue(UserProfileData.class);

                if (snapshot.exists()) {
                    editName.setText(mission.getUserName());
                    jobDescriptionEditProfile.setText(mission.getJob());
                    companyDescriptionEditProfile.setText(mission.getCompany());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //Toast.makeText(getActivity(), "failed", Toast.LENGTH_SHORT).show();
            }
        });

        addB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                        .setDisplayName(editName.getText().toString()).build();

                user.updateProfile(profileUpdates)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                   // Log.d(TAG, "User profile updated.");
                                }
                            }
                        });


                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("job",jobDescriptionEditProfile.getText().toString());
                hashMap.put("company",companyDescriptionEditProfile.getText().toString());
                hashMap.put("userName",editName.getText().toString());

                myRef.updateChildren(hashMap);
                getActivity().getSupportFragmentManager().beginTransaction().remove(EditProfileFragment.this).commit();

            }
        });


        return view;
    }
}
