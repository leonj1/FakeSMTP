package com.nilhcem.fakesmtp.controllers;


import com.nilhcem.fakesmtp.controllers.routes.health.HealthCheckRoute;

import static spark.Spark.get;

/**
 * Created for K and M Consulting LLC.
 * Created by Jose M Leon 2017
 **/
public class HealthCheckController implements Controller {

    private HealthCheckRoute healthCheckRoute;

    public HealthCheckController(HealthCheckRoute healthCheckRoute) {
        this.healthCheckRoute = healthCheckRoute;
    }

    public void expose() {
        get("/health", this.healthCheckRoute);
    }
}
