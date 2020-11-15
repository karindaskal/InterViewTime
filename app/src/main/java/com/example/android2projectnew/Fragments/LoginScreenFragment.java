package com.example.android2projectnew.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.android2projectnew.R;
import com.google.android.material.textfield.TextInputLayout;

public class LoginScreenFragment extends Fragment {

    EditText emailEt;
    TextInputLayout textInputLayoutEmail;
    TextInputLayout textInputLayoutPass;
    EditText passwordEt;


    public interface OnLoginListner {
         void onClickLoginButton(String email, String password);
         void onClickCreateAccountButton();
         void onClickForgatPassword(String email);
         void onClickContinueAsGuest();

    }

   OnLoginListner callback;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            callback=(OnLoginListner) context;

        }
        catch (ClassCastException e){
            throw  new ClassCastException("the activity must implemnt interface");
        }
    }



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        emailEt = view.findViewById(R.id.email_textInput);
        textInputLayoutEmail = view.findViewById(R.id.email_et);
        passwordEt=view.findViewById(R.id.password_textInput);
        Button loginButton=view.findViewById(R.id.login_btn);
        Button forgotPassBtn=view.findViewById(R.id.forgot_pass);
        Button createAccBtn=view.findViewById(R.id.create_new_account);
        Button continueAsGuestBtn=view.findViewById(R.id.continue_as_guest_btn);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // loginFragmentListener.onClickLoginButton(emailEt.getText().toString(), passwordEt.getText().toString());
                if(emailEt.getText().toString().equals("") || passwordEt.getText().toString().equals("")){
                    //Toast.makeText(getActivity(), "you need to enter somethinggg", Toast.LENGTH_SHORT).show();
                    if(emailEt.getText().toString().equals("")){
                        emailEt.setError(getString(R.string.enter_email));
                        //textInputLayoutEmail.setEndIconMode(TextInputLayout.END_ICON_CUSTOM);
                    }
                    if(passwordEt.getText().toString().equals("")){
                        passwordEt.setError(getString(R.string.please_enter_pass));
                        //textInputLayoutPass.setEndIconMode(TextInputLayout.END_ICON_CUSTOM);
                    }
                }
                else {
                    callback.onClickLoginButton(emailEt.getText().toString(), passwordEt.getText().toString());
                }
            }
        });
        forgotPassBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getActivity(), "pressedddd", Toast.LENGTH_SHORT).show();
                callback.onClickForgatPassword(emailEt.getText().toString());
            }
        });
        createAccBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.onClickCreateAccountButton();
            }
        });
        continueAsGuestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.onClickContinueAsGuest();
            }
        });

    }
}
