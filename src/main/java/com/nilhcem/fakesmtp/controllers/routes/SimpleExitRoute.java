package com.nilhcem.fakesmtp.controllers.routes;

import com.google.gson.Gson;
import org.eclipse.jetty.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Response;

import java.lang.reflect.Type;

/**
 * Created for K and M Consulting LLC.
 * Created by Jose M Leon 2017
 **/
public class SimpleExitRoute {

    private SimpleExitRoute() {
    }

    public static HttpStatusStep builder(Response response) {
        return new Builder(response);
    }

    public interface HttpStatusStep {
        JsonStep OK_200();
        JsonStep CREATED_201();
        JsonStep BAD_REQUEST_400();
        JsonStep UNAUTHORIZED_401();
        JsonStep FORBIDDEN_403();
        JsonStep NOT_FOUND_404();
        JsonStep INTERNAL_SERVER_ERROR_500();
        JsonStep customHttpStatus(int status);
    }

    public interface JsonStep {
        String json(Object obj, Type typeOfSrc);
        String text(String message);
        String text(String message, Exception e);
    }

    public static class Builder implements HttpStatusStep, JsonStep {
        private static final Logger log = LoggerFactory.getLogger(Builder.class);
        private int httpStatus;
        private Response response;
        private Gson gson;

        private Builder(Response response) {
            this.response = response;
            this.gson = new Gson();
        }

        @Override
        public JsonStep OK_200() {
            this.httpStatus = HttpStatus.OK_200;
            return this;
        }

        @Override
        public JsonStep CREATED_201() {
            this.httpStatus = HttpStatus.CREATED_201;
            return this;
        }

        @Override
        public JsonStep BAD_REQUEST_400() {
            this.httpStatus = HttpStatus.BAD_REQUEST_400;
            return this;
        }

        @Override
        public JsonStep UNAUTHORIZED_401() {
            this.httpStatus = HttpStatus.UNAUTHORIZED_401;
            return this;
        }

        @Override
        public JsonStep FORBIDDEN_403() {
            this.httpStatus = HttpStatus.FORBIDDEN_403;
            return this;
        }

        @Override
        public JsonStep NOT_FOUND_404() {
            this.httpStatus = HttpStatus.NOT_FOUND_404;
            return this;
        }

        @Override
        public JsonStep INTERNAL_SERVER_ERROR_500() {
            this.httpStatus = HttpStatus.INTERNAL_SERVER_ERROR_500;
            return this;
        }

        @Override
        public JsonStep customHttpStatus(int status) {
            this.httpStatus = status;
            return this;
        }

        @Override
        public String json(Object obj, Type typeOfSrc) {
            this.response.status(this.httpStatus);
            return this.gson.toJson(obj, typeOfSrc);
        }

        @Override
        public String text(String message) {
            this.response.status(this.httpStatus);
            return message;
        }

        @Override
        public String text(String message, Exception e) {
            this.response.status(this.httpStatus);
            log.error(message, e);
            return message;
        }
    }
}
