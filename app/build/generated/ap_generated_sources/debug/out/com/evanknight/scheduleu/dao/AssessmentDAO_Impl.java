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
import com.evanknight.scheduleu.entities.Assessment;
import com.evanknight.scheduleu.util.AssessmentStatus;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SuppressWarnings({"unchecked", "deprecation"})
public final class AssessmentDAO_Impl implements AssessmentDAO {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<Assessment> __insertionAdapterOfAssessment;

  private final EntityDeletionOrUpdateAdapter<Assessment> __deletionAdapterOfAssessment;

  private final EntityDeletionOrUpdateAdapter<Assessment> __updateAdapterOfAssessment;

  private final SharedSQLiteStatement __preparedStmtOfResetTable;

  public AssessmentDAO_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfAssessment = new EntityInsertionAdapter<Assessment>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR IGNORE INTO `assessment_table` (`isDateRange`,`status`,`courseID`,`startDate`,`endDate`,`itemID`,`entityName`) VALUES (?,?,?,?,?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Assessment value) {
        final int _tmp = value.getIsDateRange() ? 1 : 0;
        stmt.bindLong(1, _tmp);
        final String _tmp_1 = Converter.fromAssessmentStatus(value.getStatus());
        if (_tmp_1 == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, _tmp_1);
        }
        stmt.bindLong(3, value.getCourseID());
        stmt.bindLong(4, value.getStartDate());
        stmt.bindLong(5, value.getEndDate());
        stmt.bindLong(6, value.getItemID());
        if (value.getEntityName() == null) {
          stmt.bindNull(7);
        } else {
          stmt.bindString(7, value.getEntityName());
        }
      }
    };
    this.__deletionAdapterOfAssessment = new EntityDeletionOrUpdateAdapter<Assessment>(__db) {
      @Override
      public String createQuery() {
        return "DELETE FROM `assessment_table` WHERE `itemID` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Assessment value) {
        stmt.bindLong(1, value.getItemID());
      }
    };
    this.__updateAdapterOfAssessment = new EntityDeletionOrUpdateAdapter<Assessment>(__db) {
      @Override
      public String createQuery() {
        return "UPDATE OR ABORT `assessment_table` SET `isDateRange` = ?,`status` = ?,`courseID` = ?,`startDate` = ?,`endDate` = ?,`itemID` = ?,`entityName` = ? WHERE `itemID` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Assessment value) {
        final int _tmp = value.getIsDateRange() ? 1 : 0;
        stmt.bindLong(1, _tmp);
        final String _tmp_1 = Converter.fromAssessmentStatus(value.getStatus());
        if (_tmp_1 == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, _tmp_1);
        }
        stmt.bindLong(3, value.getCourseID());
        stmt.bindLong(4, value.getStartDate());
        stmt.bindLong(5, value.getEndDate());
        stmt.bindLong(6, value.getItemID());
        if (value.getEntityName() == null) {
          stmt.bindNull(7);
        } else {
          stmt.bindString(7, value.getEntityName());
        }
        stmt.bindLong(8, value.getItemID());
      }
    };
    this.__preparedStmtOfResetTable = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "DELETE FROM assessment_table";
        return _query;
      }
    };
  }

  @Override
  public void insert(final Assessment assessment) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfAssessment.insert(assessment);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void delete(final Assessment assessment) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __deletionAdapterOfAssessment.handle(assessment);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void update(final Assessment assessment) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __updateAdapterOfAssessment.handle(assessment);
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
  public List<Assessment> getAllItems() {
    final String _sql = "SELECT * FROM assessment_table ORDER BY itemID ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfIsDateRange = CursorUtil.getColumnIndexOrThrow(_cursor, "isDateRange");
      final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
      final int _cursorIndexOfCourseID = CursorUtil.getColumnIndexOrThrow(_cursor, "courseID");
      final int _cursorIndexOfStartDate = CursorUtil.getColumnIndexOrThrow(_cursor, "startDate");
      final int _cursorIndexOfEndDate = CursorUtil.getColumnIndexOrThrow(_cursor, "endDate");
      final int _cursorIndexOfItemID = CursorUtil.getColumnIndexOrThrow(_cursor, "itemID");
      final int _cursorIndexOfEntityName = CursorUtil.getColumnIndexOrThrow(_cursor, "entityName");
      final List<Assessment> _result = new ArrayList<Assessment>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final Assessment _item;
        _item = new Assessment();
        final boolean _tmpIsDateRange;
        final int _tmp;
        _tmp = _cursor.getInt(_cursorIndexOfIsDateRange);
        _tmpIsDateRange = _tmp != 0;
        _item.setIsDateRange(_tmpIsDateRange);
        final AssessmentStatus _tmpStatus;
        final String _tmp_1;
        if (_cursor.isNull(_cursorIndexOfStatus)) {
          _tmp_1 = null;
        } else {
          _tmp_1 = _cursor.getString(_cursorIndexOfStatus);
        }
        _tmpStatus = Converter.toAssessmentStatus(_tmp_1);
        _item.setStatus(_tmpStatus);
        final int _tmpCourseID;
        _tmpCourseID = _cursor.getInt(_cursorIndexOfCourseID);
        _item.setCourseID(_tmpCourseID);
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
  public Assessment getAssessmentByID(final int itemID) {
    final String _sql = "SELECT * FROM assessment_table WHERE itemID=?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, itemID);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfIsDateRange = CursorUtil.getColumnIndexOrThrow(_cursor, "isDateRange");
      final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
      final int _cursorIndexOfCourseID = CursorUtil.getColumnIndexOrThrow(_cursor, "courseID");
      final int _cursorIndexOfStartDate = CursorUtil.getColumnIndexOrThrow(_cursor, "startDate");
      final int _cursorIndexOfEndDate = CursorUtil.getColumnIndexOrThrow(_cursor, "endDate");
      final int _cursorIndexOfItemID = CursorUtil.getColumnIndexOrThrow(_cursor, "itemID");
      final int _cursorIndexOfEntityName = CursorUtil.getColumnIndexOrThrow(_cursor, "entityName");
      final Assessment _result;
      if(_cursor.moveToFirst()) {
        _result = new Assessment();
        final boolean _tmpIsDateRange;
        final int _tmp;
        _tmp = _cursor.getInt(_cursorIndexOfIsDateRange);
        _tmpIsDateRange = _tmp != 0;
        _result.setIsDateRange(_tmpIsDateRange);
        final AssessmentStatus _tmpStatus;
        final String _tmp_1;
        if (_cursor.isNull(_cursorIndexOfStatus)) {
          _tmp_1 = null;
        } else {
          _tmp_1 = _cursor.getString(_cursorIndexOfStatus);
        }
        _tmpStatus = Converter.toAssessmentStatus(_tmp_1);
        _result.setStatus(_tmpStatus);
        final int _tmpCourseID;
        _tmpCourseID = _cursor.getInt(_cursorIndexOfCourseID);
        _result.setCourseID(_tmpCourseID);
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
