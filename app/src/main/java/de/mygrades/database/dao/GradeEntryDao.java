package de.mygrades.database.dao;

import java.util.List;
import java.util.ArrayList;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.SqlUtils;
import de.greenrobot.dao.internal.DaoConfig;

import de.mygrades.database.dao.GradeEntry;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "GRADE_ENTRY".
*/
public class GradeEntryDao extends AbstractDao<GradeEntry, String> {

    public static final String TABLENAME = "GRADE_ENTRY";

    /**
     * Properties of entity GradeEntry.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Name = new Property(0, String.class, "name", false, "NAME");
        public final static Property Grade = new Property(1, Double.class, "grade", false, "GRADE");
        public final static Property ExamId = new Property(2, String.class, "examId", false, "EXAM_ID");
        public final static Property Semester = new Property(3, String.class, "semester", false, "SEMESTER");
        public final static Property State = new Property(4, String.class, "state", false, "STATE");
        public final static Property CreditPoints = new Property(5, Double.class, "creditPoints", false, "CREDIT_POINTS");
        public final static Property Annotation = new Property(6, String.class, "annotation", false, "ANNOTATION");
        public final static Property Attempt = new Property(7, String.class, "attempt", false, "ATTEMPT");
        public final static Property ExamDate = new Property(8, String.class, "examDate", false, "EXAM_DATE");
        public final static Property SemesterNumber = new Property(9, Integer.class, "semesterNumber", false, "SEMESTER_NUMBER");
        public final static Property Hash = new Property(10, String.class, "hash", true, "HASH");
        public final static Property OverviewId = new Property(11, Long.class, "overviewId", false, "OVERVIEW_ID");
    };

    private DaoSession daoSession;


    public GradeEntryDao(DaoConfig config) {
        super(config);
    }
    
    public GradeEntryDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
        this.daoSession = daoSession;
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"GRADE_ENTRY\" (" + //
                "\"NAME\" TEXT NOT NULL ," + // 0: name
                "\"GRADE\" REAL," + // 1: grade
                "\"EXAM_ID\" TEXT," + // 2: examId
                "\"SEMESTER\" TEXT," + // 3: semester
                "\"STATE\" TEXT," + // 4: state
                "\"CREDIT_POINTS\" REAL," + // 5: creditPoints
                "\"ANNOTATION\" TEXT," + // 6: annotation
                "\"ATTEMPT\" TEXT," + // 7: attempt
                "\"EXAM_DATE\" TEXT," + // 8: examDate
                "\"SEMESTER_NUMBER\" INTEGER," + // 9: semesterNumber
                "\"HASH\" TEXT PRIMARY KEY NOT NULL ," + // 10: hash
                "\"OVERVIEW_ID\" INTEGER);"); // 11: overviewId
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"GRADE_ENTRY\"";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, GradeEntry entity) {
        stmt.clearBindings();
        stmt.bindString(1, entity.getName());
 
        Double grade = entity.getGrade();
        if (grade != null) {
            stmt.bindDouble(2, grade);
        }
 
        String examId = entity.getExamId();
        if (examId != null) {
            stmt.bindString(3, examId);
        }
 
        String semester = entity.getSemester();
        if (semester != null) {
            stmt.bindString(4, semester);
        }
 
        String state = entity.getState();
        if (state != null) {
            stmt.bindString(5, state);
        }
 
        Double creditPoints = entity.getCreditPoints();
        if (creditPoints != null) {
            stmt.bindDouble(6, creditPoints);
        }
 
        String annotation = entity.getAnnotation();
        if (annotation != null) {
            stmt.bindString(7, annotation);
        }
 
        String attempt = entity.getAttempt();
        if (attempt != null) {
            stmt.bindString(8, attempt);
        }
 
        String examDate = entity.getExamDate();
        if (examDate != null) {
            stmt.bindString(9, examDate);
        }
 
        Integer semesterNumber = entity.getSemesterNumber();
        if (semesterNumber != null) {
            stmt.bindLong(10, semesterNumber);
        }
 
        String hash = entity.getHash();
        if (hash != null) {
            stmt.bindString(11, hash);
        }
 
        Long overviewId = entity.getOverviewId();
        if (overviewId != null) {
            stmt.bindLong(12, overviewId);
        }
    }

    @Override
    protected void attachEntity(GradeEntry entity) {
        super.attachEntity(entity);
        entity.__setDaoSession(daoSession);
    }

    /** @inheritdoc */
    @Override
    public String readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10);
    }    

    /** @inheritdoc */
    @Override
    public GradeEntry readEntity(Cursor cursor, int offset) {
        GradeEntry entity = new GradeEntry( //
            cursor.getString(offset + 0), // name
            cursor.isNull(offset + 1) ? null : cursor.getDouble(offset + 1), // grade
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // examId
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // semester
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // state
            cursor.isNull(offset + 5) ? null : cursor.getDouble(offset + 5), // creditPoints
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // annotation
            cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7), // attempt
            cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8), // examDate
            cursor.isNull(offset + 9) ? null : cursor.getInt(offset + 9), // semesterNumber
            cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10), // hash
            cursor.isNull(offset + 11) ? null : cursor.getLong(offset + 11) // overviewId
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, GradeEntry entity, int offset) {
        entity.setName(cursor.getString(offset + 0));
        entity.setGrade(cursor.isNull(offset + 1) ? null : cursor.getDouble(offset + 1));
        entity.setExamId(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setSemester(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setState(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setCreditPoints(cursor.isNull(offset + 5) ? null : cursor.getDouble(offset + 5));
        entity.setAnnotation(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setAttempt(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
        entity.setExamDate(cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8));
        entity.setSemesterNumber(cursor.isNull(offset + 9) ? null : cursor.getInt(offset + 9));
        entity.setHash(cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10));
        entity.setOverviewId(cursor.isNull(offset + 11) ? null : cursor.getLong(offset + 11));
     }
    
    /** @inheritdoc */
    @Override
    protected String updateKeyAfterInsert(GradeEntry entity, long rowId) {
        return entity.getHash();
    }
    
    /** @inheritdoc */
    @Override
    public String getKey(GradeEntry entity) {
        if(entity != null) {
            return entity.getHash();
        } else {
            return null;
        }
    }

    /** @inheritdoc */
    @Override    
    protected boolean isEntityUpdateable() {
        return true;
    }
    
    private String selectDeep;

    protected String getSelectDeep() {
        if (selectDeep == null) {
            StringBuilder builder = new StringBuilder("SELECT ");
            SqlUtils.appendColumns(builder, "T", getAllColumns());
            builder.append(',');
            SqlUtils.appendColumns(builder, "T0", daoSession.getOverviewDao().getAllColumns());
            builder.append(" FROM GRADE_ENTRY T");
            builder.append(" LEFT JOIN OVERVIEW T0 ON T.\"OVERVIEW_ID\"=T0.\"OVERVIEW_ID\"");
            builder.append(' ');
            selectDeep = builder.toString();
        }
        return selectDeep;
    }
    
    protected GradeEntry loadCurrentDeep(Cursor cursor, boolean lock) {
        GradeEntry entity = loadCurrent(cursor, 0, lock);
        int offset = getAllColumns().length;

        Overview overview = loadCurrentOther(daoSession.getOverviewDao(), cursor, offset);
        entity.setOverview(overview);

        return entity;    
    }

    public GradeEntry loadDeep(Long key) {
        assertSinglePk();
        if (key == null) {
            return null;
        }

        StringBuilder builder = new StringBuilder(getSelectDeep());
        builder.append("WHERE ");
        SqlUtils.appendColumnsEqValue(builder, "T", getPkColumns());
        String sql = builder.toString();
        
        String[] keyArray = new String[] { key.toString() };
        Cursor cursor = db.rawQuery(sql, keyArray);
        
        try {
            boolean available = cursor.moveToFirst();
            if (!available) {
                return null;
            } else if (!cursor.isLast()) {
                throw new IllegalStateException("Expected unique result, but count was " + cursor.getCount());
            }
            return loadCurrentDeep(cursor, true);
        } finally {
            cursor.close();
        }
    }
    
    /** Reads all available rows from the given cursor and returns a list of new ImageTO objects. */
    public List<GradeEntry> loadAllDeepFromCursor(Cursor cursor) {
        int count = cursor.getCount();
        List<GradeEntry> list = new ArrayList<GradeEntry>(count);
        
        if (cursor.moveToFirst()) {
            if (identityScope != null) {
                identityScope.lock();
                identityScope.reserveRoom(count);
            }
            try {
                do {
                    list.add(loadCurrentDeep(cursor, false));
                } while (cursor.moveToNext());
            } finally {
                if (identityScope != null) {
                    identityScope.unlock();
                }
            }
        }
        return list;
    }
    
    protected List<GradeEntry> loadDeepAllAndCloseCursor(Cursor cursor) {
        try {
            return loadAllDeepFromCursor(cursor);
        } finally {
            cursor.close();
        }
    }
    

    /** A raw-style query where you can pass any WHERE clause and arguments. */
    public List<GradeEntry> queryDeep(String where, String... selectionArg) {
        Cursor cursor = db.rawQuery(getSelectDeep() + where, selectionArg);
        return loadDeepAllAndCloseCursor(cursor);
    }
 
}
