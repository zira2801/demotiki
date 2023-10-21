package com.example.demotiki.DanhsachAnhSP;

import android.net.Uri;

import java.io.Serializable;

public class AnhSP implements Serializable {
    Uri resourceId;

    public AnhSP(Uri resourceId) {
        this.resourceId = resourceId;
    }

    public Uri getResourceId() {
        return resourceId;
    }

    public void setResourceId(Uri resourceId) {
        this.resourceId = resourceId;
    }
}
