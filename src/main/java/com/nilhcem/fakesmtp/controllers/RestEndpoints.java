package com.nilhcem.fakesmtp.controllers;

/**
 * Created for K and M Consulting LLC.
 * Created by Jose M Leon 2017
 **/
public class RestEndpoints {
    private Controller[] controllers;

    public RestEndpoints(Controller[] controllers) {
        this.controllers = controllers;
    }

    public void start() {
        for(Controller c : controllers) {
            c.expose();
        }
    }
}
