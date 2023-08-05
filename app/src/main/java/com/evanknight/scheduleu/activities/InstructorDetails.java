package com.evanknight.scheduleu.activities;

import static com.evanknight.scheduleu.util.Constants.*;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.evanknight.scheduleu.R;
import com.evanknight.scheduleu.entities.Instructor;
import com.evanknight.scheduleu.util.InstructorValidator;
import com.evanknight.scheduleu.util.RepositoryOperations;

import java.io.Serializable;

public class InstructorDetails extends AppCompatActivity implements Serializable {
    private Instructor instructor = new Instructor();

    // Editable Fields
    private EditText editFirstName;
    private EditText editLastName;
    private EditText editEmail;
    private EditText editPhoneNumber;

    private final RepositoryOperations repoOps = RepositoryOperations.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (null == instructor){
            instructor = new Instructor();
        }
        Intent intent = getIntent();
        setContentView(R.layout.widget_activity_instructor_details_view);

        // Assigning -1 as the default ensures that if the record is missing it's ID number forces an assignment of a valid ID at Save event
        instructor.setItemID(intent.getIntExtra(INSTRUCTOR_ID_KEY, INIT_ENTITY_ID));
        if (INIT_ENTITY_ID < instructor.getItemID()) {
            instructor.setEntityName(intent.getStringExtra(INSTRUCTOR_FIRST_NAME_KEY));
            instructor.setLastName(intent.getStringExtra(INSTRUCTOR_LAST_NAME_KEY));
        }

        // Initialize Name fields and label
        TextView firstNameLabel = findViewById(R.id.instructorNameLabel);
        firstNameLabel.setText(R.string.instructor_name_component_label);
        editFirstName = findViewById(R.id.instructorEditFirstName);
        editFirstName.setText(instructor.getEntityName());
        editFirstName.setHint(R.string.first_name);

        editLastName = findViewById(R.id.instructorEditLastName);
        editLastName.setText(instructor.getLastName());
        editLastName.setHint(R.string.last_name);

        // Initialize Email field and label
        TextView emailLabel = findViewById(R.id.instructorEmailLabel);
        emailLabel.setText(R.string.email_component_label);
        instructor.setInstructorEmail(intent.getStringExtra(EMAIL_KEY));
        editEmail = findViewById(R.id.instructorEditEmail);
        editEmail.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        editEmail.setText(instructor.getInstructorEmail());
        editEmail.setHint(R.string.instructor_email_hint);

        // Initialize Phone Number field and label
        TextView phoneLabel = findViewById(R.id.instructorPoneLabel);
        phoneLabel.setText(R.string.phone_component_label);
        instructor.setInstructorPhoneNumber(intent.getStringExtra(PHONE_NUMBER_KEY));
        editPhoneNumber = findViewById(R.id.instructorEditPhoneNumber);
        editPhoneNumber.setInputType(InputType.TYPE_CLASS_PHONE);
        editPhoneNumber.setText(instructor.getInstructorEmail());
        editPhoneNumber.setHint(R.string.instructor_phone_hint);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit_details, menu);
        return super.onCreateOptionsMenu(menu);
    }
    // TODO: This class looks a little sparse make sure everything is correctly initialized and save function is configured properly
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(android.R.id.home == item.getItemId()){
            this.finish();
            return true;}
        if(R.id.detailsMenuSave == item.getItemId()){
            if (null != instructor && repoOps.instructorListContains(instructor)) {
                repoOps.update(new InstructorValidator(instructor));
            } else {
                instructor = new Instructor();
                instructor.setItemID(repoOps.getNextInstructorIdNumber());
                instructor.setEntityName(editFirstName.getText().toString());
                instructor.setLastName(editLastName.getText().toString());
                instructor.setInstructorEmail(editEmail.getText().toString());
                instructor.setInstructorPhoneNumber(editPhoneNumber.getText().toString());
                repoOps.insert(new InstructorValidator(instructor));
            }
            Intent returnData = new Intent();
            returnData.putExtra(INSTRUCTOR_ID_KEY, instructor.getItemID());
            InstructorDetails.this.setResult(Activity.RESULT_OK, returnData);
            finish();
            // InstructorDetails.this.finish();
        }
        if(R.id.detailsMenuDelete == item.getItemId()) {
            InstructorValidator iv = new InstructorValidator(instructor);
            if (repoOps.delete(iv)) {
                Toast.makeText(InstructorDetails.this, iv.getInstructor().getEntityTypeID() + " was deleted", Toast.LENGTH_LONG).show();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
