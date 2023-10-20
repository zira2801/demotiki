package com.example.demotiki.Slider;

import java.io.Serializable;

public class SliderTin implements Serializable {
    int resourceId;

    public SliderTin(int resourceId) {
        this.resourceId = resourceId;
    }

    public int getResourceId() {
        return resourceId;
    }

    public void setResourceId(int resourceId) {
        this.resourceId = resourceId;
    }
}
