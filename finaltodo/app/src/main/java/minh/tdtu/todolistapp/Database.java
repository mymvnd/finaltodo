package minh.tdtu.todolistapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import android.database.Cursor;
import android.util.Log;

import java.util.ArrayList;

public class Database extends SQLiteOpenHelper {
    private static final String TAG = "SQLite";
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "DATABASE_NOTE.sqlite";
    // tasks table name
    private static final String TABLE_NOTE = "NOTES";
    // tasks Table Columns names
    private static final String KEY_ID = "id";
    private static final String COLUMN_TITLE = "NOTE_TITLE";
    private static final String COLUMN_STATUS = "NOTE_STATUS";
    private static final String COLUMN_TIME = "NOTE_TIME";
    private static final String COLUMN_DATE = "NOTE_DATE";

    private static final String COLUMN_CONTENT = "NOTE_CONTENT";


    public Database(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    public void QueryData(String sql){
        SQLiteDatabase database = getWritableDatabase();
        database.execSQL(sql);
    }

    //public void


    public Cursor GetData(String sql){
        SQLiteDatabase database = getReadableDatabase();
        return database.rawQuery(sql,null);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String script = "CREATE TABLE IF NOT EXISTS " + TABLE_NOTE + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
                + COLUMN_TITLE + " TEXT, "
                + COLUMN_DATE + " TEXT, "
                + COLUMN_TIME + " TEXT, "
                + COLUMN_STATUS + " TEXT, "
                + COLUMN_CONTENT + " TEXT "

                + ")";
        // Execute Script.
        db.execSQL(script);

    }

    public ArrayList<Note> sortDB() {
        ArrayList<Note> noteList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_NOTE +" ORDER BY "+ COLUMN_STATUS + " DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Note note = new Note();
                //note.setId(Integer.parseInt(cursor.getString(0)));
                note.setTitle(cursor.getString(1));
                note.setDate(cursor.getString(2));
                note.setTime(cursor.getString(3));
                note.setStatus(cursor.getString(4));
                note.setContent(cursor.getString(5));

                // Adding note to list
                noteList.add(note);
            } while (cursor.moveToNext());
        }

        return noteList;
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTE);
        // Create tables again
        onCreate(db);
    }


    public void createDefaultNotesIfNeed()  {
        int count = this.getNotesCount();
        if(count ==0 ) {
            Note note1 = new Note(0, "Note 1", "24/12/2022","14:12", "Done","Di an com 1");
            this.addNote(note1);
        }
    }


    public void addNote(Note note) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        //values.put(KEY_ID, note.getId());
        values.put(COLUMN_TITLE, note.getTitle());
        values.put(COLUMN_DATE, note.getDate());
        values.put(COLUMN_TIME, note.getTime());
        values.put(COLUMN_STATUS, note.getStatus());
        values.put(COLUMN_CONTENT, note.getContent());


        // Inserting Row
        db.insert(TABLE_NOTE, null, values);

        // Closing database connection
        db.close();
    }


    public Note getNote(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_NOTE, new String[] {
                KEY_ID, COLUMN_TITLE, COLUMN_DATE, COLUMN_TIME, COLUMN_STATUS, COLUMN_CONTENT }, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Note note = new Note(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1),
                cursor.getString(2),
                cursor.getString(3),
                cursor.getString(4),
                cursor.getString(5));
        ;
        // return note
        return note;
    }


    public ArrayList<Note> getAllNotes() {
        Log.i(TAG, "MyDatabaseHelper.addNote ... ");
        ArrayList<Note> noteList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_NOTE;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Note note = new Note();
                note.setId(Integer.parseInt(cursor.getString(0)));
                note.setTitle(cursor.getString(1));
                note.setDate(cursor.getString(2));
                note.setTime(cursor.getString(3));
                note.setStatus(cursor.getString(4));
                note.setContent(cursor.getString(5));

                // Adding note to list
                noteList.add(note);
            } while (cursor.moveToNext());
        }

        // return note list
        return noteList;
    }


    public int getNotesCount() {
        String countQuery = "SELECT  * FROM " + TABLE_NOTE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();

        cursor.close();

        // return count
        return count;
    }

    public int updateNote(Note note) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, note.getTitle());
        values.put(COLUMN_DATE, note.getDate());
        values.put(COLUMN_TIME, note.getTime());
        values.put(COLUMN_STATUS, note.getStatus());
        values.put(COLUMN_CONTENT, note.getContent());

        // updating row
        return db.update(TABLE_NOTE, values, KEY_ID + " = ?",
                new String[]{String.valueOf(note.getId())});
    }

    public void deleteNote(Note note) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NOTE, KEY_ID + " = ?",
                new String[] { String.valueOf(note.getId()) });
        db.close();
    }

}
