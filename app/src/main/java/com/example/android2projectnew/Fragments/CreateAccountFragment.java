package com.example.android2projectnew.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.android2projectnew.R;
import com.google.android.material.snackbar.Snackbar;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CreateAccountFragment extends Fragment {

    EditText emailAccEt, passwordAccEt,password2AccEt, usernameAccEt;
    Button signUpAccBtn,alreadyHaveAccBtn;
    ImageButton createAccBackBtn;
    private OncreateAccountClickListener createAccountClickListener;

     public interface OncreateAccountClickListener {
        void onCreateAccount(String userName, String userEmail, String userPassword, String userRepeatPassword);
        void AlreadyHaveAcc();
    }

    OncreateAccountClickListener callBack;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            callBack = (OncreateAccountClickListener) context;

        }
        catch (ClassCastException e){
            throw  new ClassCastException("the activity must implemnt interface");
        }
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_create_acc, container, false);
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        emailAccEt = view.findViewById(R.id.mail_acc_textInput);
        passwordAccEt = view.findViewById(R.id.password_acc_textInput);
        password2AccEt = view.findViewById(R.id.password2_acc_textInput);
        usernameAccEt = view.findViewById(R.id.user_name_acc_textInput);
        signUpAccBtn = view.findViewById(R.id.btn_signup);
        alreadyHaveAccBtn = view.findViewById(R.id.already_btn);
        createAccBackBtn = view.findViewById(R.id.create_acc_back_btn);


        signUpAccBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(emailAccEt.getText().toString().equals("") || passwordAccEt.getText().toString().length() < 6 && !isValidPassword(passwordAccEt.getText().toString())
                        ||  password2AccEt.getText().toString().length() < 6 && !isValidPassword(passwordAccEt.getText().toString()) || usernameAccEt.getText().toString().equals("")){
                    //Toast.makeText(getActivity(), "please enter everything", Toast.LENGTH_SHORT).show();
                    if(emailAccEt.getText().toString().equals("")){
                        emailAccEt.setError(getString(R.string.enter_email));

                    }
                    if(usernameAccEt.getText().toString().equals("")){
                        usernameAccEt.setError(getString(R.string.enter_username));
                    }
                    if(passwordAccEt.getText().toString().length() < 6 && !isValidPassword(passwordAccEt.getText().toString()) ){
                        passwordAccEt.setError(getString(R.string.illegal_pass));

                    }
                    if(password2AccEt.getText().toString().length() < 6 && !isValidPassword(password2AccEt.getText().toString())){
                        password2AccEt.setError(getString(R.string.illegal_pass));

                    }
                }
                else {
                    if(passwordAccEt.getText().toString().equals(password2AccEt.getText().toString())){
                        callBack.onCreateAccount(usernameAccEt.getText().toString(), emailAccEt.getText().toString(), passwordAccEt.getText().toString(), password2AccEt.getText().toString());
                    }

                    else{
                        //Toast.makeText(getActivity(), "Please confirm password", Toast.LENGTH_SHORT).show();
                        Snackbar.make(view, R.string.pass_not_match, Snackbar.LENGTH_SHORT)
                                .show();
                    }
                }

            }
        });


        alreadyHaveAccBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callBack.AlreadyHaveAcc();
            }
        });

        createAccBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callBack.AlreadyHaveAcc();
            }
        });

    }

    public static boolean isValidPassword(final String password) {

        Pattern pattern;
        Matcher matcher;
        final String PASSWORD_PATTERN = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{6,20})";
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);

        return matcher.matches();

    }



}
