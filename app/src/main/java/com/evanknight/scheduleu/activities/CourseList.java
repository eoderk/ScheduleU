package com.evanknight.scheduleu.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.evanknight.scheduleu.R;
import com.evanknight.scheduleu.entities.Course;
import com.evanknight.scheduleu.util.RepositoryOperations;

import java.util.List;
import java.util.logging.Logger;

public class CourseList extends AppCompatActivity {
    RepositoryOperations repoOps = RepositoryOperations.getInstance();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Logger.getGlobal().config(">>>>>>>>>>>>>>>>>>>>>> INSTANCE=\" " + (repoOps != null ? "RUNNING" : "STOPPED") + ">>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        Logger.getGlobal().config(">>>>>>>>>>>>>>>>>>>>>> INSTANCE=\" " + (repoOps.getCourseList() != null ? "RUNNING" : "STOPPED") + ">>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        setContentView(R.layout.widget_course_list_no_fab_activity);

//        FloatingActionButton fab = findViewById(R.id.courseListFloatingActionButton);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                try {
//                    Logger.getGlobal().severe("**************** Course List OnClick: Starting Click Event **********************");
//                    Intent intent = new Intent(CourseList.this, AddItemActivity.class);
//                    Logger.getGlobal().severe("**************** Course List OnClick: New Intent Defined **********************");
//                    intent.putExtra(TYPE_ID_KEY, COURSE_ENTITY);
//                    Logger.getGlobal().severe("**************** Course List OnClick: Extra Inserted **********************");
//                    startActivity(intent);
//                    Toast.makeText(CourseList.this, "This button works fine", Toast.LENGTH_SHORT).show();
//                } catch (Exception ex) {
//                    Logger.getGlobal().severe("**************** Course List OnClick Error: This button does not work fine **********************");
//                }
//            }
//        });
        List<Course> allItems = repoOps.getCourseList();
        final CourseAdapter courseAdapter = new CourseAdapter(this);
        RecyclerView recyclerView = findViewById(R.id.courseListRecyclerviewNoFAB);
        recyclerView.setAdapter(courseAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        courseAdapter.setItemList(allItems);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_nav_course_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (android.R.id.home == item.getItemId()){
            this.finish();
            return true;
        }

        if (R.id.termsOption == item.getItemId()){
            Intent intent = new Intent(CourseList.this, TermList.class);
            startActivity(intent);
            return true;
        }
        if (R.id.assessmentsOption == item.getItemId()){
            Intent intent = new Intent(CourseList.this, AssessmentList.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
