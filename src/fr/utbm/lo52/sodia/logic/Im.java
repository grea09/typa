package fr.utbm.lo52.sodia.logic;

import android.accounts.Account;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.ContactsContract.RawContacts;

public class Im extends DataBaseObject
{
	private String userId;
	private String name;
	private Account account;
	private Presence presence;
	private String status;
	
	protected Uri uri = RawContacts.CONTENT_URI;
	protected  String[] projection = new String[] { ContactsContract.Contacts._ID, ContactsContract.Contacts.DISPLAY_NAME };
	
	
	public Im(long id)
	{
		super(id);
	}
	
	public Im(String userId, String name, Account account,
			Presence presence, String status)
	{
		this.userId = userId;
		this.name = name;
		this.account = account;
		this.presence = presence;
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
	public String getName()
	{
		return this.name;
	}
	public void setName(String name)
	{
		this.name = name;
	}
	public Account getAccount()
	{
		return this.account;
	}
	public void setAccount(Account account)
	{
		this.account = account;
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
	@Override
	public void save()
	{
		// TODO Auto-generated method stub
		
	}
	
//	@Override
//	public ArrayList<Im> get(String selection,
//			String[] selectionArgs)
//	{
//		ArrayList<Im> list = new ArrayList<Im>();
//		final Cursor cursor = query(selection, selectionArgs);
//		while(cursor.moveToNext())
//		{
//			Im item = new Im(cursor.getLong(0));
//			list.add(item);
//		}
//		return list;
//	}

	public long getId()
	{
		return this.id;
	}

	public void setId(long id)
	{
		this.id = id;
	}

	@Override
	protected void get()
	{
		// TODO Auto-generated method stub
		
	}
}
