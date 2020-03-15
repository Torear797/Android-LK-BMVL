package com.bmvl.lk.models;

public class SamplesResearch {
    private int id;
    private int sample_id;
    private int id_method;
    private int id_material;
    private int id_indicator;
    private int id_method_nd;
    private int id_indicator_nd;
    private int id_tariff;
    private int id_type_research;
    private String protocol;
    private String date_download_protocol;
    private int number;

    public SamplesResearch(int id, int sample_id) {
        this.id = id;
        this.sample_id = sample_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSample_id() {
        return sample_id;
    }

    public void setSample_id(int sample_id) {
        this.sample_id = sample_id;
    }

    public int getId_method() {
        return id_method;
    }

    public void setId_method(int id_method) {
        this.id_method = id_method;
    }

    public int getId_material() {
        return id_material;
    }

    public void setId_material(int id_material) {
        this.id_material = id_material;
    }

    public int getId_indicator() {
        return id_indicator;
    }

    public void setId_indicator(int id_indicator) {
        this.id_indicator = id_indicator;
    }

    public int getId_method_nd() {
        return id_method_nd;
    }

    public void setId_method_nd(int id_method_nd) {
        this.id_method_nd = id_method_nd;
    }

    public int getId_indicator_nd() {
        return id_indicator_nd;
    }

    public void setId_indicator_nd(int id_indicator_nd) {
        this.id_indicator_nd = id_indicator_nd;
    }

    public int getId_tariff() {
        return id_tariff;
    }

    public void setId_tariff(int id_tariff) {
        this.id_tariff = id_tariff;
    }

    public int getId_type_research() {
        return id_type_research;
    }

    public void setId_type_research(int id_type_research) {
        this.id_type_research = id_type_research;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getDate_download_protocol() {
        return date_download_protocol;
    }

    public void setDate_download_protocol(String date_download_protocol) {
        this.date_download_protocol = date_download_protocol;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }
}
