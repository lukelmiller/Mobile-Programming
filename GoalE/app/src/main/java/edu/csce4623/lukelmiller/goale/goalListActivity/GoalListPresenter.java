package edu.csce4623.lukelmiller.goale.goalListActivity;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import androidx.annotation.NonNull;
import java.util.List;

import edu.csce4623.lukelmiller.goale.addgoalactivity.AddGoalActivity;
import edu.csce4623.lukelmiller.goale.data.GoalItem;
import edu.csce4623.lukelmiller.goale.data.GoalItemRepository;
import edu.csce4623.lukelmiller.goale.data.GoalListDataSource;

public class GoalListPresenter implements GoalListContract.Presenter{

    private static GoalItemRepository goalItemRepository;
    private final GoalListContract.View goalItemView;
    private AddGoalActivity addGoal;
//    private static ListFragment frag;
    private static final int CREATE_GOAL_REQUEST = 0;
    private static final int UPDATE_GOAL_REQUEST = 1;
    private static final int DELETE_GOAL_REQUEST = 2;

    public GoalListPresenter(@NonNull GoalItemRepository goalItemRepository, @NonNull GoalListContract.View goalItemView){
        this.goalItemRepository = goalItemRepository;
        this.goalItemView = goalItemView;
        this.addGoal = addGoal;
        goalItemView.setPresenter(this);
    }

    public void start(){
        loadGoalItems();
    }

    public void addNewGoalItem(){
        GoalItem goal = new GoalItem();
        goal.setTitle("Title");
        goal.setCategory(1);
        goal.setCurrent(0);
        goal.setNote("Notes");
        goal.setEnd(0);
        goal.setStart(0);
        goal.setUnit("");
        goalItemView.showAddGoalItem(goal,CREATE_GOAL_REQUEST);
    }


    public void showExistingGoalItem(GoalItem item){
        goalItemView.showEditGoalItem(item,UPDATE_GOAL_REQUEST);
    }

    public void result(int requestCode, int resultCode, GoalItem goal){
        if(resultCode == Activity.RESULT_OK){
            if(requestCode == CREATE_GOAL_REQUEST){
                createGoalItem(goal);
            }else if(requestCode == UPDATE_GOAL_REQUEST){
                updateGoalItem(goal);
            }else if(requestCode== DELETE_GOAL_REQUEST){
                deleteGoalItem(goal);
            }
            else{
                Log.e("GoalPresenter", "No such request! PANIC");
            }
        }
    }

    private void createGoalItem(GoalItem item){
        goalItemRepository.createGoalItem(item);
    }

    @Override
    public void updateGoalItem(GoalItem item){
        goalItemRepository.saveGoalItem(item);
    }

    public void loadGoalItems(){

        List<GoalItem> goalItems;
        goalItemRepository.getGoalItems(new GoalListDataSource.LoadGoalItemsCallback() {
            @Override
            public void onGoalItemsLoaded(List<GoalItem> goalItems) {
               /* if(goalItems.size() == 0) {
                    GoalItem temp = new GoalItem();
                    temp.setId(-1);
                    temp.setTitle("Test");
                    temp.setCategory(1);
                    temp.setCurrent(0);
                    temp.setNote("testing testing");
                    temp.setEnd(0);
                    temp.setStart(0);
                    temp.setId(-1);
                    temp.setUnit("Watts");
                    goalItems.add(temp);
                }*/

                goalItemView.showGoalItems(goalItems);
            }

            @Override
            public void onDataNotAvailable() {

            }
        });

    }

    public void deleteGoalItem(GoalItem item){
        goalItemRepository.deleteGoalItem(item);
    }


}
