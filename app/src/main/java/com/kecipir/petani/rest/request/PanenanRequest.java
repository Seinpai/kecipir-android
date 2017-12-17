package com.kecipir.petani.rest.request;

/**
 * Created by UMAR on 7/27/2017.
 */

public class PanenanRequest {
    private String tanggal;
    private String kategori;
    private String value;

    private int page;

    public void setPage(int page) {
        this.page = page;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public void setKategori(String kategori) {
        this.kategori = kategori;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
