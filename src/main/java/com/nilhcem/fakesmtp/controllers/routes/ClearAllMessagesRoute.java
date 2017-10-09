package com.nilhcem.fakesmtp.controllers.routes;

import com.nilhcem.fakesmtp.services.EmailMessageService;
import spark.Request;
import spark.Response;
import spark.Route;

/**
 * Created for K and M Consulting LLC.
 * Created by Jose M Leon 2017
 **/
public class ClearAllMessagesRoute implements Route {
    private EmailMessageService emailMessageService;

    public ClearAllMessagesRoute(EmailMessageService emailMessageService) {
        this.emailMessageService = emailMessageService;
    }

    @Override
    public Object handle(Request request, Response response) throws Exception {
        this.emailMessageService.clearAllMessages();
        return SimpleExitRoute.builder(response).OK_200().text("done");
    }
}
