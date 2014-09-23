package com.kenanpulak.gridimagesearch.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by kenanpulak on 9/22/14.
 */
public class Filter implements Parcelable{

    public String size;
    public String color;
    public String type;
    public String site;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(size);
        out.writeString(color);
        out.writeString(type);
        out.writeString(site);
    }

    public static final Parcelable.Creator<Filter> CREATOR
            = new Parcelable.Creator<Filter>() {
        @Override
        public Filter createFromParcel(Parcel in) {
            return new Filter(in);
        }

        @Override
        public Filter[] newArray(int size) {
            return new Filter[size];
        }
    };

    private Filter(Parcel in) {
        size = in.readString();
        color = in.readString();
        type = in.readString();
        site = in.readString();
    }

    public Filter() {
        //normal actions performed by class, it's still a normal object!
    }
}
