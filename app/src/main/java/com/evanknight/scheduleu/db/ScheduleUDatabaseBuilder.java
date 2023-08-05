package com.evanknight.scheduleu.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;

import com.evanknight.scheduleu.dao.AssessmentDAO;
import com.evanknight.scheduleu.dao.CourseDAO;
import com.evanknight.scheduleu.dao.InstructorDAO;
import com.evanknight.scheduleu.dao.TermDAO;
import com.evanknight.scheduleu.entities.Assessment;
import com.evanknight.scheduleu.entities.Course;
import com.evanknight.scheduleu.entities.Instructor;
import com.evanknight.scheduleu.entities.Term;

@Database(entities = {Term.class, Course.class, Assessment.class, Instructor.class}, version = 8, exportSchema = false)
@TypeConverters(Converter.class)
public abstract class ScheduleUDatabaseBuilder extends RoomDatabase {
    public abstract TermDAO termDAO();
    public abstract CourseDAO courseDAO();
    public abstract AssessmentDAO assessmentDAO();
    public abstract InstructorDAO instructorDAO();

    private static volatile ScheduleUDatabaseBuilder INSTANCE;

    static ScheduleUDatabaseBuilder getDatabase(final Context context){
        if(INSTANCE == null){
            synchronized (ScheduleUDatabaseBuilder.class){
                if (INSTANCE == null){
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), ScheduleUDatabaseBuilder.class, "SUDatabase.db")
                            .fallbackToDestructiveMigration()
                            .allowMainThreadQueries()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
