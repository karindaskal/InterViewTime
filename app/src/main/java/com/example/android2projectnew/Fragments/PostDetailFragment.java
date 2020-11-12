package com.example.android2projectnew.Fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.android2projectnew.Adapter.AdapterAnswer;
import com.example.android2projectnew.Module.Comment;
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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class PostDetailFragment extends Fragment {

    FirebaseDatabase database = FirebaseDatabase.getInstance();

    DatabaseReference myRef = database.getReference();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    Date postOriginalString;;


    ProgressDialog progressDialog;


    String myUid, myEmail, myName, myDp, pLikes, hisDp, hisName, postTimeComment;
    int postId;
    InterviewQuestions interviewQuestions;

    CircleImageView uProfilePic1, pImageIv2;
    TextView nameTv, pickerTimeTv, pTitleTv, pInformationTv, pQuestionTv,pAnswerTv, pLikesTv, originalPostDate;
    Button pCommentTv;
    String userId;
    int newCommentVal;
    ImageView likeBtn;
    LinearLayout profileLayout;
    EditText commentEt;
    ImageButton sendBtn;
    CircleImageView add_pic_iv;
    boolean mProcessComment = false;
    boolean mProcessLike = false;
    RecyclerView recyclerView;

    List<Comment> commentList;
    AdapterAnswer adapterAnswer;

    ImageButton backBtn;

    String textString;



    public static PostDetailFragment newInstance(InterviewQuestions interviewQuestions){
        PostDetailFragment postDetailFragment = new PostDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("interview_question",interviewQuestions);
        postDetailFragment.setArguments(bundle);
        return postDetailFragment;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.comments_fragment,container,false);

      /*  ActionBar actionBar = getActivity().getActionBar();
        actionBar.setTitle("Question Deatails");
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
*/

        uProfilePic1 = view.findViewById(R.id.img_question_a);
        pImageIv2 = view.findViewById(R.id.answer_add_image);
        nameTv = view.findViewById(R.id.username_question_tv_a);
        pickerTimeTv = view.findViewById(R.id.interview_date_a);
        pTitleTv = view.findViewById(R.id.Headline_a);
        pInformationTv = view.findViewById(R.id.Information_a);
        pQuestionTv = view.findViewById(R.id.Question_a);
        pAnswerTv = view.findViewById(R.id.Answers_a);
        likeBtn = view.findViewById(R.id.btn_like1);
        profileLayout = view.findViewById(R.id.profile_layout_a);
        commentEt = view.findViewById(R.id.answer_et);
        sendBtn = view.findViewById(R.id.send_btn);
        originalPostDate = view.findViewById(R.id.date_a);
        pCommentTv = view.findViewById(R.id.btn_number_of_li);

        pLikesTv = view.findViewById(R.id.likes_tv);
        recyclerView = view.findViewById(R.id.recycler_comments);
        backBtn = view.findViewById(R.id.back_comment);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack();

            }
        });


        interviewQuestions = (InterviewQuestions)getArguments().getSerializable("interview_question");


        postId =interviewQuestions.getIdQ();



        myUid = user.getUid();

        loadPostInfo();

        loadUserInfo();

        loadComments();


        isLiked(String.valueOf(interviewQuestions.getIdQ()),likeBtn);
        nrLikes(pLikesTv,String.valueOf(interviewQuestions.getIdQ()));



        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postComment();

            }
        });

        likeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(likeBtn.getTag().equals("like")){
                    FirebaseDatabase.getInstance().getReference().child("Likes")
                            .child(String.valueOf(interviewQuestions.getIdQ()))
                            .child(user.getUid()).setValue(true);
                    addToHisNotifications(""+userId ,""+postId,getString(R.string.liked_your_post));


                }
                else {
                    FirebaseDatabase.getInstance().getReference().child("Likes")
                            .child(String.valueOf(interviewQuestions.getIdQ()))
                            .child(user.getUid()).removeValue();

                }




            }
        });




        return view;
    }

    private void addToHisNotifications(String hisUid, String pId, String notification){
        //Date timeStamp = System.currentTimeMillis();

        String timeStamp = String.valueOf(System.currentTimeMillis());
        Date calender1 = Calendar.getInstance().getTime();

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("pId",pId);
        hashMap.put("timeStamp",calender1);
        hashMap.put("pUid",hisUid);
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

    private void loadComments() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        commentList = new ArrayList<>();
        adapterAnswer = new AdapterAnswer(commentList,getContext());
        recyclerView.setAdapter(adapterAnswer);


        myRef = database.getReference("list/Questions").child(String.valueOf(postId)).child("commentList");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                commentList.clear();

                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    Comment modelComment = ds.getValue(Comment.class);
                    modelComment.setTimpeStamp(ds.child("timeStamp").getValue(Date.class));

                    commentList.add(modelComment);

                    adapterAnswer = new AdapterAnswer(commentList,getContext());
                    recyclerView.setAdapter(adapterAnswer);

                }
                //adapterAnswer.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }



    private void isLiked(final String postId, final ImageView imageView){

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
                    //addToHisNotifications(""+userId ,""+postId,"Liked Your Post");

                }
                else{
                    imageView.setImageResource(R.drawable.ic_baseline_thumb_up2_24);
                    imageView.setTag("like");
                    //addToHisNotifications(""+userId ,""+postId,"Liked Your Post");

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

                if(getContext()!=null){
                    textString ="" + dataSnapshot.getChildrenCount() + " " +getContext().getString(R.string.likes_space);
                }

                likes.setText(textString);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void loadPostInfo() {
        myRef = database.getReference("list/Questions").child(String.valueOf(postId));
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               for(DataSnapshot ds: dataSnapshot.getChildren()){
                   nameTv.setText(interviewQuestions.getUserName());
                   originalPostDate.setText(java.text.DateFormat.getDateTimeInstance().format(interviewQuestions.getTimeStamp()));
                   pickerTimeTv.setText(interviewQuestions.getDate());

                   pTitleTv.setText(getString(R.string.interview_for_a) + " " + interviewQuestions.getJob()+" " + getString(R.string.at) + interviewQuestions.getCompany());
                   pInformationTv.setText(interviewQuestions.getProses());
                   pQuestionTv.setText(interviewQuestions.getQuestion());
                   pAnswerTv.setText(interviewQuestions.getAnswer());
                   userId = interviewQuestions.getId();
                   myRef = database.getInstance().getReference().child("user");
                   myRef.child(userId).addValueEventListener(new ValueEventListener() {
                       @Override
                       public void onDataChange(@NonNull DataSnapshot snapshot) {

                           UserProfileData mission = snapshot.getValue(UserProfileData.class);

                           if (snapshot.exists()) {


                               // holder.userName.setText(mission.getUserName());
                               if(getContext()!=null){
                                   Glide.with(getContext()).load(mission.getImageURL()).centerCrop().into(uProfilePic1);
                               }

                           }

                       }

                       @Override
                       public void onCancelled(@NonNull DatabaseError error) {
                           // Toast.makeText(getActivity(), "failed", Toast.LENGTH_SHORT).show();
                       }
                   });
                   myRef.child(user.getUid()).addValueEventListener(new ValueEventListener() {
                       @Override
                       public void onDataChange(@NonNull DataSnapshot snapshot) {

                           UserProfileData mission = snapshot.getValue(UserProfileData.class);

                           if (snapshot.exists()) {


                               // holder.userName.setText(mission.getUserName());
                               if(getContext()!=null){
                                   Glide.with(getContext()).load(mission.getImageURL()).centerCrop().into(pImageIv2);

                               }

                           }

                       }

                       @Override
                       public void onCancelled(@NonNull DatabaseError error) {
                           // Toast.makeText(getActivity(), "failed", Toast.LENGTH_SHORT).show();
                       }
                   });

                   String commentCount = "" + dataSnapshot.child("pComment"+"").getValue();

                   pCommentTv.setText(commentCount + " " + getContext().getString(R.string.answers));


                   String postOriginalString = java.text.DateFormat.getDateTimeInstance().format(interviewQuestions.getTimeStamp());


                   Calendar calendar = Calendar.getInstance(Locale.getDefault());
                   postTimeComment = android.text.format.DateFormat.format("yyyy-MM-dd HH:mm:ss", calendar).toString();

               }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void postComment() {
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Adding comment... ");
        
        String comment = commentEt.getText().toString().trim();
        if(TextUtils.isEmpty(comment)){
            //Toast.makeText(getContext(), "Comment is empty...", Toast.LENGTH_SHORT).show();
            return;

        }
        String timeStampCommentPost = String.valueOf(System.currentTimeMillis());
       Date calender = Calendar.getInstance().getTime();

        DatabaseReference newRef = database.getReference("list/Questions").child(String.valueOf(postId)).child("commentList");

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("cId", timeStampCommentPost);
        hashMap.put("comment", comment);
        hashMap.put("timeStamp", calender);
        hashMap.put("uId", myUid);
        //hashMap.put("uDp",myDp);
        hashMap.put("uName", myName);

        newRef.child(timeStampCommentPost).setValue(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        progressDialog.dismiss();
                        //Toast.makeText(getContext(), "Answer Added.. ", Toast.LENGTH_SHORT).show();
                        commentEt.setText("");
                        updateCommentCount();

                        addToHisNotifications(""+userId,""+postId,"Commented on your Post");

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        //Toast.makeText(getContext(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });



        
    }


    private void updateCommentCount() {
        mProcessComment = true;
        myRef = database.getReference("list/Questions").child(String.valueOf(postId));
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(mProcessComment){
                    String comments = "" + dataSnapshot.child("pComment").getValue();
                     int newCommentVal = Integer.parseInt(comments) +1;
                    myRef.child("pComment").setValue(""+ newCommentVal);

                    mProcessComment = false;
                    pCommentTv.setText(newCommentVal + getContext().getString(R.string.answers));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void loadUserInfo() {

        myRef = database.getReference();
        myRef.child("user/"+user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                UserProfileData mission = dataSnapshot.getValue(UserProfileData.class);

                if (dataSnapshot.exists()) {
                   myName = mission.getUserName();

                   //picture
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //Toast.makeText(getActivity(), "failed", Toast.LENGTH_SHORT).show();
            }
        });

    }


}
