package com.kecipir.petani.rest.response;

import java.util.List;

/**
 * Created by UMAR on 8/6/2017.
 */

public class KategoriResponse {
    private KategoriResponse.Data data;

    public KategoriResponse.Data getData() {
        return data;
    }

    public static class Data {
        private List<Kategoris> kategoriarr;
        private List<Hari> hariarr;
        //private PanenanResponse.tanggal tanggal;

        public List<Kategoris> getKategoriArr() {
            return kategoriarr;
        }

        public List<Hari> getHariArr() {
            return hariarr;
        }
    }


    public static class Kategoris {
        private String kategori;

        public String getKategori() {
            return kategori;
        }
    }

    public static class Hari {
        private String hari;

        public String getHari() {
            return hari;
        }
    }

}
