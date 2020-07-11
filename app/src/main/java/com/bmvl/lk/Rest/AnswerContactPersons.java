package com.bmvl.lk.Rest;

import com.bmvl.lk.data.models.ContactPersons;

import java.util.List;

public class AnswerContactPersons extends StandardAnswer {
    private List<ContactPersons> contactPersons;

    public List<ContactPersons> getContactPersons() {
        return contactPersons;
    }
}
