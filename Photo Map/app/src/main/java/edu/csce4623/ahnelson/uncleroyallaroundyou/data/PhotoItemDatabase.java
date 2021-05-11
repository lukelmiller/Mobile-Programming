package edu.csce4623.ahnelson.uncleroyallaroundyou.data;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {PhotoItem.class}, version = 1, exportSchema = false)
public abstract class PhotoItemDatabase extends RoomDatabase {
    public static final String DATABASE_NAME = "photo_db";
    private static PhotoItemDatabase INSTANCE;

    public static PhotoItemDatabase getInstance(Context context){
        if(INSTANCE == null){
            INSTANCE = Room.databaseBuilder(context,PhotoItemDatabase.class,DATABASE_NAME).build();
        }
        return INSTANCE;
    }

    public abstract PhotoItemDao getPhotoItemDao();

}
