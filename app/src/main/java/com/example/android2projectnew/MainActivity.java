package com.example.android2projectnew;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.widget.SearchView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
//import android.widget.SearchView;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.util.ContentLengthInputStream;
import com.example.android2projectnew.Adapter.OpinionAdapter;
import com.example.android2projectnew.Adapter.WorkPagerAdapter;
import com.example.android2projectnew.Add.AddQuestionsFragment;
import com.example.android2projectnew.Fragments.APIService;
import com.example.android2projectnew.Fragments.ArticleFragment;
import com.example.android2projectnew.Fragments.ChatFragment;
import com.example.android2projectnew.Fragments.CreateAccountFragment;
import com.example.android2projectnew.Fragments.LoginScreenFragment;
import com.example.android2projectnew.Fragments.NotificationFragment;
import com.example.android2projectnew.Fragments.ProfileFragment;
import com.example.android2projectnew.Fragments.UserFragment;
import com.example.android2projectnew.Fragments.WorkFragment;
import com.example.android2projectnew.Module.UserProfileData;
//import com.example.android2projectnew.Notifications.Client;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.R.id.home;

public class MainActivity extends AppCompatActivity implements LoginScreenFragment.OnLoginListner , CreateAccountFragment.OncreateAccountClickListener{

    public static final String LOGIN_SCREEN_TAG = "login" ;
    private static final String TAG_FRAGMENT = "tag";
    private FirebaseAuth mAuth;
    FragmentManager fragmentManager;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference userRef;
    DatabaseReference myRef;
    FirebaseUser user;

    CreateAccountFragment createAccount;
    LoginScreenFragment loginScreen;

    boolean isLogIn = false;

    String CREATE_ACC_TAG = "create_acc_tag";
    String PROFILE_ACC_TAG = "profile_tag";
    String USER_CHAT_LIST_TAG = "user_chat_list_tag";
    String USER_NOTIF = "user_notif";
    String ARTICLE_ITEM = "article";

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    CoordinatorLayout coordinatorLayout;

    TabLayout tabLayout;
    ViewPager viewPager;

    Menu nav_Menu;

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private OpinionAdapter opinionAdapter;

    String imageDefaultUrl="https://i.pinimg.com/564x/0c/3b/3a/0c3b3adb1a7530892e55ef36d3be6cb8.jpg";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mAuth = FirebaseAuth.getInstance();
        createAccount = new CreateAccountFragment();;
        loginScreen = new LoginScreenFragment();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        myRef = database.getInstance().getReference().child("list");


        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigetion_view);
        coordinatorLayout = findViewById(R.id.coordinator);
        user = mAuth.getCurrentUser();

        nav_Menu = navigationView.getMenu();

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
       // actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.black)));

        actionBar.setHomeAsUpIndicator(R.drawable.baseline_menu_black_18dp);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                if(item.getItemId()==R.id.profile_drawer)
                {
                    if(user != null && user.isAnonymous()){
                        Snackbar.make(coordinatorLayout, R.string.reg_for_profile, Snackbar.LENGTH_LONG)
                                .setAction(R.string.create_acc, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        FragmentManager fragmentManager = getSupportFragmentManager();
                                        FragmentTransaction transaction = fragmentManager.beginTransaction();
                                        transaction.add(R.id.drawer_layout, new LoginScreenFragment(), MainActivity.LOGIN_SCREEN_TAG);
                                        transaction.commit();
                                    }
                                }).show();
                    }
                    else {
                        String userName = user.getUid();
                        ProfileFragment profileFragment = ProfileFragment.newInstance(userName);
                        FragmentManager fragmentManager = getSupportFragmentManager();
                        FragmentTransaction transaction = fragmentManager.beginTransaction();
                        transaction.add(R.id.drawer_layout,profileFragment, WorkFragment.USER_NAME_FRAGMENT_TAG);
                        transaction.addToBackStack(null);
                        transaction.commit();
                    }

                }

                if(item.getItemId()==R.id.chats_drawer){
                    if(user != null && user.isAnonymous()) {
                        // nav_Menu.findItem(R.id.profile_drawer).setVisible(false);
                        Snackbar.make(coordinatorLayout, R.string.reg_for_chat, Snackbar.LENGTH_LONG)
                                .setAction(R.string.create_acc, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        FragmentManager fragmentManager = getSupportFragmentManager();
                                        FragmentTransaction transaction = fragmentManager.beginTransaction();
                                        transaction.add(R.id.drawer_layout, new LoginScreenFragment(), MainActivity.LOGIN_SCREEN_TAG);
                                        transaction.commit();
                                    }
                                }).show();
                    }
                    else{
                        FragmentManager fragmentManager = getSupportFragmentManager();
                        FragmentTransaction transaction = fragmentManager.beginTransaction();
                        transaction.add(R.id.drawer_layout,new ChatFragment(),USER_CHAT_LIST_TAG);
                        transaction.addToBackStack(null);
                        transaction.commit();
                    }
                }

                if(item.getItemId() == R.id.log_out_menu_drawer){
                    mAuth.signOut();
                    updateUI(null,false);
                }

                if(item.getItemId() == R.id.item_notification){
                    if(user != null && user.isAnonymous()) {
                        Snackbar.make(coordinatorLayout, R.string.reg_for_chat, Snackbar.LENGTH_LONG)
                                .setAction(R.string.create_acc, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        FragmentManager fragmentManager = getSupportFragmentManager();
                                        FragmentTransaction transaction = fragmentManager.beginTransaction();
                                        transaction.add(R.id.drawer_layout, new LoginScreenFragment(), MainActivity.LOGIN_SCREEN_TAG);
                                        transaction.commit();
                                    }
                                }).show();
                    }
                    else {
                        NotificationFragment notificationFragment = new NotificationFragment();
                        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.add(R.id.drawer_layout,notificationFragment,USER_NOTIF);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                    }


                }

                if(item.getItemId() == R.id.intersting_posts){
                    ArticleFragment articleFragment = new ArticleFragment();
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.add(R.id.drawer_layout,articleFragment,ARTICLE_ITEM);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }

                item.setChecked(true);
                drawerLayout.closeDrawers();
                return true;
            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
        user = mAuth.getCurrentUser();
        updateUI(user,false);

    }

    //isLogin is if we need to remove the login fragment

    private void updateUI(FirebaseUser currentUser, boolean isLogIn) {
        if(currentUser != null){
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);

            WorkPagerAdapter adapter = new WorkPagerAdapter(getSupportFragmentManager(),MainActivity.this);

            if(isLogIn){
                FragmentManager manager = getSupportFragmentManager();
                FragmentTransaction trans = manager.beginTransaction();
                Fragment fragment = getSupportFragmentManager().findFragmentByTag(LOGIN_SCREEN_TAG);
                trans.remove(fragment);
                trans.commit();
            }

            ViewPager viewPager = findViewById(R.id.pager);
            viewPager.setAdapter(adapter);
            TabLayout tabLayout = findViewById(R.id.tab_layout);
            tabLayout.setupWithViewPager(viewPager);
        }

        else{
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            WorkPagerAdapter adapter = new WorkPagerAdapter(getSupportFragmentManager(), MainActivity.this);
            if(isLogIn){
                FragmentManager manager = getSupportFragmentManager();
                FragmentTransaction trans = manager.beginTransaction();
                Fragment fragment = getSupportFragmentManager().findFragmentByTag(LOGIN_SCREEN_TAG);
                trans.remove(fragment);
                trans.commit();
            }
            fragmentManager = getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.add(R.id.drawer_layout,new LoginScreenFragment(), LOGIN_SCREEN_TAG);
            transaction.commit();
        }
    }


    //log in and create account
    @Override
    public void onClickLoginButton(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            user = mAuth.getCurrentUser();
                            updateUI(user,true);
                        } else {
                            // If sign in fails, display a message to the user.
                            Snackbar.make(drawerLayout, R.string.auth_faile_lease, Snackbar.LENGTH_SHORT)
                                    .show();
                            updateUI(null,true);
                        }
                    }
                });
    }

    @Override
    public void onClickCreateAccountButton() {
        getSupportFragmentManager().beginTransaction().replace(R.id.drawer_layout, new CreateAccountFragment(), CREATE_ACC_TAG).addToBackStack(null).commit();
    }

    @Override
    public void onClickForgatPassword(String email) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle(getString(R.string.forgot_your_password));
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        builder.setView(input);

        builder.setPositiveButton(R.string.send, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(input.equals("")) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                            builder.setMessage(R.string.invalid_email).setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }

                            }).show();
                        }
                        else {
                            mAuth.getInstance().sendPasswordResetEmail(input.getText().toString())
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Snackbar.make(drawerLayout, R.string.pass_send_succesful, Snackbar.LENGTH_SHORT)
                                                        .show();
                                            }
                                            else {
                                                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

                                                builder.setMessage(R.string.invalid_email).setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        dialog.dismiss();
                                                    }

                                                }).show();
                                            }

                                        }
                                    });
                            }

                    }


                });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.show();

    }
    @Override
    public void onClickContinueAsGuest() {
        mAuth.signInAnonymously()
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            user = mAuth.getCurrentUser();
                            updateUI(user,true);
                        } else {
                            // If sign in fails, display a message to the user.
                            Snackbar.make(drawerLayout, R.string.auth_failed, Snackbar.LENGTH_SHORT)
                                    .show();
                            updateUI(null,true);
                        }

                    }
                });

    }

    @Override
    public void onCreateAccount( final  String userName,  String userEmail, String userPassword, String userRepeatPassword) {
        mAuth.createUserWithEmailAndPassword(userEmail, userPassword)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            user = mAuth.getCurrentUser();
                            user.updateProfile(new UserProfileChangeRequest.Builder().setDisplayName(userName).build()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()) {
                                        Snackbar.make(coordinatorLayout, user.getDisplayName() + " Welcome!!!", Snackbar.LENGTH_SHORT).show();
                                    }
                                }
                            });
                            userRef = database.getReference().child("user/"+user.getUid());
                            UserProfileData user1 = new UserProfileData(userName,user.getUid(),imageDefaultUrl);
                            userRef.setValue(user1).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                }
                            });

                            FragmentManager manager = getSupportFragmentManager();
                            FragmentTransaction trans = manager.beginTransaction();
                            Fragment fragment = getSupportFragmentManager().findFragmentByTag(CREATE_ACC_TAG);
                            trans.remove(fragment);
                            trans.commit();
                            updateUI(null,true);

                        } else {
                            // If sign in fails, display a message to the user.
                            Snackbar.make(drawerLayout, R.string.auth_failed, Snackbar.LENGTH_SHORT)
                                    .show();
                        }
                    }
                });
    }


    @Override
    protected void onPostResume() {
        super.onPostResume();
    }



    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == home){
            drawerLayout.openDrawer(GravityCompat.START);
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void AlreadyHaveAcc() {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction trans = manager.beginTransaction();
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(CREATE_ACC_TAG);
        trans.remove(fragment);
        trans.commit();
        updateUI(null,false);
    }

    public void lockDrawer() {
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
    }


    public void unlockDrawer() {
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
    }


    private void status(String status){
        if(user!=null){
            if(!user.isAnonymous()){
                myRef = database.getReference("user").child(user.getUid());
                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("status",status);
                myRef.updateChildren(hashMap);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        user = mAuth.getCurrentUser();
        updateUI(user,false);
        status("online");
    }

    @Override
    protected void onPause() {
        super.onPause();
        status("offline");

    }

}


