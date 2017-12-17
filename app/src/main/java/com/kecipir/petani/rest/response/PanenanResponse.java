package com.kecipir.petani.rest.response;

/**
 * Created by UMAR on 7/27/2017.
 */
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import java.net.URI;
import java.util.List;

public class PanenanResponse implements Parcelable {
        private String id_petani_barang;
        private String grade;
        private String nama_barang;
        private String harga_beli;
        private String qty_awal;
        private String qty_dipesan;
        private String satuan_barang;
        ///private String ket_tambahan_panenan;
        private String foto;
        //private int thumbnail;

    public PanenanResponse() {}

    protected PanenanResponse(Parcel in) {
        id_petani_barang = in.readString();
        grade = in.readString();
        nama_barang = in.readString();
        harga_beli = in.readString();
        qty_awal = in.readString();
        qty_dipesan = in.readString();
        satuan_barang = in.readString();
        foto = in.readString();
    }

    public static final Creator<PiutangResponse> CREATOR = new Creator<PiutangResponse>() {
        @Override
        public PiutangResponse createFromParcel(Parcel in) {
            return new PiutangResponse(in);
        }

        @Override
        public PiutangResponse[] newArray(int size) {
            return new PiutangResponse[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id_petani_barang);
        dest.writeString(grade);
        dest.writeString(nama_barang);
        dest.writeString(harga_beli);
        dest.writeString(qty_awal);
        dest.writeString(qty_dipesan);
        dest.writeString(satuan_barang);
        dest.writeString(foto);
    }

        public String getPanenangrades() {
            return grade;
        }

        public String getPanenanItemPanenans() {
            return nama_barang;
        }

        public String getPanenanPricess() {return harga_beli;}

        public String getPanenanQuantitys() {return qty_awal;}

        public String getPesananQuantitys() {return qty_dipesan;}

        public String getpetaniBarangId() {
            return id_petani_barang;
        }

        public String getPanenanSatuans() {return satuan_barang;}

        //public String getPanenanKetTambahans() {return ket_tambahan_panenan;}

        public String getPanenanThumbnailPath() {return foto;}

        //public int getPanenanThumbnail() {return thumbnail;}
}
