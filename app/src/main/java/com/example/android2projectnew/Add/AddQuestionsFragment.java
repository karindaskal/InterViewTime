package com.example.android2projectnew.Add;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.android2projectnew.Module.CompanyList;
import com.example.android2projectnew.Module.JobList;
import com.example.android2projectnew.Lists.QuestionsList;
import com.example.android2projectnew.Module.InterviewQuestions;
import com.example.android2projectnew.Module.Opinion;
import com.example.android2projectnew.R;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class AddQuestionsFragment extends Fragment {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef;
    FirebaseUser user;

    LatLng latLng;

    String companyName;
    Date calender;

    PlacesClient placesClient;
    String apiKey;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        myRef = database.getInstance().getReference().child("list/Questions");
        user = FirebaseAuth.getInstance().getCurrentUser();

         final View rootView= inflater.inflate(R.layout.interview_questions_add,container,false);
         CompanyList companyList = CompanyList.getMySingleton();

        calender = Calendar.getInstance().getTime();

        apiKey = "";

        if(!Places.isInitialized()){
            Places.initialize(getContext(),apiKey);
        }

        placesClient = Places.createClient(getActivity());
/*

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, companyList.read());
        final AutoCompleteTextView comp = (AutoCompleteTextView) rootView.findViewById(R.id.company);
        comp.setAdapter(adapter);

        ArrayAdapter<String> companyAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, companyList.read());
        /*final AutoCompleteTextView companyName = (AutoCompleteTextView) view.findViewById(R.id.company_opinion);
        companyName.setAdapter(new PlaceAutoSuggestAdapter(getContext(),android.R.layout.simple_list_item_1));
*/

        final AutocompleteSupportFragment autocompleteSupportFragmentCompanyName = (AutocompleteSupportFragment)getChildFragmentManager().findFragmentById(R.id.company);
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



       JobList jobList= JobList.getMySingleton();
        ArrayAdapter<String> adapterJob = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, jobList.read(getContext()));


        final AutoCompleteTextView job =rootView.findViewById(R.id.role);
        job.setAdapter(adapterJob);
        final TextView process =rootView.findViewById(R.id.process_add);
        final TextView qu =rootView.findViewById(R.id.question_add);
        final TextView an =rootView.findViewById(R.id.Answers_Add);
        Button add =rootView.findViewById(R.id.add_Button);
        final Button dateB = rootView.findViewById(R.id.date_button);

        dateB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int year  = calendar.get(Calendar.YEAR);
                int month  = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dpd = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        dateB.setText(dayOfMonth + " / " + (month+1 ) + " / " + year);

                    }


                },year,month,day);

                dpd.show();

            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                QuestionsList qListM = QuestionsList.getMySingleton();
                List<InterviewQuestions> qList = qListM.read();

                if (job.getText().toString().equals("") || qu.getText().toString().equals("") || companyName == null) {
                    if(job.getText().toString().equals("")){
                        job.setError(getString(R.string.this_field_can_not_be_blank));
                    }
                    if(qu.getText().toString().equals("")){
                        qu.setError(getString(R.string.this_field_can_not_be_blank));
                    }

                    if(companyName == null){
                        //Toast.makeText(getContext(), "hhhh", Toast.LENGTH_SHORT).show();
                        Snackbar.make(rootView, R.string.please_add_work_place, Snackbar.LENGTH_SHORT)
                                .show();
                    }
                }

                else {
                    qList.add(new InterviewQuestions(companyName,dateB.getText().toString(),job.getText().toString(),process.getText().toString(),qu.getText().toString(),an.getText().toString(),user.getDisplayName(),user.getUid(),latLng.latitude,latLng.longitude, calender,0,qList.size()));
                    getActivity().getSupportFragmentManager().beginTransaction().remove(AddQuestionsFragment.this).commit();
                    myRef.setValue(qList).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            //Toast.makeText(getActivity(), "fail to save", Toast.LENGTH_SHORT).show();
                        }
                    });

                }

            }
        });

        return rootView;
    }
}

