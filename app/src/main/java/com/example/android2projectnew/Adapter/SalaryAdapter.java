package com.example.android2projectnew.Adapter;

import android.content.Context;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.android2projectnew.Module.UserProfileData;
import com.example.android2projectnew.R;
import com.example.android2projectnew.Module.Salary;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class SalaryAdapter extends RecyclerView.Adapter<SalaryAdapter.salaryViewHoleder> implements ItemTouchHelperAdapter {

    FirebaseUser user;
    UserProfileData mission;

    private List<Salary> salaries;
    private Context context;

    private ItemTouchHelper mTouchHelper;
    private ItemTouchHelperAdapter actionsListener;

    private MySalaryListener salaryListener;




    @NonNull
    @Override
    public salaryViewHoleder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.salary_cell, parent, false);
//        context = parent.getContext();
        salaryViewHoleder salaryViewHoleder=new salaryViewHoleder(view);
        return salaryViewHoleder;
    }

    @Override
    public void onBindViewHolder(@NonNull final salaryViewHoleder holder, int position) {

        DatabaseReference myRef;
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getInstance().getReference().child("user");

        Salary salary=salaries.get(position);

        holder.jobFromYear.setText(salary.getFromYear());
        holder.jobToYear.setText(salary.getToYear());
        holder.jobDescription.setText(salary.getJob());
        holder.jobCompany.setText(salary.getCompanySalary());
        holder.structureOfJob.setText(salary.getJobStructure());
        holder.jobSalary.setText(salary.getGrossSalary());
        holder.seniorityOfJob.setText(salary.getJobSeniority());
        holder.timeStamp.setText(java.text.DateFormat.getDateTimeInstance().format(salary.getTimeStamp()));

        myRef.child(salary.getId()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                mission = snapshot.getValue(UserProfileData.class);

                if (snapshot.exists()) {


                    holder.userName.setText(mission.getUserName());

                    if(context.getApplicationContext()!=null) {

                        Glide.with(context.getApplicationContext()).load(mission.getImageURL()).centerCrop().into(holder.circleImageView);
                    }


                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Toast.makeText(getActivity(), "failed", Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public int getItemCount() {
        return salaries.size();
    }




    public interface MySalaryListener{
        void onSalaryClick(int position, View view);
        void onImageOrTextUserNameClicked(int position, View view);
    }

    public void setSalaryListener(MySalaryListener salaryListener) {
        this.salaryListener = salaryListener;
    }

    public void setActionsListener(ItemTouchHelperAdapter actionsListener) {
        this.actionsListener = actionsListener;
    }

    public SalaryAdapter(List<Salary> salaries,Context context) {
        this.salaries = salaries;
        this.context = context;

    }

    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        if(actionsListener!=null)
            actionsListener.onItemMove(fromPosition, toPosition);
    }

    @Override
    public void onItemSwiped(int position) {
        if(actionsListener!=null)
            actionsListener.onItemSwiped(position);
    }

    public void setTouchHelper(ItemTouchHelper touchHelper){
        this.mTouchHelper=touchHelper;
    }


    public class salaryViewHoleder extends RecyclerView.ViewHolder implements View.OnTouchListener, GestureDetector.OnGestureListener {

        TextView jobFromYear;
        TextView jobToYear;
        TextView jobDescription;
        TextView jobCompany;
        TextView structureOfJob;
        TextView jobSalary;
        TextView seniorityOfJob;
        TextView userName;
        TextView timeStamp;
        CircleImageView circleImageView;

        GestureDetector mGestureDetector;


        public salaryViewHoleder(View itemView){
            super(itemView);
            jobFromYear=itemView.findViewById(R.id.jobFromYearCell);
            jobToYear=itemView.findViewById(R.id.jobToYearCell);
            jobDescription=itemView.findViewById(R.id.jobDescriptionCell);
            jobCompany=itemView.findViewById(R.id.companyJobDescriptionCell);
            structureOfJob=itemView.findViewById(R.id.structureDescriptionCell);
            jobSalary=itemView.findViewById(R.id.salaryDescriptionCell);
            seniorityOfJob=itemView.findViewById(R.id.seniorityDescriptionCell);
            userName=itemView.findViewById(R.id.username_salary_tv);
            timeStamp = itemView.findViewById(R.id.date_salary);
            circleImageView = itemView.findViewById(R.id.img_salary);

            mGestureDetector=new GestureDetector(itemView.getContext(), this);

            userName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    salaryListener.onImageOrTextUserNameClicked(getAdapterPosition(),v);
                }
            });


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(salaryListener!=null)
                        salaryListener.onSalaryClick(getAdapterPosition(), view);
                }
            });

            circleImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    salaryListener.onImageOrTextUserNameClicked(getAdapterPosition(),v);

                }
            });




        }

        @Override
        public boolean onDown(MotionEvent e) {
            return false;
        }

        @Override
        public void onShowPress(MotionEvent e) {

        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            return false;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            return false;
        }

        @Override
        public void onLongPress(MotionEvent e) {
            mTouchHelper.startDrag(this);
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            return false;
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            mGestureDetector.onTouchEvent(event);
            return false;
        }
    }



}
