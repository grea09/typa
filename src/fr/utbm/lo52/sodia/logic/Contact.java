package fr.utbm.lo52.sodia.logic;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Contacts;
import fr.utbm.lo52.sodia.common.GroupUpdater;

public class Contact extends DataBaseObject implements InterGroup<Group>
{
	
	private static Map<Long, Contact> contacts;
	
	private Uri lookup;
	
	private String name;
	private ArrayList<Im> ims;
	private Set<Group> groups;
	
	protected Uri uri = ContactsContract.Contacts.CONTENT_URI;
	protected  String[] projection = new String[] { ContactsContract.Contacts._ID, ContactsContract.Contacts.DISPLAY_NAME };
	
	public Contact(Uri lookup)
	{
		super(getIdFromUri(lookup));
		groups = new HashSet<Group>();
		this.lookup = lookup;
	}
	
	public Contact(long id)
	{
		
		super(id);
		groups = new HashSet<Group>();
	}
	
	public Contact(String name)
	{
		groups = new HashSet<Group>();
		this.name = name;
	}
	
	@Override
	public void add(Group group)
	{
		// TODO Auto-generated method stub
		GroupUpdater.magicUpdate(groups.contains(group), group, this);
		
	}
	
	@Override
	public void remove(Group group)
	{
		// TODO Auto-generated method stub
		GroupUpdater.magicUpdate(groups.contains(group), group, this);
	}
	
	public String getName()
	{
		return this.name;
	}
	
	public void setName(String name)
	{
		this.name = name;
		for(Im im : ims)
		{
			im.setName(name);
		}
	}
	
	public void addIm(Im im)
	{
		this.ims.add(im);
	}
	
	public void removeIm(Im im)
	{
		this.ims.remove(im);
	}
	
	public Uri getLookup()
	{
		return this.lookup;
	}
	
	
	
	public Set<Group> getGroups()
	{
		return this.groups;
	}
	
	public static long getIdFromUri(Uri uri)
	{
		final Cursor contact = contentResolver.query(uri, new String[]{Contacts._ID}, null, null, null);
		contact.moveToFirst();
		return contact.getLong(0);
	}

	@Override
	protected void get()
	{
		contacts.put(id, this);
		final Cursor cursor = query(ContactsContract.Contacts._ID + "=?", new String[]{Long.toString(id)});
		if(cursor != null && cursor.moveToFirst())
		{
			setName(cursor.getString(1));
		}
		if(cursor != null)
		{
			cursor.close();
		}
		//IM Fill
		final Cursor ims = (new Im(-1)).query("=?", null);
		if(ims != null && ims.moveToFirst())
		{
			//TODO Array push Im get
		}
		if(ims != null)
		{
			ims.close();
		}
		//Group Fill
		final Cursor groups = contentResolver.query(ContactsContract.Data.CONTENT_URI, 
			new String[] {ContactsContract.CommonDataKinds.GroupMembership.GROUP_ROW_ID}, 
			ContactsContract.CommonDataKinds.GroupMembership.CONTACT_ID + "=?", 
			new String[]{Long.toString(id)},
			null);
		while(groups.moveToNext())
		{
			this.add(Group.get(groups.getLong(0)));
		}
	}
	
	public static Contact get(long id)
	{
		if(contacts.containsKey(id))
		{
			return contacts.get(id);
		}
		return new Contact(id);
	}
	
	public void save()
	{
		//TODO if new insert in DB else save data;
		/* TODO db connect
		Uri.withAppendedPath(
			RawContacts.CONTENT_URI.buildUpon()
				.appendQueryParameter(RawContacts.ACCOUNT_TYPE, account.type)
				.build(), Entity.CONTENT_DIRECTORY);
		final Cursor contact = context.getContentResolver().query(lookup, new String[]{Contacts._ID}, null, null, null);
		contact.moveToFirst();
		final Cursor rawContacts = resolver.query(suportedRawContactsUri,
				null,
				RawContacts.CONTACT_ID + "=?",
				new String[]{String.valueOf(contact.getLong(0))}, 
				null);
		*/
	}

	@Override
	public void setId(long id)
	{
		super.setId(id);
		contacts.put(id, this);
	}

/*
	@Override
	public ArrayList<Contact> get(String selection,
			String[] selectionArgs)
	{
		ArrayList<Contact> list = new ArrayList<Contact>();
		final Cursor cursor = query(selection, selectionArgs);
		while(cursor.moveToNext())
		{
			Contact item = new Contact(cursor.getLong(0));
			list.add(item);
		}
		return list;
	}
*/

	@Override
	public boolean equals(Object object)
	{
		Contact contact = (Contact) object;
		return contact.name == this.name || super.equals(object);
	}
}
