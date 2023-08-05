package com.evanknight.scheduleu.util;

import static com.evanknight.scheduleu.util.Constants.*;

import com.evanknight.scheduleu.entities.SUObject;
import com.evanknight.scheduleu.entities.Term;

import java.util.TreeMap;

public class TermValidator implements Validator{
    private Term t;
    private boolean valid;

    private final TreeMap<String, String> invalidFields = new TreeMap<>();

    public TermValidator(SUObject suObject){
        if (null != suObject && Term.class == suObject.getClass()){
            t = new Term().termConverter(suObject);
            valid = true;
        } else {
            invalidFields.put(TERM, "Object to validate not an instance of Term");
            valid = false;
        }
    }

    @Override
    public boolean getValidation() { return (null != t && valid); }

    @Override
    public TreeMap<String, String> invalidAttributes() {
        return invalidFields;
    }

    public Term getTerm(){ return t; }

    @Override
    public Validator insert() {
        if (!valid){
            return this;
        }
        if (0 < t.getItemID() && RepositoryOperations.getInstance().termListContains(t)){
            invalidFields.put(TERM_ID_KEY, "Invalid Identifier, Term ID already in use");
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
        if (!RepositoryOperations.getInstance().termListContains(t)){
            invalidFields.put(TERM_ID_KEY, "Term to update was not found");
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
        if (!RepositoryOperations.getInstance().termListContains(t)) {
            invalidFields.put(TERM_ID_KEY, "Invalid command, indicated term does not exist");
            valid = false;
        }
        if (!RepositoryOperations.getInstance().getTermCourses(t.getItemID()).isEmpty()){
            invalidFields.put(CHILD_LIST_KEY, "This Term's Course List contains courses");
            valid = false;
        }
        return this;
    }

    private void validateAllOtherFields(){

        if (EasyDate.todayInMilli() > t.getStartDate()) {
            invalidFields.put(START_DATE_KEY, "Date is in the past");
            valid = false;
        }
        if (t.getStartDate() > t.getEndDate()) {
            invalidFields.put(END_DATE_KEY, "End Date must happen on or after Start Date");
            valid = false;
        }
        if (null == t.getEntityName()) {
            invalidFields.put(NAME_KEY_1, "Name field is blank");
            valid = false;
        }
        if (null == t.getEntityTypeID()) {
            invalidFields.put(TYPE_ID_KEY, "Term descriptor is undefined");
            valid = false;
        } else if (EntityTypeID.TERM_ENTITY != t.getEntityTypeID()){
            invalidFields.put(TYPE_ID_KEY, "TypeID mismatch");
            valid = false;
        }
        if (null == t.getStatus()){
            invalidFields.put(STATUS_KEY, "Term Status is undefined");
            valid = false;
        }
    }
}
