package com.ersen.pulselivetest.views.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ersen.pulselivetest.R;
import com.ersen.pulselivetest.interfaces.OnItemClickListener;
import com.ersen.pulselivetest.models.article.Article;

import java.util.ArrayList;

public class ArticleListAdapter extends RecyclerView.Adapter<ArticleListAdapter.ArticleListViewHolder> {

    private ArrayList<Article> mArticles;
    private static OnItemClickListener sOnItemClickListener;

    public ArticleListAdapter(ArrayList<Article> articles, OnItemClickListener onItemClickListener) {
        this.mArticles = articles;
        sOnItemClickListener = onItemClickListener;
    }

    @Override
    public ArticleListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_article, parent, false);
        return new ArticleListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ArticleListViewHolder holder, int position) {
        Article article = mArticles.get(position);
        holder.getArticleTitle().setText(article.getTitle());
        holder.getArticleSubTitle().setText(article.getSubTitle());
        holder.getArticleDate().setHint(article.getDate());
    }

    @Override
    public int getItemCount() {
        return mArticles.size();
    }

    public Article getItem(int position){
        return mArticles.get(position);
    }

    public void addNewArticles(ArrayList<Article> articles){
       for(int i = 0; i < articles.size(); i++){
           Article article = articles.get(i);
           if(!mArticles.contains(article)){
               mArticles.add(i,article);
               notifyItemInserted(i);
           }
       }
    }

    public class ArticleListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView mArticleTitle, mArticleSubTitle, mArticleDate;

        public ArticleListViewHolder(View itemView) {
            super(itemView);
            mArticleTitle = (TextView) itemView.findViewById(R.id.text_articleTitle);
            mArticleSubTitle = (TextView) itemView.findViewById(R.id.text_articleSubTitle);
            mArticleDate = (TextView) itemView.findViewById(R.id.text_articleDate);
            itemView.setOnClickListener(this);
        }

        public TextView getArticleTitle() {
            return mArticleTitle;
        }

        public TextView getArticleSubTitle() {
            return mArticleSubTitle;
        }

        public TextView getArticleDate() {
            return mArticleDate;
        }

        @Override
        public void onClick(View view) {
            sOnItemClickListener.onItemClick(view,getLayoutPosition());
        }
    }


}
