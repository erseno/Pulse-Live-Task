package com.ersen.pulselivetest.models.article;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Article implements Parcelable {

    @SerializedName("id")
    private int mId;

    @SerializedName("title")
    private String mTitle;

    @SerializedName("subtitle")
    private String mSubTitle;

    @SerializedName("date")
    private String mDate;

    public Article(Article article){
        this.mId = article.getId();
        this.mTitle = article.getTitle();
        this.mSubTitle = article.getSubTitle();
        this.mDate = article.getDate();
    }

    protected Article(Parcel in) {
        mId = in.readInt();
        mTitle = in.readString();
        mSubTitle = in.readString();
        mDate = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(mId);
        parcel.writeString(mTitle);
        parcel.writeString(mSubTitle);
        parcel.writeString(mDate);
    }

    public static final Creator<Article> CREATOR = new Creator<Article>() {
        @Override
        public Article createFromParcel(Parcel in) {
            return new Article(in);
        }

        @Override
        public Article[] newArray(int size) {
            return new Article[size];
        }
    };

    public int getId() {
        return mId;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getSubTitle() {
        return mSubTitle;
    }

    public String getDate() {
        return mDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Article article = (Article) o;
        return mId == article.mId;
    }

    @Override
    public int hashCode() {
        int result = mId;
        result = 31 * result + (mTitle != null ? mTitle.hashCode() : 0);
        result = 31 * result + (mSubTitle != null ? mSubTitle.hashCode() : 0);
        result = 31 * result + (mDate != null ? mDate.hashCode() : 0);
        return result;
    }
}
