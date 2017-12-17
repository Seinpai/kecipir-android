package com.kecipir.petani.rest.response;

/**
 * Created by Patriot Muslim on 12/07/2017.
 */

import android.os.Parcel;
import android.os.Parcelable;

import org.joda.time.LocalDate;

import java.util.List;

public class NotificationResponse implements Parcelable {

    private String pesan;
    private String tanggal_kirim;
    private int baca;
    private String tanggal_baca;
    private String tanggal_order;

    public NotificationResponse() {

    }

    protected NotificationResponse(Parcel in) {
        pesan = in.readString();
        tanggal_baca = in.readString();
        baca = in.readInt();
       // id = in.readInt();
        tanggal_kirim = in.readString();
        tanggal_order = in.readString();
    }

    public static final Creator<NotificationResponse> CREATOR = new Creator<NotificationResponse>() {
        @Override
        public NotificationResponse createFromParcel(Parcel in) {
            return new NotificationResponse(in);
        }

        @Override
        public NotificationResponse[] newArray(int size) {
            return new NotificationResponse[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(pesan);
        dest.writeString(tanggal_baca);
        dest.writeInt(baca);
        //dest.writeInt(id);
        dest.writeString(tanggal_kirim);
        dest.writeString(tanggal_order);
    }

    public String getMessage() {
        return pesan;
    }

    public String getDeliveryDate() {
        return tanggal_kirim;
    }

    public int isRead() {
        return baca;
    }
   // public int getid() { return id; }

    public String getReadDate() {
        return tanggal_baca;
    }

    public String getOrderDate() {return tanggal_order;}
}