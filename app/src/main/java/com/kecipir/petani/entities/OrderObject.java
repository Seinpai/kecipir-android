package com.kecipir.petani.entities;

/**
 * Created by UMAR on 7/1/2017.
 */

public class OrderObject {
    private String id_barang_panen;
    private String grade_order;
    private String item_order;
    private String price_order;
    private String price_order_real;
    private String quantity_dipesan;
    private String quantity_order;
    private String satuan_order;
    private String ket_tambahan_order;
    private String thumbnail_path;

    public OrderObject(String id_barang_panen,String grade_order, String item_order,String price_order, String quantity_dipesan,String quantity_order, String satuan_order, String ket_tambahan_order, String thumbnail_path, String price_order_real) {
        this.id_barang_panen = id_barang_panen;
        this.grade_order = grade_order;
        this.item_order = item_order;
        this.price_order = price_order;
        this.quantity_dipesan = quantity_dipesan;
        this.quantity_order = quantity_order;
        this.satuan_order = satuan_order;
        this.ket_tambahan_order = ket_tambahan_order;
        this.thumbnail_path = thumbnail_path;
        this.price_order_real = price_order_real;
    }

    public String getOrdergrades() {
        return grade_order;
    }

    public String getOrderItems() {return item_order;}

    public String getIdBarangPanen() {
        return id_barang_panen;
    }
    public String getOrderPricess() {return price_order;}

    public String getOrderPricessReal() {return price_order_real;}

    public String getPesanQuantitys() {return quantity_dipesan;}

    public void setOrderQuantitys(String quantity_order) {this.quantity_order = quantity_order;}

    public String getOrderQuantitys() {return quantity_order;}

    public String getOrderSatuans() {return satuan_order;}

    public String getOrderKetTambahans() {return ket_tambahan_order;}

    public String getPanenanThumbnailPath() {return thumbnail_path;}
}
