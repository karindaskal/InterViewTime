package com.example.android2projectnew.Adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.android2projectnew.Module.Tabs;
import com.example.android2projectnew.Fragments.WorkFragment;
import com.example.android2projectnew.R;

public class WorkPagerAdapter extends FragmentPagerAdapter {

    Context context;

    final int size = Tabs.values().length;



    public WorkPagerAdapter(FragmentManager fm,Context context) {
        super(fm);
        this.context = context;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return WorkFragment.newInstance(position);

    /*    switch (position){
            case 0:
               Tabs.valueOf("Opinion1").setName("oPINION2");
            case 1:
                return
            case 2:
                return new SalaryFragment();
            default:
                return null;
        }*/
    }



    @Override
    public int getCount() {
        return size;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {

        switch (position) {
            case 0:
                return context.getString(R.string.opinion).toLowerCase();
            case 1:
                return (context.getString(R.string.interview_questions)).toLowerCase();

            case 2:
                return context.getString(R.string.salary_num).toLowerCase();


            default:
                return "Tab";

        }
    }


/*
    public View getTabView(int position){
        View tab = LayoutInflater.from(context).inflate(R.layout.tab_fragment,null);

    }*/
}
