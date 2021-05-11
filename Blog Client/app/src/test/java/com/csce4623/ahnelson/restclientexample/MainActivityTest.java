package com.csce4623.ahnelson.restclientexample;

import android.content.Intent;
import android.util.Log;
import android.widget.ListView;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.shadows.ShadowApplication;

import static org.junit.Assert.*;

/**
 * Created by ahnelson on 11/17/2017.
 */
@RunWith(RobolectricTestRunner.class)
public class MainActivityTest {

    @Test
    public void itemClicked() throws Exception {
        MainActivity mainActivity = Robolectric.setupActivity(MainActivity.class);
        mainActivity.startQuery();
        Thread.sleep(1000);
        ListView listView = mainActivity.findViewById(R.id.lvPostList);
        listView.performItemClick(listView.getAdapter().getView(1,null,null),1, listView.getAdapter().getItemId(1));

        Intent expectedIntent = new Intent(mainActivity,PostView.class);
        Intent actual = ShadowApplication.getInstance().getNextStartedActivity();
        System.out.println(""+Boolean.toString(actual==null));
        assertEquals(expectedIntent.getComponent(),actual.getComponent());
    }

}