package fr.utbm.lo52.sodia.logic;

import java.util.Map;

import android.content.ContentProviderOperation;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract.CommonDataKinds.StructuredName;
import android.provider.ContactsContract.Data;
import java.util.HashMap;

public class Name extends DataBaseObject
{
	private static Map<Long, Name> names = new HashMap<Long, Name>();

	private String name;
	private String famillyName;
	private String givenName;
	private String middleName;
	
	private long rawContactId;

	public long getRawContactId() {
		return rawContactId;
	}

	public void setRawContactId(long rawContactId) {
		this.rawContactId = rawContactId;
	}

	@Override
	protected Uri uri()
	{
		return Data.CONTENT_URI;
	}
	
	@Override
	protected String[] projection()
	{
		return new String[]
		{ Data._ID, StructuredName.DISPLAY_NAME, StructuredName.FAMILY_NAME,
				StructuredName.GIVEN_NAME, StructuredName.MIDDLE_NAME };
	}

	public Name(long id)
	{
		super(id);
		names.put(id, this);
	}

	@Override
	protected void get()
	{
		// Name
		final Cursor cursor = query(Data.MIMETYPE + "=?", new String[]
			{ StructuredName.CONTENT_ITEM_TYPE });
		if (cursor != null && cursor.moveToFirst())
		{
			setName(cursor.getString(1));
		}
		if (cursor != null)
		{
			cursor.close();
		}
	}

	public Name(String name, String famillyName, String givenName,
			String middleName)
	{
		this.name = name;
		this.famillyName = famillyName;
		this.givenName = givenName;
		this.middleName = middleName;
	}

	public String getName()
	{
		return this.name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public static Name get(long id)
	{
		if (names.containsKey(id))
		{
			return names.get(id);
		}
		return new Name(id);
	}

	@Override
	protected ContentProviderOperation operation()
	{
		return ((id == -1) ? (ContentProviderOperation.newInsert(uri()))
				: (ContentProviderOperation.newUpdate(uri()).withSelection(
						Data._ID + "=?", new String[]
							{ String.valueOf(id) })))
				.withValue(Data.MIMETYPE, StructuredName.CONTENT_ITEM_TYPE)
				.withValue(Data.RAW_CONTACT_ID, rawContactId)
				.withValue(StructuredName.DISPLAY_NAME, name)
				.withValue(StructuredName.FAMILY_NAME, famillyName)
				.withValue(StructuredName.GIVEN_NAME, givenName)
				.withValue(StructuredName.MIDDLE_NAME, middleName).build();
	}

	public String getFamillyName()
	{
		return this.famillyName;
	}

	public void setFamillyName(String famillyName)
	{
		this.famillyName = famillyName;
	}

	public String getGivenName()
	{
		return this.givenName;
	}

	public void setGivenName(String givenName)
	{
		this.givenName = givenName;
	}

	public String getMiddleName()
	{
		return this.middleName;
	}

	public void setMiddleName(String middleName)
	{
		this.middleName = middleName;
	}
}
