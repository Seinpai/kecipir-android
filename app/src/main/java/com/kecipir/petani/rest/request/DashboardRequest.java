package com.kecipir.petani.rest.request;

/**
 * Created by Patriot Muslim on 12/07/2017.
 */

public class DashboardRequest {

    private String startDate;
    private String endDate;
    private String show;

    public void setStart(String startDate) {
        this.startDate = startDate;
    }

    public void setEnd(String endDate) {
        this.endDate = endDate;
    }

    public void setShow(String show) {
        this.show = show;
    }
}