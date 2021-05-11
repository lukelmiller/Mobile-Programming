package edu.csce4623.ahnelson.todomvp3.addedittodoitem;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import edu.csce4623.ahnelson.todomvp3.AlarmReceiver;
import edu.csce4623.ahnelson.todomvp3.R;
import edu.csce4623.ahnelson.todomvp3.data.ToDoItem;

public class AddEditToDoItem extends AppCompatActivity {

    ToDoItem item;
    EditText etTitle;
    EditText etContent;
    EditText date_time_in;
    Button btnSaveItem;
    Button btnDeleteItem;
    CheckBox completeCheckBox;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_to_do_item);

        etTitle = findViewById(R.id.etItemTitle);
        etContent = findViewById(R.id.etItemContent);
        completeCheckBox = findViewById(R.id.completeCheckBox);

        date_time_in = findViewById(R.id.date_time_input);

        date_time_in.setInputType(InputType.TYPE_NULL);

        btnSaveItem = findViewById(R.id.btnSaveToDoItem);
        btnDeleteItem = findViewById(R.id.btnDeleteToDoItem);

        Intent callingIntent = getIntent();
        if(callingIntent.hasExtra("ToDoItem")){
            item = (ToDoItem)callingIntent.getSerializableExtra("ToDoItem");
        }else{
            item = new ToDoItem();
            item.setTitle("Title");
            item.setContent("Content");


        }

        date_time_in.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                showDateTimeDialog(date_time_in);
              //  item.setDueDate(Long.parseLong(date_time_in.getText().toString()));
            }
        });
        if(item.getDueDate()!=0){
            Calendar c = Calendar.getInstance();
            c.setTimeInMillis(item.getDueDate());
            SimpleDateFormat simpleDateFormat=new SimpleDateFormat("MM-dd-yy HH:mm");
            date_time_in.setText(simpleDateFormat.format(c.getTime()));
        }
        else{
            date_time_in.setText("Set Alarm");
        }

        btnSaveItem.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                saveItems();
            }
        });
        btnDeleteItem.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                deleteItem();
            }
        });
        completeCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                item.setCompleted(completeCheckBox.isChecked());
            }
        });


    }
    @Override
    protected void onStart(){
        super.onStart();
        etTitle.setText(item.getTitle());
        etContent.setText(item.getContent());
        completeCheckBox.setChecked(item.getCompleted());

    }

    @Override
    protected void onStop(){
        super.onStop();
        item.setTitle(etTitle.getText().toString());
        item.setContent(etContent.getText().toString());
        item.setCompleted(completeCheckBox.isChecked());
    }

    private void showDateTimeDialog(final EditText date_time_in) {

        final Calendar calendar=Calendar.getInstance();
        DatePickerDialog.OnDateSetListener dateSetListener=new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR,year);
                calendar.set(Calendar.MONTH,month);
                calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);

                TimePickerDialog.OnTimeSetListener timeSetListener=new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        calendar.set(Calendar.HOUR_OF_DAY,hourOfDay);
                        calendar.set(Calendar.MINUTE,minute);

                        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("MM-dd-yy HH:mm");

                        date_time_in.setText(simpleDateFormat.format(calendar.getTime()));
                        item.setDueDate(calendar.getTimeInMillis());
                    }
                };

                new TimePickerDialog(AddEditToDoItem.this,timeSetListener,calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE),false).show();
            }
        };

        new DatePickerDialog(AddEditToDoItem.this,dateSetListener,calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH)).show();

    }

    private void saveItems(){
        item.setTitle(etTitle.getText().toString());
        item.setContent(etContent.getText().toString());
        Intent dataIntent = new Intent();
        dataIntent.putExtra("ToDoItem", item);
        setResult(RESULT_OK,dataIntent);
        Calendar c = Calendar.getInstance();


        if(item.getDueDate() != 0 && item.getDueDate()>c.getTimeInMillis()) {
            AlarmManager alarmManager;
            if (Build.VERSION.SDK_INT >= 23) {
                alarmManager = this.getSystemService(AlarmManager.class);
            } else {
                alarmManager = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
            }
            Intent alarmNotificationIntent = new Intent(this, AlarmReceiver.class);
            Bundle bundle = new Bundle();

            alarmNotificationIntent.putExtra("ToDoItemTitle", item.getTitle());
            alarmNotificationIntent.putExtra("ToDoItemContent", item.getContent());
            alarmNotificationIntent.putExtra("ToDoItemId", item.getId());
            PendingIntent alarmIntent = PendingIntent.getBroadcast(this, 0, alarmNotificationIntent, 0);

            alarmManager.setExact(AlarmManager.RTC_WAKEUP, item.getDueDate(), alarmIntent);
        }


        finish();
    }
    private void deleteItem(){
        Intent dataIntent = new Intent();
        dataIntent.putExtra("ToDoItem", item);
        dataIntent.putExtra("result", 2);
        setResult(RESULT_OK,dataIntent);
        finish();
    }

}