package com.bmvl.lk.models;

public class Samples {
    private int id;
    private int proby_id;
    private int number;
    private double price;

    public Samples(int proby_id, int number) {
        this.proby_id = proby_id;
        this.number = number;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProby_id() {
        return proby_id;
    }

    public void setProby_id(int proby_id) {
        this.proby_id = proby_id;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
