package com.evanknight.scheduleu.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.evanknight.scheduleu.R;
import com.evanknight.scheduleu.util.RepositoryOperations;

import java.util.Calendar;
import java.util.logging.Logger;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Logger.getLogger("ScheduleU_MainActivity").info(">>>>>>>>>>>>>>>>>>>>>> Initializing MainActivity View  >>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Logger.getLogger("ScheduleU_MainActivity").info(">>>>>>>>>>>>>>>>>>>>>> Starting Database from onCreate >>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        try {
            RepositoryOperations.getInstance().initializeRepository(getApplication());
        } catch (NullPointerException ex){
            Logger.getLogger("ScheduleU_MainActivity").severe(">>>>>>>>>>>>>>>>>>>>>> Database Initialization Failed >>>>>>>>>>>>>>>>>>>>>>>>>>>>");
            ex.printStackTrace();
        }
        Button bottomNavButtonTerm = findViewById(R.id.navigationButtonTerms);
        Button bottomNavButtonCourse = findViewById(R.id.navigationButtonCourses);
        bottomNavButtonTerm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, TermList.class);
                startActivity(intent);
            }
        });
        bottomNavButtonCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, CourseList.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId()== android.R.id.home){
            this.finish();
            return true;}

        if (item.getItemId()== R.id.termsOption) {
            Intent activityIntent = new Intent(this, TermList.class);
            startActivity(activityIntent);
            return true;
        }
        if (item.getItemId()== R.id.coursesOption) {
            Intent activityIntent = new Intent(this, CourseList.class);
            startActivity(activityIntent);
            return true;
        }
        if (item.getItemId()== R.id.assessmentsOption) {
            Intent activityIntent = new Intent(this, AssessmentList.class);
            startActivity(activityIntent);
            return true;
        }
        if (item.getItemId()==R.id.bigRedButton){
            RepositoryOperations.getInstance().resetDataBase();
            Calendar.getInstance().getTime();
        }
        return super.onOptionsItemSelected(item);
    }
}
// TODO: Fix initial date population to be current date or stay blank until filled in
// TODO: Implement field checking for empty values, and prompt for mandatory completion
// TODO: Fix selecting instructor dialog and add new instructor, will need to include an entry "add new instructor" as part of the instructor spinner, or make a decision dialog and start distinct activity
// TODO: Fix menus for each view.
// TODO: Implement Note sharing
// TODO: Javadocs
// TODO: Implement date checking on app & activity creation, disable add on all and disable delete on all except Terms, mod Term delete requirements to no childs or completed
// TODO: Consider date scan on startup and prompt to mark complete, edit, or delete - add as part of main activity but implement dialog over splash recursive assessments first
