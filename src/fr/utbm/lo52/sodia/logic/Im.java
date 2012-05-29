package fr.utbm.lo52.sodia.logic;

import java.util.Map;

import android.accounts.Account;
import android.net.Uri;

public class Im extends DataBaseObject
{
	private static Map<Long, Im> ims;
	
	private String userId;
	private String name;
	private Presence presence;
	private String status;

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

	public Im(String userId, String name, Account account, Presence presence,
			String status)
	{
		this.userId = userId;
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

	@Override
	protected void get()
	{
		// TODO Auto-generated method stub
		
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
}
