package fr.utbm.lo52.sodia.logic;

import java.util.Map;

import android.content.res.Resources;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.StructuredName;
import android.provider.ContactsContract.Data;
import android.provider.ContactsContract.StatusUpdates;

public class Im extends DataBaseObject
{
	private static Map<Long, Im> ims;
	
	private String userId;
	private String name;
	private String protocol;
	private Status status;

	protected Uri uri = Data.CONTENT_URI;
	protected String[] projection = new String[]
	{
		Data._ID,
		ContactsContract.CommonDataKinds.Im.DATA,
		ContactsContract.CommonDataKinds.Im.PROTOCOL,
		ContactsContract.CommonDataKinds.Im.CUSTOM_PROTOCOL,
		StructuredName.DISPLAY_NAME
	};

	public Im(long id)
	{
		super(id);
	}

	public Im(String userId, String name, Status status)
	{
		this.userId = userId;
		this.status = status;
	}

	public String getUserId()
	{
		return this.userId;
	}

	public void setUserId(String id)
	{
		this.userId = id;
	}

	public Status getStatus()
	{
		return this.status;
	}

	public void setStatus(Status status)
	{
		this.status = status;
	}

	@Override
	public void save()
	{
		// TODO Auto-generated method stub

	}

	@Override
	protected void get()
	{
		// TODO Auto-generated method stub
		final Cursor cursor = query();
		if(cursor != null && cursor.moveToFirst())
		{
			setUserId(cursor.getString(1));
			setName(cursor.getString(4));
			setProtocol((String) ContactsContract.CommonDataKinds.Im.getProtocolLabel(Resources.getSystem(),cursor.getInt(2),cursor.getString(3)));
			
		}
		if (cursor != null)
		{
			cursor.close();
		}
		// Status Fill
		final Cursor status = (new Status(null, null, 0, null, null)).query(StatusUpdates.DATA_ID + "=?", new String[]{Long.toString(id)});
		if (status != null && status.moveToFirst())
		{
			setStatus(new Status(cursor.getLong(0)));
		}
		if (status != null)
		{
			status.close();
		}
	}

	public String getName()
	{
		return this.name;
	}

	public void setName(String name)
	{
		this.name = name;
	}
	
	public static Im get(long id)
	{
		if (ims.containsKey(id))
		{
			return ims.get(id);
		}
		return new Im(id);
	}

	public String getProtocol()
	{
		return this.protocol;
	}

	public void setProtocol(String protocol)
	{
		this.protocol = protocol;
	}
}
