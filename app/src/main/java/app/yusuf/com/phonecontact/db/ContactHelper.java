package app.yusuf.com.phonecontact.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

public class ContactHelper extends SQLiteOpenHelper implements BaseColumns {

    public static final String DATABASE = "phonebook";
    public static final SQLiteDatabase.CursorFactory FACTORY = null;
    public static final int DATABASE_VERSION = 1;

    public static final String TABLE = "contact";
    public static final String COL_NAME = "name";
    public static final String COL_ORGANIZATION = "organization";
    public static final String COL_PHONE = "phone";
    public static final String COL_ADDRESS = "address";

    public static final String SQL_CREATE_ENTRIES = "" +
            "CREATE TABLE IF NOT EXISTS " + ContactHelper.TABLE +" ("+
            ContactHelper._ID +" INTEGER PRIMARY KEY AUTOINCREMENT, "+
            ContactHelper.COL_NAME +" TEXT, "+
            ContactHelper.COL_ORGANIZATION +" TEXT NULL, "+
            ContactHelper.COL_PHONE +" INTEGER, "+
            ContactHelper.COL_ADDRESS +" TEXT NULL"+
            ")";

    public static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + ContactHelper.TABLE;

    public ContactHelper(Context context) {
        super(context, DATABASE, FACTORY, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public long addContact(Contact contact) {

        ContentValues values = new ContentValues();
        values.put(ContactHelper.COL_NAME, contact.getName());
        values.put(ContactHelper.COL_ORGANIZATION, contact.getOrganization());
        values.put(ContactHelper.COL_PHONE, contact.getPhone());
        values.put(ContactHelper.COL_ADDRESS, contact.getAddress());

        try {
            SQLiteDatabase db = this.getWritableDatabase();
            return db.insert(ContactHelper.TABLE, null, values);
        } catch (Exception e){
            Log.e("SQLite Exception", e.getMessage());
            return -1;
        }

    }

    public Contact getContact (int id) {

        String[] projection = {
                ContactHelper._ID,
                ContactHelper.COL_NAME,
                ContactHelper.COL_ORGANIZATION,
                ContactHelper.COL_PHONE,
                ContactHelper.COL_ADDRESS,
        };

        String selection = ContactHelper._ID + " = ?";
        String[] selectionArgs = { String.valueOf(id) };

        Contact contact = new Contact();

        try {
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.query( ContactHelper.TABLE, projection, selection, selectionArgs, null, null, null );
            if (cursor.moveToFirst()) {
                cursor.moveToFirst();
                contact.set_id(cursor.getInt(cursor.getColumnIndex(ContactHelper._ID)));
                contact.setName(cursor.getString(cursor.getColumnIndex(ContactHelper.COL_NAME)));
                contact.setOrganization(cursor.getString(cursor.getColumnIndex(ContactHelper.COL_ORGANIZATION)));
                contact.setPhone(cursor.getString(cursor.getColumnIndex(ContactHelper.COL_PHONE)));
                contact.setAddress(cursor.getString(cursor.getColumnIndex(ContactHelper.COL_ADDRESS)));
                cursor.close();
            } else {
                contact = null;
            }
            return contact;
        }catch (Exception e){
            Log.e("SQLite Exception", e.getMessage());
            return null;
        }

    }

    public Cursor getContacts () {

        String[] projection = {
                ContactHelper._ID,
                ContactHelper.COL_NAME,
                ContactHelper.COL_ORGANIZATION,
                ContactHelper.COL_PHONE,
                ContactHelper.COL_ADDRESS,
        };

        try {
            SQLiteDatabase db = this.getReadableDatabase();
            return db.query( ContactHelper.TABLE, projection, null, null, null, null, null );
        }catch (Exception e){
            Log.e("SQLite Exception", e.getMessage());
            return null;
        }

    }

    public int updateContact(Contact contact) {

        ContentValues values = new ContentValues();
        values.put(ContactHelper.COL_NAME, contact.getName());
        values.put(ContactHelper.COL_ORGANIZATION, contact.getOrganization());
        values.put(ContactHelper.COL_PHONE, contact.getPhone());
        values.put(ContactHelper.COL_ADDRESS, contact.getAddress());

        String selection = ContactHelper._ID + " LIKE ?";
        String[] selectionArgs = { String.valueOf(contact.get_id()) };

        try {
            SQLiteDatabase db = this.getReadableDatabase();
            return db.update(ContactHelper.TABLE, values, selection, selectionArgs);
        }catch (Exception e){
            Log.e("SQLite Exception", e.getMessage());
            return -1;
        }

    }

    public int deleteContact(int id) {

        String selection = ContactHelper._ID + " LIKE ?";
        String[] selectionArgs = { String.valueOf(id) };

        try {
            SQLiteDatabase db = this.getWritableDatabase();
            return db.delete(ContactHelper.TABLE, selection, selectionArgs);
        }catch (Exception e){
            Log.e("SQLite Exception", e.getMessage());
            return -1;
        }

    }

}