package com.evanknight.scheduleu.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.evanknight.scheduleu.entities.Course;
import com.evanknight.scheduleu.entities.Term;

import java.util.List;

@Dao
public interface CourseDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Course course);

    @Update
    void update(Course course);

    @Delete
    void delete(Course course);

    @Query("SELECT * FROM course_table ORDER BY itemID ASC")
    List<Course> getAllCourses();

    @Query("SELECT * FROM course_table WHERE itemID=:itemID")
    Course getCourseByID(int itemID);

    @Query("DELETE FROM course_table")
    void resetTable();
}
