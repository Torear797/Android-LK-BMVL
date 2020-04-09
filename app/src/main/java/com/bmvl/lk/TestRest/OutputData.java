package com.bmvl.lk.TestRest;

public class OutputData {
    private String login;
    private String password;
    private String device_id;
    private boolean getToken;

    public OutputData(String login, String password, String device_id) {
        this.login = login;
        this.password = password;
        this.device_id = device_id;
        this.getToken = true;
    }
}
