package com.evanknight.scheduleu.db;


import android.app.Application;

import androidx.annotation.Nullable;

import com.evanknight.scheduleu.dao.AssessmentDAO;
import com.evanknight.scheduleu.dao.CourseDAO;
import com.evanknight.scheduleu.dao.InstructorDAO;
import com.evanknight.scheduleu.dao.TermDAO;
import com.evanknight.scheduleu.entities.Assessment;
import com.evanknight.scheduleu.entities.Course;
import com.evanknight.scheduleu.entities.Instructor;
import com.evanknight.scheduleu.entities.Term;
import com.evanknight.scheduleu.util.TermValidator;
import com.evanknight.scheduleu.util.CourseValidator;
import com.evanknight.scheduleu.util.AssessmentValidator;
import com.evanknight.scheduleu.util.InstructorValidator;
import com.evanknight.scheduleu.util.Utils;
import com.evanknight.scheduleu.util.Validator;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

// TODO: Make default constructor private to force RepositoryOperations and extend this to RepositoryOperations
public class SURepository {
    private final int SLEEP_LEN = 1000;
    //List<? extends SUObject> returnList;
    private final TermDAO mTermDAO;
    private final CourseDAO mCourseDAO;
    private final AssessmentDAO mAssessmentDAO;
    private final InstructorDAO mInstructorDAO;

    private List<Term> mAllTerms;
    private List<Course> mAllCourses;
    private List<Assessment> mAllAssessments;
    private List<Instructor> mAllInstructors;

    private static final int NUMBER_OF_THREADS = 8;
    static final ExecutorService dbExecutorService = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public SURepository(Application app){
        ScheduleUDatabaseBuilder db = ScheduleUDatabaseBuilder.getDatabase(app);
        mTermDAO = db.termDAO();
        mCourseDAO = db.courseDAO();
        mAssessmentDAO = db.assessmentDAO();
        mInstructorDAO = db.instructorDAO();
    }

    public void resetDB() {
        if (null != mAllTerms) {
            mAllTerms.clear();
        }
        if (null != mAllCourses) {
            mAllCourses.clear();
        }
        if (null != mAllAssessments) {
            mAllAssessments.clear();
        }
        if (null != mAllInstructors) {
            mAllInstructors.clear();
        }

        mTermDAO.resetTable();
        mCourseDAO.resetTable();
        mAssessmentDAO.resetTable();
        mInstructorDAO.resetTable();
    }

    public List<Term> getAllTerms(){
        dbExecutorService.execute(() -> {
            mAllTerms = mTermDAO.getAllTerms();
        });
        try{
            Thread.sleep(SLEEP_LEN);
        } catch (InterruptedException ex){
            ex.printStackTrace();
        }
        return mAllTerms;
    }

    public Term getTerm(int termID){
        return mTermDAO.getTermByID(termID);
    }
    public List<Course> getAllCourses(){
        dbExecutorService.execute(() -> {
            mAllCourses = mCourseDAO.getAllCourses();
        });
        try{
            Thread.sleep(SLEEP_LEN);
        } catch (InterruptedException ex){
            ex.printStackTrace();
        }
        return mAllCourses;
    }

    public Course getCourse(int courseID){
        return mCourseDAO.getCourseByID(courseID);
    }


    public List<Assessment> getAllAssessments(){
        dbExecutorService.execute(() -> {
            mAllAssessments = mAssessmentDAO.getAllItems();
        });
        try{
            Thread.sleep(SLEEP_LEN);
        } catch (InterruptedException ex){
            ex.printStackTrace();
        }
        return mAllAssessments;
    }

    public Assessment getAssessment(int assessmentID){
        return mAssessmentDAO.getAssessmentByID(assessmentID);
    }

    public List<Instructor> getAllInstructors(){
        dbExecutorService.execute(() -> {
            mAllInstructors = mInstructorDAO.getAllInstructors();
        });
        try{
            Thread.sleep(SLEEP_LEN);
        } catch (InterruptedException ex){
            ex.printStackTrace();
        }
        return mAllInstructors;
    }

    public @Nullable Instructor getInstructor(int instructorID){
        if (null != mInstructorDAO.getInstructorByID(instructorID)) {
            return mInstructorDAO.getInstructorByID(instructorID);
        }
        return null;
    }

//    public <T extends SUObject> List<? extends SUObject> getAllItems(T t) {
//        if (t == null) {
//            returnList = new ArrayList<T>();
//            return returnList;
//        }
//        EntityTypeID entity = t.getEntityTypeID();
//        switch (entity){
//            case TERM_ENTITY:
//                dbExecutorService.execute(() -> {
//                    returnList = mTermDAO.getAllItems();
//                });
//                break;
//            case COURSE_ENTITY:
//                dbExecutorService.execute(() -> {
//                    returnList = mCourseDAO.getAllItems();
//                });
//                break;
//            case ASSESSMENT_ENTITY:
//                dbExecutorService.execute(() -> {
//                    returnList = mAssessmentDAO.getAllItems();
//                });
//                break;
//            default:
//                return null;
//        }
//
//        try{
//            Thread.sleep(SLEEP_LEN);
//        } catch (InterruptedException ex){
//            ex.printStackTrace();
//        }
//        return returnList;
//    }

    public boolean insert(Validator v){
        boolean success;
        try {
            if (TermValidator.class == v.getClass()) {
                if (v.insert().getValidation()){
                    TermValidator tv = (TermValidator) v;
                    mTermDAO.insert(tv.getTerm());
                    Logger.getLogger("SU_Repository").info("[SURepository.insert] New Term Successfully inserted into SU db");
                } else {
                    Logger.getLogger("SU_Repository").warning("[SURepository.insert] Term Validation Failed");
                    Logger.getLogger("SU_Repository").warning(Arrays.toString(Thread.currentThread().getStackTrace()));
                }
            } else if (CourseValidator.class == v.getClass()){
                if ( v.insert().getValidation()) {
                    CourseValidator cv = (CourseValidator) v;
                    mCourseDAO.insert(cv.getCourse());
                    Logger.getLogger("SU_Repository").info("[SURepository.insert] New Course Successfully inserted into SU db");
                } else {
                    Logger.getLogger("SU_Repository").warning("[SURepository.insert] Course Validation Failed" + Utils.formattedTrace(Thread.currentThread().getStackTrace()));
                }
            } else if (AssessmentValidator.class == v.getClass() ){
                if (v.insert().getValidation()){
                    AssessmentValidator av = (AssessmentValidator) v;
                    mAssessmentDAO.insert(av.getAssessment());
                    Logger.getLogger("SU_Repository").info("[SURepository.insert] New Assessment Successfully inserted into SU db");
                }  else {
                    Logger.getLogger("SU_Repository").warning("[SURepository.insert] Assessment Validation Failed");
                    Logger.getLogger("SU_Repository").warning(Arrays.toString(Thread.currentThread().getStackTrace()));
                }
            } else if (InstructorValidator.class == v.getClass()){
                if (v.insert().getValidation()) {
                    InstructorValidator iv = (InstructorValidator) v;
                    mInstructorDAO.insert(iv.getInstructor());
                    Logger.getLogger("SU_Repository").info("[SURepository.insert] New Instructor Successfully inserted into SU db");
                } else {
                    Logger.getLogger("SU_Repository").warning("[SURepository.insert] Instructor Validation Failed");
                    Logger.getLogger("SU_Repository").warning(Arrays.toString(Thread.currentThread().getStackTrace()));
                }
            } else {
                Logger.getGlobal().warning("Data provided does not match any known entity model. Operation Insert New Record suspended");
                return false;
            }
            Thread.sleep(SLEEP_LEN);
            success = true;
        } catch (InterruptedException e) {
            e.printStackTrace();
            success = false;
        }
        return success;
    }

//    public void insert(Validator v){
//        mCourseDAO.insert(course);
//        try {
//            Thread.sleep(SLEEP_LEN);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public void insert(Validator v) {
//        mAssessmentDAO.insert(assessment);
//        try {
//            Thread.sleep(SLEEP_LEN);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public void insert(Validator v) {
//        mInstructorDAO.insert(instructor);
//        try {
//            Thread.sleep(SLEEP_LEN);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//    }

//    public <T extends SUObject> void insert(T t) {
//        dbExecutorService.execute(()->{
//            if (Term.class == t.getClass()){
//                mTermDAO.insert((Term) t);
//            } else if (Course.class == t.getClass()){
//                mCourseDAO.insert((Course) t);
//            } else if (Assessment.class == t.getClass()){
//                mAssessmentDAO.insert((Assessment) t);
//            } else if (Instructor.class == t.getClass()){
//                mInstructorDAO.insert((Instructor) t);
//            }
//        });
//        try {
//            Thread.sleep(SLEEP_LEN);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//    }

    public boolean update(Validator v){
        boolean success;
        try {
            if (TermValidator.class == v.getClass()) {
                if (v.update().getValidation()) {
                    TermValidator tv = (TermValidator) v;
                    mTermDAO.update(tv.getTerm());
                }  else {
                    Logger.getLogger("SU_Repository").info("[SURepository.insert] Entry Validation Failed");
                }
            } else if (CourseValidator.class == v.getClass()) {
                if (v.update().getValidation()) {
                    CourseValidator cv = (CourseValidator) v;
                    mCourseDAO.update(cv.getCourse());
                }
            } else if (AssessmentValidator.class == v.getClass()) {
                if (v.update().getValidation()) {
                    AssessmentValidator av = (AssessmentValidator) v;
                    mAssessmentDAO.update(av.getAssessment());
                }  else {
                    Logger.getLogger("SU_Repository").info("[SURepository.insert] Entry Validation Failed");
                }
            } else if (InstructorValidator.class == v.getClass()) {
                if (v.update().getValidation()) {
                    InstructorValidator iv = (InstructorValidator) v;
                    mInstructorDAO.update(iv.getInstructor());
                }  else {
                    Logger.getLogger("SU_Repository").info("[SURepository.insert] Entry Validation Failed");
                }
            } else {
                Logger.getGlobal().warning("Data provided does not match any known entity model. Operation Update Record suspended");
                return false;
            }
            Thread.sleep(SLEEP_LEN);
            success = true;
        } catch (InterruptedException e) {
            e.printStackTrace();
            success = false;
        }
        return success;
    }

//    public void update(Validator v){
//        mCourseDAO.update(course);
//        try {
//            Thread.sleep(SLEEP_LEN);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public void update(Validator v) {
//        mAssessmentDAO.update(assessment);
//        try {
//            Thread.sleep(SLEEP_LEN);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public void update(Validator v) {
//        mInstructorDAO.update(instructor);
//        try {
//            Thread.sleep(SLEEP_LEN);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//    }

//    public <T extends SUObject> void update(T t) {
//        dbExecutorService.execute(()->{
//            if (Term.class == t.getClass()){
//                mTermDAO.update((Term) t);
//            } else if (Course.class == t.getClass()){
//                mCourseDAO.update((Course) t);
//            } else if (Assessment.class == t.getClass()){
//                mAssessmentDAO.update((Assessment) t);
//            } else if (Instructor.class == t.getClass()){
//                mInstructorDAO.update((Instructor) t);
//            }
//        });
//        try {
//            Thread.sleep(SLEEP_LEN);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//    }

    public boolean delete(Validator v){
        boolean success;
        try {
            if (TermValidator.class == v.getClass()){
                if (v.delete().getValidation()) {
                    TermValidator tv = (TermValidator) v;
                    mTermDAO.delete(tv.getTerm());
                }  else {
                    Logger.getLogger("SU_Repository").info("[SURepository.insert] Entry Validation Failed");
                }
            } else if (CourseValidator.class == v.getClass()){
                if (v.delete().getValidation()) {
                    CourseValidator cv = (CourseValidator) v;
                    mCourseDAO.delete(cv.getCourse());
                }  else {
                    Logger.getLogger("SU_Repository").info("[SURepository.insert] Entry Validation Failed");
                }
            } else if (AssessmentValidator.class == v.getClass()){
                if (v.delete().getValidation()) {
                    AssessmentValidator av = (AssessmentValidator) v;
                    mAssessmentDAO.delete(av.getAssessment());
                }
            } else if (InstructorValidator.class == v.getClass()){
                if (v.delete().getValidation()) {
                    InstructorValidator iv = (InstructorValidator) v;
                    mInstructorDAO.delete(iv.getInstructor());
                }  else {
                    Logger.getLogger("SU_Repository").info("[SURepository.insert] Entry Validation Failed");
                }
            } else {
                Logger.getGlobal().warning("Data provided does not match any known entity model. Operation Delete Record suspended");
                return false;
            }
            Thread.sleep(SLEEP_LEN);
            success = true;
        } catch (InterruptedException e) {
            e.printStackTrace();
            success = false;
        }
        return success;
    }

//    public void deleteCourse(Validator v){
//        mCourseDAO.delete(course);
//        try {
//            Thread.sleep(SLEEP_LEN);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public void deleteAssessment(Validator v) {
//        mAssessmentDAO.delete(assessment);
//        try {
//            Thread.sleep(SLEEP_LEN);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public void deleteInstructor(Validator v) {
//        mInstructorDAO.delete(instructor);
//        try {
//            Thread.sleep(SLEEP_LEN);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//    }

//    public <T extends SUObject> void delete(T t) {
//        dbExecutorService.execute(()->{
//            if (Term.class == t.getClass()){
//                mTermDAO.delete((Term) t);
//            } else if (Course.class == t.getClass()){
//                mCourseDAO.delete((Course) t);
//            } else if (Assessment.class == t.getClass()){
//                mAssessmentDAO.delete((Assessment) t);
//            } else if (Instructor.class == t.getClass()){
//                mInstructorDAO.delete((Instructor) t);
//            }
//        });
//        try {
//            Thread.sleep(SLEEP_LEN);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//    }
}
