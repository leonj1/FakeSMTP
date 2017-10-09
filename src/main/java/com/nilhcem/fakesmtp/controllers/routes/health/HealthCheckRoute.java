package com.nilhcem.fakesmtp.controllers.routes.health;

import com.nilhcem.fakesmtp.controllers.routes.SimpleExitRoute;
import spark.Request;
import spark.Response;
import spark.Route;

/**
 * Created for K and M Consulting LLC.
 * Created by Jose M Leon 2017
 **/
public class HealthCheckRoute implements Route {
    @Override
    public Object handle(Request request, Response response) throws Exception {
        return SimpleExitRoute.builder(response).OK_200().text("OK");
    }
}
