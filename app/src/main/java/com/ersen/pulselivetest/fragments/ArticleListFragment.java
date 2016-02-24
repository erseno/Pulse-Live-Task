package com.ersen.pulselivetest.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ersen.pulselivetest.R;
import com.ersen.pulselivetest.activities.BaseActivity;
import com.ersen.pulselivetest.application.PulseLiveApplication;
import com.ersen.pulselivetest.constants.PulseLiveConstants;
import com.ersen.pulselivetest.interfaces.ArticleFragmentCallbacks;
import com.ersen.pulselivetest.interfaces.OnItemClickListener;
import com.ersen.pulselivetest.models.article.Article;
import com.ersen.pulselivetest.models.deserializer.Items;
import com.ersen.pulselivetest.network.NetworkErrorHandler;
import com.ersen.pulselivetest.utils.SharedPreferencesUtils;
import com.ersen.pulselivetest.utils.VisibilityManager;
import com.ersen.pulselivetest.views.adapters.ArticleListAdapter;
import com.ersen.pulselivetest.views.widgets.PlaceholderView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ArticleListFragment extends Fragment implements PlaceholderView.OnRetryButtonPressedListener, OnItemClickListener, SwipeRefreshLayout.OnRefreshListener{

    private VisibilityManager mVisibilityManager;
    private ArticleListAdapter mAdapter;
    private RecyclerView mListOfArticles;
    private SwipeRefreshLayout mSwipeContainer;
    private ArticleFragmentCallbacks mArticleFragmentCallbacksListener;

    public static ArticleListFragment newInstance() {
        return new ArticleListFragment();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if(activity instanceof ArticleFragmentCallbacks){
            mArticleFragmentCallbacksListener = (ArticleFragmentCallbacks) activity;
        }else{
            throw new ClassCastException(activity.toString() + " must implement " + ArticleFragmentCallbacks.class.getSimpleName());
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_article_list, container, false);
        mListOfArticles = (RecyclerView)view.findViewById(R.id.recycler_article_list);
        mListOfArticles.setLayoutManager(new LinearLayoutManager(getContext()));
        mListOfArticles.setHasFixedSize(true);
        mSwipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_article_list);
        mSwipeContainer.setOnRefreshListener(this);
        PlaceholderView placeholderView = (PlaceholderView) view.findViewById(R.id.placeholderView);
        placeholderView.setOnRetryButtonPressedListener(this);
        mVisibilityManager = new VisibilityManager(placeholderView,mSwipeContainer);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((BaseActivity)getActivity()).setToolbarTitle(getString(R.string.fragment_articlesList));
        handleInitialContent();
    }

    @Override
    public void onRetryPressed() {
        handleInitialContent();
    }

    @Override
    public void onItemClick(View view, int position) {
        mArticleFragmentCallbacksListener.onArticleSelected(mAdapter.getItem(position));
    }


    @Override /** Swipe to refresh layout so the user can check to see if there are new articles */
    public void onRefresh() {
        getArticles(true);
    }

    private void handleInitialContent(){
        if(mAdapter == null){ //If we are coming back from the back stack, only the view is destroyed but not the members.
            mVisibilityManager.showLoading(getString(R.string.loading_articles));
            getArticles(false);
        }else{
            mListOfArticles.setAdapter(mAdapter);
            mVisibilityManager.showMainContent();
        }
    }

    private void getArticles(final boolean isRefresh){
        Call<Items<Article>> call = PulseLiveApplication.getInstance().getPulseLiveAPI().getArticles();
        call.enqueue(new Callback<Items<Article>>() {
            @Override
            public void onResponse(Response<Items<Article>> response) {
                if (response.isSuccess()) {
                    ArrayList<Article> articles = response.body().getItems();
                    if(!isRefresh){
                        initialiseAdapter(articles);
                    }else{
                       mAdapter.addNewArticles(articles);
                    }
                    saveArticles(articles); //Regardless of refresh, always update the saved articles
                } else { //Server side had a problem
                    if (!loadStoredArticles()) {
                        mVisibilityManager.showFailure(NetworkErrorHandler.getUnsuccessfulRequestMessage(response));
                    }
                }

                if(isRefresh){
                    mSwipeContainer.setRefreshing(false);
                }
            }

            @Override
            public void onFailure(Throwable t) { //Network error on our side
                if (!loadStoredArticles()) {
                    mVisibilityManager.showFailure(NetworkErrorHandler.getFailedRequestMessage(t));
                }

                if(isRefresh){
                    mSwipeContainer.setRefreshing(false);
                }
            }
        });
    }

    private void initialiseAdapter(ArrayList<Article> articles){
        mAdapter = new ArticleListAdapter(articles,this);
        mListOfArticles.setAdapter(mAdapter);
        mVisibilityManager.showMainContent();
    }

    /** If the request to get the articles has failed, try to see if there are articles stored from a previous successful session. If no such data exists give the user an error message.
     * Returns false if no such data was found */
    private boolean loadStoredArticles(){
        String storedArticlesAsJson = SharedPreferencesUtils.getString(PulseLiveConstants.SharedPreferenceKeys.PREF_ARTICLE_LIST_KEY);
        if(storedArticlesAsJson != null){ //The shared preference default return value is null
            Gson gson = new Gson();
            Type articleArrayType = new TypeToken<ArrayList<Article>>() {}.getType();
            ArrayList<Article> articles = gson.fromJson(storedArticlesAsJson, articleArrayType);
            initialiseAdapter(articles);
            return true; //Got data
        }
        return false; //No stored data
    }

    /** Save the list of articles obtained from the call to shared preferences as a JSON string */
    private void saveArticles(ArrayList<Article> articles){
        Gson gson = new Gson();
        SharedPreferencesUtils.store(PulseLiveConstants.SharedPreferenceKeys.PREF_ARTICLE_LIST_KEY, gson.toJson(articles));
    }
}
