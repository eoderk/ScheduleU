package com.evanknight.scheduleu.util;

import android.app.Application;

import com.evanknight.scheduleu.db.SURepository;
import com.evanknight.scheduleu.entities.Term;
import com.evanknight.scheduleu.entities.Course;
import com.evanknight.scheduleu.entities.Assessment;
import com.evanknight.scheduleu.entities.Instructor;

import java.util.ArrayList;
import java.util.logging.Logger;

public class RepositoryOperations {
    private static SURepository sur;
    private static Application app;
//    private static ArrayList<Term> termsList = new ArrayList<>();
//    private static ArrayList<Course> courseList = new ArrayList<>();
//    private static ArrayList<Assessment> assessmentList = new ArrayList<>();
//    private static ArrayList<Instructor> instructorList = new ArrayList<>();

    private RepositoryOperations() {}

    private static class RepoOpsHelper {
        private static final RepositoryOperations INSTANCE = new RepositoryOperations();
    }

    public static RepositoryOperations getInstance() {
        return RepoOpsHelper.INSTANCE;
    }
    public static void initializeRepository(Application a) throws NullPointerException {
        Logger.getLogger("RepositoryOperations").info("Initializing Repository, fetching database");
        if (null == a) { throw new NullPointerException(); }
        app = a;
        sur = new SURepository(app);
//        termsList.addAll(sur.getAllTerms());
//        courseList.addAll(sur.getAllCourses());
//        assessmentList.addAll(sur.getAllAssessments());
//        instructorList.addAll(sur.getAllInstructors());
        Logger.getLogger("RepositoryOperations").info("Database started, sur running = " + (null != sur ? "RUNNING" : "STOPPED"));
    }

    public static void resetDataBase(){
//        termsList.clear();
//        courseList.clear();
//        assessmentList.clear();
//        instructorList.clear();

        sur.resetDB();
    }

    // Term Operations
    public ArrayList<Term> getTermsList(){
        return new ArrayList<>(sur.getAllTerms());
    }
    public Term getTermByID(int id) { return sur.getTerm(id); }

    public boolean termRecordExists(int id){
        return null != getTermByID(id);
    }
    public boolean termListContains(Term t) {
        if (sur.getAllInstructors().isEmpty()){
            return false;
        }
        return t.equals(getTermByID(t.getItemID()));
    }

    // Course Operations
    public ArrayList<Course> getCourseList(){
        if (null == sur){
            Logger.getLogger("RepositoryOperations").config(">>>>>>>>>>>>>>>>>>>>>> Database Dead >>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        }
        return new ArrayList<>(sur.getAllCourses());
    }
    public ArrayList<Course> getTermCourses(int termID) {
        if (null == sur){
            Logger.getLogger("RepositoryOperations").config(">>>>>>>>>>>>>>>>>>>>>> Database Dead >>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        }
        ArrayList<Course> filteredCourseList = new ArrayList<>();
        for (Course c : sur.getAllCourses()) {
            if (termID == c.getTermID()) {
                filteredCourseList.add(c);
            }
        }
        return filteredCourseList;
    }
    public Course getCourseByID(int id) { return sur.getCourse(id);}

    public boolean courseRecordExists(int id){
        return null != getCourseByID(id);
    }
    public boolean courseListContains(Course c) {
        if (sur.getAllCourses().isEmpty()){
            return false;
        }
        return c.equals(sur.getCourse(c.getItemID()));
    }

    // Assessment Operations
    public ArrayList<Assessment> getAssessmentList(){
        if (null == sur){
            Logger.getLogger("RepositoryOperations").config(">>>>>>>>>>>>>>>>>>>>>> Database Dead >>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        }
        return new ArrayList<>(sur.getAllAssessments());
    }
    public ArrayList<Assessment> getCourseAssessments(int courseID){
        if (null == sur){
            Logger.getLogger("RepositoryOperations").config(">>>>>>>>>>>>>>>>>>>>>> Database Dead >>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        }
        ArrayList<Assessment> filteredAssessmentList = new ArrayList<>();
        for (Assessment a : sur.getAllAssessments()){
            if (courseID == a.getCourseID()){
                filteredAssessmentList.add(a);
            }
        }
        return filteredAssessmentList;
    }

    public Assessment getAssessmentByID(int id) { return sur.getAssessment(id);}

    public boolean assessmentRecordExists(int id){
        return null != sur.getAssessment(id);
    }

    public boolean assessmentListContains(Assessment a) {
        if (sur.getAllAssessments().isEmpty()){
            return false;
        }
        return a.equals(sur.getAssessment(a.getItemID()));
    }

    // Instructor Operations
    public ArrayList<Instructor> getInstructorList(){
        if (null == sur){
            Logger.getLogger("RepositoryOperations").config(">>>>>>>>>>>>>>>>>>>>>> Database Dead >>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        }
        return new ArrayList<>(sur.getAllInstructors());
    }

    public ArrayList<Course> getInstructorsCoursesTaught(int instructorID){
        if (null == sur){
            Logger.getLogger("RepositoryOperations").config(">>>>>>>>>>>>>>>>>>>>>> Database Dead >>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        }
        ArrayList<Course> filteredCourses = new ArrayList<>();
        for (Course c : sur.getAllCourses()){
            if (instructorID == c.getInstructorID()){
                filteredCourses.add(c);
            }
        }
        return filteredCourses;
    }

    public Instructor getInstructorByID(int id) { return sur.getInstructor(id); }

    public boolean instructorListContains(Instructor inst){
        if (sur.getAllInstructors().isEmpty()){
            return false;
        } else {
            return getInstructorList().contains(inst);
        }
    }

    public boolean insert(Validator validator){
        if (validator.getValidation()) {
            return sur.insert(validator);
        }
        return false;
    }

    public boolean update(Validator validator){
        if (validator.getValidation()) {
            return sur.update(validator);
        }
        return false;
    }

    public boolean delete(Validator validator){
        if (validator.getValidation()) {
            return sur.delete(validator);
        }
        return false;
    }

    // TODO: Remove if not used
    private boolean dbServiceConnected() {
        return null != sur;
    }

    public int getNextTermIdNumber() {
        ArrayList<Term> list = new ArrayList<>(sur.getAllTerms());
        if (list.isEmpty()){
            return 1;
        }
        return list.get(list.size() -1).getItemID() + 1;
    }

    public int getNextCourseIdNumber(){
        ArrayList<Course> list = new ArrayList<>(sur.getAllCourses());
        if (list.isEmpty()){
            return 1;
        }
        return list.get(list.size() -1).getItemID() + 1;
    }

    public int getNextAssessmentIdNumber(){
        ArrayList<Assessment> list = new ArrayList<>(sur.getAllAssessments());
        if (list.isEmpty()){
            return 1;
        }
        return list.get(list.size() -1).getItemID() + 1;
    }

    public int getNextInstructorIdNumber(){
        ArrayList<Instructor> list = new ArrayList<>(sur.getAllInstructors());
        if (list.isEmpty()){
            return 1;
        }
        return list.get(list.size() -1).getItemID() + 1;
    }
}


