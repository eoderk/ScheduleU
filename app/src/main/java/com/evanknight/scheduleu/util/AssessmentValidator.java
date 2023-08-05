package com.evanknight.scheduleu.util;

import static com.evanknight.scheduleu.util.Constants.ASSESSMENT;
import static com.evanknight.scheduleu.util.Constants.ASSESSMENT_ID_KEY;
import static com.evanknight.scheduleu.util.Constants.CHILD_LIST_KEY;
import static com.evanknight.scheduleu.util.Constants.END_DATE_KEY;
import static com.evanknight.scheduleu.util.Constants.NAME_KEY_1;
import static com.evanknight.scheduleu.util.Constants.START_DATE_KEY;
import static com.evanknight.scheduleu.util.Constants.STATUS_KEY;
import static com.evanknight.scheduleu.util.Constants.TERM;
import static com.evanknight.scheduleu.util.Constants.TERM_ID_KEY;
import static com.evanknight.scheduleu.util.Constants.TYPE_ID_KEY;

import com.evanknight.scheduleu.entities.Assessment;
import com.evanknight.scheduleu.entities.SUObject;
import com.evanknight.scheduleu.entities.Term;

import java.util.Arrays;
import java.util.Date;
import java.util.TreeMap;
import java.util.logging.Logger;

public class AssessmentValidator implements Validator{
    Assessment a;
    boolean valid = true;

    private final TreeMap<String, String> invalidFields = new TreeMap<>();

    public AssessmentValidator(SUObject suObject){
        if (null != suObject && Assessment.class == suObject.getClass()){
            a = new Assessment().assessmentConverter(suObject);
            valid = true;
        } else {
            invalidFields.put(ASSESSMENT, "Object to validate not an instance of Assessment");
            valid = false;
        }
    }

    @Override
    public boolean getValidation() { return (null != a && valid); }

    @Override
    public TreeMap<String, String> invalidAttributes() {
        return invalidFields;
    }

    public Assessment getAssessment(){ return a; }

    @Override
    public Validator insert() {
        if (!valid){
            return this;
        }
        if (0 < a.getItemID() && RepositoryOperations.getInstance().assessmentListContains(a)){
            invalidFields.put(ASSESSMENT_ID_KEY, "Invalid Identifier, Assessment ID already in use");
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
        if (!RepositoryOperations.getInstance().assessmentListContains(a)){
            invalidFields.put(ASSESSMENT_ID_KEY, "Assessment to update was not found");
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
        if (!RepositoryOperations.getInstance().assessmentListContains(a)) {
            invalidFields.put(ASSESSMENT_ID_KEY, "Invalid ID, indicated assessment does not exist");
            valid = false;
        }
        return this;
    }

    private void validateAllOtherFields(){

        if (EasyDate.todayInMilli() > a.getStartDate()) {
            invalidFields.put(START_DATE_KEY, "Date is in the past");
            valid = false;
        }
        if (a.getStartDate() > a.getEndDate()) {
            invalidFields.put(END_DATE_KEY, "End Date must happen on or after Start Date");
            valid = false;
        }
        if (null == a.getEntityName()) {
            invalidFields.put(NAME_KEY_1, "Name field is blank");
            valid = false;
        }
        if (null == a.getEntityTypeID()) {
            invalidFields.put(TYPE_ID_KEY, "Assessment descriptor is undefined");
            valid = false;
        } else if (EntityTypeID.TERM_ENTITY != a.getEntityTypeID()){
            invalidFields.put(TYPE_ID_KEY, "TypeID mismatch");
            valid = false;
        }
        if (null == a.getStatus()){
            invalidFields.put(STATUS_KEY, "Assessment Status is undefined");
            valid = false;
        }
    }
}
