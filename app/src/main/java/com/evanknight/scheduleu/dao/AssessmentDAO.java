package com.evanknight.scheduleu.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.evanknight.scheduleu.entities.Assessment;
import com.evanknight.scheduleu.entities.Term;

import java.util.List;
@Dao
public interface AssessmentDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    public abstract void insert(Assessment assessment);

    @Update
    public abstract void update(Assessment assessment);

    @Delete
    void delete(Assessment assessment);

    @Query("SELECT * FROM assessment_table ORDER BY itemID ASC")
    List<Assessment> getAllItems();

    @Query("SELECT * FROM assessment_table WHERE itemID=:itemID")
    Assessment getAssessmentByID(int itemID);

    @Query("DELETE FROM assessment_table")
    void resetTable();
}
