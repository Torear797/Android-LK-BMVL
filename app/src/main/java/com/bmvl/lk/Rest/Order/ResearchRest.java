package com.bmvl.lk.Rest.Order;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ResearchRest implements Serializable {

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

    @SerializedName("typeVal")
    @Expose
    private String typeVal;

    @SerializedName("price")
    @Expose
    private double price;

    @SerializedName("id")
    @Expose
    private Short id;

    public ResearchRest(Short id) {
        this.id = id;
    }

    public void ClearAll() {
        this.price = 0;
        this.methodId = 0;
        this.methodNd = null;
        this.methodNdId = 0;
        this.methodVal = null;
        this.indicatorVal = null;
        this.indicatorId = 0;
        this.indicatorNd = null;
        this.indicatorNdId = 0;
        this.typeId = 0;
        this.typeVal = null;
    }

    public void ClearType() {
        this.typeId = 0;
        this.typeVal = null;
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

    public void setIndicatorId(short indicatorId) {
        this.indicatorId = indicatorId;
    }

    public void setIndicatorNd(String indicatorNd) {
        this.indicatorNd = indicatorNd;
    }

    public void setIndicatorNdId(short indicatorNdId) {
        this.indicatorNdId = indicatorNdId;
    }

    public void setMethodId(short methodId) {
        this.methodId = methodId;
    }

    public void setMethodNd(String methodNd) {
        this.methodNd = methodNd;
    }

    public void setMethodNdId(short methodNdId) {
        this.methodNdId = methodNdId;
    }

    public void setTypeId(short typeId) {
        this.typeId = typeId;
    }

    public void setIndicatorVal(String indicatorVal) {
        this.indicatorVal = indicatorVal;
    }

    public void setMethodVal(String methodVal) {
        this.methodVal = methodVal;
    }

    public void setId(Short id) {
        this.id = id;
    }

    public void setTypeVal(String typeVal) {
        this.typeVal = typeVal;
    }

    public String getTypeVal() {
        return typeVal;
    }
}