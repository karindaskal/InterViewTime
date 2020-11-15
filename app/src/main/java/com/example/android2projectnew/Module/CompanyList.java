package com.example.android2projectnew.Module;

import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CompanyList {
    DatabaseReference myRef;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    private static List<String> company = null;
    private CompanyList() {}
    static CompanyList mySingleton;
    public static CompanyList getMySingleton() {
        if (mySingleton == null) {

            mySingleton = new CompanyList();
        }
        return mySingleton;
    }
    public void add (String company){
        this.company=new ArrayList<>();

        this.company.add(company);
        myRef = database.getInstance().getReference().child("list/company");
        myRef.setValue(this.company);

    }
    public  List<String> read(){
        myRef = database.getInstance().getReference().child("list");
     /*   if(company==null)

        {
            company=new ArrayList<>();
            company.add("אמדוקס");
            company.add("בנק לאומי");
            company.add("ג'יגה");
            company.add("נטוויזן 013");
            company.add( "amdo");
        }

       return company;*/
     if(company==null){
         company=new ArrayList<>();
     }
        myRef.child("company").addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                company.clear();

                if(snapshot.exists()) {
                    for(DataSnapshot snapshot1 : snapshot.getChildren()) {
                        String mission = snapshot1.getValue(String.class);
                        company.add(mission);
                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
               // Toast.makeText(getActivity(), "fail2", Toast.LENGTH_SHORT).show();
            }
        });
        return company;
    }
}

