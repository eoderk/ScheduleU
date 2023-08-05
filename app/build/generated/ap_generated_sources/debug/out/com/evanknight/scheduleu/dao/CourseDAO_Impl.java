package com.evanknight.scheduleu.dao;

import android.database.Cursor;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.evanknight.scheduleu.db.Converter;
import com.evanknight.scheduleu.entities.Course;
import com.evanknight.scheduleu.util.CourseStatus;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SuppressWarnings({"unchecked", "deprecation"})
public final class CourseDAO_Impl implements CourseDAO {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<Course> __insertionAdapterOfCourse;

  private final EntityDeletionOrUpdateAdapter<Course> __deletionAdapterOfCourse;

  private final EntityDeletionOrUpdateAdapter<Course> __updateAdapterOfCourse;

  private final SharedSQLiteStatement __preparedStmtOfResetTable;

  public CourseDAO_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfCourse = new EntityInsertionAdapter<Course>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR IGNORE INTO `course_table` (`status`,`matchTermDates`,`entityNote`,`termID`,`instructorID`,`startDate`,`endDate`,`itemID`,`entityName`) VALUES (?,?,?,?,?,?,?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Course value) {
        final String _tmp = Converter.fromCourseStatus(value.getStatus());
        if (_tmp == null) {
          stmt.bindNull(1);
        } else {
          stmt.bindString(1, _tmp);
        }
        final int _tmp_1 = value.getMatchTermDates() ? 1 : 0;
        stmt.bindLong(2, _tmp_1);
        if (value.getEntityNote() == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.getEntityNote());
        }
        stmt.bindLong(4, value.getTermID());
        stmt.bindLong(5, value.getInstructorID());
        stmt.bindLong(6, value.getStartDate());
        stmt.bindLong(7, value.getEndDate());
        stmt.bindLong(8, value.getItemID());
        if (value.getEntityName() == null) {
          stmt.bindNull(9);
        } else {
          stmt.bindString(9, value.getEntityName());
        }
      }
    };
    this.__deletionAdapterOfCourse = new EntityDeletionOrUpdateAdapter<Course>(__db) {
      @Override
      public String createQuery() {
        return "DELETE FROM `course_table` WHERE `itemID` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Course value) {
        stmt.bindLong(1, value.getItemID());
      }
    };
    this.__updateAdapterOfCourse = new EntityDeletionOrUpdateAdapter<Course>(__db) {
      @Override
      public String createQuery() {
        return "UPDATE OR ABORT `course_table` SET `status` = ?,`matchTermDates` = ?,`entityNote` = ?,`termID` = ?,`instructorID` = ?,`startDate` = ?,`endDate` = ?,`itemID` = ?,`entityName` = ? WHERE `itemID` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Course value) {
        final String _tmp = Converter.fromCourseStatus(value.getStatus());
        if (_tmp == null) {
          stmt.bindNull(1);
        } else {
          stmt.bindString(1, _tmp);
        }
        final int _tmp_1 = value.getMatchTermDates() ? 1 : 0;
        stmt.bindLong(2, _tmp_1);
        if (value.getEntityNote() == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.getEntityNote());
        }
        stmt.bindLong(4, value.getTermID());
        stmt.bindLong(5, value.getInstructorID());
        stmt.bindLong(6, value.getStartDate());
        stmt.bindLong(7, value.getEndDate());
        stmt.bindLong(8, value.getItemID());
        if (value.getEntityName() == null) {
          stmt.bindNull(9);
        } else {
          stmt.bindString(9, value.getEntityName());
        }
        stmt.bindLong(10, value.getItemID());
      }
    };
    this.__preparedStmtOfResetTable = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "DELETE FROM course_table";
        return _query;
      }
    };
  }

  @Override
  public void insert(final Course course) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfCourse.insert(course);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void delete(final Course course) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __deletionAdapterOfCourse.handle(course);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void update(final Course course) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __updateAdapterOfCourse.handle(course);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void resetTable() {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfResetTable.acquire();
    __db.beginTransaction();
    try {
      _stmt.executeUpdateDelete();
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
      __preparedStmtOfResetTable.release(_stmt);
    }
  }

  @Override
  public List<Course> getAllCourses() {
    final String _sql = "SELECT * FROM course_table ORDER BY itemID ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
      final int _cursorIndexOfMatchTermDates = CursorUtil.getColumnIndexOrThrow(_cursor, "matchTermDates");
      final int _cursorIndexOfEntityNote = CursorUtil.getColumnIndexOrThrow(_cursor, "entityNote");
      final int _cursorIndexOfTermID = CursorUtil.getColumnIndexOrThrow(_cursor, "termID");
      final int _cursorIndexOfInstructorID = CursorUtil.getColumnIndexOrThrow(_cursor, "instructorID");
      final int _cursorIndexOfStartDate = CursorUtil.getColumnIndexOrThrow(_cursor, "startDate");
      final int _cursorIndexOfEndDate = CursorUtil.getColumnIndexOrThrow(_cursor, "endDate");
      final int _cursorIndexOfItemID = CursorUtil.getColumnIndexOrThrow(_cursor, "itemID");
      final int _cursorIndexOfEntityName = CursorUtil.getColumnIndexOrThrow(_cursor, "entityName");
      final List<Course> _result = new ArrayList<Course>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final Course _item;
        _item = new Course();
        final CourseStatus _tmpStatus;
        final String _tmp;
        if (_cursor.isNull(_cursorIndexOfStatus)) {
          _tmp = null;
        } else {
          _tmp = _cursor.getString(_cursorIndexOfStatus);
        }
        _tmpStatus = Converter.toCourseStatus(_tmp);
        _item.setStatus(_tmpStatus);
        final boolean _tmpMatchTermDates;
        final int _tmp_1;
        _tmp_1 = _cursor.getInt(_cursorIndexOfMatchTermDates);
        _tmpMatchTermDates = _tmp_1 != 0;
        _item.setMatchTermDates(_tmpMatchTermDates);
        final String _tmpEntityNote;
        if (_cursor.isNull(_cursorIndexOfEntityNote)) {
          _tmpEntityNote = null;
        } else {
          _tmpEntityNote = _cursor.getString(_cursorIndexOfEntityNote);
        }
        _item.setEntityNote(_tmpEntityNote);
        final int _tmpTermID;
        _tmpTermID = _cursor.getInt(_cursorIndexOfTermID);
        _item.setTermID(_tmpTermID);
        final int _tmpInstructorID;
        _tmpInstructorID = _cursor.getInt(_cursorIndexOfInstructorID);
        _item.setInstructorID(_tmpInstructorID);
        final long _tmpStartDate;
        _tmpStartDate = _cursor.getLong(_cursorIndexOfStartDate);
        _item.setStartDate(_tmpStartDate);
        final long _tmpEndDate;
        _tmpEndDate = _cursor.getLong(_cursorIndexOfEndDate);
        _item.setEndDate(_tmpEndDate);
        final int _tmpItemID;
        _tmpItemID = _cursor.getInt(_cursorIndexOfItemID);
        _item.setItemID(_tmpItemID);
        final String _tmpEntityName;
        if (_cursor.isNull(_cursorIndexOfEntityName)) {
          _tmpEntityName = null;
        } else {
          _tmpEntityName = _cursor.getString(_cursorIndexOfEntityName);
        }
        _item.setEntityName(_tmpEntityName);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public Course getCourseByID(final int itemID) {
    final String _sql = "SELECT * FROM course_table WHERE itemID=?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, itemID);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
      final int _cursorIndexOfMatchTermDates = CursorUtil.getColumnIndexOrThrow(_cursor, "matchTermDates");
      final int _cursorIndexOfEntityNote = CursorUtil.getColumnIndexOrThrow(_cursor, "entityNote");
      final int _cursorIndexOfTermID = CursorUtil.getColumnIndexOrThrow(_cursor, "termID");
      final int _cursorIndexOfInstructorID = CursorUtil.getColumnIndexOrThrow(_cursor, "instructorID");
      final int _cursorIndexOfStartDate = CursorUtil.getColumnIndexOrThrow(_cursor, "startDate");
      final int _cursorIndexOfEndDate = CursorUtil.getColumnIndexOrThrow(_cursor, "endDate");
      final int _cursorIndexOfItemID = CursorUtil.getColumnIndexOrThrow(_cursor, "itemID");
      final int _cursorIndexOfEntityName = CursorUtil.getColumnIndexOrThrow(_cursor, "entityName");
      final Course _result;
      if(_cursor.moveToFirst()) {
        _result = new Course();
        final CourseStatus _tmpStatus;
        final String _tmp;
        if (_cursor.isNull(_cursorIndexOfStatus)) {
          _tmp = null;
        } else {
          _tmp = _cursor.getString(_cursorIndexOfStatus);
        }
        _tmpStatus = Converter.toCourseStatus(_tmp);
        _result.setStatus(_tmpStatus);
        final boolean _tmpMatchTermDates;
        final int _tmp_1;
        _tmp_1 = _cursor.getInt(_cursorIndexOfMatchTermDates);
        _tmpMatchTermDates = _tmp_1 != 0;
        _result.setMatchTermDates(_tmpMatchTermDates);
        final String _tmpEntityNote;
        if (_cursor.isNull(_cursorIndexOfEntityNote)) {
          _tmpEntityNote = null;
        } else {
          _tmpEntityNote = _cursor.getString(_cursorIndexOfEntityNote);
        }
        _result.setEntityNote(_tmpEntityNote);
        final int _tmpTermID;
        _tmpTermID = _cursor.getInt(_cursorIndexOfTermID);
        _result.setTermID(_tmpTermID);
        final int _tmpInstructorID;
        _tmpInstructorID = _cursor.getInt(_cursorIndexOfInstructorID);
        _result.setInstructorID(_tmpInstructorID);
        final long _tmpStartDate;
        _tmpStartDate = _cursor.getLong(_cursorIndexOfStartDate);
        _result.setStartDate(_tmpStartDate);
        final long _tmpEndDate;
        _tmpEndDate = _cursor.getLong(_cursorIndexOfEndDate);
        _result.setEndDate(_tmpEndDate);
        final int _tmpItemID;
        _tmpItemID = _cursor.getInt(_cursorIndexOfItemID);
        _result.setItemID(_tmpItemID);
        final String _tmpEntityName;
        if (_cursor.isNull(_cursorIndexOfEntityName)) {
          _tmpEntityName = null;
        } else {
          _tmpEntityName = _cursor.getString(_cursorIndexOfEntityName);
        }
        _result.setEntityName(_tmpEntityName);
      } else {
        _result = null;
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
