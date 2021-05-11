package edu.csce4623.lukelmiller.goale.goalListActivity;

import java.util.List;

import edu.csce4623.lukelmiller.goale.data.GoalItem;

public interface GoalListContract {
    interface View{

        void setPresenter(GoalListContract.Presenter presenter);


        void showGoalItems(List<GoalItem> goalItemList);


        void showEditGoalItem(GoalItem item, int requestCode);

        void showAddGoalItem(GoalItem item, int requestCode);

    }

    interface Presenter{

        void loadGoalItems();


        void start();


        void addNewGoalItem();


        void showExistingGoalItem(GoalItem item);


        void updateGoalItem(GoalItem item);


        void result(int requestCode, int resultCode, GoalItem item);
    }
}
