package com.evanknight.scheduleu.util;

import static com.evanknight.scheduleu.util.Constants.CHILD_LIST_KEY;
import static com.evanknight.scheduleu.util.Constants.EMAIL_KEY;
import static com.evanknight.scheduleu.util.Constants.ID_KEY;
import static com.evanknight.scheduleu.util.Constants.INSTRUCTOR;
import static com.evanknight.scheduleu.util.Constants.INSTRUCTOR_ID_KEY;
import static com.evanknight.scheduleu.util.Constants.NAME_KEY_1;
import static com.evanknight.scheduleu.util.Constants.NAME_KEY_2;
import static com.evanknight.scheduleu.util.Constants.PHONE_NUMBER_KEY;
import static com.evanknight.scheduleu.util.Constants.TERM;
import static com.evanknight.scheduleu.util.Constants.TYPE_ID_KEY;

import com.evanknight.scheduleu.entities.Assessment;
import com.evanknight.scheduleu.entities.Instructor;
import com.evanknight.scheduleu.entities.SUObject;
import com.evanknight.scheduleu.entities.Term;

import java.util.Arrays;
import java.util.TreeMap;
import java.util.logging.Logger;

public class InstructorValidator implements Validator{
    Instructor inst;
    boolean valid;

    private final TreeMap<String, String> invalidFields = new TreeMap<>();

    public InstructorValidator(SUObject suObject){
        if (null != suObject && Instructor.class == suObject.getClass()){
            inst = new Instructor().instructorConverter(suObject);
            valid = true;
        } else {
            invalidFields.put(INSTRUCTOR, "Object to validate not an instance of Instructor");
            valid = false;
        }
    }

    @Override
    public boolean getValidation() { return (null != inst && valid); }

    @Override
    public TreeMap<String, String> invalidAttributes() {
        return invalidFields;
    }

    public Instructor getInstructor(){ return inst; }

    @Override
    public Validator insert() {
        if (!valid){
            return this;
        }
        if (0 < inst.getItemID() && RepositoryOperations.getInstance().instructorListContains(inst)){
            invalidFields.put(INSTRUCTOR_ID_KEY, "Invalid Identifier, Instructor ID already in use");
            valid = false;
        }
        validateAllOtherFields();
        return this;
    }

    @Override
    public Validator update() {
        if (!valid){
            return this;
        }
        if (!RepositoryOperations.getInstance().instructorListContains(inst)){
            invalidFields.put(INSTRUCTOR_ID_KEY, "Instructor to update was not found");
            valid = false;
        }
        validateAllOtherFields();
        return this;
    }

    @Override
    public Validator delete() {
        if (!valid){
            return this;
        }
        if (!RepositoryOperations.getInstance().instructorListContains(inst)) {
            invalidFields.put(INSTRUCTOR_ID_KEY, "Invalid command, instructor does not exist");
            valid = false;
        }
        return this;
    }

    private void validateAllOtherFields() throws NullPointerException {
        if (null == inst.getEntityName()){
            invalidFields.put(NAME_KEY_1, "Instructor First Name has no value");
            valid = false;
        }
        if (null == inst.getLastName()){
            invalidFields.put(NAME_KEY_2, "Instructor Last Name has no value");
            valid = false;
        }
        if (null == inst.getEntityTypeID()) {
            invalidFields.put(TYPE_ID_KEY, "Instructor descriptor is undefined");
            valid = false;
        } else if (EntityTypeID.INSTRUCTOR_ENTITY != inst.getEntityTypeID()){
            invalidFields.put(TYPE_ID_KEY, "Type descriptor mismatch");
            valid = false;
        }
        if (null == inst.getInstructorEmail()){
            invalidFields.put(EMAIL_KEY, "Instructor email has no value");
            valid = false;
        }
        if (null == inst.getInstructorPhoneNumber()){
            invalidFields.put(PHONE_NUMBER_KEY, "Instructor phone number has no value");
            valid = false;
        }
    }
}
