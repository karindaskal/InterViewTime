package com.example.android2projectnew.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.android2projectnew.Module.Opinion;
import com.example.android2projectnew.R;
import com.example.android2projectnew.Module.UserProfileData;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class OpinionAdapter extends RecyclerView.Adapter<OpinionAdapter.ViewHolder> {

    FirebaseUser user;
    UserProfileData mission;

    public List<Opinion> opinionList;
    public Context context;

    public LayoutInflater layoutInflater;

    public CardView cardView;
    public OnOpinionListener listener;


    String stringHeadline, stringCompanyName, stringId;
    String stringYear1,stringYear2, stringOnDuty, stringPros, stringCons, stringtimeStamp;
    //ImageButton userImg;
    CircleImageView userImg;
    Date date;
    RatingBar ratingBar;

    private int selectedPosition;


    public interface OnOpinionListener{
        void onOpinionClick(int position, View view);
        void onImageOrTextUserNameClicked(int position, View view);
    }


    public void setListener(OnOpinionListener listener){
        this.listener = listener;
    }


    public OpinionAdapter(List<Opinion> opinionList,Context context){
        this.opinionList = opinionList;
        this.context =context;
    }


    @NonNull
    @Override
    public OpinionAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }



    @Override
    public void onBindViewHolder(@NonNull final OpinionAdapter.ViewHolder holder, int position) {

        DatabaseReference myRef;
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getInstance().getReference().child("user");

        Opinion opinion = opinionList.get(position);

        stringHeadline = opinion.getHeadline();
        stringCompanyName = opinion.getCompanyName();
        stringYear1 = opinion.getYear1();
        stringYear2 = opinion.getYear2();
        stringOnDuty = opinion.getOnDuty();
        stringPros = opinion.getPros();
        stringCons = opinion.getCons();
        stringId = opinion.getId();
        date=opinion.getTimeStamp();
        stringtimeStamp = java.text.DateFormat.getDateTimeInstance().format(date);





        holder.headlineTv.setText(stringHeadline);
        holder.companyNameTv.setText(stringCompanyName);
        holder.year1Tv.setText(stringYear1);
        holder.year2Tv.setText(stringYear2);
        holder.onDutyTv.setText(stringOnDuty);
        holder.prosTv.setText(stringPros);
        holder.consTv.setText(stringCons);
        holder.timeStamp.setText(stringtimeStamp);
       // holder.userName.setText(stringUserName);

        myRef.child(stringId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                mission = snapshot.getValue(UserProfileData.class);

                if (snapshot.exists()) {



                   if(context.getApplicationContext()!=null){

                       Glide.with(context.getApplicationContext()).load(mission.getImageURL()).centerCrop().into(holder.circleImageView);
                   }

                   holder.userName.setText(mission.getUserName());


                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
               // Toast.makeText(getActivity(), "failed", Toast.LENGTH_SHORT).show();
            }
        });
       // holder.ratingBar1.setRating(opinion.getRatingBar());
    }

    @Override
    public int getItemCount() {
        return opinionList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView headlineTv, companyNameTv, year1Tv, year2Tv, onDutyTv, prosTv, consTv, userName, timeStamp;
        public CircleImageView circleImageView;

       // public RatingBar ratingBar1;




        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            cardView = (CardView)itemView.findViewById(R.id.card_view_layout);
            headlineTv = (TextView)itemView.findViewById(R.id.tv_headline);
            companyNameTv = (TextView)itemView.findViewById(R.id.tv_company_name);
            year1Tv = (TextView)itemView.findViewById(R.id.year1_tv);
            year2Tv = (TextView)itemView.findViewById(R.id.year2_tv);
            onDutyTv = (TextView)itemView.findViewById(R.id.job_title_tv);
            prosTv = (TextView)itemView.findViewById(R.id.pros_tv_opinion);
            consTv = (TextView)itemView.findViewById(R.id.cons_tv_opinion);
            userName = (TextView)itemView.findViewById(R.id.username_opinion_tv);
            userImg = (CircleImageView) itemView.findViewById(R.id.img_opinion);
            timeStamp = (TextView)itemView.findViewById(R.id.tv_time);
            circleImageView = (CircleImageView)itemView.findViewById(R.id.img_opinion);


            /*ratingBar1 = (RatingBar)itemView.findViewById(R.id.rating);

            ratingBar1.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                @Override
                public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                    if(fromUser){mClickListener.onRatingBarChange(opinionList.get(getLayoutPosition()),rating);
                    }
                }
            });*/

            userName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onImageOrTextUserNameClicked(getAdapterPosition(),v);
                }
            });

            circleImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onImageOrTextUserNameClicked(getAdapterPosition(),v);

                }
            });


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onOpinionClick(getAdapterPosition(),v);

                }
            });
        }
    }

    public void setSelectedPosition(int selectedPosition){
        this.selectedPosition = selectedPosition;
    }

    public int getSelectedPosition() {
        return selectedPosition;
    }
}
