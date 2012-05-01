package fr.utbm.lo52.sodia.logic;

import android.accounts.Account;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract.CommonDataKinds.Im;
import android.provider.ContactsContract.Data;
import android.provider.ContactsContract.RawContacts;
import android.provider.ContactsContract.RawContacts.Entity;
import fr.utbm.lo52.sodia.protocols.Protocol;
import fr.utbm.lo52.sodia.protocols.ProtocolManager;

public class ContactQuery
{
	
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
			Cursor rawContact = resolver.query(entityUri,
			          new String[]{RawContacts.SOURCE_ID, Entity.DATA_ID, Entity.MIMETYPE, Entity.DATA1},
			          null, null, null);
		}
		return null;
	}
}
