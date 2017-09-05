package com.example.standard.popularmoviestagetwoseggio.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by vince on 02.09.2017.
 */

public class Movie implements Parcelable {
    private String mPoster, mTitle, mOverview, mDate, mRating, mId, mAuthor, mContent, mKey, mTrailer;

    public Movie(String mPoster) {
        this.mPoster = mPoster;
    }

    public Movie(String mPoster, String mTitle, String mOverview, String mDate, String mRating,
                 String mId, String mAuthor, String mContent, String mKey, String mTrailer) {
        this.mPoster = mPoster;
        this.mTitle = mTitle;
        this.mOverview = mOverview;
        this.mDate = mDate;
        this.mRating = mRating;
        this.mId = mId;
        this.mAuthor = mAuthor;
        this.mContent = mContent;
        this.mKey = mKey;
        this.mTrailer = mTrailer;
    }

    public Movie(Parcel parcel) {
        this.mPoster = parcel.readString();
        this.mTitle = parcel.readString();
        this.mOverview = parcel.readString();
        this.mDate = parcel.readString();
        this.mRating = parcel.readString();
        this.mId = parcel.readString();
        this.mAuthor = parcel.readString();
        this.mContent = parcel.readString();
        this.mKey = parcel.readString();
        this.mTrailer = parcel.readString();
    }

    public Movie (){

    }

    public String getmPoster() {
        return mPoster;
    }

    public String getmTitle() {
        return mTitle;
    }

    public String getmOverview() {
        return mOverview;
    }

    public String getmDate() {
        return mDate;
    }

    public String getmRating() {
        return mRating;
    }

    public String getmId() {
        return mId;
    }

    public String getmAuthor() {
        return mAuthor;
    }

    public String getmContent() {
        return mContent;
    }

    public String getmKey() {
        return mKey;
    }

    public String getmTrailer() {
        return mTrailer;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        /*
        * private String mPoster, mTitle, mOverview, mDate;
          private double mRating;
        */
        parcel.writeString(mPoster);
        parcel.writeString(mTitle);
        parcel.writeString(mOverview);
        parcel.writeString(mDate);
        parcel.writeString(mRating);
        parcel.writeString(mId);
        parcel.writeString(mAuthor);
        parcel.writeString(mContent);
        parcel.writeString(mKey);
        parcel.writeString(mTrailer);
    }

    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {

        @Override
        public Movie createFromParcel(Parcel source) {
            return new Movie(source);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
}
