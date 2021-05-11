package edu.csce4623.ahnelson.uncleroyallaroundyou.data;

import android.content.ContentValues;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import java.io.Serializable;

@Entity
public class PhotoItem implements Serializable{

    //Static strings for the column names usable by other classes
    public static final String PHOTOITEM_ID = "id";
    public static final String PHOTOITEM_URI = "uri";
    public static final String PHOTOITEM_LONGITUDE = "longitude";
    public static final String PHOTOITEM_LATITUDE = "latitude";
    public static final String PHOTOITEM_DATE = "date";


    @PrimaryKey(autoGenerate = true)
    private Integer id;

    @ColumnInfo(name = "uri")
    private String uri;

    @ColumnInfo(name = "longitude")
    private double longitude;

    @ColumnInfo(name = "latitude")
    private double latitude;

    @ColumnInfo(name = "date")
    private long date;

    //Following are getters and setters for all five member variables
    public Integer getId(){
        return id;
    }

    public void setId(Integer id){
        this.id = id;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }
}
