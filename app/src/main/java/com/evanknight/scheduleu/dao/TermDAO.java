package com.evanknight.scheduleu.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.evanknight.scheduleu.entities.Term;

import java.util.List;

@Dao
public interface TermDAO{

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Term term);

    @Update
    void update(Term term);

    @Delete
    void delete(Term term);

    @Query("SELECT * FROM term_table ORDER BY itemID ASC")
    List<Term> getAllTerms();

    @Query("SELECT * FROM term_table WHERE itemID=:itemID")
    Term getTermByID(int itemID);

    @Query("DELETE FROM term_table ")
    void resetTable();
}
