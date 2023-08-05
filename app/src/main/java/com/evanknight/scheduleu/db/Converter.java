package com.evanknight.scheduleu.db;

import androidx.annotation.NonNull;
import androidx.room.TypeConverter;

import com.evanknight.scheduleu.util.TermStatus;
import com.evanknight.scheduleu.util.CourseStatus;
import com.evanknight.scheduleu.util.AssessmentStatus;

import java.util.Calendar;
import java.util.Date;

public class Converter {
    // Date Converter
//    @TypeConverter
//    public static Date toDate(Long value) {
//        Calendar c = Calendar.getInstance();
//        c.setTimeInMillis(value);
//        return c.getTime();
//    }
//    @TypeConverter
//    public static Long toLongForm(Date date) {
//        return date.getTime();
//    }

    // Term Status Converter
    @TypeConverter
    public static TermStatus toTermStatus(String s){
        return TermStatus.getByName(s);
    }
    @TypeConverter
    public static String fromTermStatus(@NonNull TermStatus ts){
        return ts.name;
    }

    // Course Status Converter
    @TypeConverter
    public static CourseStatus toCourseStatus(String s){
        return CourseStatus.getByName(s);
    }
    @TypeConverter
    public static String fromCourseStatus(@NonNull CourseStatus cs){
        return cs.name;
    }

    // Assessment Status Converter
    @TypeConverter
    public static AssessmentStatus toAssessmentStatus(String s){
        return AssessmentStatus.getByName(s);
    }
    @TypeConverter
    public static String fromAssessmentStatus(@NonNull AssessmentStatus as){
        return as.name;
    }
}
