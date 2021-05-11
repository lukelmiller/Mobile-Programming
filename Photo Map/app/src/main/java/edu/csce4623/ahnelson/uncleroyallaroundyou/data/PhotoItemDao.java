package edu.csce4623.ahnelson.uncleroyallaroundyou.data;
import android.database.Cursor;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface PhotoItemDao {
    @Insert
    long insert(PhotoItem item);    /**
     * select all todoitems
     * @return A {@link Cursor} of all todoitems in the table
     */
    @Query("SELECT * FROM PhotoItem")
    List<PhotoItem> getAll();      /**

     * Delete a todoitem by ID
     * @return A number of todoitems deleted
     */
    @Query("DELETE FROM PhotoItem WHERE id = :id ")
    int delete(long id);    /**
     * Update the todoitem
     * @return A number of todoitems updated
     */
    @Delete
    void delete(PhotoItem item);
    @Update
    int update(PhotoItem item);
}