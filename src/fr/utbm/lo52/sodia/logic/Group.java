package fr.utbm.lo52.sodia.logic;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import fr.utbm.lo52.sodia.common.GroupUpdater;

public class Group extends DataBaseObject implements InterGroup<Contact>
{
	private static Map<Long, Group> groups;
	
	private Set<Contact> contacts;
	
	private String name;
	
	
	protected Uri uri = ContactsContract.Groups.CONTENT_URI;
	protected  String[] projection = new String[] { ContactsContract.Groups._ID, ContactsContract.Groups.TITLE };
	
	public Group(long id)
	{
		super(id);
		contacts = new HashSet<Contact>();
		groups.put(id, this);
	}
	
	public Group(String name)
	{
		contacts = new HashSet<Contact>();
		this.name = name;
	}
	
	public void add(Contact contact)
	{
		GroupUpdater.magicUpdate(contacts.contains(contact), contact, this);
	}
	
	public void remove(Contact contact)
	{
		GroupUpdater.magicUpdate(contacts.contains(contact), contact, this);
	}
	
	public Set<Contact> getContacts()
	{
		return contacts;
	}

	@Override
	public void save()
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void get()
	{
		groups.put(id, this);
		final Cursor cursor = query(ContactsContract.Groups._ID + "=?", new String[] { Long.toString(id), null });
		cursor.moveToFirst();
		setName(cursor.getString(1));
		//No Contact fill
	}
	
	public static Group get(long id)
	{
		if(groups.containsKey(id))
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
	
	@Override
	public void setId(long id)
	{
		super.setId(id);
		groups.put(id, this);
	}

/*
	@Override
	public ArrayList<Group> get(String selection,
			String[] selectionArgs)
	{
		ArrayList<Group> list = new ArrayList<Group>();
		final Cursor cursor = query(selection, selectionArgs);
		while(cursor.moveToNext())
		{
			Group item = new Group(cursor.getLong(0));
			list.add(item);
		}
		return list;
	}
*/
	
	@Override
	public boolean equals(Object object)
	{
		Group group = (Group) object;
		return group.name == this.name || super.equals(object);
	}
}
