package com.bmvl.lk.Rest.Order;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResearchRest {

    @SerializedName("indicatorId")
    @Expose
    private short indicatorId;

    @SerializedName("indicatorNd")
    @Expose
    private String indicatorNd;

    @SerializedName("indicatorNdId")
    @Expose
    private short indicatorNdId;

    @SerializedName("methodId")
    @Expose
    private short methodId;

    @SerializedName("methodNd")
    @Expose
    private String methodNd;

    @SerializedName("methodNdId")
    @Expose
    private short methodNdId;

    @SerializedName("typeId")
    @Expose
    private short typeId;

    @SerializedName("indicatorVal")
    @Expose
    private String indicatorVal;

    @SerializedName("methodVal")
    @Expose
    private String methodVal;

    @SerializedName("price")
    @Expose
    private double price;

    @SerializedName("id")
    @Expose
    private Short id;

    public ResearchRest(Short id) {
        this.id = id;
    }

    public short getIndicatorId() {
        return indicatorId;
    }

    public String getIndicatorNd() {
        return indicatorNd;
    }

    public short getIndicatorNdId() {
        return indicatorNdId;
    }

    public short getMethodId() {
        return methodId;
    }

    public String getMethodNd() {
        return methodNd;
    }

    public short getMethodNdId() {
        return methodNdId;
    }

    public short getTypeId() {
        return typeId;
    }

    public String getIndicatorVal() {
        return indicatorVal;
    }

    public String getMethodVal() {
        return methodVal;
    }

    public double getPrice() {
        return price;
    }

    public Short getId() {
        return id;
    }
}