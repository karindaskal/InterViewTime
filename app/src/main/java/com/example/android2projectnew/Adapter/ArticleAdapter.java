package com.example.android2projectnew.Adapter;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.android2projectnew.Module.Article;
import com.example.android2projectnew.R;

import java.util.List;

public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.Holder> {

    public Context context;
    public List<Article> articleList;
    Article article;

    public OnArticleListener listener;

    public interface OnArticleListener{
        void onArticleClick(int position, View view);
    }

    public void setListener(OnArticleListener listener) {
        this.listener = listener;
    }


    public ArticleAdapter(Context context, List<Article> articleList) {
        this.context = context;
        this.articleList = articleList;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.article_card,parent, false);
        Holder holder=new Holder(view);
        return holder;

    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        article=articleList.get(position);
        Glide
                .with(context)
                .load(article.getArticleImage())
                .centerCrop()
                .into(holder.articleImage);
        holder.nameArticleTv.setText(article.getNameArticle());
        holder.subjectArticleTv.setText(article.getSubjectArticle());




    }

    @Override
    public int getItemCount() {
        return articleList.size();
    }


    public class Holder extends RecyclerView.ViewHolder{

        ImageView articleImage;
        TextView nameArticleTv, subjectArticleTv;

        public Holder(@NonNull View itemView) {
            super(itemView);

            articleImage=itemView.findViewById(R.id.articleImage);
            nameArticleTv=itemView.findViewById(R.id.nameTv_article);
            subjectArticleTv=itemView.findViewById(R.id.subjectArticle);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onArticleClick(getAdapterPosition(), v);
                }
            });
        }
    }

}
