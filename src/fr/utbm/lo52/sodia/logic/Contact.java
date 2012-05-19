package fr.utbm.lo52.sodia.logic;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Set;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import fr.utbm.lo52.sodia.common.GroupUpdater;

public class Contact implements InterGroup<Group>
{
	private Uri lookup;
	private Cursor rawContacts;
	private Context context;
	private ContentResolver contentResolver;
	
	private String name;
	private ArrayList<Im> im;
	private Set<Group> groups;
	
	
	public void save(Context context)
	{
		this.context = context;
		this.save();
	}
	
	
	public void save()
	{
		if(context == null)
		{
			throw new InvalidParameterException("Context not set. Please use save(Context context)");
		}
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
	
	public Contact(Context context, Uri lookup)
	{
		this.lookup = lookup;
		this.context = context;
		this.contentResolver = context.getContentResolver();
		rawContacts = ContactQuery.suportedRawContacts(context, lookup);
	}
	
	public Contact(String name)
	{
		this.name = name;
	}
	
	protected void finalize() throws Throwable
	{
		rawContacts.close();
	}

	public void add(Group group)
	{
		// TODO Auto-generated method stub
		GroupUpdater.magicUpdate(groups.contains(group), group, this);
		
	}

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
	}

	public ArrayList<Im> getIm()
	{
		return this.im;
	}

	public void setIm(ArrayList<Im> im)
	{
		this.im = im;
	}

	public Uri getLookup()
	{
		return this.lookup;
	}


	public Set<Group> getGroups()
	{
		return this.groups;
	}
}
