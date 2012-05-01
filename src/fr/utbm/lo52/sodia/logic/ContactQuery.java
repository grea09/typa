package fr.utbm.lo52.sodia.logic;

import java.util.Set;

import android.accounts.Account;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract.CommonDataKinds.Im;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.Data;
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
		Set<String> types = ProtocolManager.getAccountTypes();
		final Cursor cursor = context.getContentResolver().query(Data.CONTENT_URI,
			new String[] {Data._ID, },
			registeredProtocolsSelection() + " AND " + Data.CONTACT_ID + "=?",
			new String[] {String.valueOf(contact)}, null);
		while (cursor.moveToNext()) {
			
		}
		return null;
	}
}
