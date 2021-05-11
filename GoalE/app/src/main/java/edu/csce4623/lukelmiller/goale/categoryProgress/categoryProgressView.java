package edu.csce4623.lukelmiller.goale.categoryProgress;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import edu.csce4623.lukelmiller.goale.R;
import edu.csce4623.lukelmiller.goale.data.GoalItem;
import edu.csce4623.lukelmiller.goale.data.GoalItemRepository;
import edu.csce4623.lukelmiller.goale.data.GoalListDataSource;
import util.AppExecutors;

public class categoryProgressView extends AppCompatActivity {

    private List<GoalItem> goalItemList;

    private GoalItemRepository repoDB;
    private List<GoalItem> healthItems;
    private List<GoalItem> financeItems;
    private List<GoalItem> quantityItems;
    private List<GoalItem> qualityItems;
    private int healthProgress;
    private int financeProgress;
    private int qualityProgress;
    private int quantityProgress;
    private TextView tvHealth;
    private TextView tvFinance;
    private TextView tvQuality;
    private TextView tvQuantity;
    private TextView tvOverall;
    private int avgH;
    private int avgF;
    private int avgQuan;
    private int avgQual;
    private boolean healthFlag;
    private boolean finFlag;
    private boolean quantFlag;
    private boolean qualFlag;
    ProgressBar healthProgressBar;
    ProgressBar financeProgressBar;
    ProgressBar qualityProgressBar;
    ProgressBar quantityProgressBar;
    ProgressBar overallProgressBar;


//    //public categoryProgressView() {
//        super(R.layout.category_progress_layout);
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.category_progress_layout);



        healthItems =  new ArrayList<>();
        financeItems =  new ArrayList<>();
        quantityItems =  new ArrayList<>();
        qualityItems =  new ArrayList<>();
        healthProgress = 0;
        financeProgress = 0;
        qualityProgress = 0;
        quantityProgress = 0;
        healthFlag = false;
        finFlag = false;
        quantFlag = false;
        qualFlag = false;
        tvHealth = findViewById(R.id.healthText);
        tvFinance = findViewById(R.id.financialText);
        tvQuality = findViewById(R.id.qualityText);
        tvQuantity = findViewById(R.id.quantityText);
        tvOverall = findViewById(R.id.overallText);
        Button back = findViewById(R.id.returnFromProgress);
        healthProgressBar = (ProgressBar) findViewById(R.id.healthProgress);
        financeProgressBar = (ProgressBar) findViewById(R.id.financialProgress);
        qualityProgressBar = (ProgressBar) findViewById(R.id.qualityProgress);
        quantityProgressBar = (ProgressBar) findViewById(R.id.quantityProgress);
        overallProgressBar = (ProgressBar) findViewById(R.id.overallProgress);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        repoDB = GoalItemRepository.getInstance(new AppExecutors(),this);
        repoDB.getGoalItems(new GoalListDataSource.LoadGoalItemsCallback(){
            @Override
            public void onGoalItemsLoaded(List<GoalItem> goalItems) {
              goalItemList = goalItems;
              loadItem();
            }

            @Override
            public void onDataNotAvailable() {

            }
        } );


    }

    public void loadItem(){
        goalItemList.get(0);

        for(int i =0; i < goalItemList.size(); i++){
            //Category 1 is health
            if(goalItemList.get(i).getCategory() == 1){

                healthItems.add(goalItemList.get(i));
            }
            if(goalItemList.get(i).getCategory() == 2){

                financeItems.add(goalItemList.get(i));
            }
            if(goalItemList.get(i).getCategory() == 3){

                quantityItems.add(goalItemList.get(i));
            }
            if(goalItemList.get(i).getCategory() == 4){

                qualityItems.add(goalItemList.get(i));
            }
        }

        if(healthItems.size()!=0){
            avgH = calcHealthProgress();
            tvHealth.setText(avgH + "%");
            healthProgressBar.setProgress(avgH);

        }
        else {
            tvHealth.setText("--%");
            healthProgressBar.setProgress(0);
        }
        if (financeItems.size()!=0){
           avgF = calcFinanceProgress();
           tvFinance.setText(avgF + "%");
           financeProgressBar.setProgress(avgF);
        }
        else {
            tvFinance.setText("--%");
            financeProgressBar.setProgress(0);
        }
        if(qualityItems.size()!=0){
            avgQual = calcQualityProgress();
            tvQuality.setText(avgQual + "%");
            qualityProgressBar.setProgress(avgQual);
        }
        else {
            tvQuality.setText("--%");
            qualityProgressBar.setProgress(0);
        }
        if(quantityItems.size()!=0){
            avgQuan = calcQuantityProgress();
            tvQuantity.setText(avgQuan + "%");
            quantityProgressBar.setProgress(avgQuan);
        }
        else {
            tvQuantity.setText("--%");
            quantityProgressBar.setProgress(0);

        }
        calOverallProgress();
    }

    private void calOverallProgress() {
        int totalProgress = 0;
        int avgProgress = 0;
        int denom = 0;
        if(qualFlag == true){
            denom++;
            totalProgress += avgQual;
        }
        if(quantFlag == true){
            denom++;
            totalProgress += avgQuan;
        }
        if(healthFlag == true){
            denom++;
            totalProgress += avgH;
        }
        if(finFlag == true){
            denom++;
            totalProgress += avgF;
        }
        if (denom==0){
            tvOverall.setText("--%");
            quantityProgressBar.setProgress(0);
        }
        else{
            avgProgress = totalProgress/denom;
        }
        tvOverall.setText(Integer.toString(avgProgress) + "%");
        overallProgressBar.setProgress(avgProgress);
    }


    private int calcQuantityProgress() {
        quantFlag = true;
        int totalProgress = 0;
        int avgProgress = 0;

        for (int i = 0; i < quantityItems.size(); i++) {
            totalProgress += calcProgress(quantityItems.get(i));
        }
        avgProgress = totalProgress / quantityItems.size();
        return avgProgress;
    }

    private int calcProgress(GoalItem goalItem){
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
        return progress;
    }

    private int calcQualityProgress() {
        qualFlag = true;
        int totalProgress = 0;
        int avgProgress = 0;
        for (int i = 0; i < qualityItems.size(); i++) {
            totalProgress += calcProgress(qualityItems.get(i));
        }
        avgProgress = totalProgress / qualityItems.size();
        return avgProgress;
    }

    private int calcFinanceProgress() {
        finFlag = true;
        int totalProgress = 0;
        int avgProgress = 0;
        for (int i = 0; i < financeItems.size(); i++) {
            totalProgress += calcProgress(financeItems.get(i));
        }
        avgProgress = totalProgress / financeItems.size();
        return avgProgress;
    }

    private int calcHealthProgress() {
        healthFlag = true;
        int totalProgress = 0;
        int avgProgress = 0;

        for (int i = 0; i < healthItems.size(); i++) {
            totalProgress += calcProgress(healthItems.get(i));
        }
        avgProgress = totalProgress / healthItems.size();
        return avgProgress;
    }


}
