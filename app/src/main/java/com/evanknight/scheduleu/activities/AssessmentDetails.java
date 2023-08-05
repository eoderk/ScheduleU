package com.evanknight.scheduleu.activities;

import static com.evanknight.scheduleu.util.Constants.*;
import static com.evanknight.scheduleu.util.AssessmentStatus.*;

import android.app.DatePickerDialog;
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
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import com.evanknight.scheduleu.util.AssessmentValidator;
import com.evanknight.scheduleu.util.EasyDate;
import com.evanknight.scheduleu.util.RepositoryOperations;
import com.evanknight.scheduleu.util.Utils;
import com.evanknight.scheduleu.util.AssessmentStatus;

import com.evanknight.scheduleu.R;
import com.evanknight.scheduleu.entities.Assessment;

import java.io.Serializable;
import java.util.Calendar;

public class AssessmentDetails extends AppCompatActivity implements Serializable {
    private Assessment assessment = new Assessment();

    // Editable Fields
    private EditText editName;
    private EditText noteTextField;
    private TextView startDateTextField;
    private TextView endDateTextField;
    private Spinner selectAssessmentStatus;
    private SpinnerAdapter spinnerAdapter;
    private SwitchCompat selectHasDateRange;

    // Controls
    private DatePickerDialog.OnDateSetListener startDatePickDialog;
    private DatePickerDialog.OnDateSetListener endDatePickDialog;

    private final RepositoryOperations repoOps = RepositoryOperations.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();

        initializeComponents();
        int id = intent.getIntExtra(ASSESSMENT_ID_KEY, INIT_ENTITY_ID);
        String name = intent.getStringExtra(ASSESSMENT_NAME_KEY);
        if (repoOps.assessmentRecordExists(id)){
            assessment = new Assessment(repoOps.getAssessmentByID(id));
        } else {
            // Going to just cancel for now
            Toast.makeText(this, "Record Not Found", Toast.LENGTH_LONG).show();
            AssessmentDetails.this.finish();
        }
        editName.setText(assessment.getEntityName());
        setStartDate(intent);
        setEndDate(intent);
        setStatus(intent);

        selectHasDateRange.setVisibility(View.VISIBLE);
        selectHasDateRange.setEnabled(true);
        assessment.setIsDateRange(intent.getBooleanExtra(ASSESSMENT_HAS_DATE_RANGE, false));
        selectHasDateRange.setChecked(assessment.getIsDateRange());
        selectHasDateRange.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (null != startDateTextField.getText()){
                    if (b) {
                        endDateTextField.setEnabled(true);
                    } else {
                        endDateTextField.setEnabled(false);
                        assessment.setEndDate(assessment.getStartDate());
                    }
                    startDateTextField.setText(EasyDate.getFormat_ddMMMyyyy(assessment.getStartDate()));
                    endDateTextField.setText(EasyDate.getFormat_ddMMMyyyy(assessment.getEndDate()));
                }
                assessment.setIsDateRange(b);
            }
        });

        noteTextField.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(view.hasFocus()){
                    noteTextField.setMinHeight(Utils.convertPixels(AssessmentDetails.this, 137));
                } else {
                    noteTextField.setMinimumHeight(Utils.convertPixels(AssessmentDetails.this, 48));
                }
            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem selection) {
        if(android.R.id.home == selection.getItemId()){
            this.finish();
            return true;}
        if(R.id.detailsMenuSave == selection.getItemId()){
            if (repoOps.assessmentListContains(assessment) &&
                    repoOps.update(new AssessmentValidator(assessment))){
                Toast.makeText(AssessmentDetails.this, "Assessment Saved", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(AssessmentDetails.this, "Assessment could not be saved", Toast.LENGTH_LONG).show();
            }
            return true;
        }
        if(R.id.detailsMenuDelete == selection.getItemId()) {
            AssessmentValidator av = new AssessmentValidator(assessment);
            return repoOps.delete(av.delete());
        }
        return super.onOptionsItemSelected(selection);
    }

    private void initializeComponents() {
        editName = findViewById(R.id.assessmentEditName);
        noteTextField = findViewById(R.id.assessmentDetailNote);
        startDateTextField = findViewById(R.id.assessmentEditStartDate);
        endDateTextField = findViewById(R.id.assessmentEditEndDate);
        selectAssessmentStatus = findViewById(R.id.assessmentStatusSpinner);
        selectHasDateRange = findViewById(R.id.useDateRangeSwitch);

        // Labels
        TextView editNameLabel = findViewById(R.id.assessmentNameLabel);
        editNameLabel.setText(R.string.name);
        TextView editStartDateLabel = findViewById(R.id.assessmentStartDateLabel);
        editStartDateLabel.setText(R.string.start_date_label);
        TextView editEndDateLabel = findViewById(R.id.assessmentEndDateLabel);
        editEndDateLabel.setText(R.string.end_date_label);
        selectHasDateRange.setText(R.string.use_date_range_label);

        selectHasDateRange.setChecked(false);
    }

    private void setStartDate(@NonNull Intent intent){
        if (null != assessment && INIT_DATE < assessment.getStartDate()) {
            endDateTextField.setText(EasyDate.getFormat_ddMMMyyyy(assessment.getStartDate()));
        }

        startDatePickDialog = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                startDateTextField.setText(EasyDate.getFormat_ddMMMyyyy(year, monthOfYear, dayOfMonth));
            }
        };

        startDateTextField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cStartDate = Calendar.getInstance();
                cStartDate.setTimeInMillis(EasyDate.getDate(startDateTextField.getText().toString()));
                new DatePickerDialog(AssessmentDetails.this, startDatePickDialog,
                        cStartDate.get(Calendar.YEAR),
                        cStartDate.get(Calendar.MONTH),
                        cStartDate.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    private void setEndDate(@NonNull Intent intent){
        if (null != assessment && INIT_DATE < assessment.getEndDate()) {
            endDateTextField.setText(EasyDate.getFormat_ddMMMyyyy(assessment.getEndDate()));
        }

        endDatePickDialog = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {

                endDateTextField.setText(EasyDate.getFormat_ddMMMyyyy(year, monthOfYear, dayOfMonth));
            }
        };

        endDateTextField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cEndDate = Calendar.getInstance();
                cEndDate.setTimeInMillis(EasyDate.getDate(endDateTextField.getText().toString()));

                new DatePickerDialog(AssessmentDetails.this, endDatePickDialog,
                        cEndDate.get(Calendar.YEAR),
                        cEndDate.get(Calendar.MONTH),
                        cEndDate.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    private void setStatus(@NonNull Intent intent){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            assessment.setStatus((AssessmentStatus) intent.getSerializableExtra(ASSESSMENT_STATUS_KEY, AssessmentStatus.class));
        } else {
            assessment.setStatus((AssessmentStatus) intent.getSerializableExtra(COURSE_STATUS_KEY));
        }
        SpinnerAdapter spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, AssessmentStatus.values());
        selectAssessmentStatus.setAdapter(spinnerAdapter);
        int index = null != assessment.getStatus() ? assessment.getStatusAsOrdinal() : PROJECTED.ordinal();
        selectAssessmentStatus.setSelection(index);
        selectAssessmentStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                assessment.setStatus(AssessmentStatus.getStatusByOrdinal(i));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                assessment.setStatus(null);
            }
        });
    }
}
