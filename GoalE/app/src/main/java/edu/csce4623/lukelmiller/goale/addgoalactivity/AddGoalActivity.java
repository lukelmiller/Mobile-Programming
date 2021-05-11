package edu.csce4623.lukelmiller.goale.addgoalactivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import edu.csce4623.lukelmiller.goale.R;
import edu.csce4623.lukelmiller.goale.R.layout;
import edu.csce4623.lukelmiller.goale.data.GoalItem;
import edu.csce4623.lukelmiller.goale.editgoalactivity.EditGoalActivity;
import edu.csce4623.lukelmiller.goale.goalListActivity.GoalListContract;

public class AddGoalActivity extends AppCompatActivity {

    GoalItem goal;
    Button btnHealth;
    Button btnFinancial;
    Button btnQuality;
    Button btnQuantity;
    EditText etTitle;
    EditText etStart;
    EditText etCurrent;
    EditText etEnd;
    EditText etNotes;
    GoalListContract.Presenter presenter;

    public AddGoalActivity() {
        super(layout.activity_add_goal);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_add_goal);

        btnHealth = findViewById(R.id.btnHealth);
        btnFinancial = findViewById(R.id.btnFinancial);
        btnQuality = findViewById(R.id.btnQuality);
        btnQuantity = findViewById(R.id.btnQuantity);
        Intent callingIntent = getIntent();

        //Intent EditIntent = new Intent(AddGoalActivity.this, EditGoalActivity.class);

        btnHealth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(callingIntent.hasExtra("GoalItem")){
                    goal = (GoalItem) callingIntent.getSerializableExtra("GoalItem");
                    goal.setCategory(1);
                    Intent editIntent = new Intent(view.getContext(), EditGoalActivity.class);
                    editIntent.putExtra("requestCode", 0);
                    editIntent.putExtra("GoalItem",goal);

                    startActivityForResult(editIntent, 0);
                }
                else{
                    Log.d("error","No Goal Found");
                }
            }
        });
        btnFinancial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(callingIntent.hasExtra("GoalItem")){
                    goal = (GoalItem) callingIntent.getSerializableExtra("GoalItem");
                    goal.setCategory(2);
                    Intent editIntent = new Intent(view.getContext(), EditGoalActivity.class);
                    editIntent.putExtra("requestCode", 0);
                    editIntent.putExtra("GoalItem",goal);
                    startActivityForResult(editIntent, 0);
                }
                else{
                    Log.d("error","No Goal Found");
                }
            }
        });
        btnQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(callingIntent.hasExtra("GoalItem")){
                    goal = (GoalItem) callingIntent.getSerializableExtra("GoalItem");
                    goal.setCategory(3);
                    Intent editIntent = new Intent(view.getContext(), EditGoalActivity.class);
                    editIntent.putExtra("requestCode", 0);
                    editIntent.putExtra("GoalItem",goal);
                    startActivityForResult(editIntent, 0);
                }
                else{
                    Log.d("error","No Goal Found");
                }
            }
        });
        btnQuality.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(callingIntent.hasExtra("GoalItem")){
                    goal = (GoalItem) callingIntent.getSerializableExtra("GoalItem");
                    goal.setCategory(4);
                    Intent editIntent = new Intent(view.getContext(), EditGoalActivity.class);
                    editIntent.putExtra("requestCode", 0);
                    editIntent.putExtra("GoalItem",goal);
                    startActivityForResult(editIntent, 0);
                }
                else{
                    Log.d("error","No Goal Found");
                }
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (data != null) {
                goal = (GoalItem)data.getSerializableExtra("GoalItem");
                Intent end = new Intent();
                end.putExtra("GoalItem", goal);
                setResult(RESULT_OK,end);
            }
        } else if (resultCode == RESULT_CANCELED) {
            Intent end = new Intent();
            setResult(RESULT_CANCELED,end);
        }
        finish();
    }

}

