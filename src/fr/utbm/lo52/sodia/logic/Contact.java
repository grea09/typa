package fr.utbm.lo52.sodia.logic;

import android.accounts.Account;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import android.content.ContentProviderOperation;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds;
import android.provider.ContactsContract.Contacts;
import android.provider.ContactsContract.Data;
import android.provider.ContactsContract.RawContacts;
import fr.utbm.lo52.sodia.protocols.typa.Typa;
import java.util.HashMap;

public class Contact extends DataBaseObject implements InterGroup<Group>
{

	private static Map<Long, Contact> contacts = new HashMap<Long, Contact>();

	private Uri lookup;

	private String name;
	private List<RawContact> rawContacts;
	private Set<Group> groups;
	private int[] position;

	@Override
	protected Uri uri()
	{
		return ContactsContract.Contacts.CONTENT_URI;
	}
	
	@Override
	protected String[] projection()
	{
		return new String[]
		{ ContactsContract.Contacts._ID, ContactsContract.Contacts.DISPLAY_NAME };
	}

	protected Contact(Uri lookup)
	{
		super(getIdFromUri(lookup));
		this.lookup = lookup;
	}

	protected Contact(long id)
	{
		super(id);
		contacts.put(id, this);
	}

	public Contact(String name)
	{
		rawContacts = new ArrayList<RawContact>();
		groups = new HashSet<Group>();
		this.name = name;
	}

	public void add(Group group)
	{
		if (!groups.contains(group))
		{
			groups.add(group);
			group.add(this);
		}

	}

	public void remove(Group group)
	{
		if (groups.contains(group))
		{
			groups.remove(group);
			group.remove(this);
		}
	}

	public void add(Set<Group> groups)
	{
		if (!this.groups.containsAll(groups))
		{
			this.groups.addAll(groups);
			for (Group group : groups)
			{
				group.add(this);
			}
		}
	}

	public void remove(Set<Group> groups)
	{
		for (Group group : groups)
		{
			remove(group);
		}
	}
	
	public Im[] getImByProtocol(String protocol)
	{
		ArrayList<Im> ims = new ArrayList<Im>();
		for(RawContact rawContact : getRawContacts())
		{
			for(Im im :rawContact.getImByProtocol(protocol))
			{
				ims.add(im);
			}
		}
		return ims.toArray(new Im[ims.size()]);
	}

	public String getName()
	{
		return this.name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public void addRawContact(RawContact rawContact)
	{
		this.rawContacts.add(rawContact);
	}

	public void removeRawContact(RawContact rawContact)
	{
		this.rawContacts.remove(rawContact);
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
		final Cursor contact = contentResolver.query(uri, new String[]
			{ Contacts._ID }, null, null, null);
		contact.moveToFirst();
		return contact.getLong(0);
	}

	@Override
	protected void get()
	{
		rawContacts = new ArrayList<RawContact>();
		groups = new HashSet<Group>();
		contacts.put(id, this);
		final Cursor cursor = query();
		if (cursor != null && cursor.moveToFirst())
		{
			setName(cursor.getString(1));
		}
		if (cursor != null)
		{
			cursor.close();
		}
		
		// RawContact Fill
		final Cursor rawContacts = (new RawContact(false, null, null)).query(
				RawContacts.CONTACT_ID + "=?", new String[]
					{ Long.toString(id) });
		while (rawContacts != null && rawContacts.moveToNext())
		{
			addRawContact(RawContact.get(rawContacts.getLong(0)));
		}
		
		if (rawContacts != null)
		{
			rawContacts.close();
		}
		// Group Fill
		final Cursor groups = contentResolver
				.query(ContactsContract.Data.CONTENT_URI,
						new String[]
							{ ContactsContract.CommonDataKinds.GroupMembership.GROUP_ROW_ID },
						ContactsContract.CommonDataKinds.GroupMembership.CONTACT_ID
								+ "=?", new String[]
							{ Long.toString(id) }, null);
		while (groups != null && groups.moveToNext())
		{
			this.add(Group.get(groups.getLong(0)));
		}
		if (groups != null)
		{
			groups.close();
		}
	}

	public static Contact get(long id)
	{
		if (contacts.containsKey(id))
		{
			return contacts.get(id);
		}
		return new Contact(id);
	}

	@Override
	public void save() throws RemoteException, OperationApplicationException
	{
		Log.i(getClass().getSimpleName(), "save");
		List<Group> corrupted = new ArrayList<Group>();
		for (Group group : this.groups)
		{
			if(group.getAccount() == null)
			{
				corrupted.add(group);
				continue;
			}
			//group.save();
		}
		groups.removeAll(corrupted);
		
		for (RawContact rawContact : this.rawContacts)
		{
			rawContact.setName(name);
			rawContact.save();
			for (Group group : this.groups)
			{
				rawContact.addToGroup(group);
			}
		}
		
		if(!rawContacts.isEmpty())
		{
			final Cursor cursor = contentResolver.query(RawContacts.CONTENT_URI, 
					new String[]{RawContacts.CONTACT_ID}, 
					RawContacts._ID + "=?", 
					new String[]{"" + rawContacts.get(0).getId()}, null);
			if (cursor != null && cursor.moveToFirst())
			{
				id = cursor.getLong(0);
			}
			if (cursor != null)
			{
				cursor.close();
			}
		}
	}

	@Override
	public boolean equals(Object object)
	{
		Contact contact = (Contact) object;
		return (contact.name == null ? this.name == null : contact.name.equals(this.name)) || super.equals(object);
	}
	
	public static Contact simpleCreate(Account account, String name, Im im, String groupName) throws RemoteException, OperationApplicationException
	{
		Contact contact = getByName(name, account);
		RawContact rawContact = new RawContact(false, account, new Name(name, null, null, null));
		rawContact.addIm(im);
		Group group = Group.getByName(groupName, account);
		contact.addRawContact(rawContact);
		contact.add(group);
		contact.save();
		return contact;
	}
	
	public static Contact getByName(String name, Account account)
	{
		Contact contact = null;
		final Cursor cursor = new Contact("").query(ContactsContract.Contacts.DISPLAY_NAME + "=? ", new String[]{name});
		if(cursor != null && cursor.moveToFirst())
		{
			contact = Contact.get(cursor.getLong(0));
		}
		if (cursor != null)
		{
			cursor.close();
		}
		if(contact == null)
		{
			return new Contact(name);
		}
		return contact;
	}
	
	public static Contact getByIm(String im)
	{
		Contact contact = null;
		final Cursor cursor = contentResolver.query(Data.CONTENT_URI, 
				new String[] {Data.CONTACT_ID}, 
				Data.MIMETYPE + "=? AND " + CommonDataKinds.Im.DATA + "=?" , 
				new String[]{CommonDataKinds.Im.MIMETYPE, im}, 
				null
		);
		if(cursor != null && cursor.moveToFirst())
		{
			contact = Contact.get(cursor.getLong(0));
		}
		if (cursor != null)
		{
			cursor.close();
		}
		return contact;
	}
	
	
	
	@Override
	protected ContentProviderOperation operation()
	{
		return null;
	}

	public List<RawContact> getRawContacts()
	{
		return this.rawContacts;
	}

	public int[] getPosition()
	{
		return position;
	}

	public void setPosition(int[] position)
	{
		this.position = position;
	}
	
	public static Contact[] getAll(Account account)
	{
		List<Contact> allContacts = new ArrayList<Contact>();
		final Cursor cursor = contentResolver.query(
			RawContacts.CONTENT_URI, 
			new String[]
			{
				RawContacts.CONTACT_ID
			}, 
			RawContacts.ACCOUNT_NAME + "=? AND " + RawContacts.ACCOUNT_TYPE + "=?", 
			new String[]
			{
				account.name, account.type
			},
			null);
		while(cursor != null && cursor.moveToNext())
		{
			allContacts.add(Contact.get(cursor.getLong(0)));
		}
		if (cursor != null)
		{
			cursor.close();
		}
		List<Contact> contacts = new ArrayList<Contact>();
		Map<String, Contact> names = new HashMap<String, Contact>();
		for(Contact contact : allContacts)
		{
			if(contact.name == null)
			{
				continue;
			}
			if(names.containsKey(contact.name))
			{
				continue;
			}
			names.put(contact.name, contact);
			contacts.add(contact);
		}
		
		
		return contacts.toArray(new Contact[contacts.size()]);
	}
	
	public static void removeAll(Account account) throws RemoteException, OperationApplicationException
	{
		ArrayList<ContentProviderOperation> operations = new ArrayList<ContentProviderOperation>();
		operations.add(
			ContentProviderOperation.newDelete(RawContacts.CONTENT_URI)
				.withSelection(RawContacts.ACCOUNT_NAME + "=? AND " + RawContacts.ACCOUNT_TYPE + "=?", new String[]{account.name, account.type}).build()
		);
		contentResolver.applyBatch(ContactsContract.AUTHORITY,
				operations);
	}
	
	public Status getBest()
	{
		Status status = new Status(Presence.OFFLINE, "");
		for(RawContact rawContact : rawContacts)
		{
			for(Im im: rawContact.getImByProtocol((new Typa()).getName()))
			{
				if(status.getPresence().ordinal() < im.getStatus().getPresence().ordinal())
				{
					status = im.getStatus();
				}
			}
		}
		return status;
	}
}
