package com.ersen.pulselivetest.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ersen.pulselivetest.R;
import com.ersen.pulselivetest.activities.BaseActivity;
import com.ersen.pulselivetest.application.PulseLiveApplication;
import com.ersen.pulselivetest.models.article.Article;
import com.ersen.pulselivetest.models.article.ArticleDetail;
import com.ersen.pulselivetest.models.deserializer.Item;
import com.ersen.pulselivetest.network.NetworkErrorHandler;
import com.ersen.pulselivetest.utils.SharedPreferencesUtils;
import com.ersen.pulselivetest.utils.VisibilityManager;
import com.ersen.pulselivetest.views.widgets.PlaceholderView;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ArticleDetailFragment extends Fragment implements PlaceholderView.OnRetryButtonPressedListener {
    private static final String ARGUMENT_ARTICLE_OBJECT = "articleObject";
    private Article mArticle;
    private ArticleDetail mArticleDetail;
    private VisibilityManager mVisibilityManager;

    public static ArticleDetailFragment newInstance(@NonNull Article article){
        ArticleDetailFragment articleDetailFragment = new ArticleDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARGUMENT_ARTICLE_OBJECT,article);
        articleDetailFragment.setArguments(args);
        return articleDetailFragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mArticle = getArguments().getParcelable(ARGUMENT_ARTICLE_OBJECT);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_article_detail,container,false);
        PlaceholderView placeholderView = (PlaceholderView) view.findViewById(R.id.placeholderView);
        placeholderView.setOnRetryButtonPressedListener(this);
        mVisibilityManager = new VisibilityManager(placeholderView,view.findViewById(R.id.scroll_main_content));
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((BaseActivity) getActivity()).setToolbarTitle(mArticle.getTitle());
        handleInitialContent();
    }

    @Override
    public void onRetryPressed() {
        handleInitialContent();
    }

    private void handleInitialContent(){
        if(mArticleDetail == null){
            mVisibilityManager.showLoading(getString(R.string.loading_article));
            getArticleDetail();
        }else{
            updateUi();
        }
    }

    private void getArticleDetail(){
        Call<Item<ArticleDetail>> call = PulseLiveApplication.getInstance().getPulseLiveAPI().getArticleDetail(mArticle.getId());
        call.enqueue(new Callback<Item<ArticleDetail>>() {
            @Override
            public void onResponse(Response<Item<ArticleDetail>> response) {
                if(response.isSuccess()){
                    mArticleDetail = response.body().getItem();
                    updateUi();
                    saveArticle(mArticleDetail);
                }else{
                    if (!loadStoredArticle()) {
                        mVisibilityManager.showFailure(NetworkErrorHandler.getUnsuccessfulRequestMessage(response));
                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {
                if (!loadStoredArticle()) {
                    mVisibilityManager.showFailure(NetworkErrorHandler.getFailedRequestMessage(t));
                }
            }
        });
    }

    private void updateUi(){
        View view = getView();
        if(view != null){
            TextView articleSubTitle = (TextView)view.findViewById(R.id.text_articleSubTitle);
            TextView articleDate = (TextView)view.findViewById(R.id.text_articleDate);
            TextView articleBody = (TextView)view.findViewById(R.id.text_articleBody);
            articleSubTitle.setText(mArticleDetail.getSubTitle());
            articleDate.setText(mArticleDetail.getDate());
            articleBody.setText(mArticleDetail.getBody());
        }
        mVisibilityManager.showMainContent();
    }


    /** If the request to get the article detail has failed, try to see if the same article was stored from a previous successful session. If no such data exists give the user an error message.
     * Returns false if no such data was found */
    private boolean loadStoredArticle(){
        String storedArticleAsJson = SharedPreferencesUtils.getString(String.valueOf(mArticle.getId()));
        if(storedArticleAsJson != null){
            Gson gson = new Gson();
            mArticleDetail  = gson.fromJson(storedArticleAsJson, ArticleDetail.class);
            updateUi();
            return true;
        }
        return false;
    }

    /** Save the viewed article to shared preferences.Note that the key will be the article's unique ID */
    private void saveArticle(ArticleDetail articleDetail){
        Gson gson = new Gson();
        SharedPreferencesUtils.store(String.valueOf(articleDetail.getId()), gson.toJson(articleDetail));
    }

}
