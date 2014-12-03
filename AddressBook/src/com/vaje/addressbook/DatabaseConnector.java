// DatabaseConnector.java
// Provides easy connection and creation of UserContacts database.
package com.vaje.addressbook;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

public class DatabaseConnector {
	// database name
	private static final String DATABASE_NAME = "UserContacts";
	private static final int DATABASE_VERSION = 3;
	private SQLiteDatabase database; // database object
	private DatabaseOpenHelper databaseOpenHelper; // database helper

	// public constructor for DatabaseConnector
	public DatabaseConnector(Context context) {
		// create a new DatabaseOpenHelper
		databaseOpenHelper = new DatabaseOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION);
	} // end DatabaseConnector constructor

	// open the database connection
	public void open() throws SQLException {
		// create or open a database for reading/writing
		database = databaseOpenHelper.getWritableDatabase();
	} // end method open

	// close the database connection
	public void close() {
		if (database != null)
			database.close(); // close the database connection
	} // end method close

	// inserts a new contact in the database
	public void insertContact(String name, String email, String phone, String state, String city, String note) {
		ContentValues newContact = new ContentValues();
		newContact.put("name", name);
		newContact.put("email", email);
		newContact.put("phone", phone);
		newContact.put("street", state);
		newContact.put("city", city);
		newContact.put("note", note);

		open(); // open the database
		database.insert("contacts", null, newContact);
		close(); // close the database
	} // end method insertContact

	// inserts a new contact in the database
	public void updateContact(long id, String name, String email, String phone, String state, String city, String note) {
		ContentValues editContact = new ContentValues();
		editContact.put("name", name);
		editContact.put("email", email);
		editContact.put("phone", phone);
		editContact.put("street", state);
		editContact.put("city", city);
		editContact.put("note", note);

		open(); // open the database
		database.update("contacts", editContact, "_id=" + id, null);
		close(); // close the database
	} // end method updateContact

	// return a Cursor with all contact information in the database
	public Cursor getAllContacts() {
		return database.query("contacts", new String[]{"_id", "name"}, null, null, null, null, "name");
	} // end method getAllContacts

	// get a Cursor containing all information about the contact specified
	// by the given id
	public Cursor getOneContact(long id) {
		return database.query("contacts", null, "_id=" + id, null, null, null, null);
	} // end method getOnContact

	// delete the contact specified by the given String name
	public void deleteContact(long id) {
		open(); // open the database
		database.delete("contacts", "_id=" + id, null);
		close(); // close the database
	} // end method deleteContact

	private class DatabaseOpenHelper extends SQLiteOpenHelper {
		// public constructor
		public DatabaseOpenHelper(Context context, String name, CursorFactory factory, int version) {
			super(context, name, factory, version);
		} // end DatabaseOpenHelper constructor

		// creates the contacts table when the database is created
		@Override
		public void onCreate(SQLiteDatabase db) {
			// query to create a new table named contacts
			String createQuery = "CREATE TABLE contacts (_id integer primary key autoincrement, name TEXT, email TEXT, phone TEXT, street TEXT, city TEXT, note TEXT);";
			// initializing the database
			String insertValues = "INSERT INTO contacts (_ID, name, email, phone, street, city, note) values (NULL, 'MOJCA', 'mojca@gmail.com', '041-444-555', 'Trzaska cesta 25', 'Ljubljana', 'To je sporocilo');";

			db.execSQL(createQuery); // execute the query
			db.execSQL(insertValues);
		} // end method onCreate

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			if(oldVersion == 2 && newVersion == 3) {
				final String sql = "ALTER TABLE contacts ADD COLUMN note TEXT";
				db.execSQL(sql);
			}
		} // end method onUpgrade

	} // end class DatabaseOpenHelper
} // end class DatabaseConnector
