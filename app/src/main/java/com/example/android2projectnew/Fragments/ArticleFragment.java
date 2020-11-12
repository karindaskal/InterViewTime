package com.example.android2projectnew.Fragments;

import android.media.Image;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;

import androidx.annotation.AnimatorRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android2projectnew.Adapter.ArticleAdapter;
import com.example.android2projectnew.Module.Article;
import com.example.android2projectnew.R;

import java.util.ArrayList;
import java.util.List;

public class ArticleFragment extends Fragment {

    public List<Article> articleList;
    RecyclerView articleRecycler;
    ArticleAdapter articleAdapter;
    ImageButton backBtn;

    final String ARTICLE_FRAGMENT_TAG = "article";


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       View view=inflater.inflate(R.layout.article_recycle, container, false);

       articleList = new ArrayList<>();
       articleRecycler = view.findViewById(R.id.articleRecycler);

       articleAdapter = new ArticleAdapter(getContext(), articleList);

       articleRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));

       backBtn = view.findViewById(R.id.back_article);

       backBtn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               getActivity().getSupportFragmentManager().popBackStack();

           }
       });



       articleList.add(new Article("https://www.cv-library.co.uk/career-advice/wp-content/uploads/2019/01/how-to-write-a-cv-2.jpg","How to write a CV: Tips for 2020",
               "This guide will show you how to write a great CV that’s ready for 2020 and beyond","https://www.cv-library.co.uk/career-advice/cv/how-to-write-a-cv-tips/"));

        articleList.add(new Article("https://coda.newjobs.com/api/imagesproxy/ms/cms/content30/images/resume-samples.jpg","CV Tips Which Really Work",
                "Writing a CV always takes care. This is the case whether you are starting from scratch or updating your details.","https://www.monster.co.uk/career-advice/article/cv-tips"));

        articleList.add(new Article("https://www.livecareer.com/wp-content/uploads/2020/09/14-job-hunting-tips.jpg","14 Quick Tips for Finding a New Job",
                "Here are some of my best tips for finding a new job at any career level.","https://www.livecareer.com/resources/jobs/search/14-job-hunting-tips"));


        articleList.add(new Article("https://www.gotfriends.co.il/media/1058/17-%D7%98%D7%99%D7%A4%D7%99%D7%9D-%D7%A7%D7%95%D7%A8%D7%95%D7%AA-%D7%97%D7%99%D7%99%D7%9D.jpg","17 טיפים לכתיבת קורות חיים. מנקודת מבטה של המגייסת",
                "17 טיפים לכתיבת קורות חיים. מנקודת מבטה של המגייסת","https://www.gotfriends.co.il/%D7%91%D7%9C%D7%95%D7%92%D7%99%D7%9D/17-%D7%98%D7%99%D7%A4%D7%99%D7%9D-%D7%9C%D7%9B%D7%AA%D7%99%D7%91%D7%AA-%D7%A7%D7%95%D7%A8%D7%95%D7%AA-%D7%97%D7%99%D7%99%D7%9D/"));


        articleList.add(new Article("https://www.savethestudent.org/uploads/job-1.jpg","10 smarter ways to find a job",
                "Whether you're a fresh graduate or someone who's been in the job market for years – job hunting is tough. Have you tried rethinking your approach?","https://www.savethestudent.org/student-jobs/top-10-smarter-ways-to-find-a-job.html"));


        articleList.add(new Article("https://d4y70tum9c2ak.cloudfront.net/articleThumbnail/C_pCyjauQWMIS27zt%2BuFKUbtGsLFEOKRyZY43rmCznc/600.jpg","How to Prepare for an Interview",
                "You’ve taken the time to craft your CV, write your cover letter and send off your job application. And lucky you – you’ve reached the next stage. But what should you wear to an interview?","https://www.cv-library.co.uk/career-advice/interviews/how-to-dress-for-an-interview/"));


        articleList.add(new Article("https://www.cv-library.co.uk/career-advice/wp-content/uploads/2017/01/what-to-wear-to-an-interview-in-2018-1.jpg","What to wear to an interview in 2020",
                "Preparing for an interview might seem intimidating, but there are several steps you can take to prepare yourself for a successful interview.","https://www.indeed.com/career-advice/interviewing/how-to-prepare-for-an-interview"));



        articleList.add(new Article("https://www.cv-library.co.uk/career-advice/wp-content/uploads/2019/11/7-signs-your-interview-went-well-1-1024x683.jpg","7 signs your interview went well",
                "It’s a horrible feeling when you have to wait to hear back after an interview.","https://www.cv-library.co.uk/career-advice/interviews/7-signs-your-interview-went-well/"));


        articleList.add(new Article("https://www.thebalancecareers.com/thmb/LpLWcdZsssJSv5AUbfCmp2FX62w=/900x0/filters:no_upscale():max_bytes(150000):strip_icc():format(webp)/top-interview-tips-2058577_FINAL-5b7339fb46e0fb0050b4b20d.png","7 Interview Tips That Will Help You Get Hired",
                "Even when you have gone on more interviews than you can count, job interviewing never seems to get any easier. ","https://www.taasuka.gov.il/he/Applicants/JobSearchStages/pages/jobinterview.aspx"));


        articleList.add(new Article("https://d4y70tum9c2ak.cloudfront.net/articleThumbnail/VR-XN3s8ZzoWTg24p9q1yDladuo4LdLgsdQGqjTdIVA/600.jpg","125 Common Interview Questions and Answers (With Tips)",
                "In this article, we share some of the most commonly asked interview questions with tips on what interviewers are looking for in your response and example answers.","https://www.indeed.com/career-advice/interviewing/top-interview-questions-and-answers"));


        articleList.add(new Article("https://pilbox.themuse.com/image.png?filter=antialias&h=502&opt=1&pos=top-left&prog=1&q=keep&url=https%3A%2F%2Fcms-assets.themuse.com%2Fmedia%2Flead%2F6eaf632e-d2d8-403c-9a4f-bf3291427152.png%3Fv%3D326b51daae4a3e9c17da5154c4ff30f10d3636dc&w=1024","Your Ultimate Guide to Answering the Most Common Interview Questions",
                "Wouldn’t it be great if you knew exactly what questions a hiring manager would be asking you in your next job interview?","https://www.themuse.com/amp/advice/interview-questions-and-answers"));







        articleRecycler.setAdapter(articleAdapter);


       articleAdapter.setListener(new ArticleAdapter.OnArticleListener() {
           @Override
           public void onArticleClick(int position, View view) {
               String link = articleList.get(position).getArticleLink();
               WebViewFragment webViewFragment = WebViewFragment.newInstance(link);
               FragmentManager fragmentManager = getFragmentManager();
               FragmentTransaction transaction = fragmentManager.beginTransaction();
               transaction.add(R.id.drawer_layout,webViewFragment, ARTICLE_FRAGMENT_TAG);
               transaction.addToBackStack(null);
               transaction.commit();
           }
       });

        return view;

    }
}
