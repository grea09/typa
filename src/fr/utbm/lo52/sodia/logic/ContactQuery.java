package fr.utbm.lo52.sodia.logic;

import android.accounts.Account;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.net.Uri.Builder;
import android.provider.ContactsContract.CommonDataKinds.Im;
import android.provider.ContactsContract.Contacts;
import android.provider.ContactsContract.Data;
import android.provider.ContactsContract.RawContacts;
import android.provider.ContactsContract.RawContacts.Entity;
import fr.utbm.lo52.sodia.protocols.Protocol;
import fr.utbm.lo52.sodia.protocols.ProtocolManager;
import fr.utbm.lo52.sodia.protocols.bonjour.Bonjour;

public class ContactQuery
{
	
	public static final Uri suportedRawContactsUri;
	static
	{
		Builder builder = RawContacts.CONTENT_URI.buildUpon();
		
		//TODO for each supported account Type
		//.appendQueryParameter(RawContacts.ACCOUNT_NAME, accountName)
		builder.appendQueryParameter(RawContacts.ACCOUNT_TYPE, (new Bonjour()).getName());
		
		suportedRawContactsUri = Uri.withAppendedPath(builder.build(), Entity.CONTENT_DIRECTORY);
	}
	
	
	public static String registeredProtocolsSelection()
	{
		String value = Data.MIMETYPE + "='" + Im.CONTENT_ITEM_TYPE + "' AND (" +
				Im.PROTOCOL+ " IN ('" + Im.PROTOCOL_GOOGLE_TALK + "','" + Im.PROTOCOL_JABBER + "') OR (" +
				Im.PROTOCOL + "=" + Im.PROTOCOL_CUSTOM + " AND "+ 
				Im.CUSTOM_PROTOCOL + " IN ("; 
		for(Class<? extends Protocol> clazz : ProtocolManager.getProtocols())
		{
			value += "'" + clazz.getSimpleName() + "',";
		}
		value = value.substring(0, value.length()-1);
		value += ") ) )";
		return value;
	}
	
	public static Account best(Context context, long contact)
	{
		final ContentResolver resolver = context.getContentResolver();
		final Cursor rawContacts = resolver.query(RawContacts.CONTENT_URI,
				new String[]{RawContacts._ID},
				RawContacts.CONTACT_ID + "=? AND " + registeredProtocolsSelection(),
				new String[]{String.valueOf(contact)}, null);
		while(rawContacts.moveToNext())
		{
			final long rawContactId = rawContacts.getLong(0);
			Uri rawContactUri = ContentUris.withAppendedId(RawContacts.CONTENT_URI, rawContactId);
			Uri entityUri = Uri.withAppendedPath(rawContactUri, Entity.CONTENT_DIRECTORY);
			//TODO Great Contact wrapper
			Cursor rawContact = resolver.query(entityUri,
					new String[]{RawContacts.SOURCE_ID, Entity.DATA_ID, Entity.MIMETYPE, Entity.DATA1},
					null, null, null);
		}
		return null;
	}
	
	public static Cursor suportedRawContacts(Context context, Uri lookup)
	{
		final ContentResolver resolver = context.getContentResolver();
		final Cursor contact = context.getContentResolver().query(lookup, new String[]{Contacts._ID}, null, null, null);
		contact.moveToFirst();
		final Cursor rawContacts = resolver.query(suportedRawContactsUri,
				null,
				RawContacts.CONTACT_ID + "=?",
				new String[]{String.valueOf(contact.getLong(0))}, 
				null);
		return rawContacts;
	}
}
