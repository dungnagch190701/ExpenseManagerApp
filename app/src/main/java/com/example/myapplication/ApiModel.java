package com.example.myapplication;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class ApiModel {
    @SerializedName("userId")
    private String userId;
    @SerializedName("detailList")
    private List<TripEntity> detailList;

    public ApiModel(String userId, List<TripEntity> tripDetails) {
        this.userId = userId;
        this.detailList = tripDetails;
    }

}
