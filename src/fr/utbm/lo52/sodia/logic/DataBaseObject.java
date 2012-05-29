package fr.utbm.lo52.sodia.logic;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

public abstract class DataBaseObject
{
	/**
	 * Sould be initialized before anything
	 **/
	public static Context context;
	
	/**
	 * Sould be initialized before anything
	 **/
	public static ContentResolver contentResolver;

	protected long id;

	protected Uri uri;
	/**
	 * ID_ should always be first
	 **/
	protected String[] projection;

	protected DataBaseObject(long id)
	{
		this.id = id;
		if (id != -1)
		{
			get();
		}
	}

	public DataBaseObject()
	{
		this.id = -1;
	}

	public abstract void save();

	protected abstract void get();

	protected Cursor query(String selection, String[] selectionArgs)
	{
		return contentResolver.query(uri, projection, selection, selectionArgs,
				null);
	}
	
	protected Cursor query()
	{
		return contentResolver.query(uri, projection, projection[0] + "=?", new String[]{Long.toString(id)},
				null);
	}

	protected void insert(ContentValues values)
	{
		contentResolver.insert(uri, values);
	}

	// public abstract ArrayList<? extends DataBaseObject> get(String selection,
	// String[] selectionArgs);

	public long getId()
	{
		return this.id;
	}

	@Override
	public boolean equals(Object object)
	{
		DataBaseObject dataBaseObject = (DataBaseObject) object;
		return ((dataBaseObject.id != -1) && (dataBaseObject.id == this.id));
	}
}
