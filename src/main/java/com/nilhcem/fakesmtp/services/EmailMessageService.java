package com.nilhcem.fakesmtp.services;

import com.nilhcem.fakesmtp.model.EmailMessage;

import java.util.List;

/**
 * Created for K and M Consulting LLC.
 * Created by Jose M Leon 2017
 **/
public class EmailMessageService {
    private List<EmailMessage> messages;

    public EmailMessageService(List<EmailMessage> messages) {
        this.messages = messages;
    }

    public List<EmailMessage> getAll() {
        return messages;
    }

    public void clearAllMessages() {
        this.messages.clear();
    }
}
