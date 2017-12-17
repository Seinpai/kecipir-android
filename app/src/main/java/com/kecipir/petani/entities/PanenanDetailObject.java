package com.kecipir.petani.entities;

import android.graphics.Bitmap;

/**
 * Created by UMAR on 7/1/2017.
 */

public class PanenanDetailObject {
    private String grade;
    private String item_name;
    private String price_panenan;
    private String satuan_panenan;
    private String ket_tambahan_panenan;
    private String satuan_panenan_detail;
    private String thumbnail_path;

    public PanenanDetailObject(String grade, String item_name,String price_panenan, String ket_tambahan_panenan, String satuan_panenan_detail,String thumbnail_path) {
        this.grade = grade;
        this.item_name = item_name;
        this.price_panenan = price_panenan;
        this.satuan_panenan = satuan_panenan;
        this.ket_tambahan_panenan = ket_tambahan_panenan;
        this.satuan_panenan_detail = satuan_panenan_detail;
        this.thumbnail_path = thumbnail_path;
    }

    public String getPanenangrades() {
        return grade;
    }

    public String getPanenanItemPanenans() {
        return item_name;
    }

    public String getPanenanPricess() {return price_panenan;}

    public String getPanenanSatuanDetails() {return satuan_panenan_detail;}

    public String getPanenanKetTambahans() {return ket_tambahan_panenan;}

    public String getPanenanThumbnail() {
        return thumbnail_path;
    }
}
