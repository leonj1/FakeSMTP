package com.nilhcem.fakesmtp.controllers;

import com.nilhcem.fakesmtp.controllers.routes.ClearAllMessagesRoute;
import com.nilhcem.fakesmtp.controllers.routes.GetAllEmailsRoute;

import static spark.Spark.delete;
import static spark.Spark.get;
import static spark.Spark.post;
import static spark.Spark.put;

/**
 * Created for K and M Consulting LLC.
 * Created by Jose M Leon 2017
 **/
public class EmailMessagesController implements Controller {

    private GetAllEmailsRoute getAllEmailsRoute;
    private ClearAllMessagesRoute clearAllMessagesRoute;

    public EmailMessagesController(GetAllEmailsRoute getAllEmailsRoute, ClearAllMessagesRoute clearAllMessagesRoute) {
        this.getAllEmailsRoute = getAllEmailsRoute;
        this.clearAllMessagesRoute = clearAllMessagesRoute;
    }

    public void expose() {
        get("/emails/all", this.getAllEmailsRoute);
        delete("/emails/all", this.clearAllMessagesRoute);
    }
}
