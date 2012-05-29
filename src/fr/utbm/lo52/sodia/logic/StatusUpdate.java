package fr.utbm.lo52.sodia.logic;

import java.util.Map;

import android.accounts.Account;
import android.net.Uri;

public class StatusUpdate extends DataBaseObject
{
	private static Map<Long, StatusUpdate> statusUpdates;
	
	private Presence presence;
	private String status;
	private long timestamp;
	private String label;
	private Drawable icon;

	protected Uri uri = StatusUpdates.CONTENT_URI;
	protected String[] projection = new String[]{
		StatusUpdates._ID, 
		StatusUpdates.PRESENCE, 
		StatusUpdates.STATUS,
		StatusUpdates.STATUS_TIMESTAMP,
		StatusUpdates.STATUS_RES_PACKAGE,
		StatusUpdates.STATUS_LABEL,
		StatusUpdates.STATUS_ICON
	};

	public StatusUpdate(long id)
	{
		super(id);
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
			setPresence(cursor.getLong(1));
			setStatus(cursor.getString(2));
			setTimestamp(cursor.getLong(3));
			//TODO respackage
			
		}
		if (cursor != null)
		{
			cursor.close();
		}
	}

	public static StatusUpdate get(long id)
	{
		if (statusUpdates.containsKey(id))
		{
			return statusUpdates.get(id);
		}
		return new StatusUpdate(id);
	}
}
