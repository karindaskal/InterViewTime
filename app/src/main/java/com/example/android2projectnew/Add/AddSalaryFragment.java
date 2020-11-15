package com.example.android2projectnew.Add;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.android2projectnew.Module.CompanyList;
import com.example.android2projectnew.Module.JobList;
import com.example.android2projectnew.Module.Opinion;
import com.example.android2projectnew.R;
import com.example.android2projectnew.Module.Salary;
import com.example.android2projectnew.Lists.SalaryList;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class AddSalaryFragment extends Fragment {

    final String TAG = "SALARYFRAGMENT";

    TextInputEditText jobDescriptionAdd;
    TextInputEditText jobSalaryAdd;

    PlacesClient placesClient;
    String apiKey;
    LatLng latLng;
    String companyName;
    Date calender;


    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef;
    FirebaseUser user;

/*    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        Log.d(TAG, "on attach");
    }*/

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        String[] years = {"1980", "1981", "1982", "1983 ", "1984", "1985", "1986", "1987", "1988", "1989","1990", "1991", "1992", "1993 ", "1994", "1995", "1996", "1997", "1998", "1999", "2000", "2001", "2002", "2003", "2004", "2005", "2006", "2007", "2008", "2009", "2010", "2011", "2012", "2013", "2014", "2015", "2016", "2017", "2018", "2019", "2020"};
        String[] jobStructure = getContext().getResources().getStringArray(R.array.jobStructure);
        String[] seniority = {"0-5", "6-10", "11-15", "16-20", "21-25", "26-30", "30+"};

        myRef = database.getInstance().getReference().child("list/salary");
        user = FirebaseAuth.getInstance().getCurrentUser();

        final View view = inflater.inflate(R.layout.fragment_add_salery, container, false);

        apiKey = "";

        if(!Places.isInitialized()){
            Places.initialize(getContext(),apiKey);
        }

        placesClient = Places.createClient(getActivity());

        CompanyList companyList = CompanyList.getMySingleton();
        JobList jobList=JobList.getMySingleton();

    /*    ArrayAdapter<String> companyAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, companyList.read());
        final AutoCompleteTextView companyNameSalary = (AutoCompleteTextView) view.findViewById(R.id.company_salary);
        companyNameSalary.setAdapter(companyAdapter);*/

        calender = Calendar.getInstance().getTime();


        ArrayAdapter<String> companyAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, companyList.read());

        final AutocompleteSupportFragment autocompleteSupportFragmentCompanyName = (AutocompleteSupportFragment)getChildFragmentManager().findFragmentById(R.id.company_name_input_salary);
        autocompleteSupportFragmentCompanyName.setPlaceFields(Arrays.asList(Place.Field.ID,Place.Field.LAT_LNG,Place.Field.NAME));
        autocompleteSupportFragmentCompanyName.setHint(getString(R.string.search_work_place));
        autocompleteSupportFragmentCompanyName.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                latLng = place.getLatLng();
                companyName = place.getName();

                //Toast.makeText(getContext(), "on place "+latLng.latitude+"\n"+latLng.longitude, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(@NonNull Status status) {

            }
        });

        ArrayAdapter<String> jobAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, jobList.read(getContext()));
        final AutoCompleteTextView jobNameSalary = (AutoCompleteTextView) view.findViewById(R.id.job_name_tiet);
        jobNameSalary.setAdapter(jobAdapter);

        jobSalaryAdd=view.findViewById(R.id.salary_til);

        ArrayAdapter<String> YearsAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, years);
        ArrayAdapter<String> jobStructureAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, jobStructure);
        ArrayAdapter<String> seniorityAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, seniority);

        final AutoCompleteTextView jobFromYearAdd = (AutoCompleteTextView) view.findViewById(R.id.year_Start_tv);
        final AutoCompleteTextView jobToYearAdd = (AutoCompleteTextView) view.findViewById(R.id.year_End_tv);
        final AutoCompleteTextView structureOfJobAdd=(AutoCompleteTextView) view.findViewById(R.id.job_explain_tv);
        final AutoCompleteTextView seniorityOfJobAdd=(AutoCompleteTextView) view.findViewById(R.id.seniority_add_tv);

        jobFromYearAdd.setAdapter(YearsAdapter);
        jobToYearAdd.setAdapter(YearsAdapter);
        structureOfJobAdd.setAdapter(jobStructureAdapter);
        seniorityOfJobAdd.setAdapter(seniorityAdapter);

        Button add = view.findViewById(R.id.addButton);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Salary salary=new Salary(jobFromYearAdd.toString(), jobToYearAdd.toString(),jobNameSalary.toString(),companyName, structureOfJobAdd.toString(), jobSalaryAdd.toString(), seniorityOfJobAdd.toString(), user.getDisplayName(), user.getUid(),latLng.latitude,latLng.longitude, calender);

                SalaryList salaryListM = SalaryList.getMySingleton();
                List<Salary> salaryList = salaryListM.read();

                if (jobFromYearAdd.getText().toString().equals("") || jobToYearAdd.getText().toString().equals("") || jobNameSalary.getText().toString().equals("") ||jobSalaryAdd.toString().equals("") ||  companyName == null) {
                    if(jobFromYearAdd.getText().toString().equals("")){
                        jobFromYearAdd.setError(getString(R.string.this_field_can_not_be_blank));
                    }
                    if(jobToYearAdd.getText().toString().equals("")){
                        jobToYearAdd.setError(getString(R.string.this_field_can_not_be_blank));
                    }
                    if(jobNameSalary.getText().toString().equals("")){
                        jobNameSalary.setError(getString(R.string.this_field_can_not_be_blank));
                    }

                    if(jobSalaryAdd.toString().equals("")){
                        jobSalaryAdd.setError(getString(R.string.this_field_can_not_be_blank));
                    }

                    if(companyName == null){
                        //Toast.makeText(getContext(), "hhhh", Toast.LENGTH_SHORT).show();
                        Snackbar.make(view, R.string.please_add_work_place, Snackbar.LENGTH_SHORT)
                                .show();
                    }
                }

                else {
                    salaryList.add(new Salary(jobFromYearAdd.getText().toString(), jobToYearAdd.getText().toString(),jobNameSalary.getText().toString(),companyName, structureOfJobAdd.getText().toString(), jobSalaryAdd.getText().toString(), seniorityOfJobAdd.getText().toString(), user.getDisplayName(), user.getUid(),latLng.latitude,latLng.longitude, calender));
                    getActivity().getSupportFragmentManager().beginTransaction().remove(AddSalaryFragment.this).commit();
                    myRef.setValue(salaryList).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            //Toast.makeText(getActivity(), "fail to save", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
    }


    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
