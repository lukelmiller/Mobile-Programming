package edu.csce4623.lukelmiller.goale.data;

import androidx.annotation.NonNull;

import java.util.List;

public interface GoalListDataSource {
    interface LoadGoalItemsCallback {

        void onGoalItemsLoaded(List<GoalItem> goalItems);

        void onDataNotAvailable();
    }

    void getGoalItems(@NonNull LoadGoalItemsCallback callback);

    void saveGoalItem (@NonNull final GoalItem goalItem);

    void createGoalItem(@NonNull GoalItem goalItem);
}
