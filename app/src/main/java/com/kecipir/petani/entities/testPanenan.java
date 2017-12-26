package com.kecipir.petani.entities;

/**
 * Created by husein on 21/12/17.
 */

public class testPanenan {
    public String idpanen;
    public String quantity;

    public testPanenan(String id, String quantity) {
        this.idpanen = id;
        this.quantity = quantity;
    }

    public String getIdpanen() {
        return idpanen;
    }

    public void setIdpanen(String idpanen) {
        this.idpanen = idpanen;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getQuantity() {
        return quantity;
    }



}
