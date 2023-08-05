package com.evanknight.scheduleu.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.evanknight.scheduleu.entities.Instructor;
import com.evanknight.scheduleu.entities.Term;

import java.util.List;

@Dao
public interface InstructorDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Instructor instructor);

    @Update
    void update(Instructor instructor);

    @Delete
    void delete(Instructor instructor);

    @Query("SELECT * FROM instructor_table ORDER BY itemID ASC")
    List<Instructor> getAllInstructors();

    @Query("SELECT * FROM instructor_table WHERE itemID=:itemID")
    Instructor getInstructorByID(int itemID);

    @Query("DELETE FROM instructor_table")
    void resetTable();
}
