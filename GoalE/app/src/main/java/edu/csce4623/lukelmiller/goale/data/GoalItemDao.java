package edu.csce4623.lukelmiller.goale.data;

import android.database.Cursor;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import java.util.List;

@Dao
public interface GoalItemDao {

    //Insert an item into database
    @Insert
    long insert(GoalItem item);

    //Updates an item by reference
    @Update
    void update(GoalItem item);

    //Returns the list of Goals
    @Query("SELECT * FROM GoalItem")
    List<GoalItem> getAll();

    //Delete an item by ID
    @Query("DELETE FROM GoalItem WHERE id = :id ")
    int delete(long id);

    //Delete an item by reference
    @Delete
    void delete(GoalItem item);

}
