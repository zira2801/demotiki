package com.example.demotiki.DanhsachAnhSP;

import android.location.Location;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class AnhSP implements Parcelable {

    private Uri resourceId;

    public AnhSP(Uri resourceId) {
        this.resourceId = resourceId;
    }

    public Uri getResourceId() {
        return resourceId;
    }

    public void setResourceId(Uri resourceId) {
        this.resourceId = resourceId;
    }

    protected AnhSP(Parcel in) {
        resourceId = in.readParcelable(Uri.class.getClassLoader());
    }

    public static final Creator<AnhSP> CREATOR = new Creator<AnhSP>() {
        @Override
        public AnhSP createFromParcel(Parcel in) {
            return new AnhSP(in);
        }

        @Override
        public AnhSP[] newArray(int size) {
            return new AnhSP[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeParcelable(resourceId, i);
    }
}
