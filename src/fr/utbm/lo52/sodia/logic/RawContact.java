package fr.utbm.lo52.sodia.logic;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.accounts.Account;
import android.content.ContentProviderOperation;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.net.Uri;
import android.os.RemoteException;
import android.provider.ContactsContract.CommonDataKinds;
import android.provider.ContactsContract.CommonDataKinds.StructuredName;
import android.provider.ContactsContract.Data;
import android.provider.ContactsContract.RawContacts;

public class RawContact extends DataBaseObject
{
	private static Map<Long, RawContact> rawContacts;

	private boolean dirty;
	private Account account;
	private List<Im> ims;
	private Name name;

	protected Uri uri = RawContacts.CONTENT_URI;
	protected String[] projection = new String[]
		{ RawContacts._ID, RawContacts.ACCOUNT_TYPE, RawContacts.ACCOUNT_NAME,
				RawContacts.DIRTY };

	public RawContact(long id)
	{
		super(id); // Mais c'est une super idée !
	}

	public RawContact(boolean dirty, Account account, Name name)
	{
		this.account = account;
		this.dirty = dirty;
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

	public void addIm(Im im)
	{
		this.ims.add(im);
	}

	public void removeIm(Im im)
	{
		this.ims.remove(im);
	}
	
	public Im[] getImByProtocol(String protocol)
	{
		ArrayList<Im> ims = new ArrayList<Im>();
		for(Im im : getIms())
		{
			if(im.getProtocol() == protocol)
			{
				ims.add(im);
			}
		}
		return (Im[]) ims.toArray();
	}

	@Override
	protected void get()
	{
		ims = new ArrayList<Im>();
		rawContacts.put(id, this);
		final Cursor cursor = query();
		if (cursor != null && cursor.moveToFirst())
		{
			setDirty(cursor.getInt(3) != 0);
			setAccount(fromString(cursor.getString(1), cursor.getString(2)));
		}
		if (cursor != null)
		{
			cursor.close();
		}
		// Im Fill
		final Cursor ims = (new Im("", "", null)).query(Data.RAW_CONTACT_ID
				+ "=? AND " + Data.MIMETYPE + "=?", new String[]
			{ Long.toString(id), CommonDataKinds.Im.CONTENT_ITEM_TYPE });
		while (ims != null && ims.moveToNext())
		{
			addIm(new Im(cursor.getLong(0)));
		}
		if (ims != null)
		{
			ims.close();
		}
		// Name Fill
		final Cursor name = (new Name("", "", "", "")).query(
				Data.RAW_CONTACT_ID + "=? AND " + Data.MIMETYPE + "=?",
				new String[]
					{ Long.toString(id), StructuredName.CONTENT_ITEM_TYPE });
		if (name != null && name.moveToFirst())
		{
			setName(new Name(cursor.getLong(0)));
		}
		if (name != null)
		{
			name.close();
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

	public void setName(String name)
	{
		this.name.setName(name);
	}

	@Override
	protected ContentProviderOperation operation()
	{
		return ((id == -1) ? (ContentProviderOperation.newInsert(uri))
				: (ContentProviderOperation.newUpdate(uri).withSelection(
						RawContacts._ID + "=?", new String[]
							{ String.valueOf(id) })))
				.withValue(RawContacts.ACCOUNT_NAME, account.name)
				.withValue(RawContacts.ACCOUNT_TYPE, account.type)
				.withValue(RawContacts.DIRTY, dirty).build();
	}

	@Override
	public void save() throws RemoteException, OperationApplicationException
	{
		super.save();
		save((DataBaseObject[]) ims.toArray());
		save(new DataBaseObject[]
			{ name });
	}

	public Name getName()
	{
		return this.name;
	}

	public void setName(Name name)
	{
		this.name = name;
	}

	public List<Im> getIms()
	{
		return this.ims;
	}
}
