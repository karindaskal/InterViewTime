package com.example.android2projectnew.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.android2projectnew.R;

public class WebViewFragment extends Fragment {

    private WebView webView;


    public static WebViewFragment newInstance(String link) {
        WebViewFragment webViewFragment = new WebViewFragment();
        Bundle bundle = new Bundle();
        bundle.putString("link", link);
        webViewFragment.setArguments(bundle);
        return webViewFragment;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.web_view_fragment, container, false);

        webView = view.findViewById(R.id.web_view);
        //webView.setWebViewClient(new WebViewClient());

        String link = getArguments().getString("link");
        webView.loadUrl(link);

        return view;

    }
}
