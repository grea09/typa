package fr.utbm.lo52.sodia.logic;

import java.util.Map;

import android.content.ContentProviderOperation;
import android.content.OperationApplicationException;
import android.content.res.Resources;
import android.database.Cursor;
import android.net.Uri;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds;
import android.provider.ContactsContract.Data;
import android.provider.ContactsContract.StatusUpdates;
import java.util.HashMap;

public class Im extends DataBaseObject
{
	public static final char USER_ID_SEPARATOR = '@';
	
	private static Map<Long, Im> ims = new HashMap<Long, Im>();

	private String userId;
	private int protocolType;
	private String customProtocol;
	private Status status;
	private long rawContactId;

	public long getRawContactId() {
		return rawContactId;
	}

	public void setRawContactId(long rawContactId) {
		this.rawContactId = rawContactId;
		status.setRawContactId(rawContactId);
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
		{ CommonDataKinds.Im._ID, CommonDataKinds.Im.DATA,
				CommonDataKinds.Im.PROTOCOL, CommonDataKinds.Im.CUSTOM_PROTOCOL };
	}

	public Im(long id)
	{
		super(id);
		ims.put(id, this);
	}

	public Im(String userId, Status status, int protocolType, String customProtocol)
	{
		this.userId = userId;
		this.status = status;
		if(status != null)
		{
			status.setIm(this);
		}
		this.protocolType = protocolType;
		this.customProtocol = customProtocol;
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
		if(status instanceof Status)
		{
			status.setIm(this);
		}
	}

	@Override
	protected void get()
	{
		// Im
		final Cursor cursor = query(Data.MIMETYPE + "=?", new String[]
			{ CommonDataKinds.Im.CONTENT_ITEM_TYPE });
		if (cursor != null && cursor.moveToFirst())
		{
			setUserId(cursor.getString(1));
			setProtocolType(cursor.getInt(2));
			setCustomProtocol(cursor.getString(3));
		}
		if (cursor != null)
		{
			cursor.close();
		}

		// Status Fill
		final Cursor status = (new Status(null, null, 0, null, null)).query(
				StatusUpdates.DATA_ID + "=?", new String[]
					{ Long.toString(id) });
		if (status != null && status.moveToFirst())
		{
			setStatus(new Status(status.getLong(0)));
		}
		if (status != null)
		{
			status.close();
		}
	}

	public static Im get(long id)
	{
		if (ims.containsKey(id))
		{
			return ims.get(id);
		}
		return new Im(id);
	}

	@Override
	protected ContentProviderOperation operation()
	{
		return ((id == -1) ? (ContentProviderOperation.newInsert(uri()))
				: (ContentProviderOperation.newUpdate(uri()).withSelection(
						CommonDataKinds.Im._ID + "=?", new String[]
							{ String.valueOf(id) })))
				.withValue(Data.MIMETYPE, CommonDataKinds.Im.CONTENT_ITEM_TYPE)
				.withValue(Data.RAW_CONTACT_ID, rawContactId)
				.withValue(CommonDataKinds.Im.DATA, userId)
				.withValue(CommonDataKinds.Im.PROTOCOL, protocolType)
				.withValue(CommonDataKinds.Im.CUSTOM_PROTOCOL, customProtocol)
				.build();
	}

	public int getProtocolType()
	{
		return this.protocolType;
	}

	public void setProtocolType(int protocolType)
	{
		this.protocolType = protocolType;
	}

	public String getCustomProtocol()
	{
		return this.customProtocol;
	}

	public void setCustomProtocol(String customProtocol)
	{
		this.customProtocol = customProtocol;
	}

	public String getProtocol()
	{
		return (String) ContactsContract.CommonDataKinds.Im.getProtocolLabel(
				Resources.getSystem(), protocolType, customProtocol);
	}

	@Override
	public void save() throws RemoteException, OperationApplicationException
	{
		super.save();
		status.save();
	}
}
