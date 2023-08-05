package com.evanknight.scheduleu.activities;

import static com.evanknight.scheduleu.util.EntityTypeID.*;
import static com.evanknight.scheduleu.util.Constants.*;
import static com.evanknight.scheduleu.util.CourseStatus.*;

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
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
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
import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.evanknight.scheduleu.entities.Course;
import com.evanknight.scheduleu.entities.Instructor;
import com.evanknight.scheduleu.entities.Term;
import com.evanknight.scheduleu.entities.Assessment;
import com.evanknight.scheduleu.R;
import com.evanknight.scheduleu.util.CourseValidator;
import com.evanknight.scheduleu.util.RepositoryOperations;
import com.evanknight.scheduleu.util.EasyDate;
import com.evanknight.scheduleu.util.Utils;
import com.evanknight.scheduleu.util.CourseStatus;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.Serializable;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Logger;

public class CourseDetails extends AppCompatActivity implements Serializable {

    // Term to Detail
    private Course course = new Course();
    private Term parent;

    // Editable Fields
    private EditText editName;
    private EditText noteTextField;
    private TextView instructorFirstNameText;
    private TextView instructorLastNameText;
    private EditText termNameTextField;
    private TextView startDateTextField;
    private TextView endDateTextField;
    private Spinner selectCourseStatus;
    private SwitchCompat selectMatchTermDates;

    // Controls
    private LinearLayout courseEditInstructor;
    private DatePickerDialog.OnDateSetListener startDatePickDialog;
    private DatePickerDialog.OnDateSetListener endDatePickDialog;
    private ActivityResultLauncher<Intent> addNewAssessmentActivityResultLauncher;
    private ActivityResultLauncher<Intent> editCourseInstructorActivityLauncher;
    private boolean dialogResult = true;

    private final RepositoryOperations repoOps = RepositoryOperations.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();

        setContentView(R.layout.widget_activity_course_details_view);
        initializeComponents();
        // TODO: Internal Exception Thrown that Java didn't like, you'll need to fix it
        int id = intent.getIntExtra(COURSE_ID_KEY, INIT_ENTITY_ID);
        String name = intent.getStringExtra(COURSE_NAME_KEY);
        if (repoOps.courseRecordExists(id)){
            course = new Course(repoOps.getCourseByID(id));
        } else {
            // Going to just cancel for now
            Toast.makeText(this, "Record Not Found", Toast.LENGTH_LONG).show();
            CourseDetails.this.finish();
        }
        editName.setText(course.getEntityName());
        noteTextField.setText(course.getEntityNote());
        parent = repoOps.getTermByID(course.getTermID());
        termNameTextField.setClickable(false);
        termNameTextField.setText(parent.getEntityName());
        setStartDate();
        setEndDate();
        setSelectMatchTermDates();
        setStatus(intent);
        setInstructor();
        setCourseNote();

        RecyclerView recyclerView = findViewById(R.id.courseAssessmentListView);
        final AssessmentAdapter assessmentAdapter = new AssessmentAdapter(this);
        recyclerView.setAdapter(assessmentAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        List<Assessment> filteredAssessments = repoOps.getCourseAssessments(course.getItemID());
        assessmentAdapter.setItemList(filteredAssessments);

        addNewAssessmentActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (Activity.RESULT_OK == result.getResultCode()){
                            CourseDetails.this.recreate();
                        }
                    }
                });

        // Floating Action Button components and data
        FloatingActionButton fab = findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent intent = new Intent(CourseDetails.this, AddItemActivity.class);
                    intent.putExtra(CALLING_CLASS, CourseDetails.this);
                    intent.putExtra(TYPE_ID_KEY, ASSESSMENT_ID_KEY);
                    intent.putExtra(PARENT_TYPE_ID_KEY, COURSE_ENTITY);
                    intent.putExtra(COURSE_ID_KEY, course.getItemID());
                    addNewAssessmentActivityResultLauncher.launch(intent);
                } catch (Exception ex) {
                    Logger.getGlobal().severe("**************** TermDetails OnClick Error: This button does not work fine **********************");
                }
            }
        });

        editCourseInstructorActivityLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (Activity.RESULT_OK == result.getResultCode()){
                            Intent data = result.getData();
                            int instID;
                            if (null != data && INIT_ENTITY_ID < data.getIntExtra(INSTRUCTOR_ID_KEY, INIT_ENTITY_ID)){
                                instID = data.getIntExtra(INSTRUCTOR_ID_KEY, INIT_ENTITY_ID);
                                course.setInstructorID(instID);
                                Instructor inst = repoOps.getInstructorByID(instID);
                                setInstructorName(inst.getEntityName(), inst.getLastName());
                            }

                        }
                    }
        });

        // Course note components and data
        noteTextField.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(view.hasFocus()){
                    noteTextField.setMinHeight(Utils.convertPixels(CourseDetails.this, 137));
                } else {
                    noteTextField.setMinimumHeight(Utils.convertPixels(CourseDetails.this, 48));
                }
            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit_details, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem selection) {
        if(android.R.id.home == selection.getItemId()){
            this.finish();
            return true;}
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
        final AssessmentAdapter assessmentAdapter = new AssessmentAdapter(this);
        RecyclerView recyclerView = findViewById(R.id.listRecyclerview);
        recyclerView.setAdapter(assessmentAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        assessmentAdapter.setItemList(repoOps.getCourseAssessments(course.getItemID()));
    }

    private void initializeComponents(){
        editName = findViewById(R.id.courseEditName);
        noteTextField = findViewById(R.id.courseDetailNote);
        startDateTextField = findViewById(R.id.courseEditStartDate);
        endDateTextField = findViewById(R.id.editTextEndDate);
        courseEditInstructor = findViewById(R.id.courseEditInstructor);
        instructorFirstNameText = findViewById(R.id.doubleListEntryCol1);
        instructorLastNameText = findViewById(R.id.doubleListEntryCol2);
        termNameTextField = findViewById(R.id.courseEditTerm);
        selectCourseStatus = findViewById(R.id.courseStatusSpinner);
        selectMatchTermDates = findViewById(R.id.matchTermDateSwitch);

        // Labels
        TextView editNameLabel = findViewById(R.id.courseNameLabel);
        editNameLabel.setText(R.string.name);
        TextView editStartDateLabel = findViewById(R.id.courseStartDateLabel);
        editStartDateLabel.setText(R.string.start_date_label);
        TextView editEndDateLabel = findViewById(R.id.courseEndDateLabel);
        editEndDateLabel.setText(R.string.end_date_label);
        TextView editInstructorLabel = findViewById(R.id.courseInstructorLabel);
        editInstructorLabel.setText(R.string.instructor_name_component_label);
        TextView editTermLabel = findViewById(R.id.courseTermNameLabel);
        editTermLabel.setText(R.string.component_term_name_label);
        selectMatchTermDates.setText(R.string.match_term_date_range);

        selectMatchTermDates.setChecked(true);
    }

    private void setStartDate(){
        if (null != course && INIT_DATE < course.getStartDate()) {
            endDateTextField.setText(EasyDate.getFormat_ddMMMyyyy(course.getStartDate()));
        }

        startDatePickDialog = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                course.setStartDate(EasyDate.getDate(year, monthOfYear, dayOfMonth));
                startDateTextField.setText(EasyDate.getFormat_ddMMMyyyy(course.getStartDate()));
            }
        };

        startDateTextField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cStartDate = Calendar.getInstance();
                cStartDate.setTimeInMillis(EasyDate.getDate(startDateTextField.getText().toString()));
                new DatePickerDialog(CourseDetails.this, startDatePickDialog,
                        cStartDate.get(Calendar.YEAR),
                        cStartDate.get(Calendar.MONTH),
                        cStartDate.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    private void setEndDate(){
        if (null != course && INIT_DATE < course.getEndDate()) {
            endDateTextField.setText(EasyDate.getFormat_ddMMMyyyy(course.getEndDate()));
        }

        endDatePickDialog = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                course.setEndDate(EasyDate.getDate(year, monthOfYear, dayOfMonth));
                endDateTextField.setText(EasyDate.getFormat_ddMMMyyyy(course.getEndDate()));
            }
        };

        endDateTextField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cEndDate = Calendar.getInstance();
                cEndDate.setTimeInMillis(EasyDate.getDate(endDateTextField.getText().toString()));
                new DatePickerDialog(CourseDetails.this, endDatePickDialog,
                        cEndDate.get(Calendar.YEAR),
                        cEndDate.get(Calendar.MONTH),
                        cEndDate.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    private void setSelectMatchTermDates(){
        // Match term dates component and data
        selectMatchTermDates.setVisibility(View.VISIBLE);
        selectMatchTermDates.setEnabled(true);
        selectMatchTermDates.setChecked(course.getMatchTermDates());
        selectMatchTermDates.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    startDateTextField.setText(EasyDate.getFormat_ddMMMyyyy(parent.getStartDate()));
                    endDateTextField.setText(EasyDate.getFormat_ddMMMyyyy(parent.getEndDate()));
                    startDateTextField.setEnabled(false);
                    endDateTextField.setEnabled(false);
                } else {
                    startDateTextField.setEnabled(true);
                    endDateTextField.setEnabled(true);
                    if (null != startDateTextField.getText()) {
                        course.setStartDate(EasyDate.todayInMilli());
                        course.setEndDate(EasyDate.addMonths(course.getStartDate(), TERM_LENGTH));
                        startDateTextField.setText(EasyDate.getFormat_ddMMMyyyy(course.getStartDate()));
                        endDateTextField.setText(EasyDate.getFormat_ddMMMyyyy(course.getEndDate()));
                    }
                }
                course.setMatchTermDates(b);
            }
        });
    }

    private void setInstructor(){
        instructorFirstNameText.setHint(R.string.add_instructor);
        instructorFirstNameText.setClickable(false);
        instructorLastNameText.setClickable(false);
        courseEditInstructor.setClickable(true);
        courseEditInstructor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent instructorData = new Intent(CourseDetails.this, InstructorDetails.class);
                if (instructorFirstNameText.getText().toString().isEmpty() ||
                    instructorLastNameText.getText().toString().isEmpty()){
                    instructorData.putExtra(COURSE_ID_KEY, course.getItemID());
                    editCourseInstructorActivityLauncher.launch(instructorData);
                }
//                AlertDialog.Builder adBuilder = new AlertDialog.Builder(CourseDetails.this);
//                adBuilder.setView(R.layout.instructor_name_dialog);
//                EditText firstName = findViewById(R.id.addInstructorFirstName);
//                EditText lastName = findViewById(R.id.addInstructorLastName);
//                adBuilder.setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        boolean valid = true;
//                        if(null == firstName.getText()){
//                            Toast.makeText(CourseDetails.this, "Instructor field First Name must contain a value", Toast.LENGTH_SHORT).show();
//                            valid = false;
//                        }
//                        if (valid && null == lastName.getText()){
//                            Toast.makeText(CourseDetails.this, "Instructor field Last Name must contain a value", Toast.LENGTH_SHORT).show();
//                            valid = false;
//                        }
//                        if (valid){
//                            String f = firstName.getText().toString();
//                            String l = firstName.getText().toString();
//                            setInstructorName(f, l);
//                        }
//                    }
//                }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        finish();
//                    }
//                }).show();
            }
        });
    }

    private void setInstructorName(String first, String last){
        instructorLastNameText.setText(first);
        instructorLastNameText.setText(last);
    }

    private void setStatus(@NonNull Intent intent){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            course.setStatus((CourseStatus) intent.getSerializableExtra(COURSE_STATUS_KEY, CourseStatus.class));
        } else {
            course.setStatus((CourseStatus) intent.getSerializableExtra(COURSE_STATUS_KEY));
        }
        SpinnerAdapter spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, CourseStatus.values());
        selectCourseStatus.setAdapter(spinnerAdapter);
        int index = null != course.getStatus() ? course.getStatusAsOrdinal() : PROJECTED.ordinal();
        selectCourseStatus.setSelection(index);
        selectCourseStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                course.setStatus(CourseStatus.getStatusByOrdinal(i));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                course.setStatus(null);
            }
        });
    }

    private void setCourseNote(){
        noteTextField.setText(course.getEntityNote());
        noteTextField.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(view.hasFocus()){
                    noteTextField.setMinHeight(Utils.convertPixels(CourseDetails.this, 137));
                } else {
                    noteTextField.setMinimumHeight(Utils.convertPixels(CourseDetails.this, 48));
                }
            }
        });
    }

    // TODO: This didn't prevent the save from happening. Reevaluate methodology, consider real-time validation
    private boolean validateForm(){
        return !editName.getText().toString().isEmpty() &&
                !startDateTextField.getText().toString().isEmpty() &&
                !endDateTextField.getText().toString().isEmpty() &&
                !termNameTextField.getText().toString().isEmpty() &&
                !instructorFirstNameText.getText().toString().isEmpty() &&
                !instructorLastNameText.getText().toString().isEmpty() &&
                null != selectCourseStatus.getSelectedItem();
    }

    private boolean updateRecord(int saveButton){
        boolean success = true;
        CourseValidator cv;
        if (validateForm() && repoOps.courseListContains(course) && startOverwriteWarningDialog(saveButton)) {
            course.setEntityNote(noteTextField.getText().toString());
            cv = (CourseValidator) new CourseValidator(course).update();
            success = repoOps.insert(cv);
        } else if (dialogResult){
            Toast.makeText(this, "Item Course could not be saved", Toast.LENGTH_LONG).show();
            success = false;
        } else {
            Toast.makeText(this, "Save Course canceled", Toast.LENGTH_LONG).show();
        }
        return false;
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

        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(CourseDetails.this);
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
