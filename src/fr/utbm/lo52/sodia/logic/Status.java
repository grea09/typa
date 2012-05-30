package fr.utbm.lo52.sodia.logic;

import java.util.Map;

import android.content.ContentProviderOperation;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.ContactsContract.StatusUpdates;

public class Status extends DataBaseObject
{
	public Status(Presence presence, String status, long timestamp,
			String label, Drawable icon)
	{
		this.presence = presence;
		this.status = status;
		this.timestamp = timestamp;
		this.label = label;
		this.icon = icon;
	}

	private static Map<Long, Status> statusUpdates;

	private Presence presence;
	private String status;
	private long timestamp;
	private String label;
	private Drawable icon;

	protected Uri uri = StatusUpdates.CONTENT_URI;
	protected String[] projection = new String[]
		{ StatusUpdates.DATA_ID, StatusUpdates.PRESENCE, StatusUpdates.STATUS,
				StatusUpdates.STATUS_TIMESTAMP,
				StatusUpdates.STATUS_RES_PACKAGE, StatusUpdates.STATUS_LABEL,
				StatusUpdates.STATUS_ICON };

	public Status(long id)
	{
		super(id);
	}

	@Override
	public void save()
	{

	}

	@Override
	protected void get()
	{
		// TODO Auto-generated method stub
		final Cursor cursor = query();
		if (cursor != null && cursor.moveToFirst())
		{
			setPresence(Presence.get(cursor.getLong(1)));
			setStatus(cursor.getString(2));
			setTimestamp(cursor.getLong(3));
			// TODO respackage

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
		return ((id == -1) ? (ContentProviderOperation.newInsert(uri))
				: (ContentProviderOperation.newUpdate(uri).withSelection(
						StatusUpdates.DATA_ID + "=?", new String[]
							{ String.valueOf(id) })))
				.withValue(StatusUpdates.PRESENCE, presence.ordinal())
				.withValue(StatusUpdates.STATUS, status)
				.withValue(StatusUpdates.STATUS_ICON, icon)
				.withValue(StatusUpdates.STATUS_LABEL, label)
				.withValue(StatusUpdates.STATUS_TIMESTAMP, timestamp).build();
	}

}
