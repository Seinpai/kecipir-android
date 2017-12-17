package com.kecipir.petani.rest.response;

/**
 * Created by UMAR on 7/26/2017.
 */

public class PanenanProsesResponse{

    private PanenanProsesResponse.Data data;

    public PanenanProsesResponse.Data getData() {
        return data;
    }

    public static class Data {
        private String message;
        public String getMessage() {
            return message;
        }
    }
}
