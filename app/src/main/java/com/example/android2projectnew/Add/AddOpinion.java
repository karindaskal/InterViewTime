package com.example.android2projectnew.Add;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.android2projectnew.Module.CompanyList;
import com.example.android2projectnew.Module.JobList;
import com.example.android2projectnew.Module.Opinion;
import com.example.android2projectnew.Lists.OpinionList;
import com.example.android2projectnew.R;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AddOpinion extends Fragment {

    private String[] years = {"1990", "1991", "1992", "1993 ", "1994", "1995", "1996", "1997", "1998", "1999", "2000", "2001", "2002", "2003", "2004", "2005", "2006", "2007", "2008", "2009", "2010", "2011", "2012", "2013", "2014", "2015", "2016", "2017", "2018", "2019", "2020"};
    EditText  headlineEt, prosEt, consEt,userName;
    Button sendOpinionBtn;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef;
    FirebaseUser user;

    PlacesClient placesClient;
    String apiKey;
    LatLng latLng;
    String companyName;
    Date calender;


    public String placeId;

    List<Place.Field> placeField;
    FetchPlaceRequest request;



    String timeStamp;


    SimpleDateFormat simpleDateFormat;
    String formattedDate;

    String currentDateTimeString;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        myRef = database.getInstance().getReference().child("list/Opinion");
        user = FirebaseAuth.getInstance().getCurrentUser();

        final View view = inflater.inflate(R.layout.fragment_add_opinion,container,false);
        CompanyList companyList = CompanyList.getMySingleton();
        JobList jobList = JobList.getMySingleton();

        apiKey = "";

        if(!Places.isInitialized()){
            Places.initialize(getContext(),apiKey);
        }

        placesClient = Places.createClient(getActivity());

      /*  timeStamp = "00/00/0000";
        calender = Calendar.getInstance().getTime();
        simpleDateFormat = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
        formattedDate = simpleDateFormat.format(calender);*/

        // currentDateTimeString = java.text.DateFormat.getDateTimeInstance().format(calender);
        calender = Calendar.getInstance().getTime();


        final ArrayAdapter<String> companyAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, companyList.read());
        /*final AutoCompleteTextView companyName = (AutoCompleteTextView) view.findViewById(R.id.company_opinion);
        companyName.setAdapter(new PlaceAutoSuggestAdapter(getContext(),android.R.layout.simple_list_item_1));
*/

        final AutocompleteSupportFragment autocompleteSupportFragmentCompanyName = (AutocompleteSupportFragment)getChildFragmentManager().findFragmentById(R.id.company_opinion);
        autocompleteSupportFragmentCompanyName.setPlaceFields(Arrays.asList(Place.Field.ID,Place.Field.LAT_LNG,Place.Field.NAME));
        autocompleteSupportFragmentCompanyName.setHint("Search Work Place");
        autocompleteSupportFragmentCompanyName.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                latLng = place.getLatLng();
                companyName = place.getName();
                //Toast.makeText(getContext(), "on place "+latLng.latitude+"\n"+latLng.longitude, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(@NonNull Status status) {
                //Toast.makeText(getContext(), "error", Toast.LENGTH_SHORT).show();
            }
        });





        ArrayAdapter<String> jobAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, jobList.read(getContext()));
        final AutoCompleteTextView jobName = (AutoCompleteTextView)view.findViewById(R.id.job_name_opinion);
        jobName.setAdapter(jobAdapter);


        ArrayAdapter<String> YearsExposedAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, years);
        final AutoCompleteTextView year1 = (AutoCompleteTextView) view.findViewById(R.id.filled_exposed_dropdown1);
        final AutoCompleteTextView year2 = (AutoCompleteTextView) view.findViewById(R.id.filled_exposed_dropdown2);
        year1.setAdapter(YearsExposedAdapter);
        year2.setAdapter(YearsExposedAdapter);


        headlineEt = view.findViewById(R.id.headline_et_input);
        prosEt = view.findViewById(R.id.pros_input);
        consEt = view.findViewById(R.id.cons_input);



        sendOpinionBtn = view.findViewById(R.id.send_btn);
        sendOpinionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                OpinionList opinionListM = OpinionList.getMySingleton();
                List<Opinion> opinionList = opinionListM.read();


                if (jobName.getText().toString().equals("") || year1.getText().toString().equals("") || year2.getText().toString().equals("") || companyName == null) {
                    if(jobName.getText().toString().equals("")){
                        jobName.setError(getString(R.string.this_field_can_not_be_blank));
                    }
                    if(year1.getText().toString().equals("")){
                        year1.setError(getString(R.string.this_field_can_not_be_blank));
                    }
                    if(year2.getText().toString().equals("")){
                        year2.setError(getString(R.string.this_field_can_not_be_blank));
                    }
                    if(companyName == null){
                       //Toast.makeText(getContext(), "hhhh", Toast.LENGTH_SHORT).show();
                        Snackbar.make(view, R.string.please_add_work_place, Snackbar.LENGTH_SHORT)
                                .show();
                    }
                }

                else {
                    opinionList.add(new Opinion(headlineEt.getText().toString(), companyName, year1.getText().toString(), year2.getText().toString(), jobName.getText().toString(), prosEt.getText().toString(), consEt.getText().toString(), user.getUid(), user.getDisplayName(), latLng.latitude, latLng.longitude, calender));
                    getActivity().getSupportFragmentManager().beginTransaction().remove(AddOpinion.this).commit();
                    myRef.setValue(opinionList).addOnFailureListener(new OnFailureListener() {
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
