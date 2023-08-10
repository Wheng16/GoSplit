package my.edu.utar.assignment2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

import java.util.ArrayList;

public class SQLiteAdapter {

    // SQL Commands
    // constant variables

    // update the following if changes made **
    private static final String MYDATABASE_NAME = "MY_DATABASE_4";
    private static final String DATABASE_TABLE = "MY_TABLE_4";  // table
    private static final String KEY_CONTENT = "Content"; // column
    public static final int MYDATABASE_VERSION = 4; //version

    public static final String KEY_CONTENT_2 = "Content2";
    public static final String KEY_CONTENT_3 = "Content3";
    public static final String KEY_CONTENT_4 = "Content4";
    public static final String KEY_CONTENT_5 = "Content5";
    public static final String KEY_CONTENT_6 = "Content6";

    // sql command to create the table with the column
    // ID, Content, Ingredients, Price
    private static final String SCRIPT_CREATE_DATABASE =
            "create table " + DATABASE_TABLE +
            " (id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + KEY_CONTENT + " text not null, "
                    + KEY_CONTENT_2 + " text, "
                    + KEY_CONTENT_3 + " text, "
                    + KEY_CONTENT_4 + " text, "
                    + KEY_CONTENT_5 + " text, "
                    + KEY_CONTENT_6 + " text);";

    // variables
    private Context context;
    private SQLiteHelper sqLiteHelper;
    private SQLiteDatabase sqLiteDatabase;

    // constructor
    public SQLiteAdapter(Context c) {
        context = c;
    }

    // open the database to insert data/ to write data
    public SQLiteAdapter openToWrite() throws android.database.SQLException{

        // create a table with MYDATABASE_NAME and
        // the version of MYDATABASE_VERSION
        sqLiteHelper  = new SQLiteHelper(context, MYDATABASE_NAME, null, MYDATABASE_VERSION);

        // open to write
        sqLiteDatabase = sqLiteHelper.getWritableDatabase();

        return this;
    }

    // open the database to read
    public SQLiteAdapter openToRead() throws android.database.SQLException{

        sqLiteHelper = new SQLiteHelper(context, MYDATABASE_NAME, null,
                MYDATABASE_VERSION);

        // open to read
        sqLiteDatabase = sqLiteHelper.getReadableDatabase();

        return this;
    }

    //insert data into the Column
    public long insert( String content, String content_2, String content_3, String content_4, String content_5, String content_6)
    {
        ContentValues contentValues = new ContentValues();

        contentValues.put(KEY_CONTENT, content);
        contentValues.put(KEY_CONTENT_2, content_2);
        contentValues.put(KEY_CONTENT_3, content_3);
        contentValues.put(KEY_CONTENT_4, content_4);
        contentValues.put(KEY_CONTENT_5, content_5);
        contentValues.put(KEY_CONTENT_6, content_6);

        return sqLiteDatabase.insert(DATABASE_TABLE, null,
                contentValues);
    }
    public String queueAll(){

        String [] columns = new String[] { KEY_CONTENT, KEY_CONTENT_2, KEY_CONTENT_3, KEY_CONTENT_4, KEY_CONTENT_5, KEY_CONTENT_6 };
        Cursor cursor = sqLiteDatabase.query(DATABASE_TABLE, columns,
                null, null, null, null, null);
        String result = "";
        Boolean first = true;

        int index_CONTENT = cursor.getColumnIndex(KEY_CONTENT);
        int index_CONTENT_2 = cursor.getColumnIndex(KEY_CONTENT_2);
        int index_CONTENT_3 = cursor.getColumnIndex(KEY_CONTENT_3);
        int index_CONTENT_4 = cursor.getColumnIndex(KEY_CONTENT_4);
        int index_CONTENT_5 = cursor.getColumnIndex(KEY_CONTENT_5);
        int index_CONTENT_6 = cursor.getColumnIndex(KEY_CONTENT_6);

        for(cursor.moveToFirst(); !(cursor.isAfterLast()); cursor.moveToNext()){
            if(first){
                first = false;
            }else{
                result = result + ";";
            }

            result = result + cursor.getString(index_CONTENT)
                    + ";" + cursor.getString(index_CONTENT_2)
                    + ";" + cursor.getString(index_CONTENT_3)
                    + ";" + cursor.getString(index_CONTENT_4)
                    + ";" + cursor.getString(index_CONTENT_5)
                    + ";" + cursor.getString(index_CONTENT_6);
        }

        return result;

    }

    //close the database
    public void close(){
        sqLiteHelper.close();
    }

    // delete all the content in the table
    public int deleteAll()
    {
        return sqLiteDatabase.delete(DATABASE_TABLE,
                null, null);
    }


//superclass of SQLiteOpenHelper --> implement both the
// override methods which creates the database

    public class SQLiteHelper extends SQLiteOpenHelper {

        // constructor with 4 parameters
        public SQLiteHelper(@Nullable Context context,
                            @Nullable String name,
                            @Nullable SQLiteDatabase.CursorFactory factory,
                            int version) {
            super(context, name, factory, version);
        }

        // to create the database
        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            sqLiteDatabase.execSQL(SCRIPT_CREATE_DATABASE);
        }

        // version control
        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
            sqLiteDatabase.execSQL(SCRIPT_CREATE_DATABASE);
        }

    }
}

