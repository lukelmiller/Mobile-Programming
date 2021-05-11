package edu.csce4623.lukelmiller.goale.goalListActivity;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ProgressBar;
import java.util.ArrayList;
import java.util.List;
import edu.csce4623.lukelmiller.goale.R;
import edu.csce4623.lukelmiller.goale.addgoalactivity.AddGoalActivity;
import edu.csce4623.lukelmiller.goale.categoryProgress.categoryProgressView;
import edu.csce4623.lukelmiller.goale.data.GoalItem;
import edu.csce4623.lukelmiller.goale.editgoalactivity.EditGoalActivity;
import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static com.google.common.base.Preconditions.checkNotNull;

public class ListFragment extends Fragment implements GoalListContract.View{

    private GoalItemsAdapter goalItemsAdapter;
    private GoalListContract.Presenter presenter;

    public ListFragment(){
        //super(R.layout.fragment_list);

    }

     public static ListFragment newInstance(){
         ListFragment fragment = new ListFragment();
         return fragment;
     }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        goalItemsAdapter = new GoalItemsAdapter(new ArrayList<GoalItem>(0), goalItemsListener);
    }

    @Override
    public void onResume(){
         super.onResume();
         presenter.start();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

         View root = inflater.inflate(R.layout.fragment_list, container, false);
         ListView listView = (ListView) root.findViewById(R.id.list);
         listView.setAdapter(goalItemsAdapter);
         root.findViewById(R.id.btnAddGoal).setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 presenter.addNewGoalItem();
             }
         });

         root.findViewById(R.id.btnAllCategories).setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 showCategoryProgress();
             }
         });
         return root;

    }

    public void setPresenter(GoalListContract.Presenter presenter){
         this.presenter = presenter;
    }

    public void showGoalItems(List<GoalItem> goalItemList){
        if(goalItemList.size() == 0) {
            presenter.addNewGoalItem();
        }
        else {
            goalItemsAdapter.replaceData(goalItemList);
        }
    }

    public void showEditGoalItem(GoalItem item, int requestCode){
        Intent editIntent = new Intent(getActivity(), EditGoalActivity.class);
        editIntent.putExtra("requestCode", requestCode);
        editIntent.putExtra("GoalItem", item);
        startActivityForResult(editIntent, requestCode);
    }

    public void showAddGoalItem(GoalItem item, int requestCode){
        Intent addIntent = new Intent(getActivity(), AddGoalActivity.class);
        addIntent.putExtra("requestCode", requestCode);
        addIntent.putExtra("GoalItem", item);
        startActivityForResult(addIntent, requestCode);
    }

    public void showCategoryProgress(){
        Intent categoryIntent = new Intent(getActivity(),categoryProgressView.class);
        startActivity(categoryIntent);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK){
            if(data!=null){
                //Check to make sure data object has a goalItem
                if(data.hasExtra("result")){
                    presenter.result(2, resultCode,(GoalItem)data.getSerializableExtra("GoalItem") );
                }
                else if(data.hasExtra("GoalItem")) {
                    presenter.result(requestCode, resultCode,(GoalItem)data.getSerializableExtra("GoalItem") );
                }
            }
        }
        else if(resultCode == RESULT_CANCELED){

        }
    }

    GoalItemListener goalItemsListener = new GoalItemListener(){
        @Override
        public void onGoalItemClick(GoalItem clickedGoalItem) {
            Log.d("FRAGMENT","Open GoalItem Details");
            //Grab item from the ListView click and pass to presenter
            presenter.showExistingGoalItem(clickedGoalItem);
        }
    };






    private static class GoalItemsAdapter extends BaseAdapter{
         private List<GoalItem> goalItems;
         private GoalItemListener goalItemListener;

         public GoalItemsAdapter(List<GoalItem> goalItems, GoalItemListener itemListener){
             setList(goalItems);
             goalItemListener = itemListener;
         }

         public void replaceData(List<GoalItem> goalItems){
             setList(goalItems);
             notifyDataSetChanged();
         }

         private void setList(List<GoalItem> goalItems){
             this.goalItems = checkNotNull(goalItems);
         }

         @Override
         public int getCount(){ return goalItems.size(); }

         @Override
         public GoalItem getItem(int id){ return goalItems.get(id); }

         @Override
         public long getItemId(int i){ return i; }

         @Override
         public View getView(int i, View view, ViewGroup viewGroup){
            View rowView = view;
            if (rowView == null) {
                LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
                rowView = inflater.inflate(R.layout.item_layout, viewGroup, false);
            }

            final GoalItem goalItem = getItem(i);

            TextView titleTV = (TextView) rowView.findViewById(R.id.etItemTitle);
            titleTV.setText(goalItem.getTitle());
            ProgressBar progressBar = (ProgressBar) rowView.findViewById(R.id.etItemProgress);
            TextView progressTV = (TextView) rowView.findViewById(R.id.etProgressText);
            int progress = 0;
            if(goalItem.getCurrent() == goalItem.getEnd() && goalItem.getEnd()!=0){
                progress = 100;
            }else if(goalItem.getCurrent()< goalItem.getEnd() && goalItem.getEnd()!=0){
                progress = (int) Math.ceil(goalItem.getCurrent()/goalItem.getEnd()*100);
            }else if(goalItem.getCurrent()< goalItem.getEnd() && goalItem.getEnd()==0){
                progress = (int) Math.ceil(goalItem.getCurrent()/0.00000001*100);
            }else if(goalItem.getEnd() < goalItem.getCurrent() && goalItem.getCurrent()!=0){
                progress = (int) Math.ceil(goalItem.getEnd()/goalItem.getCurrent()*100);
            }else if(goalItem.getEnd() < goalItem.getCurrent() && goalItem.getCurrent()==0){
                progress = (int) Math.ceil(goalItem.getEnd()/0.00000001*100);
            }else{
                progress = 0;
            }

            if(progress>=100){
                progress = 100;
            }
            progressBar.setProgress(progress);
            progressTV.setText(String.valueOf(progress)+"%");

            rowView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    goalItemListener.onGoalItemClick(goalItem);
                }
            });
            return rowView;
         }

    }

    public interface GoalItemListener{
        void onGoalItemClick(GoalItem clickedGoalItem);
    }
}
