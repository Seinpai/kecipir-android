package com.kecipir.petani.rest.response;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by UMAR on 7/26/2017.
 */

public class HistoryResponse implements Parcelable{

    private String hari;
    private String tanggal_pembayaran;
    private String nominal;

    public HistoryResponse() {}

    protected HistoryResponse(Parcel in) {
        hari = in.readString();
        tanggal_pembayaran = in.readString();
        nominal = in.readString();
    }

    public static final Parcelable.Creator<HistoryResponse> CREATOR = new Parcelable.Creator<HistoryResponse>() {
        @Override
        public HistoryResponse createFromParcel(Parcel in) {
            return new HistoryResponse(in);
        }

        @Override
        public HistoryResponse[] newArray(int size) {
            return new HistoryResponse[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }


    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(hari);
        dest.writeString(tanggal_pembayaran);
        dest.writeString(nominal);
    }

    public String getHari() {
        return hari;
    }

    public String getTanggalPembayaran() {
        return tanggal_pembayaran;
    }

    public String getNominal() {
        return nominal;
    }
}
