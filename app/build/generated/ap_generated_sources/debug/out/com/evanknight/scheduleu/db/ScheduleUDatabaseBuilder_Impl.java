package com.evanknight.scheduleu.db;

import androidx.annotation.NonNull;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomOpenHelper;
import androidx.room.RoomOpenHelper.Delegate;
import androidx.room.RoomOpenHelper.ValidationResult;
import androidx.room.migration.AutoMigrationSpec;
import androidx.room.migration.Migration;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.room.util.TableInfo.Column;
import androidx.room.util.TableInfo.ForeignKey;
import androidx.room.util.TableInfo.Index;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import androidx.sqlite.db.SupportSQLiteOpenHelper.Callback;
import androidx.sqlite.db.SupportSQLiteOpenHelper.Configuration;
import com.evanknight.scheduleu.dao.AssessmentDAO;
import com.evanknight.scheduleu.dao.AssessmentDAO_Impl;
import com.evanknight.scheduleu.dao.CourseDAO;
import com.evanknight.scheduleu.dao.CourseDAO_Impl;
import com.evanknight.scheduleu.dao.InstructorDAO;
import com.evanknight.scheduleu.dao.InstructorDAO_Impl;
import com.evanknight.scheduleu.dao.TermDAO;
import com.evanknight.scheduleu.dao.TermDAO_Impl;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@SuppressWarnings({"unchecked", "deprecation"})
public final class ScheduleUDatabaseBuilder_Impl extends ScheduleUDatabaseBuilder {
  private volatile TermDAO _termDAO;

  private volatile CourseDAO _courseDAO;

  private volatile AssessmentDAO _assessmentDAO;

  private volatile InstructorDAO _instructorDAO;

  @Override
  protected SupportSQLiteOpenHelper createOpenHelper(DatabaseConfiguration configuration) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(configuration, new RoomOpenHelper.Delegate(8) {
      @Override
      public void createAllTables(SupportSQLiteDatabase _db) {
        _db.execSQL("CREATE TABLE IF NOT EXISTS `term_table` (`status` TEXT, `entityNote` TEXT, `startDate` INTEGER NOT NULL, `endDate` INTEGER NOT NULL, `itemID` INTEGER NOT NULL, `entityName` TEXT, PRIMARY KEY(`itemID`))");
        _db.execSQL("CREATE TABLE IF NOT EXISTS `course_table` (`status` TEXT, `matchTermDates` INTEGER NOT NULL, `entityNote` TEXT, `termID` INTEGER NOT NULL, `instructorID` INTEGER NOT NULL, `startDate` INTEGER NOT NULL, `endDate` INTEGER NOT NULL, `itemID` INTEGER NOT NULL, `entityName` TEXT, PRIMARY KEY(`itemID`), FOREIGN KEY(`termID`) REFERENCES `term_table`(`itemID`) ON UPDATE NO ACTION ON DELETE CASCADE , FOREIGN KEY(`instructorID`) REFERENCES `instructor_table`(`itemID`) ON UPDATE NO ACTION ON DELETE CASCADE )");
        _db.execSQL("CREATE INDEX IF NOT EXISTS `index_course_table_termID` ON `course_table` (`termID`)");
        _db.execSQL("CREATE INDEX IF NOT EXISTS `index_course_table_instructorID` ON `course_table` (`instructorID`)");
        _db.execSQL("CREATE TABLE IF NOT EXISTS `assessment_table` (`isDateRange` INTEGER NOT NULL, `status` TEXT, `courseID` INTEGER NOT NULL, `startDate` INTEGER NOT NULL, `endDate` INTEGER NOT NULL, `itemID` INTEGER NOT NULL, `entityName` TEXT, PRIMARY KEY(`itemID`), FOREIGN KEY(`courseID`) REFERENCES `course_table`(`itemID`) ON UPDATE NO ACTION ON DELETE CASCADE )");
        _db.execSQL("CREATE INDEX IF NOT EXISTS `index_assessment_table_courseID` ON `assessment_table` (`courseID`)");
        _db.execSQL("CREATE TABLE IF NOT EXISTS `instructor_table` (`lastName` TEXT, `instructorEmail` TEXT, `instructorPhoneNumber` TEXT, `itemID` INTEGER NOT NULL, `entityName` TEXT, PRIMARY KEY(`itemID`))");
        _db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        _db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, '9eea65b3774c0ee6aefa5374a3ea3efa')");
      }

      @Override
      public void dropAllTables(SupportSQLiteDatabase _db) {
        _db.execSQL("DROP TABLE IF EXISTS `term_table`");
        _db.execSQL("DROP TABLE IF EXISTS `course_table`");
        _db.execSQL("DROP TABLE IF EXISTS `assessment_table`");
        _db.execSQL("DROP TABLE IF EXISTS `instructor_table`");
        if (mCallbacks != null) {
          for (int _i = 0, _size = mCallbacks.size(); _i < _size; _i++) {
            mCallbacks.get(_i).onDestructiveMigration(_db);
          }
        }
      }

      @Override
      public void onCreate(SupportSQLiteDatabase _db) {
        if (mCallbacks != null) {
          for (int _i = 0, _size = mCallbacks.size(); _i < _size; _i++) {
            mCallbacks.get(_i).onCreate(_db);
          }
        }
      }

      @Override
      public void onOpen(SupportSQLiteDatabase _db) {
        mDatabase = _db;
        _db.execSQL("PRAGMA foreign_keys = ON");
        internalInitInvalidationTracker(_db);
        if (mCallbacks != null) {
          for (int _i = 0, _size = mCallbacks.size(); _i < _size; _i++) {
            mCallbacks.get(_i).onOpen(_db);
          }
        }
      }

      @Override
      public void onPreMigrate(SupportSQLiteDatabase _db) {
        DBUtil.dropFtsSyncTriggers(_db);
      }

      @Override
      public void onPostMigrate(SupportSQLiteDatabase _db) {
      }

      @Override
      public RoomOpenHelper.ValidationResult onValidateSchema(SupportSQLiteDatabase _db) {
        final HashMap<String, TableInfo.Column> _columnsTermTable = new HashMap<String, TableInfo.Column>(6);
        _columnsTermTable.put("status", new TableInfo.Column("status", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTermTable.put("entityNote", new TableInfo.Column("entityNote", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTermTable.put("startDate", new TableInfo.Column("startDate", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTermTable.put("endDate", new TableInfo.Column("endDate", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTermTable.put("itemID", new TableInfo.Column("itemID", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTermTable.put("entityName", new TableInfo.Column("entityName", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysTermTable = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesTermTable = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoTermTable = new TableInfo("term_table", _columnsTermTable, _foreignKeysTermTable, _indicesTermTable);
        final TableInfo _existingTermTable = TableInfo.read(_db, "term_table");
        if (! _infoTermTable.equals(_existingTermTable)) {
          return new RoomOpenHelper.ValidationResult(false, "term_table(com.evanknight.scheduleu.entities.Term).\n"
                  + " Expected:\n" + _infoTermTable + "\n"
                  + " Found:\n" + _existingTermTable);
        }
        final HashMap<String, TableInfo.Column> _columnsCourseTable = new HashMap<String, TableInfo.Column>(9);
        _columnsCourseTable.put("status", new TableInfo.Column("status", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCourseTable.put("matchTermDates", new TableInfo.Column("matchTermDates", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCourseTable.put("entityNote", new TableInfo.Column("entityNote", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCourseTable.put("termID", new TableInfo.Column("termID", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCourseTable.put("instructorID", new TableInfo.Column("instructorID", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCourseTable.put("startDate", new TableInfo.Column("startDate", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCourseTable.put("endDate", new TableInfo.Column("endDate", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCourseTable.put("itemID", new TableInfo.Column("itemID", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCourseTable.put("entityName", new TableInfo.Column("entityName", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysCourseTable = new HashSet<TableInfo.ForeignKey>(2);
        _foreignKeysCourseTable.add(new TableInfo.ForeignKey("term_table", "CASCADE", "NO ACTION",Arrays.asList("termID"), Arrays.asList("itemID")));
        _foreignKeysCourseTable.add(new TableInfo.ForeignKey("instructor_table", "CASCADE", "NO ACTION",Arrays.asList("instructorID"), Arrays.asList("itemID")));
        final HashSet<TableInfo.Index> _indicesCourseTable = new HashSet<TableInfo.Index>(2);
        _indicesCourseTable.add(new TableInfo.Index("index_course_table_termID", false, Arrays.asList("termID"), Arrays.asList("ASC")));
        _indicesCourseTable.add(new TableInfo.Index("index_course_table_instructorID", false, Arrays.asList("instructorID"), Arrays.asList("ASC")));
        final TableInfo _infoCourseTable = new TableInfo("course_table", _columnsCourseTable, _foreignKeysCourseTable, _indicesCourseTable);
        final TableInfo _existingCourseTable = TableInfo.read(_db, "course_table");
        if (! _infoCourseTable.equals(_existingCourseTable)) {
          return new RoomOpenHelper.ValidationResult(false, "course_table(com.evanknight.scheduleu.entities.Course).\n"
                  + " Expected:\n" + _infoCourseTable + "\n"
                  + " Found:\n" + _existingCourseTable);
        }
        final HashMap<String, TableInfo.Column> _columnsAssessmentTable = new HashMap<String, TableInfo.Column>(7);
        _columnsAssessmentTable.put("isDateRange", new TableInfo.Column("isDateRange", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAssessmentTable.put("status", new TableInfo.Column("status", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAssessmentTable.put("courseID", new TableInfo.Column("courseID", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAssessmentTable.put("startDate", new TableInfo.Column("startDate", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAssessmentTable.put("endDate", new TableInfo.Column("endDate", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAssessmentTable.put("itemID", new TableInfo.Column("itemID", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAssessmentTable.put("entityName", new TableInfo.Column("entityName", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysAssessmentTable = new HashSet<TableInfo.ForeignKey>(1);
        _foreignKeysAssessmentTable.add(new TableInfo.ForeignKey("course_table", "CASCADE", "NO ACTION",Arrays.asList("courseID"), Arrays.asList("itemID")));
        final HashSet<TableInfo.Index> _indicesAssessmentTable = new HashSet<TableInfo.Index>(1);
        _indicesAssessmentTable.add(new TableInfo.Index("index_assessment_table_courseID", false, Arrays.asList("courseID"), Arrays.asList("ASC")));
        final TableInfo _infoAssessmentTable = new TableInfo("assessment_table", _columnsAssessmentTable, _foreignKeysAssessmentTable, _indicesAssessmentTable);
        final TableInfo _existingAssessmentTable = TableInfo.read(_db, "assessment_table");
        if (! _infoAssessmentTable.equals(_existingAssessmentTable)) {
          return new RoomOpenHelper.ValidationResult(false, "assessment_table(com.evanknight.scheduleu.entities.Assessment).\n"
                  + " Expected:\n" + _infoAssessmentTable + "\n"
                  + " Found:\n" + _existingAssessmentTable);
        }
        final HashMap<String, TableInfo.Column> _columnsInstructorTable = new HashMap<String, TableInfo.Column>(5);
        _columnsInstructorTable.put("lastName", new TableInfo.Column("lastName", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsInstructorTable.put("instructorEmail", new TableInfo.Column("instructorEmail", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsInstructorTable.put("instructorPhoneNumber", new TableInfo.Column("instructorPhoneNumber", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsInstructorTable.put("itemID", new TableInfo.Column("itemID", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsInstructorTable.put("entityName", new TableInfo.Column("entityName", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysInstructorTable = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesInstructorTable = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoInstructorTable = new TableInfo("instructor_table", _columnsInstructorTable, _foreignKeysInstructorTable, _indicesInstructorTable);
        final TableInfo _existingInstructorTable = TableInfo.read(_db, "instructor_table");
        if (! _infoInstructorTable.equals(_existingInstructorTable)) {
          return new RoomOpenHelper.ValidationResult(false, "instructor_table(com.evanknight.scheduleu.entities.Instructor).\n"
                  + " Expected:\n" + _infoInstructorTable + "\n"
                  + " Found:\n" + _existingInstructorTable);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "9eea65b3774c0ee6aefa5374a3ea3efa", "af735e1794ee880afa4cca32df79f1c0");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(configuration.context)
        .name(configuration.name)
        .callback(_openCallback)
        .build();
    final SupportSQLiteOpenHelper _helper = configuration.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "term_table","course_table","assessment_table","instructor_table");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    boolean _supportsDeferForeignKeys = android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP;
    try {
      if (!_supportsDeferForeignKeys) {
        _db.execSQL("PRAGMA foreign_keys = FALSE");
      }
      super.beginTransaction();
      if (_supportsDeferForeignKeys) {
        _db.execSQL("PRAGMA defer_foreign_keys = TRUE");
      }
      _db.execSQL("DELETE FROM `term_table`");
      _db.execSQL("DELETE FROM `course_table`");
      _db.execSQL("DELETE FROM `assessment_table`");
      _db.execSQL("DELETE FROM `instructor_table`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      if (!_supportsDeferForeignKeys) {
        _db.execSQL("PRAGMA foreign_keys = TRUE");
      }
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  protected Map<Class<?>, List<Class<?>>> getRequiredTypeConverters() {
    final HashMap<Class<?>, List<Class<?>>> _typeConvertersMap = new HashMap<Class<?>, List<Class<?>>>();
    _typeConvertersMap.put(TermDAO.class, TermDAO_Impl.getRequiredConverters());
    _typeConvertersMap.put(CourseDAO.class, CourseDAO_Impl.getRequiredConverters());
    _typeConvertersMap.put(AssessmentDAO.class, AssessmentDAO_Impl.getRequiredConverters());
    _typeConvertersMap.put(InstructorDAO.class, InstructorDAO_Impl.getRequiredConverters());
    return _typeConvertersMap;
  }

  @Override
  public Set<Class<? extends AutoMigrationSpec>> getRequiredAutoMigrationSpecs() {
    final HashSet<Class<? extends AutoMigrationSpec>> _autoMigrationSpecsSet = new HashSet<Class<? extends AutoMigrationSpec>>();
    return _autoMigrationSpecsSet;
  }

  @Override
  public List<Migration> getAutoMigrations(
      @NonNull Map<Class<? extends AutoMigrationSpec>, AutoMigrationSpec> autoMigrationSpecsMap) {
    return Arrays.asList();
  }

  @Override
  public TermDAO termDAO() {
    if (_termDAO != null) {
      return _termDAO;
    } else {
      synchronized(this) {
        if(_termDAO == null) {
          _termDAO = new TermDAO_Impl(this);
        }
        return _termDAO;
      }
    }
  }

  @Override
  public CourseDAO courseDAO() {
    if (_courseDAO != null) {
      return _courseDAO;
    } else {
      synchronized(this) {
        if(_courseDAO == null) {
          _courseDAO = new CourseDAO_Impl(this);
        }
        return _courseDAO;
      }
    }
  }

  @Override
  public AssessmentDAO assessmentDAO() {
    if (_assessmentDAO != null) {
      return _assessmentDAO;
    } else {
      synchronized(this) {
        if(_assessmentDAO == null) {
          _assessmentDAO = new AssessmentDAO_Impl(this);
        }
        return _assessmentDAO;
      }
    }
  }

  @Override
  public InstructorDAO instructorDAO() {
    if (_instructorDAO != null) {
      return _instructorDAO;
    } else {
      synchronized(this) {
        if(_instructorDAO == null) {
          _instructorDAO = new InstructorDAO_Impl(this);
        }
        return _instructorDAO;
      }
    }
  }
}
