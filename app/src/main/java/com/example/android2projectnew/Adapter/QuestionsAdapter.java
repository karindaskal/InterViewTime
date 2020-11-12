package com.example.android2projectnew.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.android2projectnew.Module.InterviewQuestions;
import com.example.android2projectnew.Module.UserProfileData;
import com.example.android2projectnew.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class QuestionsAdapter extends RecyclerView.Adapter<QuestionsAdapter.questionsViewHolder> {


    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    UserProfileData mission;
    private DatabaseReference likesRef;
    DatabaseReference myRef;
    InterviewQuestions question;

    String myUid;

    private List<InterviewQuestions> questions;
    public Context context;

    private OnQuesionsListener listener;

    public CardView cardView;
    String stringtimeStamp;

    boolean mProcessLike = false;

    public interface OnQuesionsListener {
        void onQuestionsClick(int position, View view);
        void onImageOrTextUserNameClicked(int position, View view);
        void onAddAnswerClicked(int position, View view);
        //void onAddLikeClicked(int position, View view);

    }
    public void setListener(OnQuesionsListener listener){
        this.listener=listener;
    }

    public QuestionsAdapter(List<InterviewQuestions> subjects, Context context) {
        this.questions = subjects;
        this.context = context;
    }


    @NonNull
    @Override
    public questionsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_q,parent,false);
        questionsViewHolder subjectViewHolder =new questionsViewHolder(view);
        return subjectViewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull final questionsViewHolder holder, final int position) {

        //  mProcessLike = true;

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getInstance().getReference().child("user");
        likesRef = database.getInstance().getReference().child("list/Questions");
        myUid = user.getUid();

        question = questions.get(position);

        holder.headLineTV.setText((context.getString(R.string.interview_for_a)) +" "+ question.getJob()
                + (context.getString(R.string.at)) +" "+  question.getCompany());
        holder.date.setText(question.getDate());
        holder.answerTV.setText(question.getAnswer());
        holder.questionTV.setText(question.getQuestion());
        holder.InformationTV.setText(question.getProses());
        holder.likes.setText(question.getLikes() + " " + context.getString(R.string.likes));
        holder.dateTV.setText(java.text.DateFormat.getDateTimeInstance().format(question.getTimeStamp()));
        holder.commentBtn.setText(question.getpComment() + " " + context.getString(R.string.answers));

        final int pId = questions.get(position).getIdQ();


      /*  holder.likeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final int pLikes = questions.get(position).getLikes();
                if(mProcessLike){
                    likesRef.child(position+"/likes").setValue(pLikes+1);
                    // questions.get(position).setLikes(pLikes+1);
                    mProcessLike=false;


                }
                else
                    {
                    likesRef.child(position+"/likes").setValue(pLikes-1);
                    //questions.get(position).setLikes(pLikes-1);
                    mProcessLike=true;
                }

            }
        });*/

        isLiked(String.valueOf(questions.get(position).getIdQ()),holder.likeBtn,questions.get(position).getId());
        nrLikes(holder.likes,String.valueOf(questions.get(position).getIdQ()));

        holder.likeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.likeBtn.getTag().equals("like")){
                    FirebaseDatabase.getInstance().getReference().child("Likes")
                            .child(String.valueOf(questions.get(position).getIdQ()))
                            .child(user.getUid()).setValue(true);
                    addToHisNotifications(""+questions.get(position).getId(),""+pId,context.getString(R.string.liked_your_post));



                }
                else {
                    FirebaseDatabase.getInstance().getReference().child("Likes")
                            .child(String.valueOf(questions.get(position).getIdQ()))
                            .child(user.getUid()).removeValue();
                }
            }
        });
        likesRef.child(position+"/likes").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                final  int pLikes1;

                holder.likes.setText(dataSnapshot.getValue()+ " "+ context.getString(R.string.likes));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        //setLikes(holder,pId);



        // stringtimeStamp = java.text.DateFormat.getDateTimeInstance().format(date);

        myRef.child(question.getId()).addValueEventListener(new ValueEventListener() {
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
        return questions.size();
    }

    public class questionsViewHolder extends RecyclerView.ViewHolder {
        TextView headLineTV;
        TextView dateTV;
        TextView InformationTV;
        TextView questionTV ;
        TextView answerTV;
        TextView userName;
        TextView likes;
        Button answerBtn;
        TextView date;
        Button commentBtn;
        ImageView likeBtn;
        CircleImageView circleImageView;




        public questionsViewHolder(@NonNull View itemView) {
            super(itemView);
            headLineTV=itemView.findViewById(R.id.Headline);
            dateTV=itemView.findViewById(R.id.date);
            InformationTV =itemView.findViewById(R.id.Information);
            questionTV=itemView.findViewById(R.id.Question);
            answerTV=itemView.findViewById(R.id.Answers);
            userName=itemView.findViewById(R.id.username_question_tv);
            answerBtn=itemView.findViewById(R.id.btn_comment);
            date = itemView.findViewById(R.id.interview_date);
            likes = itemView.findViewById(R.id.likes_tv);
            commentBtn = itemView.findViewById(R.id.btn_number_of_li);
            likeBtn = itemView.findViewById(R.id.btn_like);
            circleImageView = itemView.findViewById(R.id.img_question);




            userName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onImageOrTextUserNameClicked(getAdapterPosition(),v);
                }
            });


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onQuestionsClick(getAdapterPosition(),v);

                }
            });

            answerBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onAddAnswerClicked(getAdapterPosition(),v);
                }
            });

            commentBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onAddAnswerClicked(getAdapterPosition(),v);
                }
            });

            circleImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onImageOrTextUserNameClicked(getAdapterPosition(),v);

                }
            });


        }


    }

    private void addToHisNotifications(String hisUid, String pId, String notification){
        //String timeStamp = ""+System.currentTimeMillis();

        String timeStamp = String.valueOf(System.currentTimeMillis());
        Date calender = Calendar.getInstance().getTime();

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("pId",pId);
        hashMap.put("timeStamp",calender);
        hashMap.put("pUid",pId);
        hashMap.put("notification",notification);
        hashMap.put("sUid",myUid);


        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("user");
        ref.child(hisUid).child("NotificationCommentLike").child(timeStamp).setValue(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {


                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });


    }

    private void isLiked(final String postId, final ImageView imageView, final String hisId){

        final  FirebaseUser firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference()
                .child("Likes")
                .child(postId);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(firebaseUser.getUid()).exists()){
                    imageView.setImageResource(R.drawable.ic_baseline_thumb_up_24);
                    imageView.setTag("liked");
                    //addToHisNotifications(""+hisId,""+postId,"Liked Your Post");


                }
                else{
                    imageView.setImageResource(R.drawable.ic_baseline_thumb_up2_24);
                    imageView.setTag("like");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void nrLikes(final TextView likes, String postid){
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference().child("Likes").child(postid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                likes.setText(dataSnapshot.getChildrenCount()+" " +  context.getString(R.string.likes));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
