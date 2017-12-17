package com.kecipir.petani.rest.request;

/**
 * Created by UMAR on 9/1/2017.
 */
import com.kecipir.petani.entities.OrderSave;

import java.util.List;

public class OrderSaveRequest {
    private List<OrderSave> dataorder;

    public void setSchedules(List<OrderSave> dataorder) {
        this.dataorder = dataorder;
    }
}
