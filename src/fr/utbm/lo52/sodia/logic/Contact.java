package fr.utbm.lo52.sodia.logic;

import android.accounts.Account;
import android.content.ContentResolver;
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
	private final ContentResolver contentResolver;
	
	public Contact(Context context, Uri lookup)
	{
		this.lookup = lookup;
		this.context = context;
		this.contentResolver = context.getContentResolver();
		rawContacts = ContactQuery.suportedRawContacts(context, lookup);
	}
	
	protected void finalize() throws Throwable
	{
		rawContacts.close();
	} 
	
	public String name()
	{
		 Cursor contact = contentResolver.query(lookup,
				new String[]{Contacts.DISPLAY_NAME}, null, null, null);
		return contact.getString(0);
	}
	
	public void name(String name, Account account)
	{
/* TODO Setters
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
	
	public String status()
	{
		return "";
	}
	
	public int presence()
	{
		return 42;
	}
}
