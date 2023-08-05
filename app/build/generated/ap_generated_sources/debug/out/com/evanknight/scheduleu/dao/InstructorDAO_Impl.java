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
import com.evanknight.scheduleu.entities.Instructor;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SuppressWarnings({"unchecked", "deprecation"})
public final class InstructorDAO_Impl implements InstructorDAO {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<Instructor> __insertionAdapterOfInstructor;

  private final EntityDeletionOrUpdateAdapter<Instructor> __deletionAdapterOfInstructor;

  private final EntityDeletionOrUpdateAdapter<Instructor> __updateAdapterOfInstructor;

  private final SharedSQLiteStatement __preparedStmtOfResetTable;

  public InstructorDAO_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfInstructor = new EntityInsertionAdapter<Instructor>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR IGNORE INTO `instructor_table` (`lastName`,`instructorEmail`,`instructorPhoneNumber`,`itemID`,`entityName`) VALUES (?,?,?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Instructor value) {
        if (value.getLastName() == null) {
          stmt.bindNull(1);
        } else {
          stmt.bindString(1, value.getLastName());
        }
        if (value.getInstructorEmail() == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.getInstructorEmail());
        }
        if (value.getInstructorPhoneNumber() == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.getInstructorPhoneNumber());
        }
        stmt.bindLong(4, value.getItemID());
        if (value.getEntityName() == null) {
          stmt.bindNull(5);
        } else {
          stmt.bindString(5, value.getEntityName());
        }
      }
    };
    this.__deletionAdapterOfInstructor = new EntityDeletionOrUpdateAdapter<Instructor>(__db) {
      @Override
      public String createQuery() {
        return "DELETE FROM `instructor_table` WHERE `itemID` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Instructor value) {
        stmt.bindLong(1, value.getItemID());
      }
    };
    this.__updateAdapterOfInstructor = new EntityDeletionOrUpdateAdapter<Instructor>(__db) {
      @Override
      public String createQuery() {
        return "UPDATE OR ABORT `instructor_table` SET `lastName` = ?,`instructorEmail` = ?,`instructorPhoneNumber` = ?,`itemID` = ?,`entityName` = ? WHERE `itemID` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Instructor value) {
        if (value.getLastName() == null) {
          stmt.bindNull(1);
        } else {
          stmt.bindString(1, value.getLastName());
        }
        if (value.getInstructorEmail() == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.getInstructorEmail());
        }
        if (value.getInstructorPhoneNumber() == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.getInstructorPhoneNumber());
        }
        stmt.bindLong(4, value.getItemID());
        if (value.getEntityName() == null) {
          stmt.bindNull(5);
        } else {
          stmt.bindString(5, value.getEntityName());
        }
        stmt.bindLong(6, value.getItemID());
      }
    };
    this.__preparedStmtOfResetTable = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "DELETE FROM instructor_table";
        return _query;
      }
    };
  }

  @Override
  public void insert(final Instructor instructor) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfInstructor.insert(instructor);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void delete(final Instructor instructor) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __deletionAdapterOfInstructor.handle(instructor);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void update(final Instructor instructor) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __updateAdapterOfInstructor.handle(instructor);
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
  public List<Instructor> getAllInstructors() {
    final String _sql = "SELECT * FROM instructor_table ORDER BY itemID ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfLastName = CursorUtil.getColumnIndexOrThrow(_cursor, "lastName");
      final int _cursorIndexOfInstructorEmail = CursorUtil.getColumnIndexOrThrow(_cursor, "instructorEmail");
      final int _cursorIndexOfInstructorPhoneNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "instructorPhoneNumber");
      final int _cursorIndexOfItemID = CursorUtil.getColumnIndexOrThrow(_cursor, "itemID");
      final int _cursorIndexOfEntityName = CursorUtil.getColumnIndexOrThrow(_cursor, "entityName");
      final List<Instructor> _result = new ArrayList<Instructor>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final Instructor _item;
        _item = new Instructor();
        final String _tmpLastName;
        if (_cursor.isNull(_cursorIndexOfLastName)) {
          _tmpLastName = null;
        } else {
          _tmpLastName = _cursor.getString(_cursorIndexOfLastName);
        }
        _item.setLastName(_tmpLastName);
        final String _tmpInstructorEmail;
        if (_cursor.isNull(_cursorIndexOfInstructorEmail)) {
          _tmpInstructorEmail = null;
        } else {
          _tmpInstructorEmail = _cursor.getString(_cursorIndexOfInstructorEmail);
        }
        _item.setInstructorEmail(_tmpInstructorEmail);
        final String _tmpInstructorPhoneNumber;
        if (_cursor.isNull(_cursorIndexOfInstructorPhoneNumber)) {
          _tmpInstructorPhoneNumber = null;
        } else {
          _tmpInstructorPhoneNumber = _cursor.getString(_cursorIndexOfInstructorPhoneNumber);
        }
        _item.setInstructorPhoneNumber(_tmpInstructorPhoneNumber);
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
  public Instructor getInstructorByID(final int itemID) {
    final String _sql = "SELECT * FROM instructor_table WHERE itemID=?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, itemID);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfLastName = CursorUtil.getColumnIndexOrThrow(_cursor, "lastName");
      final int _cursorIndexOfInstructorEmail = CursorUtil.getColumnIndexOrThrow(_cursor, "instructorEmail");
      final int _cursorIndexOfInstructorPhoneNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "instructorPhoneNumber");
      final int _cursorIndexOfItemID = CursorUtil.getColumnIndexOrThrow(_cursor, "itemID");
      final int _cursorIndexOfEntityName = CursorUtil.getColumnIndexOrThrow(_cursor, "entityName");
      final Instructor _result;
      if(_cursor.moveToFirst()) {
        _result = new Instructor();
        final String _tmpLastName;
        if (_cursor.isNull(_cursorIndexOfLastName)) {
          _tmpLastName = null;
        } else {
          _tmpLastName = _cursor.getString(_cursorIndexOfLastName);
        }
        _result.setLastName(_tmpLastName);
        final String _tmpInstructorEmail;
        if (_cursor.isNull(_cursorIndexOfInstructorEmail)) {
          _tmpInstructorEmail = null;
        } else {
          _tmpInstructorEmail = _cursor.getString(_cursorIndexOfInstructorEmail);
        }
        _result.setInstructorEmail(_tmpInstructorEmail);
        final String _tmpInstructorPhoneNumber;
        if (_cursor.isNull(_cursorIndexOfInstructorPhoneNumber)) {
          _tmpInstructorPhoneNumber = null;
        } else {
          _tmpInstructorPhoneNumber = _cursor.getString(_cursorIndexOfInstructorPhoneNumber);
        }
        _result.setInstructorPhoneNumber(_tmpInstructorPhoneNumber);
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
