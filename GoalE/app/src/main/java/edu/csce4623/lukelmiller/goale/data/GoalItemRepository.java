package edu.csce4623.lukelmiller.goale.data;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Room;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import util.AppExecutors;

public class GoalItemRepository implements GoalListDataSource {

    private static volatile GoalItemRepository INSTANCE;
    //Thread pool for execution on other threads
    private AppExecutors mAppExecutors;
    //Context
    private Context mContext;
    //Database
    static GoalItemDao goalItemDao;


    //private constructor
    private GoalItemRepository(@NonNull AppExecutors appExecutors, @NonNull Context context){
        mAppExecutors = appExecutors;
        mContext = context;
        goalItemDao = GoalItemDatabase.getInstance(mContext).getGoalItemDao();
//        Runnable runnable = new Runnable() {
//            @Override
//            public void run() {
//                synchronized (GoalItemRepository.class) {
//                    //GoalItemDatabase database = Room.databaseBuilder(mContext, GoalItemDatabase.class, "goal_db").allowMainThreadQueries().build();
//                    goalItemDao = GoalItemDatabase.getInstance(mContext).getGoalItemDao();
//                }
//            }
//        };
//        mAppExecutors.diskIO().execute(runnable);

    }

    //public constructor
    public static GoalItemRepository getInstance(@NonNull AppExecutors appExecutors, @NonNull Context context){
        if(INSTANCE == null){
            synchronized (GoalItemRepository.class){
                if(INSTANCE == null){
                    INSTANCE = new GoalItemRepository(appExecutors, context);
                }
            }
        }
        return INSTANCE;
    }

    @Override
    public void getGoalItems(@NonNull final LoadGoalItemsCallback callback) {

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                final List<GoalItem> items = goalItemDao.getAll();
                mAppExecutors.mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        if(items == null){
                            callback.onDataNotAvailable();
                        } else{
                            callback.onGoalItemsLoaded(items);
                        }
                    }
                });
            }
        };
        mAppExecutors.diskIO().execute(runnable);
//        synchronized (items){
//            return items;
//        }


    }


    @Override
    public void saveGoalItem(@NonNull final GoalItem goalItem) {
        Runnable runnable = new Runnable(){
            @Override
            public void run() {
               synchronized (GoalItemRepository.class){
                   goalItemDao.update(goalItem);
               }
            }
        };
        mAppExecutors.diskIO().execute(runnable);

    }

    @Override
    public void createGoalItem(@NonNull final GoalItem goalItem) {
        Runnable runnable = new Runnable(){
            @Override
            public void run() {
                synchronized (GoalItemRepository.class){
                    goalItemDao.insert(goalItem);
                }
            }
        };
        mAppExecutors.diskIO().execute(runnable);

    }
    public void deleteGoalItem(@NonNull final GoalItem goalItem) {
        Runnable runnable = new Runnable(){
            @Override
            public void run() {
                synchronized (GoalItemRepository.class){
                    goalItemDao.delete(goalItem);
                }
            }
        };
        mAppExecutors.diskIO().execute(runnable);

    }
}
