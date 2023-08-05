package com.evanknight.scheduleu.activities;

import static com.evanknight.scheduleu.util.Constants.*;
import static com.evanknight.scheduleu.util.TermStatus.*;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.evanknight.scheduleu.util.EntityTypeID;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import com.evanknight.scheduleu.entities.Course;
import com.evanknight.scheduleu.entities.Term;
import com.evanknight.scheduleu.R;
import com.evanknight.scheduleu.util.RepositoryOperations;
import com.evanknight.scheduleu.util.EasyDate;
import com.evanknight.scheduleu.util.TermValidator;
import com.evanknight.scheduleu.util.Utils;
import com.evanknight.scheduleu.util.TermStatus;

import java.io.Serializable;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Logger;

public class TermDetails extends AppCompatActivity implements Serializable {

    // Term to Detail
    private Term term;

    // Editable Fields
    private EditText editName;
    private EditText noteTextField;
    private TextView startDateTextField;
    private TextView endDateTextField;
    private Spinner selectTermStatus;

    // Controls
    private DatePickerDialog.OnDateSetListener startDatePickDialog;
    private DatePickerDialog.OnDateSetListener endDatePickDialog;
    private RecyclerView recyclerView;
    private ActivityResultLauncher<Intent> newCourseLauncher;
    private boolean dialogResult = true;

    final RepositoryOperations repoOps = RepositoryOperations.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();

        setContentView(R.layout.widget_activity_term_details_view);
        // Assigning -1 as the default ensures that if the record is missing it's ID number forces an assignment of a valid ID at Save event
        initializeComponents();
        int id = intent.getIntExtra(TERM_ID_KEY, INIT_ENTITY_ID);
        String name = intent.getStringExtra(TERM_NAME_KEY);
        if (repoOps.termRecordExists(id)){
            term = new Term(repoOps.getTermByID(id));
        } else {
            // Going to just cancel for now
            Toast.makeText(this, "Record Not Found", Toast.LENGTH_LONG).show();
            TermDetails.this.finish();
        }

        editName.setText(term.getEntityName());
        setStartDate(intent);
        setEndDate(intent);
        setStatus(intent);
        setTermNote();

        final CourseAdapter courseAdapter = new CourseAdapter(this);
        if (null == recyclerView){
            Logger.getLogger("TermDetails").warning("---- Recycler View Not Set To An Instance Of An Object -----");
        }
        recyclerView.setAdapter(courseAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        List<Course> filteredCourses = repoOps.getTermCourses(term.getItemID());
        courseAdapter.setItemList(filteredCourses);

        newCourseLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (Activity.RESULT_OK == result.getResultCode()){
                            TermDetails.this.recreate();
                        }
                    }
                });

        FloatingActionButton fab = findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent intentOut = new Intent(TermDetails.this, AddItemActivity.class);
                    intentOut.putExtra(ID_KEY, INIT_ENTITY_ID);
                    intentOut.putExtra(TYPE_ID_KEY, EntityTypeID.COURSE_ENTITY);
                    intentOut.putExtra(PARENT_ID, term.getItemID());
                    intentOut.putExtra(PARENT_TYPE_ID_KEY, term.getEntityTypeID());
                    intentOut.putExtra(TERM_START_DATE_KEY, term.getStartDate());
                    intentOut.putExtra(TERM_END_DATE_KEY, term.getEndDate());
                    newCourseLauncher.launch(intentOut);
                } catch (Exception ex) {
                    Logger.getLogger("TermDetails").severe("**************** TermDetails OnClick Error: This button does not work fine **********************");
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit_details, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(@NonNull MenuItem selection) {
        if (R.id.cancelEditDetails == selection.getItemId()){
            AlertDialog.Builder adBuilder = new AlertDialog.Builder(this);
            adBuilder.setView(android.R.layout.select_dialog_singlechoice)
                    .setMessage(R.string.app_cancel_warning)
                    .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            finish();
                            Toast.makeText(getApplicationContext(), "Add new course canceled", Toast.LENGTH_LONG).show();
                        }
                    })
                    .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });

            AlertDialog alert = adBuilder.create();
            alert.setTitle(R.string.warning);
            alert.show();
            return true;
        }

        if (R.id.detailsMenuSave == selection.getItemId()){
            return updateRecord(selection.getItemId());
        }

        if (R.id.detailsMenuDelete == selection.getItemId()){
            return startOverwriteWarningDialog(selection.getItemId());
        }

        return super.onOptionsItemSelected(selection);
    }

    @Override
    protected void onResume() {
        super.onResume();
        final CourseAdapter courseAdapter = new CourseAdapter(this);
        RecyclerView recyclerView = findViewById(R.id.listRecyclerview);
        recyclerView.setAdapter(courseAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        List<Course> filteredCourses = repoOps.getTermCourses(term.getItemID());
        courseAdapter.setItemList(filteredCourses);
    }

    private void initializeComponents(){
        editName = findViewById(R.id.termEditName);
        noteTextField = findViewById(R.id.termDetailNote);
        startDateTextField = findViewById(R.id.termEditStartDate);
        endDateTextField = findViewById(R.id.termEditEndDate);
        selectTermStatus = findViewById(R.id.termStatusSpinner);
        recyclerView = findViewById(R.id.listRecyclerview);
        selectTermStatus = findViewById(R.id.termStatusSpinner);

        // Labels
        TextView editNameLabel = findViewById(R.id.termNameLabel);
        editNameLabel.setText(R.string.name);
        TextView editStartDateLabel = findViewById(R.id.termStartDateLabel);
        editStartDateLabel.setText(R.string.start_date_label);
        TextView editEndDateLabel = findViewById(R.id.termEndDateLabel);
        editEndDateLabel.setText(R.string.end_date_label);
    }

    private void setStartDate(@NonNull Intent intent){
        if (null != term && INIT_DATE < term.getStartDate()){
            startDateTextField.setText(EasyDate.getFormat_ddMMMyyyy(term.getStartDate()));
        }

        startDatePickDialog = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                term.setStartDate(EasyDate.getDate(year, monthOfYear, dayOfMonth));
                startDateTextField.setText(EasyDate.getFormat_ddMMMyyyy(term.getStartDate()));
            }
        };

        startDateTextField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cStartDate = Calendar.getInstance();
                cStartDate.setTimeInMillis(EasyDate.getDate(startDateTextField.getText().toString()));
                new DatePickerDialog(TermDetails.this, startDatePickDialog,
                        cStartDate.get(Calendar.YEAR),
                        cStartDate.get(Calendar.MONTH),
                        cStartDate.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    private void setEndDate(@NonNull Intent intent){
        if (null != term && INIT_DATE < term.getEndDate()) {
            endDateTextField.setText(EasyDate.getFormat_ddMMMyyyy(term.getEndDate()));
        }

        endDatePickDialog = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                term.setEndDate(EasyDate.getDate(year, monthOfYear, dayOfMonth));
                endDateTextField.setText(EasyDate.getFormat_ddMMMyyyy(term.getEndDate()));
            }
        };

        endDateTextField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cEndDate = Calendar.getInstance();
                cEndDate.setTimeInMillis(EasyDate.getDate(endDateTextField.getText().toString()));
                new DatePickerDialog(TermDetails.this, endDatePickDialog,
                        cEndDate.get(Calendar.YEAR),
                        cEndDate.get(Calendar.MONTH),
                        cEndDate.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }
    // TODO: Status is not getting set or saved correctly
    private void setStatus(@NonNull Intent intent){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            term.setStatus((TermStatus) intent.getSerializableExtra(TERM_STATUS_KEY, TermStatus.class));
        } else {
            term.setStatus((TermStatus) intent.getSerializableExtra(TERM_STATUS_KEY));
        }
        SpinnerAdapter spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, TermStatus.values());
        selectTermStatus.setAdapter(spinnerAdapter);
        int index = (null != term.getStatus() ? term.getStatus().ordinal() : PROJECTED.ordinal()) ;
        selectTermStatus.setSelection(index);
        selectTermStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                term.setStatus(TermStatus.getStatusByOrdinal(i));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                term.setStatus(null);
            }
        });
    }

    private void setTermNote(){
        noteTextField.setText(term.getEntityNote());
        noteTextField.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(view.hasFocus()){
                    noteTextField.setMinHeight(Utils.convertPixels(TermDetails.this, 137));
                } else {
                    noteTextField.setMinimumHeight(Utils.convertPixels(TermDetails.this, 48));
                }
            }
        });
    }

    // Save Menu Button action
    private boolean updateRecord(int saveButton){
        boolean success = true;
        TermValidator tv;
        if (validateForm() && repoOps.termListContains(term) && startOverwriteWarningDialog(saveButton)) {
            term.setEntityNote(noteTextField.getText().toString());
            tv = new TermValidator(term);
            success = repoOps.insert(tv);
        } else if (dialogResult){
            Toast.makeText(this, "Item Term could not be saved", Toast.LENGTH_LONG).show();
            success = false;
        } else {
            Toast.makeText(this, "Save Term canceled", Toast.LENGTH_LONG).show();
        }
        return false;
    }

    private boolean validateForm(){
        return null != editName.getText() &&
                !startDateTextField.getText().toString().isEmpty() &&
                !endDateTextField.getText().toString().isEmpty() &&
                null != selectTermStatus.getSelectedItem();
    }

    private boolean startOverwriteWarningDialog(int menuItem){
        int message;
        int title;

        if (R.id.detailsMenuSave == menuItem){
            message = R.string.overwrite_dialog_message;
            title = R.string.overwrite_warning;
        } else {
            message = R.string.delete_dialog_message;
            title = R.string.delete_warning;
        }

        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(TermDetails.this);
        alertBuilder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogResult = true;
                    }
                }).setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogResult = false;
                    }
                }).setMessage(message)
                .setTitle(title);

        AlertDialog overwriteWarning = alertBuilder.create();
        overwriteWarning.show();
        return dialogResult;
    }
}
