package fr.utbm.lo52.sodia.logic;

import android.accounts.Account;
import android.content.ContentProviderOperation;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.net.Uri;
import android.os.RemoteException;
import android.provider.ContactsContract;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Group extends DataBaseObject implements InterGroup<Contact>
{
	private static Map<Long, Group> groups = new HashMap<Long, Group>();

	private Set<Contact> contacts;

	private String name;

	private Account account;

	protected Group(long id)
	{
		super(id);
		groups.put(id, this);
	}

	public Group(String name)
	{
		contacts = new HashSet<Contact>();
		this.name = name;
	}

	public void add(Contact contact)
	{
		if (!contacts.contains(contact))
		{
			contacts.add(contact);
			contact.add(this);
		}
	}

	public void remove(Contact contact)
	{
		if (contacts.contains(contact))
		{
			contacts.remove(contact);
			contact.remove(this);
		}
	}

	public void add(Set<Contact> contacts)
	{
		if (!this.contacts.containsAll(contacts))
		{
			this.contacts.addAll(contacts);
			for (Contact contact : contacts)
			{
				contact.add(this);
			}
		}
	}

	public void remove(Set<Contact> contacts)
	{
		for (Contact contact : contacts)
		{
			remove(contact);
		}
	}

	public Set<Contact> getContacts()
	{
		return contacts;
	}

	@Override
	protected void get()
	{
		contacts = new HashSet<Contact>();
		groups.put(id, this);
		final Cursor cursor = query();
		if (cursor != null && cursor.moveToFirst())
		{
			setName(cursor.getString(1));
			setAccount(fromString(cursor.getString(2), cursor.getString(3)));
		}
		if (cursor != null)
		{
			cursor.close();
		}
	}

	public static Group get(long id)
	{
		if (groups.containsKey(id))
		{
			return groups.get(id);
		}
		return new Group(id);
	}

	public String getName()
	{
		return this.name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	/*
	 * @Override public ArrayList<Group> get(String selection, String[]
	 * selectionArgs) { ArrayList<Group> list = new ArrayList<Group>(); final
	 * Cursor cursor = query(selection, selectionArgs);
	 * while(cursor.moveToNext()) { Group item = new Group(cursor.getLong(0));
	 * list.add(item); } return list; }
	 */

	@Override
	public boolean equals(Object object)
	{
		Group group = (Group) object;
		return group.name == this.name || super.equals(object);
	}

	@Override
	protected ContentProviderOperation operation()
	{
		if (account == null)
		{
			throw new UnsupportedOperationException(
					"Account must be suplied before save.");
		}
		return ((id == -1) ? (ContentProviderOperation.newInsert(uri()))
				: (ContentProviderOperation.newUpdate(uri())))
				.withValue(ContactsContract.Groups.ACCOUNT_NAME, account.name)
				.withValue(ContactsContract.Groups.ACCOUNT_TYPE, account.type)
				.withValue(ContactsContract.Groups.TITLE, name).build();
	}

	public Account getAccount()
	{
		return this.account;
	}

	public void setAccount(Account account)
	{
		this.account = account;
	}
	
	public static Group getByName(String name, Account account) throws RemoteException, OperationApplicationException
	{
		Group group = null;
		final Cursor cursor = contentResolver.query(ContactsContract.Groups.CONTENT_URI, 
				new String[]{ContactsContract.Groups._ID}, 
				ContactsContract.Groups.TITLE + " =? AND " +
				ContactsContract.Groups.ACCOUNT_NAME + " =? AND " +
				ContactsContract.Groups.ACCOUNT_TYPE + " =?", 
				new String[]{name, account.name, account.type}, null);
		if(cursor != null && cursor.moveToFirst())
		{
			group = Group.get(cursor.getLong(0));
		}
		if (cursor != null)
		{
			cursor.close();
		}
		if(group == null)
		{
			group = new Group(name);
			group.setAccount(account);
			group.save();
		}
		return group;
	}

	@Override
	protected Uri uri() {
		return ContactsContract.Groups.CONTENT_URI;
	}

	@Override
	protected String[] projection() {
		return new String[]
		{ ContactsContract.Groups._ID, ContactsContract.Groups.TITLE,
				ContactsContract.Groups.ACCOUNT_TYPE,
				ContactsContract.Groups.ACCOUNT_NAME };
	}

}
