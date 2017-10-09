package com.nilhcem.fakesmtp.controllers.routes;

import com.google.gson.Gson;
import com.nilhcem.fakesmtp.services.EmailMessageService;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.List;

/**
 * Created for K and M Consulting LLC.
 * Created by Jose M Leon 2017
 **/
public class GetAllEmailsRoute implements Route {
    private EmailMessageService emailMessageService;
    private Gson gson;

    public GetAllEmailsRoute(EmailMessageService emailMessageService) {
        this.emailMessageService = emailMessageService;
        this.gson = new Gson();
    }

    private String execute(Response res) {
        return SimpleExitRoute.builder(res).OK_200().json(this.emailMessageService.getAll(), List.class);
    }

    @Override
    public Object handle(Request request, Response response) throws Exception {
        return execute(response);
    }
}
