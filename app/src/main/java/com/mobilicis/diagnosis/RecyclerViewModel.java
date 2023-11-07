package com.mobilicis.diagnosis;

import android.net.Uri;

public class RecyclerViewModel {

    private String mtest;
    private Uri mImage;
    private boolean mStatus;

    public boolean ismStatus() {
        return mStatus;
    }

    public void setmStatus(boolean mStatus) {
        this.mStatus = mStatus;
    }

    public String getMtest() {
        return mtest;
    }

    public void setMtest(String mtest) {
        this.mtest = mtest;
    }

    public Uri getmImage() {
        return mImage;
    }

    public void setmImage(Uri mImage) {
        this.mImage = mImage;
    }

    public RecyclerViewModel(String mtest, Uri mImage, boolean mStatus) {
        this.mtest = mtest;
        this.mImage = mImage;
        this.mStatus = mStatus;
    }

    public RecyclerViewModel(String mtest, boolean mStatus) {
        this.mtest = mtest;
        this.mStatus = mStatus;
    }
}
