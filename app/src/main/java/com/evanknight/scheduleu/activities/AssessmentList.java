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
import com.evanknight.scheduleu.util.RepositoryOperations;
import com.evanknight.scheduleu.entities.Assessment;

import java.util.List;
import java.util.logging.Logger;

public class AssessmentList extends AppCompatActivity{
    private int entityId;
    private final RepositoryOperations repoOps = RepositoryOperations.getInstance();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Logger.getGlobal().config(">>>>>>>>>>>>>>>>>>>>>> INSTANCE=\" " + (repoOps != null ? "RUNNING" : "STOPPED") + ">>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        Logger.getGlobal().config(">>>>>>>>>>>>>>>>>>>>>> INSTANCE=\" " + (repoOps.getAssessmentList() != null ? "RUNNING" : "STOPPED") + ">>>>>>>>>>>>>>>>>>>>>>>>>>>>");

        setContentView(R.layout.widget_assessment_list_no_fab_activity);

        List<Assessment> allItems = repoOps.getAssessmentList();
        final AssessmentAdapter assessmentAdapter = new AssessmentAdapter(this);
        RecyclerView recyclerView = findViewById(R.id.assessmentListRecyclerviewNoFAB);
        recyclerView.setAdapter(assessmentAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        assessmentAdapter.setItemList(allItems);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_nav_assessment_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (android.R.id.home == item.getItemId()){
            this.finish();
            return true;
        }

        if (R.id.termsOption == item.getItemId()){
            Intent intent = new Intent(AssessmentList.this, TermList.class);
            startActivity(intent);
            return true;
        }
        if (R.id.coursesOption == item.getItemId()){
            Intent intent = new Intent(AssessmentList.this, CourseList.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
