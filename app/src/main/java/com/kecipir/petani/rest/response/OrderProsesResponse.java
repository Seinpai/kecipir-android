package com.kecipir.petani.rest.response;

/**
 * Created by UMAR on 8/6/2017.
 */

public class OrderProsesResponse {
    private OrderProsesResponse.Data data;

    public OrderProsesResponse.Data getData() {
        return data;
    }

    public static class Data {
        private String message;
        public String getMessage() {
            return message;
        }
    }
}
