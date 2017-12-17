package com.kecipir.petani.rest.response;

/**
 * Created by UMAR on 8/5/2017.
 */

public class NotificationReadResponse {

    private NotificationReadResponse.Data data;

    public NotificationReadResponse.Data getData() {
        return data;
    }

    public static class Data {
        private String message;
        public String getMessage() {
            return message;
        }
    }

}
