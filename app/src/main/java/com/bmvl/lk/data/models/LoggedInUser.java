package com.bmvl.lk.data.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */
public class LoggedInUser {

    @SerializedName("fio")
    @Expose
    private String FIO;

    @SerializedName("email")
    @Expose
    private String Email;

    @SerializedName("phone")
    @Expose
    private String Phone;

    @SerializedName("address")
    @Expose
    private String Adress;

    @SerializedName("clientName")
    @Expose
    private String clientName;

    @SerializedName("clientFullName")
    @Expose
    private String clientFullName;

    @SerializedName("inn")
    @Expose
    private String inn;

    @SerializedName("position")
    @Expose
    private String position;

    @SerializedName("basis")
    @Expose
    private byte basis;

    @SerializedName("bank_details")
    @Expose
    private String bank_details;

    @SerializedName("contract_number")
    @Expose
    private String Contract_number;

    @SerializedName("contract_date")
    @Expose
    private String Contract_date;

    private String Contract_term;
    private String Termless_contract;


    public LoggedInUser(String FIO, String email, String phone, String adress, String inn, String bank_details, String contract_number, String contract_date, String contract_term, String termless_contract) {
        this.FIO = FIO;
        Email = email;
        Phone = phone;
        Adress = adress;
        this.inn = inn;
        this.bank_details = bank_details;
        Contract_number = contract_number;
        Contract_date = contract_date;
        Contract_term = contract_term;
        Termless_contract = termless_contract;
    }

    public String getFIO() {
        return FIO;
    }

    public String getEmail() {
        return Email;
    }

    public String getPhone() {
        return Phone;
    }

    public String getAdress() {
        return Adress;
    }

    public String getInn() {
        return inn;
    }

    public String getBank_details() {
        return bank_details;
    }

    public String getContract_number() {
        return Contract_number;
    }

    public String getContract_date() {
        return Contract_date;
    }

    public String getContract_term() {
        return Contract_term;
    }

    public String getTermless_contract() {
        return Termless_contract;
    }

    public String getClientName() {
        return clientName;
    }

    public String getClientFullName() {
        return clientFullName;
    }

    public String getPosition() {
        return position;
    }

    public byte getBasis() {
        return basis;
    }
}
