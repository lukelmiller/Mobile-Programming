package edu.csce4623.lukelmiller.goale.goalListActivity;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import static com.google.common.base.Preconditions.checkNotNull;
import android.os.Bundle;
import android.widget.Button;

import java.util.List;

import edu.csce4623.lukelmiller.goale.R;
import edu.csce4623.lukelmiller.goale.data.GoalItem;
import edu.csce4623.lukelmiller.goale.data.GoalItemRepository;
import util.AppExecutors;

public class FullListActivity extends AppCompatActivity{


    private GoalListPresenter goalListPresenter;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_list);
//
        ListFragment goalListFragment = (ListFragment) getSupportFragmentManager().findFragmentById(R.id.fragList);
        if(goalListFragment == null){
            goalListFragment = ListFragment.newInstance();
            checkNotNull(goalListFragment);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

            //transaction.replace(R.id.fragList, goalListFragment).commit();
            transaction.add(R.id.fragList, goalListFragment);
            transaction.commit();
        }
        goalListPresenter = new GoalListPresenter(GoalItemRepository.getInstance(new AppExecutors(),getApplicationContext()),goalListFragment);
//        GoalItemRepository repo = GoalItemRepository.getInstance(new AppExecutors(),getApplicationContext());
//        GoalItem goal = new GoalItem();
//        goal.setTitle("This Test");
//        goal.setCategory(1);
//        goal.setCurrent(10);
//        goal.setNote("Notes");
//        goal.setEnd(100);
//        goal.setStart(0);
//        goal.setId(2);
//        goal.setUnit("Calories");
//        goal.setNote("Goalies");
//        repo.createGoalItem(goal);

//        goalListPresenter.loadGoalItems();
    }

}
