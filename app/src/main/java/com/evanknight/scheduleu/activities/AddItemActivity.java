package com.evanknight.scheduleu.activities;

import static com.evanknight.scheduleu.util.Constants.*;
import static com.evanknight.scheduleu.util.EntityTypeID.*;

import com.evanknight.scheduleu.entities.Term;
import com.evanknight.scheduleu.entities.Course;
import com.evanknight.scheduleu.entities.Assessment;
import com.evanknight.scheduleu.entities.Instructor;
import com.evanknight.scheduleu.util.AssessmentValidator;
import com.evanknight.scheduleu.util.CourseValidator;
import com.evanknight.scheduleu.util.EasyDate;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.evanknight.scheduleu.R;
import com.evanknight.scheduleu.util.InstructorValidator;
import com.evanknight.scheduleu.util.RepositoryOperations;
import com.evanknight.scheduleu.util.TermValidator;
import com.evanknight.scheduleu.util.EntityTypeID;
import com.evanknight.scheduleu.util.TermStatus;
import com.evanknight.scheduleu.util.CourseStatus;
import com.evanknight.scheduleu.util.AssessmentStatus;
import com.evanknight.scheduleu.util.Utils;


import java.util.logging.Logger;

public class AddItemActivity extends AppCompatActivity {

    // Constants
    private final String START = "start";
    private final String END = "end";

    // Identifiers
    private int entityID = -1;
    private EntityTypeID typeID;
    private EntityTypeID parentTypeID;

    // Entity Objects
    private Term newTerm;
    private Course newCourse;
    private Assessment newAssessment;

    // Entity Attributes
    private String entityName1;
    private String entityName2;
    private int instructorID = -1;
    private String entityNoteText;
    private String instructorPhoneNumber;
    private String instructorEmail;
    private long startDate = BAD_DATE;
    private long endDate = BAD_DATE;
    private long termStartDate = BAD_DATE;
    private long termEndDate = BAD_DATE;
    private boolean isMatchTermDates = true;
    private boolean isDateRange = false;
    private TermStatus termStatus;
    private CourseStatus courseStatus;
    private AssessmentStatus assessmentStatus;

    // Editable Fields
    private EditText editNameText1;
    private EditText editNameText2;
    private EditText editNoteText;

    // Interface Fields
    private TextView startDateTextField;
    private TextView endDateTextField;
    private TextView choseInstructorField;

    // Buttons
    private Button pickStartDateButton;
    private Button pickEndDateButton;
    private Button saveEntryButton;

    // Other Input Components
    private SwitchCompat matchDateSwitch;
    private SwitchCompat isDateRangeSwitch;
    private Spinner editStatus;

    // Menus and Controls
    private Menu addItemMenu;
    private DatePickerDialog.OnDateSetListener startDatePickDialog;
    private DatePickerDialog.OnDateSetListener endDatePickDialog;
    ActivityResultLauncher<Intent> pickInstructorResultLauncher;
    private boolean dialogResult = false;

    // Data/Repository/DAO
    //private SURepository repository;
    private final RepositoryOperations repoOps = RepositoryOperations.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Instantiate View and Layout
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        setContentView(R.layout.widget_activity_add_item);
        // TODO: Really need to scale back what is being passed in the intent. Only pass the data needed to query the db then instantiate any fields with that info.
        entityID = intent.getIntExtra(ID_KEY, INIT_ENTITY_ID);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            typeID = (EntityTypeID) intent.getSerializableExtra(TYPE_ID_KEY, EntityTypeID.class);
            parentTypeID = (EntityTypeID) intent.getSerializableExtra(PARENT_TYPE_ID_KEY, EntityTypeID.class);

            Logger.getLogger("AddItemActivity").warning("APK 33 get parent type = " + (parentTypeID != null ? parentTypeID.name() : " NO VALUE SAVED") );

        } else {
            typeID = (EntityTypeID) intent.getSerializableExtra(TYPE_ID_KEY);
            parentTypeID = (EntityTypeID) intent.getSerializableExtra(PARENT_TYPE_ID_KEY);

            Logger.getLogger("AddItemActivity").warning("APK 28 get parent type = " + (parentTypeID != null ? parentTypeID.name() : " NO VALUE SAVED") );

        }
        if (null != parentTypeID && null != typeID){
            int parentID;
            switch (parentTypeID) {
                case TERM_ENTITY:
                    parentID = getIntent().getIntExtra(PARENT_ID, INIT_ENTITY_ID);
                    if (INIT_ENTITY_ID == parentID || null == repoOps.getTermByID(parentID)){
                        // This sets activity result to cancelled and ends the activity
                        parentEntityNotFound();
                    }

                    Term t = repoOps.getTermByID(parentID);
                    termStartDate = t.getStartDate();
                    termEndDate = t.getEndDate();
                    typeID = COURSE_ENTITY;
                    newCourse = new Course();
                    break;
                case COURSE_ENTITY:
                    parentID = getIntent().getIntExtra(PARENT_ID, INIT_ENTITY_ID);
                    if (INIT_ENTITY_ID == parentID || null == repoOps.getCourseByID(parentID)){
                        // This sets activity result to cancelled and ends the activity
                        parentEntityNotFound();
                    }
                    Course c = repoOps.getCourseByID(parentID);
                    typeID = ASSESSMENT_ENTITY;
                    newAssessment = new Assessment();
                    break;
                case UNDEFINED:
                    typeID = TERM_ENTITY;
                    newTerm = new Term();
                    break;
                case INSTRUCTOR_ENTITY:
                case ASSESSMENT_ENTITY:
                default:
                    Logger.getLogger("AddItemActivity").warning("AddItemActivity.SetRecordType ERROR: Invalid parent type, " +
                            "only MainActivity, TermActivity, and CourseActivity can generate new items");
            }
            // TODO: Title is not getting set when adding new Course
            setTitle(typeID.TITLE);
        } else {
            setTitle(R.string.app_name);
        }


        initializeFormFields();
        // TODO: Trouble shoot incorrect initialized date fields (should be empty for term and assessment)
        initializeStartDatePickers();
        initializeEndDatePickers();
        populateFieldsSetListeners();

//        cancelButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                // TODO: Discard Changes Warning
//                AddItemActivity.this.finish();
//            }
//        });

        saveEntryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (saveNewEntry()){
                    AddItemActivity.this.setResult(Activity.RESULT_OK);
                    AddItemActivity.this.finish();
                } else {
                    Toast.makeText(AddItemActivity.this, "Save Entry Failed", Toast.LENGTH_LONG).show();
                }
            }
        });

        pickInstructorResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (Activity.RESULT_OK == result.getResultCode()){
                            Intent data = result.getData();
                            Logger.getLogger("AddItemActivity").warning("Instructor form completed successfully data received="+
                                    (INIT_ENTITY_ID < data.getIntExtra(INSTRUCTOR_ID_KEY, INIT_ENTITY_ID) ? "GOOD DATA" : "BAD DATA"));

                            int instID = -1;
                            if (null != data && 0 < data.getIntExtra(INSTRUCTOR_ID_KEY, INIT_ENTITY_ID)) {
                                instID = data.getIntExtra(INSTRUCTOR_ID_KEY, INIT_ENTITY_ID);
                            }
                            if (-1 != instID) {
                                setInstructorName(instID);
                            }
                        } else {
                            Logger.getLogger("AddItemActivity").warning("Instructor form result was = " + result.getResultCode());
                        }
                    }
                });
    }

    private void initializeFormFields(){
        // Core Activity Views
        saveEntryButton = findViewById(R.id.saveEntryButton);
        pickStartDateButton = findViewById(R.id.pickStartDateButton);
        pickEndDateButton = findViewById(R.id.pickEndDateButton);

        // Entity ID Views
        editNameText1 = findViewById(R.id.edit2RowName1);
        editNameText2 = findViewById(R.id.edit2RowName2);
        // Course Date Match Switch View
        matchDateSwitch = findViewById(R.id.matchTermDateSwitch);
        // Assessment Date Range Switch View
        isDateRangeSwitch = findViewById(R.id.useDateRangeSwitch);
        // Date Views
        startDateTextField = findViewById(R.id.editTextStartDate);
        endDateTextField = findViewById(R.id.editTextEndDate);
        // Chose Instructor
        choseInstructorField = findViewById(R.id.courseInstructorField);
        // Status Spinner View
        editStatus = findViewById(R.id.addItemStatusSpinner);
        // Note View
        editNoteText = findViewById(R.id.editAddItemNoteText);
    }

    // Override Methods
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_add_item, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem selection) {
        if(android.R.id.home == selection.getItemId()){
           return showCancelDialog();
        }
        if (R.id.cancelAddItem == selection.getItemId()){
            return showCancelDialog();
        }
        return super.onOptionsItemSelected(selection);
    }

    private boolean showCancelDialog(){
        AlertDialog.Builder adBuilder = new AlertDialog.Builder(this);
        adBuilder.setView(android.R.layout.select_dialog_singlechoice)
                .setMessage(R.string.app_cancel_warning)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        AddItemActivity.this.setResult(Activity.RESULT_CANCELED);
                        finish();
                        AddItemActivity.this.finish();
                        Toast.makeText(getApplicationContext(), "Add new " + typeID.name() + " canceled", Toast.LENGTH_LONG).show();
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

    // Initialize Activity Views
    private void populateFieldsSetListeners(){
        editNameText1.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!view.hasFocus()){
                    entityName1 = editNameText1.getText().toString();
                }
            }
        });

        // Toggle Instructor Specific Assets - This is the odd one out and enables/disables components the other entities do/don't use
        if (INSTRUCTOR_ENTITY == typeID){
            editNameText1.setHint(R.string.first_name);
            editNameText2.setEnabled(true);
            editNameText2.setVisibility(View.VISIBLE);
            pickDatesSetEnabled(false);
            startDateTextField.setVisibility(View.GONE);
            pickStartDateButton.setVisibility(View.GONE);
            endDateTextField.setVisibility(View.GONE);
            pickEndDateButton.setVisibility(View.GONE);
            editStatus.setEnabled(false);
            editStatus.setVisibility(View.GONE);

            LinearLayout l1 = findViewById(R.id.startDateGroup);
            LinearLayout l2 = findViewById(R.id.endDateGroup);

            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
            int dp = Utils.convertPixels(this, 5);

            EditText editInstructorEmail = new EditText(this);
            editInstructorEmail.setEnabled(true);
            editInstructorEmail.setVisibility(View.VISIBLE);
            editInstructorEmail.setLayoutParams(layoutParams);
            editInstructorEmail.setPadding(dp, dp, dp, dp);
            editInstructorEmail.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
            editInstructorEmail.setHint(R.string.instructor_email_hint);
            l1.addView(editInstructorEmail);

            EditText editInstructorPhoneNumber = new EditText(this);
            editInstructorPhoneNumber.setEnabled(true);
            editInstructorPhoneNumber.setVisibility(View.VISIBLE);
            editInstructorPhoneNumber.setLayoutParams(layoutParams);
            editInstructorPhoneNumber.setPadding(dp, dp, dp, dp);
            editInstructorPhoneNumber.setInputType(InputType.TYPE_CLASS_PHONE);
            editInstructorPhoneNumber.setHint(R.string.instructor_phone_hint);
            l2.addView(editInstructorPhoneNumber);


        } else {
            editNameText1.setHint(R.string.name);
            editNameText2.setEnabled(false);
            editNameText2.setVisibility(View.GONE);
            editStatus.setVisibility(View.VISIBLE);
            editStatus.setEnabled(true);
        }

        editNameText2.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!view.hasFocus()){
                    entityName2 = editNameText2.getText().toString();
                }
            }
        });
        editNoteText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!view.hasFocus()){
                    entityNoteText = editNoteText.getText().toString();
                }
            }
        });

        // Toggle Term Assets
        if(TERM_ENTITY == typeID){
            pickDatesSetEnabled(true);
            editStatus.setAdapter(new ArrayAdapter<TermStatus>(this, android.R.layout.simple_list_item_1, TermStatus.values()));
            editStatus.setSelection(TermStatus.PROJECTED.ordinal());
            editStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    termStatus = TermStatus.getStatusByOrdinal(i);
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                    termStatus = null;
                }
            });
        }

        // Toggle Course Specific Assets
        if (COURSE_ENTITY == typeID) {
            pickDatesSetEnabled(false);
            if (BAD_DATE != termStartDate && BAD_DATE != termEndDate) {
                setDateFieldValues(START, termStartDate);
                setDateFieldValues(END, termEndDate);
            }
            matchDateSwitch.setEnabled(true);
            matchDateSwitch.setVisibility(View.VISIBLE);
            matchDateSwitch.setChecked(true);
            matchDateSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (b) {
                        setDateFieldValues(START, termStartDate);
                        setDateFieldValues(END, termEndDate);
                        pickDatesSetEnabled(false);
                    } else {
                        pickDatesSetEnabled(true);
                        if (!startDateTextField.getText().toString().isEmpty() && !endDateTextField.getText().toString().isEmpty() && EasyDate.todayInMilli() > startDate) {
                            setDateFieldValues(START, startDate);
                            setDateFieldValues(END, endDate);
//                            startDate = EasyDate.todayInMilli();
//                            endDate = EasyDate.addMonths(startDate, TERM_LENGTH);
                        }
                    }
                    isMatchTermDates = b;
                }
            });
            editStatus.setAdapter(new ArrayAdapter<CourseStatus>(this, android.R.layout.simple_list_item_1, CourseStatus.values()));
            editStatus.setSelection(CourseStatus.PROJECTED.ordinal());
            editStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    courseStatus = CourseStatus.getStatusByOrdinal(i);
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                    courseStatus = null;
                }
            });
            choseInstructorField.setVisibility(View.VISIBLE);
            choseInstructorField.setEnabled(true);
            choseInstructorField.setClickable(true);
            choseInstructorField.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int newID = repoOps.getNextCourseIdNumber();
                    Intent instructorIntent = new Intent(AddItemActivity.this, InstructorDetails.class);
                    instructorIntent.putExtra(COURSE_ID_KEY, newID);
                    // TODO: Need to get the Instructor ID back as a result to populate the instructor name
                    pickInstructorResultLauncher.launch(instructorIntent);
                }
            });
        } else {
            matchDateSwitch.setEnabled(false);
            matchDateSwitch.setVisibility(View.GONE);
            choseInstructorField.setEnabled(false);
            choseInstructorField.setVisibility(View.GONE);
        }


        // Toggle Assessment Specific Assets
        if (ASSESSMENT_ENTITY == typeID) {
            pickDatesSetEnabled(true, false);
            isDateRangeSwitch.setEnabled(true);
            isDateRangeSwitch.setVisibility(View.VISIBLE);
            isDateRangeSwitch.setChecked(false);
            isDateRange = false;
            isDateRangeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    isDateRange = b;
                    pickDatesSetEnabled(true, b);
                    if (!b){
                        pickDatesSetEnabled(true, false);
                        setDateFieldValues(END, startDate);
                    } else {
                        pickDatesSetEnabled(true);
                        setDateFieldValues(END, endDate);
                    }
                }
            });

            editStatus.setAdapter(new ArrayAdapter<AssessmentStatus>(this, android.R.layout.simple_list_item_1, AssessmentStatus.values()));
            editStatus.setSelection(AssessmentStatus.PROJECTED.ordinal());
            editStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    assessmentStatus = AssessmentStatus.getStatusByOrdinal(i);
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                    assessmentStatus = null;
                }
            });
        } else {
            isDateRangeSwitch.setEnabled(false);
            isDateRangeSwitch.setVisibility(View.GONE);
        }
    }

    private void setInstructorName(int instructorID){
        if (0 < instructorID) {
            this.instructorID = instructorID;
            Instructor inst = repoOps.getInstructorByID(this.instructorID);
            String fullName = inst.getEntityName() + " " + inst.getLastName();
            choseInstructorField.setText(fullName);
        } else {
            choseInstructorField.setHint(R.string.no_instructor_name);
        }
    }

    private void initializeStartDatePickers(){

        startDatePickDialog = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                setDateFieldValues(START, EasyDate.getDate(year, month, day));
            }
        };

        pickStartDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                configureStartDateOnClickListener(AddItemActivity.this, startDatePickDialog);
            }
        });

        startDateTextField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                configureStartDateOnClickListener(AddItemActivity.this, startDatePickDialog);
            }
        });
    }

    private void initializeEndDatePickers(){
        endDatePickDialog = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                setDateFieldValues(END, EasyDate.getDate(year, monthOfYear, dayOfMonth));
            }
        };

        pickEndDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                configureEndDateOnClickListener(AddItemActivity.this, endDatePickDialog);
            }
        });

        endDateTextField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                configureEndDateOnClickListener(AddItemActivity.this, endDatePickDialog);
            }
        });
    }

    private void configureStartDateOnClickListener(Context context, DatePickerDialog.OnDateSetListener dateListener){
        int[] dateValues = EasyDate.getDateAsYMD(EasyDate.getDateInMilli(startDateTextField.getText().toString()));
        new DatePickerDialog(context, dateListener,
               dateValues[EasyDate.YEAR], dateValues[EasyDate.MONTH], dateValues[EasyDate.DAY]).show();
    }

    private void configureEndDateOnClickListener(Context context, DatePickerDialog.OnDateSetListener dateListener){
        int[] dateValues = EasyDate.getDateAsYMD(EasyDate.getDateInMilli(endDateTextField.getText().toString()));
        new DatePickerDialog(context, dateListener,
                dateValues[EasyDate.YEAR], dateValues[EasyDate.MONTH], dateValues[EasyDate.DAY]).show();
    }

    private void setDateFieldValues(String fieldID, long d){
        if (EasyDate.todayInMilli() <= d) {
            if (START.equals(fieldID)) {
                startDate = d;
                startDateTextField.setText(EasyDate.getFormat_ddMMMyyyy(startDate));
            } else {
                endDate = d;
                endDateTextField.setText(EasyDate.getFormat_ddMMMyyyy(endDate));
            }
        } else {
            Logger.getLogger("AddItemActivity").warning("[setDateFieldValues] Dates cannot be set in the past");
        }
    }

    private void pickDatesSetEnabled(boolean enable){
        pickDatesSetEnabled(enable, enable);
    }
    private void pickDatesSetEnabled(boolean enableStartDate, boolean enableEndDate){
        pickStartDateButton.setEnabled(enableStartDate);
        startDateTextField.setEnabled(enableStartDate);
        pickEndDateButton.setEnabled(enableEndDate);
        endDateTextField.setEnabled(enableEndDate);
    }

    private boolean startOverwriteWarningDialog(){
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(AddItemActivity.this);
        alertBuilder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogResult = true;
            }
        }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogResult = false;
            }
        }).setMessage(R.string.overwrite_dialog_message)
          .setTitle(R.string.warning);

        AlertDialog overwriteWarning = alertBuilder.create();
        overwriteWarning.show();
        return dialogResult;
    }

    private void parentEntityNotFound(){
        Toast.makeText(AddItemActivity.this, "Parent Activity could not be found, canceling add operation", Toast.LENGTH_LONG).show();
        AddItemActivity.this.setResult(Activity.RESULT_CANCELED);
        AddItemActivity.this.finish();
    }

    private boolean saveNewEntry(){
        boolean success = false;
        switch(typeID) {
            case TERM_ENTITY:
                Term term;
                TermValidator tv;
                if (-1 == entityID) {
                    entityID = repoOps.getNextTermIdNumber();
                    term = new Term(entityID, entityName1, startDate, endDate, termStatus);
                    term.setEntityNote(editNoteText.getText().toString());
                    tv = new TermValidator(term);
                    success = repoOps.insert(tv);
                } else if (startOverwriteWarningDialog()) {
                    term = new Term(entityID, entityName1, startDate, endDate, termStatus);
                    term.setEntityNote(editNoteText.getText().toString());
                    tv = new TermValidator(term);
                    success = repoOps.update(tv);
                }
                break;
            case COURSE_ENTITY:
                Course course;
                CourseValidator cv;
                if (-1 == entityID){
                    entityID = repoOps.getNextCourseIdNumber();
                    course = new Course(entityID, entityName1, startDate, endDate, courseStatus);
                    course.setMatchTermDates(isMatchTermDates);
                    course.setInstructorID(instructorID);
                    course.setEntityNote(editNoteText.getText().toString());
                    cv = new CourseValidator(course);
                    success = repoOps.insert(cv);
                }else if (startOverwriteWarningDialog()) {
                    course = new Course(entityID, entityName1, startDate, endDate, courseStatus);
                    course.setMatchTermDates(isMatchTermDates);
                    course.setEntityNote(editNoteText.getText().toString());
                    cv = new CourseValidator(course);
                    success = repoOps.update(cv);
                }
                break;
            case ASSESSMENT_ENTITY:
                Assessment assessment;
                AssessmentValidator av;
                if (-1 == entityID){
                    entityID = repoOps.getNextAssessmentIdNumber();
                    assessment = new Assessment(entityID, entityName1, startDate, endDate, assessmentStatus);
                    assessment.setIsDateRange(isDateRange);
                    av = new AssessmentValidator(assessment);
                    success = repoOps.insert(av);
                }else if (startOverwriteWarningDialog()) {
                    assessment = new Assessment(entityID, entityName1, startDate, endDate, assessmentStatus);
                    assessment.setIsDateRange(isDateRange);
                    av = new AssessmentValidator(assessment);
                    success = repoOps.update(av);
                }
                break;
            case INSTRUCTOR_ENTITY:
                Instructor instructor;
                InstructorValidator iv;
                if (-1 == entityID){
                    entityID = repoOps.getNextInstructorIdNumber();
                    instructor = new Instructor(entityID, entityName1, entityName2, instructorEmail, instructorPhoneNumber);
                    iv = new InstructorValidator(instructor);
                    success = repoOps.insert(iv);
                }else if (startOverwriteWarningDialog()){
                    instructor = new Instructor(entityID, entityName1, entityName2, instructorEmail, instructorPhoneNumber);
                    iv = new InstructorValidator(instructor);
                    success = repoOps.update(iv);
                }
                break;
            default:
        }
        return success;
    }
}