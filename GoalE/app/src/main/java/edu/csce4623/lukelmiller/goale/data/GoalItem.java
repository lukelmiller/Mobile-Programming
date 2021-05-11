package edu.csce4623.lukelmiller.goale.data;

import android.content.ContentValues;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import java.io.Serializable;

@Entity
public class GoalItem implements Serializable{

    //Static strings for the column names usable by other classes
    public static final String GOALITEM_ID = "id";
    public static final String GOALITEM_TITLE = "title";
    public static final String GOALITEM_CATEGORY = "category";
    public static final String GOALITEM_UNIT = "unit";
    public static final String GOALITEM_START = "start";
    public static final String GOALITEM_CURRENT = "current";
    public static final String GOALITEM_END = "end";
    public static final String GOALITEM_NOTE = "note";

    @PrimaryKey(autoGenerate = true)
    private Integer id;

    @ColumnInfo(name = "title")
    private String title;

    @ColumnInfo(name = "category")
    private Integer category;

    @ColumnInfo(name = "unit")
    private String unit;

    @ColumnInfo(name = "start")
    private float start;

    @ColumnInfo(name = "current")
    private float current;

    @ColumnInfo(name = "end")
    private float end;

    @ColumnInfo(name = "note")
    private String note;


    //Getter/Setters/ For Variable of Item
    public Integer getId(){
        return id;
    }

    public void setId(Integer id){
        this.id = id;
    }

    public String getTitle() { return title; }

    public void setTitle(String title) { this.title = title; }

    public Integer getCategory() { return category; }

    public void setCategory(Integer category) { this.category = category; }

    public String getUnit() { return unit; }

    public void setUnit(String unit) { this.unit = unit; }

    public float getStart() { return start; }

    public void setStart(float start) { this.start = start; }

    public float getCurrent() { return current; }

    public void setCurrent(float current) { this.current = current; }

    public float getEnd() { return end; }

    public void setEnd(float end) { this.end = end; }

    public String getNote() { return note; }

    public void setNote(String note) { this.note = note; }

}
