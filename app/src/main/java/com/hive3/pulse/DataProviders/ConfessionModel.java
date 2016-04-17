package com.hive3.pulse.DataProviders;

import android.os.Parcel;
import android.os.Parcelable;

public class ConfessionModel implements Parcelable {

    String confession, objID,author;
    

    public String getConfession() {
        return confession;
    }

    public void setConfession(String confession) {
        this.confession = confession;
    }



    public String getObjID() {
        return objID;
    }

    public void setObjID(String objID) {
        this.objID = objID;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }


    protected ConfessionModel(Parcel in) {
        String[] array = new String[5];
        in.readStringArray(array);
        confession = array[0];
        objID = array[1];
        author = array[2];
    }

    public ConfessionModel() {

    }

    public final Creator<ConfessionModel> CREATOR = new Creator<ConfessionModel>() {
        @Override
        public ConfessionModel createFromParcel(Parcel in) {
            return new ConfessionModel(in);
        }

        @Override
        public ConfessionModel[] newArray(int size) {
            return new ConfessionModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[]{this.confession, this.objID,this.author});
    }
}