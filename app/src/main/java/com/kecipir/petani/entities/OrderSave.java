package com.kecipir.petani.entities;

/**
 * Created by UMAR on 9/1/2017.
 */

public class OrderSave {

    private String id_barang_panen;
    private String quantity;

    public OrderSave(String id_barang_panen, String quantity) {
        this.id_barang_panen = id_barang_panen;
        this.quantity = quantity;
    }
    public String getIdBarangPanen() {
        return id_barang_panen;
    }
    public String getQuantitys() {return quantity;}

    public void setQuantitys(String quantity_order) {this.quantity = quantity_order;}
}


