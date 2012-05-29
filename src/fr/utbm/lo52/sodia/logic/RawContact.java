package fr.utbm.lo52.sodia.logic;

import java.util.List;
import java.util.Map;
import java.util.Set;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract.RawContacts;

public class RawContact extends DataBaseObject
{
	private static Map<Long, RawContact> rawContacts;
	
	private boolean dirty;
	private Account account;
	private List<Im> ims;

	protected Uri uri = RawContacts.CONTENT_URI;
	protected String[] projection = new String[]
		{ RawContacts._ID, RawContacts.ACCOUNT_TYPE, RawContacts.ACCOUNT_NAME, RawContacts.DIRTY };

	public RawContact(long id)
	{
		super(id); // Mais c'est une super idée !
	}

	public RawContact(boolean dirty, Account account)
	{
		this.account = account;
	}

	public Account getAccount()
	{
		return this.account;
	}

	public void setAccount(Account account)
	{
		this.account = account;
	}
	
	public void addIm(Im im)
	{
		this.ims.add(im);
	}
	
	public void removeIm(Im im)
	{
		this.ims.remove(im);
	}

	@Override
	public void save()
	{
		// TODO Auto-generated method stub

	}

	@Override
	protected void get()
	{
		ims = new ArrayList<Im>();
		rawContacts.put(id, this);
		final Cursor cursor = query();
		if(cursor != null && cursor.moveToFirst())
		{
			setDirty(cursor.getInt(3) !=0);
			for(Account account : AccountManager.get(context).getAccountsByType(cursor.getString(1)))
			{
				if(account.name == cursor.getString(2))
				{
					setAccount(account);
					break;
				}
			}
		}
		if (cursor != null)
		{
			cursor.close();
		}
	}

	public boolean isDirty()
	{
		return this.dirty;
	}

	public void setDirty(boolean dirty)
	{
		this.dirty = dirty;
	}
	
	public static RawContact get(long id)
	{
		if (rawContacts.containsKey(id))
		{
			return rawContacts.get(id);
		}
		return new RawContact(id);
	}
}
