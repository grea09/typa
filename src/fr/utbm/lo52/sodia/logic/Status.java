package fr.utbm.lo52.sodia.logic;

import java.util.Map;

import android.content.ContentProviderOperation;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.ContactsContract.StatusUpdates;
import java.util.HashMap;

public class Status extends DataBaseObject
{

	private long rawContactId;

	public long getRawContactId()
	{
		return rawContactId;
	}

	public void setRawContactId(long rawContactId)
	{
		this.rawContactId = rawContactId;
	}

	public Status(Presence presence, String status, long timestamp,
		String label, Drawable icon)
	{
		this.presence = presence;
		this.status = status;
		this.timestamp = timestamp;
		this.label = label;
		this.icon = icon;
	}
	
	public Status(Presence presence, String status)
	{
		this.presence = presence;
		this.status = status;
		this.timestamp = System.currentTimeMillis();
		this.label = null;
		this.icon = null;
	}
	private static Map<Long, Status> statusUpdates = new HashMap<Long, Status>();
	private Presence presence;
	private String status;
	private long timestamp;
	private String label;
	private Drawable icon;
	private Im im;

	public Im getIm()
	{
		return im;
	}

	public void setIm(Im im)
	{
		this.im = im;
	}

	@Override
	protected Uri uri()
	{
		return StatusUpdates.CONTENT_URI;
	}

	@Override
	protected String[] projection()
	{
		return new String[]
			{
				StatusUpdates.DATA_ID, StatusUpdates.PRESENCE, StatusUpdates.STATUS,
				StatusUpdates.STATUS_TIMESTAMP,
				StatusUpdates.STATUS_RES_PACKAGE, StatusUpdates.STATUS_LABEL,
				StatusUpdates.STATUS_ICON, StatusUpdates.IM_HANDLE
			};
	}

	protected Status(long id)
	{
		super(id);
		statusUpdates.put(id, this);
	}

	@Override
	protected void get()
	{
		final Cursor cursor = query();
		if (cursor != null && cursor.moveToFirst())
		{
			setPresence(Presence.get(cursor.getLong(1)));
			setStatus(cursor.getString(2));
			setTimestamp(cursor.getLong(3));
			// TODO respackage
			if(im != null)
			{
				im.setUserId(cursor.getString(7));
			}
		}
		if (cursor != null)
		{
			cursor.close();
		}
	}

	public static Status get(long id)
	{
		if (statusUpdates.containsKey(id))
		{
			return statusUpdates.get(id);
		}
		return new Status(id);
	}

	public Presence getPresence()
	{
		return this.presence;
	}

	public void setPresence(Presence presence)
	{
		this.presence = presence;
	}

	public String getStatus()
	{
		return this.status;
	}

	public void setStatus(String status)
	{
		this.status = status;
	}

	public long getTimestamp()
	{
		return this.timestamp;
	}

	public void setTimestamp(long timestamp)
	{
		this.timestamp = timestamp;
	}

	@Override
	protected ContentProviderOperation operation()
	{
		return ((id == -1) ? (ContentProviderOperation.newInsert(uri()))
				: (ContentProviderOperation.newUpdate(uri()).withSelection(
				   StatusUpdates.DATA_ID + "=?", new String[]
				   {
					   String.valueOf(id)
				   })))
			.withValue(ContactsContract.Data.RAW_CONTACT_ID, rawContactId)
			.withValue(StatusUpdates.PRESENCE, presence.ordinal())
			.withValue(StatusUpdates.STATUS, status)
			//.withValue(StatusUpdates.STATUS_ICON, icon)
			//.withValue(StatusUpdates.STATUS_LABEL, label)
			.withValue(StatusUpdates.IM_HANDLE, im.getUserId())
			.withValue(StatusUpdates.PROTOCOL, im.getProtocolType())
			.withValue(StatusUpdates.CUSTOM_PROTOCOL, im.getCustomProtocol())
			.withValue(StatusUpdates.STATUS_TIMESTAMP, timestamp).build();
	}
}
