package com.ersen.pulselivetest.network;

import com.ersen.pulselivetest.constants.PulseLiveConstants;
import com.ersen.pulselivetest.models.article.Article;
import com.ersen.pulselivetest.models.article.ArticleDetail;
import com.ersen.pulselivetest.models.deserializer.Item;
import com.ersen.pulselivetest.models.deserializer.Items;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface PulseLiveAPI {

    @GET(PulseLiveConstants.URLConstants.CONTENT_LIST_ENDPOINT)
    Call<Items<Article>> getArticles();

    @GET(PulseLiveConstants.URLConstants.CONTENT_DETAIL_ENDPOINT)
    Call<Item<ArticleDetail>> getArticleDetail(@Path("id") int articleId);
}
