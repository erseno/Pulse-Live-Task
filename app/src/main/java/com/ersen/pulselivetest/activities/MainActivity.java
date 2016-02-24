package com.ersen.pulselivetest.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.view.MenuItem;

import com.ersen.pulselivetest.R;
import com.ersen.pulselivetest.fragments.ArticleDetailFragment;
import com.ersen.pulselivetest.fragments.ArticleListFragment;
import com.ersen.pulselivetest.interfaces.ArticleFragmentCallbacks;
import com.ersen.pulselivetest.models.article.Article;

public class MainActivity extends BaseActivity implements FragmentManager.OnBackStackChangedListener, ArticleFragmentCallbacks{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportFragmentManager().addOnBackStackChangedListener(this);
        showArticleListFragment();
    }

    @Override
    public void onBackStackChanged() {
        displayHomeAsUpEnabled(getSupportFragmentManager().getBackStackEntryCount() != 0);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getSupportFragmentManager().popBackStack();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showArticleListFragment(){
        replaceFragment(ArticleListFragment.newInstance(),R.id.fragmentContainer,false,ArticleListFragment.class.getSimpleName(),true);
    }

    private void showArticleDetailFragment(@NonNull Article article){
        replaceFragment(ArticleDetailFragment.newInstance(article),R.id.fragmentContainer,true,ArticleDetailFragment.class.getSimpleName(),true);
    }


    @Override
    public void onArticleSelected(Article article) {
        showArticleDetailFragment(article);
    }
}
