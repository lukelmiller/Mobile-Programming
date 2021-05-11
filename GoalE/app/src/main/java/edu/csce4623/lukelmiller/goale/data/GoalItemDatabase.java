package edu.csce4623.lukelmiller.goale.data;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {GoalItem.class}, version = 1, exportSchema = false)
public abstract class GoalItemDatabase extends RoomDatabase {
    public static final String DATABASE_NAME = "goal_db";
    private static GoalItemDatabase INSTANCE;

    public static GoalItemDatabase getInstance(Context context){
        if(INSTANCE == null){
            INSTANCE = Room.databaseBuilder(context,GoalItemDatabase.class,DATABASE_NAME).build();
        }
        return INSTANCE;
    }

    public abstract GoalItemDao getGoalItemDao();

}
