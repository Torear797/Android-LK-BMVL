package com.bmvl.lk.Rest;

import java.util.Map;

public class AnswerNotifySettings extends StandardAnswer{

    private Map<String, Map<String, Byte>> userNotifiactions;

    public Map<String, Map<String, Byte>> getUserNotifiactions() {
        return userNotifiactions;
    }
}
