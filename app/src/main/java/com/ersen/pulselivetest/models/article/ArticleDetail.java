package com.ersen.pulselivetest.models.article;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class ArticleDetail extends Article implements Parcelable {
    @SerializedName("body")
    private String mBody;

    protected ArticleDetail(Parcel in) {
        super(in);
        mBody = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(mBody);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ArticleDetail> CREATOR = new Creator<ArticleDetail>() {
        @Override
        public ArticleDetail createFromParcel(Parcel in) {
            return new ArticleDetail(in);
        }

        @Override
        public ArticleDetail[] newArray(int size) {
            return new ArticleDetail[size];
        }
    };

    public String getBody() {
        return mBody;
    }
}
