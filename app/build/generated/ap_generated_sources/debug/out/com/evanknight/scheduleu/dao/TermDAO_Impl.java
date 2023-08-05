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
import com.evanknight.scheduleu.entities.Term;
import com.evanknight.scheduleu.util.TermStatus;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SuppressWarnings({"unchecked", "deprecation"})
public final class TermDAO_Impl implements TermDAO {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<Term> __insertionAdapterOfTerm;

  private final EntityDeletionOrUpdateAdapter<Term> __deletionAdapterOfTerm;

  private final EntityDeletionOrUpdateAdapter<Term> __updateAdapterOfTerm;

  private final SharedSQLiteStatement __preparedStmtOfResetTable;

  public TermDAO_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfTerm = new EntityInsertionAdapter<Term>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR IGNORE INTO `term_table` (`status`,`entityNote`,`startDate`,`endDate`,`itemID`,`entityName`) VALUES (?,?,?,?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Term value) {
        final String _tmp = Converter.fromTermStatus(value.getStatus());
        if (_tmp == null) {
          stmt.bindNull(1);
        } else {
          stmt.bindString(1, _tmp);
        }
        if (value.getEntityNote() == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.getEntityNote());
        }
        stmt.bindLong(3, value.getStartDate());
        stmt.bindLong(4, value.getEndDate());
        stmt.bindLong(5, value.getItemID());
        if (value.getEntityName() == null) {
          stmt.bindNull(6);
        } else {
          stmt.bindString(6, value.getEntityName());
        }
      }
    };
    this.__deletionAdapterOfTerm = new EntityDeletionOrUpdateAdapter<Term>(__db) {
      @Override
      public String createQuery() {
        return "DELETE FROM `term_table` WHERE `itemID` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Term value) {
        stmt.bindLong(1, value.getItemID());
      }
    };
    this.__updateAdapterOfTerm = new EntityDeletionOrUpdateAdapter<Term>(__db) {
      @Override
      public String createQuery() {
        return "UPDATE OR ABORT `term_table` SET `status` = ?,`entityNote` = ?,`startDate` = ?,`endDate` = ?,`itemID` = ?,`entityName` = ? WHERE `itemID` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Term value) {
        final String _tmp = Converter.fromTermStatus(value.getStatus());
        if (_tmp == null) {
          stmt.bindNull(1);
        } else {
          stmt.bindString(1, _tmp);
        }
        if (value.getEntityNote() == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.getEntityNote());
        }
        stmt.bindLong(3, value.getStartDate());
        stmt.bindLong(4, value.getEndDate());
        stmt.bindLong(5, value.getItemID());
        if (value.getEntityName() == null) {
          stmt.bindNull(6);
        } else {
          stmt.bindString(6, value.getEntityName());
        }
        stmt.bindLong(7, value.getItemID());
      }
    };
    this.__preparedStmtOfResetTable = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "DELETE FROM term_table ";
        return _query;
      }
    };
  }

  @Override
  public void insert(final Term term) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfTerm.insert(term);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void delete(final Term term) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __deletionAdapterOfTerm.handle(term);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void update(final Term term) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __updateAdapterOfTerm.handle(term);
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
  public List<Term> getAllTerms() {
    final String _sql = "SELECT * FROM term_table ORDER BY itemID ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
      final int _cursorIndexOfEntityNote = CursorUtil.getColumnIndexOrThrow(_cursor, "entityNote");
      final int _cursorIndexOfStartDate = CursorUtil.getColumnIndexOrThrow(_cursor, "startDate");
      final int _cursorIndexOfEndDate = CursorUtil.getColumnIndexOrThrow(_cursor, "endDate");
      final int _cursorIndexOfItemID = CursorUtil.getColumnIndexOrThrow(_cursor, "itemID");
      final int _cursorIndexOfEntityName = CursorUtil.getColumnIndexOrThrow(_cursor, "entityName");
      final List<Term> _result = new ArrayList<Term>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final Term _item;
        _item = new Term();
        final TermStatus _tmpStatus;
        final String _tmp;
        if (_cursor.isNull(_cursorIndexOfStatus)) {
          _tmp = null;
        } else {
          _tmp = _cursor.getString(_cursorIndexOfStatus);
        }
        _tmpStatus = Converter.toTermStatus(_tmp);
        _item.setStatus(_tmpStatus);
        final String _tmpEntityNote;
        if (_cursor.isNull(_cursorIndexOfEntityNote)) {
          _tmpEntityNote = null;
        } else {
          _tmpEntityNote = _cursor.getString(_cursorIndexOfEntityNote);
        }
        _item.setEntityNote(_tmpEntityNote);
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
  public Term getTermByID(final int itemID) {
    final String _sql = "SELECT * FROM term_table WHERE itemID=?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, itemID);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
      final int _cursorIndexOfEntityNote = CursorUtil.getColumnIndexOrThrow(_cursor, "entityNote");
      final int _cursorIndexOfStartDate = CursorUtil.getColumnIndexOrThrow(_cursor, "startDate");
      final int _cursorIndexOfEndDate = CursorUtil.getColumnIndexOrThrow(_cursor, "endDate");
      final int _cursorIndexOfItemID = CursorUtil.getColumnIndexOrThrow(_cursor, "itemID");
      final int _cursorIndexOfEntityName = CursorUtil.getColumnIndexOrThrow(_cursor, "entityName");
      final Term _result;
      if(_cursor.moveToFirst()) {
        _result = new Term();
        final TermStatus _tmpStatus;
        final String _tmp;
        if (_cursor.isNull(_cursorIndexOfStatus)) {
          _tmp = null;
        } else {
          _tmp = _cursor.getString(_cursorIndexOfStatus);
        }
        _tmpStatus = Converter.toTermStatus(_tmp);
        _result.setStatus(_tmpStatus);
        final String _tmpEntityNote;
        if (_cursor.isNull(_cursorIndexOfEntityNote)) {
          _tmpEntityNote = null;
        } else {
          _tmpEntityNote = _cursor.getString(_cursorIndexOfEntityNote);
        }
        _result.setEntityNote(_tmpEntityNote);
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
