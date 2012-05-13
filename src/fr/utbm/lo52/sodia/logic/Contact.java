package fr.utbm.lo52.sodia.logic;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract.Contacts;
import android.provider.ContactsContract.RawContacts;

public class Contact
{
	public final Uri lookup;
	private final Cursor rawContacts;
	private final Context context;
	
	public Contact(Context context, Uri lookup)
	{
		this.lookup = lookup;
		this.context = context;
		//TODO add filtering and Column selection
		rawContacts = ContactQuery.suportedRawContacts(context, lookup);
	}
	
	protected void finalize() throws Throwable
	{
		rawContacts.close();
	} 
	
	public String name()
	{
		rawContacts.moveToFirst();
		return "";
	}
	
	public void name(String name)
	{
		
	}
	
	public String status()
	{
		return "";
	}
	
	public int presence()
	{
		return 42;
	}
}
