package com.example.android2projectnew.Fragments;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.example.android2projectnew.MainActivity;
import com.example.android2projectnew.R;
import com.example.android2projectnew.Module.UserProfileData;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.HashMap;
import java.util.Objects;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;

public class ProfileFragment extends Fragment {
    DatabaseReference myRef;
    FirebaseUser user;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    UserProfileData mission;
    StorageReference storageReference;

    int check;

    String checked;
    final int CAMERA_REQUEST = 2;
    final int IMAGE_REQUEST = 1;
    final int WRITE_PERMISSION_REQUEST = 1;

    private final static int IMAGE_PICK_CODE = 1000;
    private final static int PERMISSION_CODE = 1001;
    File file;
    Uri imageUri;
    String imagePath;

    private StorageTask uploadTask;

    String userName;
    String email;
    Uri photoUrl;


    TextView profileName, profileEmail, jobTitle, jobCompany;
    Button editProfileBtn, chatBtn;
    String userId;
    CircleImageView profileImage;
    ImageButton backBtn;

    final static String EDIT_PROFILE_FRAGMENT_TAG = "edit_profile_fragment";
    final static String MESSAGE_FRAGMENT_TAG = "message_fragment";
    final static String CREATE_FRAGMENT_TAG = "create_fragment";

    public static ProfileFragment newInstance(String username) {
        ProfileFragment profileFragment = new ProfileFragment();
        Bundle bundle = new Bundle();
        bundle.putString("user_name1", username);
        profileFragment.setArguments(bundle);
        return profileFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        profileName = view.findViewById(R.id.profile_name);
        profileEmail = view.findViewById(R.id.profile_email);
        jobTitle = view.findViewById(R.id.profile_jobTitleDescription);
        jobCompany = view.findViewById(R.id.profile_companyDescription);
        editProfileBtn = view.findViewById(R.id.profile_edit_btn);
        chatBtn = view.findViewById(R.id.profile_chat_btn);
        final UserProfileData user1 = new UserProfileData();
        userId = getArguments().getString("user_name1");
        profileImage = view.findViewById(R.id.profile_image);
        backBtn = view.findViewById(R.id.back_profile);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack();

            }
        });

        storageReference = FirebaseStorage.getInstance().getReference("uploads");
        myRef = database.getInstance().getReference().child("user");
        user = FirebaseAuth.getInstance().getCurrentUser();


        if (user != null) {
            // Name, email address, and profile photo Url
            userName = user.getDisplayName();
            email = user.getEmail();
            imageUri = user.getPhotoUrl();

            // Check if user's email is verified
            boolean emailVerified = user.isEmailVerified();

            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getIdToken() instead.
            String uid = user.getUid();

            if (!uid.equals(userId)) {
                editProfileBtn.setVisibility(View.INVISIBLE);
                profileImage.setClickable(false);
            }
            else {
                profileName.setText(userName);
                profileEmail.setText(email);
                chatBtn.setVisibility(View.INVISIBLE);


                profileImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (Build.VERSION.SDK_INT >= 23) {
                            int hasWritePermission = ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
                            if (hasWritePermission != PackageManager.PERMISSION_GRANTED) {
                                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_PERMISSION_REQUEST);
                            }
                            else {
                                dialogProfile();
                            }
                        }
                        else {
                            dialogProfile();
                        }


                    }

                });
            }
        }
        else {
            userName = "NAME";
            email = "EMAIL";

        }

        editProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.add(R.id.drawer_layout, new EditProfileFragment(), EDIT_PROFILE_FRAGMENT_TAG);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        chatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //String userNameForChat = user.getUid();
                if(!user.isAnonymous())
                {
                    MessageFragment profileFragment = MessageFragment.newInstance(userId);
                    FragmentManager fragmentManager = getFragmentManager();
                    FragmentTransaction transaction = fragmentManager.beginTransaction();
                    transaction.add(R.id.drawer_layout, profileFragment, MESSAGE_FRAGMENT_TAG);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
                else {
                    Snackbar.make(getView(), R.string.you_need_to_reg, Snackbar.LENGTH_LONG)
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
            }
        });

        myRef.child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                mission = snapshot.getValue(UserProfileData.class);


                if (snapshot.exists()) {
                    if(getActivity()!= null){
                        Glide.with(getActivity())
                                .load(mission.getImageURL())
                                .centerCrop()
                                .into(profileImage);
                    }
                    profileName.setText(mission.getUserName());
                    jobTitle.setText(mission.getJob());
                    jobCompany.setText(mission.getCompany());


                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //Toast.makeText(getActivity(), "failed", Toast.LENGTH_SHORT).show();
            }
        });


        return view;
    }

    public void dialogProfile(){

        final String[] importList = {getString(R.string.import_from_gallary), getString(R.string.import_from_camera)};
        checked = importList[0];
        check = 0;

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(getString(R.string.pick_image_from));

        final int checkedItemSelected = 0; //this will checked the item when user open the dialog
        //this will checked the item when user open the dialog
        builder.setSingleChoiceItems(importList, checkedItemSelected, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                checked = importList[which];
                check = which;
            }
        });


        builder.setPositiveButton(getString(R.string.done), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                switch (check) {
                    case 0:
                        //pickImageFromGallery();
                        openImage();
                        break;

                    case 1:

                        ContentValues values = new ContentValues();
                        values.put(MediaStore.Images.Media.TITLE,"Temp pic");
                        imageUri = getActivity().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,values);

                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        intent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
                        startActivityForResult(intent,CAMERA_REQUEST);

                }

                //}
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();


    }
    private void openImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, IMAGE_REQUEST);
    }


    private String getFileWxtension(Uri uri) {
        ContentResolver contentResolver = getContext().getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void uploadImage() {
        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage(getString(R.string.uploading));
        progressDialog.show();

        if (imageUri != null) {
            final StorageReference fileReference = storageReference.child(System.currentTimeMillis()
                    + "." + getFileWxtension(imageUri));

            uploadTask = fileReference.putFile(imageUri);
            uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    return fileReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();
                        String mUri = downloadUri.toString();

                        myRef = database.getReference("user").child(user.getUid());
                        HashMap<String, Object> map = new HashMap<>();
                        map.put("imageURL", mUri);
                        myRef.updateChildren(map);

                        progressDialog.dismiss();

                    } else {
                        //Toast.makeText(getContext(), "Failed!", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    //Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();

                }
            });
        } else {
            //Toast.makeText(getContext(), "No Image Selected", Toast.LENGTH_SHORT).show();
            Snackbar.make(getView(), R.string.no_image_selected, Snackbar.LENGTH_SHORT)
                    .show();

        }
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == WRITE_PERMISSION_REQUEST) {
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {

                //Toast.makeText(getContext(), "can't take pic", Toast.LENGTH_SHORT).show();
            }
            else{
                dialogProfile();
            }
        }
        else {
            dialogProfile();

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {


            imageUri = data.getData();

            if (uploadTask != null && uploadTask.isInProgress()) {
                //Toast.makeText(getContext(), "Upload in progress", Toast.LENGTH_SHORT).show();
            } else {
                uploadImage();
            }

        }

        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {

            if (uploadTask != null && uploadTask.isInProgress()) {
            }
            else {
                uploadImage();
            }
        }
    }
}

