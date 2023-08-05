package com.evanknight.scheduleu.activities;

import static com.evanknight.scheduleu.util.EntityTypeID.*;
import static com.evanknight.scheduleu.util.Constants.*;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.evanknight.scheduleu.R;
import com.evanknight.scheduleu.entities.SUObjectList;
import com.evanknight.scheduleu.entities.Term;
import com.evanknight.scheduleu.util.RepositoryOperations;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class TermList extends AppCompatActivity implements SUObjectList {
    RepositoryOperations repoOps = RepositoryOperations.getInstance();
    ActivityResultLauncher<Intent> addNewCourseActivityResultLauncher;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.widget_term_list_activity);

        addNewCourseActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (Activity.RESULT_OK == result.getResultCode()){
                            TermList.this.recreate();
                        }
                    }
                });

        FloatingActionButton fab = findViewById(R.id.termsListFloatingActionButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TermList.this, AddItemActivity.class);
                intent.putExtra(TYPE_ID_KEY, TERM_ENTITY);
                intent.putExtra(PARENT_TYPE_ID_KEY, UNDEFINED);
                addNewCourseActivityResultLauncher.launch(intent);
            }
        });

        List<Term> allItems = repoOps.getTermsList();
        final TermAdapter termAdapter = new TermAdapter(this);
        RecyclerView recyclerView = findViewById(R.id.termsListRecyclerview);
        recyclerView.setAdapter(termAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        termAdapter.setItemList(allItems);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_nav_terms_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (android.R.id.home == item.getItemId()){
            this.finish();
            return true;
        }

        if (R.id.coursesOption == item.getItemId()){
            Intent intent = new Intent(TermList.this, CourseList.class);
            startActivity(intent);
            return true;
        }

        if (R.id.assessmentsOption == item.getItemId()){
            Intent intent = new Intent(TermList.this, AssessmentList.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void refreshView() {
        this.recreate();
    }
}
