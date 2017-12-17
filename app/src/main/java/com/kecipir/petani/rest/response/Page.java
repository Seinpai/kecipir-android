package com.kecipir.petani.rest.response;

import java.util.List;

/**
 * Created by Patriot Muslim on 12/07/2017.
 */

public class Page<T> {

    private int perPage;
    private int currentPage;
    private String nextPageUrl;
    private String prevPageUrl;
    private int from;
    private int to;
    private List<T> data;

    public int getPerPage() {
        return perPage;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public String getNextPageUrl() {
        return nextPageUrl;
    }

    public String getPrevPageUrl() {
        return prevPageUrl;
    }

    public int getFrom() {
        return from;
    }

    public int getTo() {
        return to;
    }

    public List<T> getData() {
        return data;
    }
}