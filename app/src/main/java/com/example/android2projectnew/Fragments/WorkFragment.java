package com.example.android2projectnew.Fragments;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android2projectnew.Adapter.OpinionAdapter;
import com.example.android2projectnew.Adapter.QuestionsAdapter;
import com.example.android2projectnew.Adapter.SalaryAdapter;
import com.example.android2projectnew.Add.AddOpinion;
import com.example.android2projectnew.Add.AddQuestionsFragment;
import com.example.android2projectnew.Add.AddSalaryFragment;
import com.example.android2projectnew.MainActivity;
import com.example.android2projectnew.Module.Comparable;
import com.example.android2projectnew.Module.InterviewQuestions;
import com.example.android2projectnew.Module.Opinion;
import com.example.android2projectnew.Module.Salary;
import com.example.android2projectnew.Lists.OpinionList;
import com.example.android2projectnew.Lists.QuestionsList;
import com.example.android2projectnew.Notifications.Data;
import com.example.android2projectnew.R;
import com.example.android2projectnew.Lists.SalaryList;
import com.example.android2projectnew.Module.Tabs;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class WorkFragment extends Fragment implements SearchView.OnQueryTextListener {

    boolean mProcessLike = false;


    final public static String ADD_FRAGMENT_QUESTIONS_TAG = "Questions_Tag_Add";
    final public static String ADD_FRAGMENT_OPINION_TAG = "Opinion_Tag_Add";
    final public static String ADD_FRAGMENT_SALARY_TAG = "Salary_Tag_Add";
    final public static String USER_NAME_FRAGMENT_TAG = "user_name_tag";
    final public static String INTER_Q_FRAGMENT_TAG = "interview_q_tag";

    final int LOCATION_PERMISSION_REQUEST = 1;
    Location lastLocation;

    FusedLocationProviderClient client;
    boolean isAllFabsVisible = false;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    View view;

    public List<InterviewQuestions> questionsList;
    public List<InterviewQuestions> questionsListSearch;
    public List<Opinion> opinionList;
    public List<Opinion> opinionListSearch;
    public List<Salary> salaryList;
    public List<Salary> salaryListSearch;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef;

    SearchView searchView;

    RecyclerView recyclerView;

    QuestionsAdapter adapterQuestions;
    QuestionsAdapter adapterQuestionsSearch;

    OpinionAdapter adapterOpinion;
    OpinionAdapter adapterOpinionSearch;

    SalaryAdapter adapterSalary;
    SalaryAdapter adapterSalarySearch;

    Tabs tabs;

    int pLikes;
    String myUid;

    int dist0;

    String postIdQuestion;

    TabLayout tabLayout;


    String checked;

    public static WorkFragment newInstance(int num) {
        WorkFragment workFragment = new WorkFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("tabs", num);
        workFragment.setArguments(bundle);
        return workFragment;
    }


    @Override
    public boolean onQueryTextSubmit(final String query) {

        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull final Menu menu, @NonNull MenuInflater inflater) {


        inflater.inflate(R.menu.top_app_bar, menu);

        setHasOptionsMenu(true);


        MenuItem.OnActionExpandListener onActionExpandListener = new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                return true;
            }
        };

        final Tabs tabs = Tabs.values()[getArguments().getInt("tabs")];

        menu.findItem(R.id.search1).setOnActionExpandListener(onActionExpandListener);
        searchView = (SearchView) menu.findItem(R.id.search1).getActionView();
        searchView.setQueryHint("Search Data Here..");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(final String query) {

                myRef.child(tabs.getNameForDataBase()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        opinionListSearch.clear();

                        if (snapshot.exists()) {
                            for (DataSnapshot snapshot1 : snapshot.getChildren()) {


                                switch (tabs.getNameForDataBase()) {
                                    case "Opinion":
                                        Opinion mission = snapshot1.getValue(Opinion.class);

                                        if (mission.getCompanyName().toLowerCase().contains(query.toLowerCase()) || mission.getHeadline().toLowerCase().contains(query) ||
                                                mission.getOnDuty().toLowerCase().contains(query) || mission.getUserName().toLowerCase().contains(query)) {
                                            opinionListSearch.add(mission);

                                        }
                                        adapterOpinionSearch = new OpinionAdapter(opinionListSearch,getContext());
                                        recyclerView.setAdapter(adapterOpinionSearch);
                                        break;

                                    case "Questions":

                                        InterviewQuestions mission1= new InterviewQuestions();
                                        mission1.setAnswer(snapshot1.child("answer").getValue(String.class));
                                        mission1.setCompany(snapshot1.child("company").getValue(String.class));
                                        mission1.setDate(snapshot1.child("date").getValue(String.class));
                                        mission1.setId(snapshot1.child("id").getValue(String.class));
                                        mission1.setIdQ(snapshot1.child("idQ").getValue(Integer.class));
                                        mission1.setJob(snapshot1.child("job").getValue(String.class));
                                        mission1.setLikes(snapshot1.child("likes").getValue(Integer.class));
                                        mission1.setpComment(snapshot1.child("pComment").getValue(String.class));
                                        mission1.setProses(snapshot1.child("proses").getValue(String.class));
                                        mission1.setQuestion(snapshot1.child("question").getValue(String.class));
                                        mission1.setUserName(snapshot1.child("userName").getValue(String.class));
                                        mission1.setTimeStamp(snapshot1.child("timeStamp").getValue(Date.class));
                                        mission1.setLongitude(snapshot1.child("longitude").getValue(Double.class));
                                        mission1.setLatitude(snapshot1.child("latitude").getValue(Double.class));


                                        if (mission1.getAnswer().toLowerCase().contains(query) || mission1.getCompany().toLowerCase().contains(query) ||
                                                mission1.getJob().toLowerCase().contains(query) || mission1.getProses().toLowerCase().contains(query) ||
                                                mission1.getQuestion().toLowerCase().contains(query) || mission1.getUserName().toLowerCase().contains(query)) {
                                            questionsListSearch.add(mission1);
                                        }
                                        adapterQuestionsSearch = new QuestionsAdapter(questionsListSearch, getContext());
                                        recyclerView.setAdapter(adapterQuestionsSearch);
                                        break;

                                    case "salary":
                                        Salary mission2 = snapshot1.getValue(Salary.class);

                                        if (mission2.getCompanySalary().toLowerCase().contains(query) || mission2.getGrossSalary().toLowerCase().contains(query) ||
                                                mission2.getJob().toLowerCase().contains(query) || mission2.getUserName().toLowerCase().contains(query) ||
                                                mission2.getJobStructure().toLowerCase().contains(query)) {
                                            salaryListSearch.add(mission2);
                                        }
                                        adapterSalarySearch = new SalaryAdapter(salaryListSearch, getContext());
                                        recyclerView.setAdapter(adapterSalarySearch);
                                        break;

                                }


                            }
                            // adapterOpinion.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        //Toast.makeText(getActivity(), "fail2", Toast.LENGTH_SHORT).show();
                    }


                });
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                switch (tabs.getNameForDataBase()) {
                    case "Opinion":
                        recyclerView.setAdapter(adapterOpinion);
                        break;

                    case "Questions":
                        recyclerView.setAdapter(adapterQuestions);
                        break;

                    case "salary":
                        recyclerView.setAdapter(adapterSalary);
                        break;
                }

                return false;
            }
        });




        super.onCreateOptionsMenu(menu, inflater);

    }


    @Override
    public void onPrepareOptionsMenu(@NonNull Menu menu) {
        super.onPrepareOptionsMenu(menu);

    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.search1) {
            //Toast.makeText(getContext(), "search", Toast.LENGTH_SHORT).show();
            return true;
        }
        if (id == R.id.filter) {

            if (Build.VERSION.SDK_INT >= 23) {
                int hasLocationPermission = ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION);
                if (hasLocationPermission != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST);
                }
                else {
                    startLocation();
                    dialog();
                }
            }

            else {
                startLocation();
                dialog();
            }

           // Toast.makeText(getContext(), "filter1", Toast.LENGTH_SHORT).show();

            return true;
        }

     /*   if (id == R.id.filter2) {
            Toast.makeText(getContext(), "filter2", Toast.LENGTH_SHORT).show();




            return true;
        }*/


        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    public void dialog(){

        final String[] listItems = {getString(R.string.sort_by_distance), getString(R.string.sort_by_time_posted)};
        checked=listItems[0];

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(getString(R.string.choose_item));

        //, R.style.MaterialThemeDialog

        final int checkedItem = 0;
        dist0 = 0;
        //this will checked the item when user open the dialog
        builder.setSingleChoiceItems(listItems, checkedItem, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                checked = listItems[which];
                dist0 = which;
            }
        });

        final Tabs tabs = Tabs.values()[getArguments().getInt("tabs")];

        builder.setPositiveButton(getString(R.string.done), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


                switch (tabs){
                    case OPINION:
                        switch (dist0) {
                            case 0:
                                Collections.sort(opinionList, new Comparable(lastLocation.getLongitude(), lastLocation.getLatitude(), getContext()));
                                recyclerView.setAdapter(adapterOpinion);
                                break;
                            case 1:
                                Collections.sort(opinionList, new Comparator<Opinion>() {
                                    @Override
                                    public int compare(Opinion o1, Opinion o2) {
                                        Date date1, date2;
                                        date1 = o1.getTimeStamp();
                                        date2 = o2.getTimeStamp();

                                        return date1.compareTo(date2);
                                    }
                                });
                                recyclerView.setAdapter(adapterOpinion);
                                break;
                        }
                        break;
                    case SALARY:
                        switch (dist0) {

                            case 0:
                                Collections.sort(salaryList, new Comparable(lastLocation.getLongitude(), lastLocation.getLatitude(), getContext()));
                                recyclerView.setAdapter(adapterSalary);
                                break;

                            case 1:
                                Collections.sort(salaryList, new Comparator<Salary>() {
                                    @Override
                                    public int compare(Salary o1, Salary o2) {
                                        Date date1, date2;
                                        date1 = o1.getTimeStamp();
                                        date2 = o2.getTimeStamp();

                                        return date1.compareTo(date2);
                                    }
                                });
                                recyclerView.setAdapter(adapterSalary);
                                break;
                        }
                        break;
                    case INTERVIEWQUESTIONS:
                        switch (dist0) {

                            case 0:
                                Collections.sort(questionsList, new Comparable(lastLocation.getLongitude(), lastLocation.getLatitude(), getContext()));
                                recyclerView.setAdapter(adapterQuestions);
                                break;

                            case 1:
                                Collections.sort(questionsList, new Comparator<InterviewQuestions>() {
                                    @Override
                                    public int compare(InterviewQuestions o1, InterviewQuestions o2) {
                                        Date date1, date2;
                                        date1 = o1.getTimeStamp();
                                        date2 = o2.getTimeStamp();

                                        return date1.compareTo(date2);
                                    }
                                });
                                recyclerView.setAdapter(adapterQuestions);
                                break;
                        }
                        break;


                }

            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        view = inflater.inflate(R.layout.tab_fragment, container, false);

        recyclerView = view.findViewById(R.id.recycler);
        final ExtendedFloatingActionButton fabButton = view.findViewById(R.id.extended_fab);
        fabButton.shrink();



        QuestionsList qListM = QuestionsList.getMySingleton();
        OpinionList opinionListM = OpinionList.getMySingleton();
        SalaryList salaryListM = SalaryList.getMySingleton();
        questionsList = qListM.read();
        opinionList = opinionListM.read();
        salaryList = salaryListM.read();


        myRef = database.getInstance().getReference().child("list");

        adapterQuestions = new QuestionsAdapter(questionsList, getContext());
        adapterOpinion = new OpinionAdapter(opinionList,getContext());
        adapterSalary = new SalaryAdapter(salaryList, getContext());
        //myRef =qListM.getData();

        opinionListSearch = new ArrayList<>();
        adapterOpinionSearch = new OpinionAdapter(opinionListSearch,getContext());

        questionsListSearch = new ArrayList<>();
        adapterQuestionsSearch = new QuestionsAdapter(questionsListSearch, getContext());

        salaryListSearch = new ArrayList<>();
        adapterSalarySearch = new SalaryAdapter(salaryListSearch, getContext());

        myUid = user.getUid();


        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));



        tabs = Tabs.values()[getArguments().getInt("tabs")];


        switch (tabs) {
            case OPINION:

                fabButton.setText("opinion");

                recyclerView.setAdapter(adapterOpinion);

                myRef.child("Opinion").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        opinionList.clear();

                        if (snapshot.exists()) {
                            for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                                Opinion mission = snapshot1.getValue(Opinion.class);
                                opinionList.add(mission);
                            }
                            adapterOpinion.notifyDataSetChanged();
                            recyclerView.setAdapter(adapterOpinion);
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        //Toast.makeText(getActivity(), "fail2", Toast.LENGTH_SHORT).show();
                    }


                });

                adapterOpinion.setListener(new OpinionAdapter.OnOpinionListener() {

                    @Override
                    public void onOpinionClick(int position, View view) {

                    }

                    @Override
                    public void onImageOrTextUserNameClicked(int position, View view) {
                        String userName = opinionList.get(position).getId();
                        ProfileFragment profileFragment = ProfileFragment.newInstance(userName);
                        FragmentManager fragmentManager = getFragmentManager();
                        FragmentTransaction transaction = fragmentManager.beginTransaction();
                        transaction.add(R.id.drawer_layout, profileFragment, USER_NAME_FRAGMENT_TAG);
                        transaction.addToBackStack(null);
                        transaction.commit();

                    }
                });


                fabButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(user != null && user.isAnonymous()) {
                            Snackbar.make((getActivity().findViewById(android.R.id.content)), R.string.reg_for_add, Snackbar.LENGTH_LONG)
                                    .setAction(R.string.create_acc, new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            FragmentManager fragmentManager = getFragmentManager();
                                            FragmentTransaction transaction = fragmentManager.beginTransaction();
                                            transaction.add(R.id.drawer_layout, new LoginScreenFragment(), MainActivity.LOGIN_SCREEN_TAG);
                                            transaction.commit();
                                        }
                                    }).show();
                        }
                        else {

                            FragmentManager fragmentManager = getFragmentManager();
                            FragmentTransaction transaction = fragmentManager.beginTransaction();
                            transaction.add(R.id.drawer_layout, new AddOpinion(), ADD_FRAGMENT_OPINION_TAG);
                            transaction.addToBackStack(null);
                            transaction.commit();
                            recyclerView.setAdapter(adapterOpinion);
                        }

                    }

                });


                break;

            case INTERVIEWQUESTIONS:
                fabButton.setText("INTERVIEW QUESTIONS");
                recyclerView.setAdapter(adapterQuestions);

                myRef.child("Questions").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        questionsList.clear();

                        if (snapshot.exists()) {
                            for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                              //  InterviewQuestions mission = snapshot1.getValue(InterviewQuestions.class);
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



                                questionsList.add(mission);
                            }
                            adapterOpinion.notifyDataSetChanged();
                            recyclerView.setAdapter(adapterQuestions);
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        //Toast.makeText(getActivity(), "fail2", Toast.LENGTH_SHORT).show();
                    }
                });

                adapterQuestions.setListener(new QuestionsAdapter.OnQuesionsListener() {



                    @Override
                    public void onAddAnswerClicked(int position, View view) {
                        InterviewQuestions question = questionsList.get(position);
                        PostDetailFragment postDetailFragment = PostDetailFragment.newInstance(question);
                        FragmentManager fragmentManager = getFragmentManager();
                        FragmentTransaction transaction = fragmentManager.beginTransaction();
                        transaction.add(R.id.drawer_layout, postDetailFragment, INTER_Q_FRAGMENT_TAG);
                        transaction.addToBackStack(null);
                        transaction.commit();
                    }

                    @Override
                    public void onQuestionsClick(int position, View view) {

                    }

                    @Override
                    public void onImageOrTextUserNameClicked(int position, View view) {
                        String userName = questionsList.get(position).getId();
                        ProfileFragment profileFragment = ProfileFragment.newInstance(userName);
                        FragmentManager fragmentManager = getFragmentManager();
                        FragmentTransaction transaction = fragmentManager.beginTransaction();
                        transaction.add(R.id.drawer_layout, profileFragment, USER_NAME_FRAGMENT_TAG);
                        transaction.addToBackStack(null);
                        transaction.commit();
                    }

                });

                fabButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(user != null && user.isAnonymous()) {
                            Snackbar.make((getActivity().findViewById(android.R.id.content)), R.string.reg_for_add, Snackbar.LENGTH_LONG)
                                    .setAction(R.string.create_acc, new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            FragmentManager fragmentManager = getFragmentManager();
                                            FragmentTransaction transaction = fragmentManager.beginTransaction();
                                            transaction.add(R.id.drawer_layout, new LoginScreenFragment(), MainActivity.LOGIN_SCREEN_TAG);
                                            transaction.commit();
                                        }
                                    }).show();
                        }
                        else {

                            FragmentManager fragmentManager = getFragmentManager();
                            FragmentTransaction transaction = fragmentManager.beginTransaction();
                            transaction.add(R.id.drawer_layout, new AddQuestionsFragment(), ADD_FRAGMENT_QUESTIONS_TAG);
                            transaction.addToBackStack(null);
                            transaction.commit();
                            recyclerView.setAdapter(adapterQuestions);
                        }

                    }

                });
                break;

            case SALARY:
                fabButton.setText("SALARY");
                recyclerView.setAdapter(adapterSalary);

                //search("salary");

                myRef.child("salary").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        salaryList.clear();

                        if (snapshot.exists()) {
                            for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                                Salary mission = snapshot1.getValue(Salary.class);
                                salaryList.add(mission);
                            }
                            adapterSalary.notifyDataSetChanged();
                            recyclerView.setAdapter(adapterSalary);
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        //Toast.makeText(getActivity(), "fail2", Toast.LENGTH_SHORT).show();
                    }
                });

                adapterSalary.setSalaryListener(new SalaryAdapter.MySalaryListener() {
                    @Override
                    public void onSalaryClick(int position, View view) {

                    }

                    @Override
                    public void onImageOrTextUserNameClicked(int position, View view) {

                        String userName = salaryList.get(position).getId();
                        ProfileFragment profileFragment = ProfileFragment.newInstance(userName);
                        FragmentManager fragmentManager = getFragmentManager();
                        FragmentTransaction transaction = fragmentManager.beginTransaction();
                        transaction.add(R.id.drawer_layout, profileFragment, USER_NAME_FRAGMENT_TAG);
                        transaction.addToBackStack(null);
                        transaction.commit();
                    }
                });

                fabButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if(user != null && user.isAnonymous()) {
                            Snackbar.make((getActivity().findViewById(android.R.id.content)), R.string.reg_for_add, Snackbar.LENGTH_LONG)
                                    .setAction(R.string.create_acc, new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            FragmentManager fragmentManager = getFragmentManager();
                                            FragmentTransaction transaction = fragmentManager.beginTransaction();
                                            transaction.add(R.id.drawer_layout, new LoginScreenFragment(), MainActivity.LOGIN_SCREEN_TAG);
                                            transaction.commit();
                                        }
                                    }).show();
                        }
                        else{

                            FragmentManager fragmentManager = getFragmentManager();
                            FragmentTransaction transaction = fragmentManager.beginTransaction();
                            transaction.add(R.id.drawer_layout, new AddSalaryFragment(), ADD_FRAGMENT_SALARY_TAG);
                            transaction.addToBackStack(null);
                            transaction.commit();
                            recyclerView.setAdapter(adapterSalary);
                        }

                    }

                });

                break;
        }


        return view;


    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case LOCATION_PERMISSION_REQUEST:
                //if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                        //Toast.makeText(getContext(), "Sorry can't work", Toast.LENGTH_SHORT).show();
                        Snackbar.make((getActivity().findViewById(android.R.id.content)), R.string.permission_denied, Snackbar.LENGTH_SHORT)
                                .show();
                    }
                    else {
                        startLocation();
                        dialog();
                    }
                //}
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);


        }

    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void startLocation() {

        //LocationRequest request = LocationRequest.create();
        LocationRequest request = new LocationRequest();
        request.setInterval(1000);
        request.setFastestInterval(500);
        request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);


        client = LocationServices.getFusedLocationProviderClient(getContext());
        LocationCallback callback = new LocationCallback(){
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                lastLocation = locationResult.getLastLocation();

            }
        };



        if(Build.VERSION.SDK_INT>=23 && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            client.requestLocationUpdates(request,callback,null);

        }
        else if(Build.VERSION.SDK_INT <=22){
            client.requestLocationUpdates(request,callback,null);

        }
     }


}
