package com.evanknight.scheduleu.util;

import static com.evanknight.scheduleu.util.Constants.*;

import com.evanknight.scheduleu.entities.Course;
import com.evanknight.scheduleu.entities.SUObject;
import com.evanknight.scheduleu.entities.Term;

import java.util.TreeMap;

public class CourseValidator implements Validator<CourseValidator>{
    Course c;
    boolean valid;

    TreeMap<String, String> invalidFields = new TreeMap<>();

    public CourseValidator(SUObject suObject){
        if (null != suObject && Course.class == suObject.getClass()){
            c = new Course().courseConverter(suObject);
            valid = true;
        } else {
            invalidFields.put(COURSE, "Object to validate not an instance of Course");
            valid = false;
        }
    }

    @Override
    public boolean getValidation() { return (null != c && valid); }

    @Override
    public TreeMap<String, String> invalidAttributes() {
        return invalidFields;
    }

    public Course getCourse(){ return c; }

    @Override
    public CourseValidator insert() {
        if (!valid){
            return this;
        }
        if (0 < c.getItemID() && RepositoryOperations.getInstance().courseListContains(c)){
            invalidFields.put(COURSE_ID_KEY, "Invalid Identifier, Course ID already in use");
            valid = false;
        }
        validateAllOtherFields();

        return this;
    }

    @Override
    public CourseValidator update() {
        if (!valid){
            return this;
        }
        if (!RepositoryOperations.getInstance().courseListContains(c)){
            invalidFields.put(COURSE_ID_KEY, "Course to update was not found");
            valid = false;
        }
        validateAllOtherFields();
        return this;
    }

    @Override
    public CourseValidator delete() {
        if (!valid){
            return this;
        }
        if (!RepositoryOperations.getInstance().courseListContains(c)) {
            invalidFields.put(COURSE_ID_KEY, "Invalid command, indicated course does not exist");
            valid = false;
        }
        if (!RepositoryOperations.getInstance().getCourseAssessments(c.getItemID()).isEmpty()){
            invalidFields.put(CHILD_LIST_KEY, "This Course's Assessment List still contains assessments");
            valid = false;
        }
        return this;
    }

    private void validateAllOtherFields() throws NullPointerException{
        if (EasyDate.todayInMilli() > c.getStartDate()) {
            invalidFields.put(START_DATE_KEY, "Date is in the past");
            valid = false;
        }
        if (c.getStartDate() > c.getEndDate()) {
            invalidFields.put(END_DATE_KEY, "End Date must happen on or after Start Date");
            valid = false;
        }
        if (null == c.getEntityName()){
            invalidFields.put(NAME_KEY_1, "Course name has no value");
            valid = false;
        }
        if (null == c.getEntityTypeID()){
            invalidFields.put(TYPE_ID_KEY, "Course descriptor is undefined");
            valid = false;
        } else if (EntityTypeID.COURSE_ENTITY == c.getEntityTypeID()){
            invalidFields.put(TYPE_ID_KEY, "Course descriptor mismatch");
            valid = false;
        }
        if (null == c.getStatus()){
            invalidFields.put(STATUS_KEY, "Course status is undefined");
            valid = false;
        }
    }
}
