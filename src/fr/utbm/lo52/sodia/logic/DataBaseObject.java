package fr.utbm.lo52.sodia.logic;

import java.util.ArrayList;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import android.os.RemoteException;
import android.provider.ContactsContract;

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

	abstract protected Uri uri();
	
	/**
	 * ID_ should always be first
	 **/
	abstract protected String[] projection();

	protected DataBaseObject(long id)
	{
		setId(id);
		if (id != -1)
		{
			get();
		}
	}

	public DataBaseObject()
	{
		setId(-1);
	}

	public void save() throws RemoteException, OperationApplicationException
	{
		Log.i(getClass().getSimpleName(), "save");
		ArrayList<ContentProviderOperation> operations = new ArrayList<ContentProviderOperation>();
		operations.add(operation());
		ContentProviderResult result = contentResolver.applyBatch(ContactsContract.AUTHORITY, operations)[0];
		if(id == -1 && result.uri != null)
		{
			setId(ContentUris.parseId(result.uri));
			Log.i(getClass().getSimpleName(), "	id = " + id);
		}
		
	}
	
	protected void setId(long id)
	{
		this.id = id;
	}

	protected abstract void get();

	protected abstract ContentProviderOperation operation();

	protected Cursor query(String selection, String[] selectionArgs)
	{
		return contentResolver.query(uri(), projection(), selection, selectionArgs,
				null);
	}

	protected Cursor query()
	{
		return contentResolver.query(uri(), projection(), projection()[0] + "=?",
				new String[]
					{ Long.toString(id) }, null);
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

	public static Account fromString(String type, String name)
	{
		for (Account account : AccountManager.get(context).getAccountsByType(
				type))
		{
			if (account.name.equals(name))
			{
				return account;
			}
		}
		return null;
	}

	public static void save(DataBaseObject[] dbDataBaseObjects)
			throws RemoteException, OperationApplicationException
	{
		ArrayList<ContentProviderOperation> operations = new ArrayList<ContentProviderOperation>();
		ContentProviderResult[] result;
		
		for (int i = 0; i < dbDataBaseObjects.length; i++)
		{
			operations.add(dbDataBaseObjects[i].operation());
		}
		result = contentResolver.applyBatch(ContactsContract.AUTHORITY,
				operations);
		for (int i = 0; i < dbDataBaseObjects.length; i++)
		{
			dbDataBaseObjects[i].setId(ContentUris.parseId(result[i].uri));
		}
	}
}
