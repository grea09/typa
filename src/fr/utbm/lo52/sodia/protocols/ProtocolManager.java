package fr.utbm.lo52.sodia.protocols;

import java.lang.reflect.Array;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import fr.utbm.lo52.sodia.R;
import android.accounts.Account;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.widget.RemoteViews;
import fr.utbm.lo52.sodia.logic.Message;

public class ProtocolManager
{
	
	public static final int NEW_CONTACT_NOTIFICATION_ID = 1;
	
	public static class ProtocolAlreadyRegisteredException extends Exception
	{
		/**
		 * Generated serialVersionUID
		 */
		private static final long serialVersionUID = 1962729403538150923L;

	}

	private static Map<Account, Protocol> accounts;
	
	public static Set<Account> getAccounts()
	{
		return accounts.keySet();
	}
	
	public static Set<Class<? extends Protocol>> getProtocols()
	{
		Set<Class<? extends Protocol>> protocols = new HashSet<Class<? extends Protocol>>();
		for(Protocol protocol : accounts.values())
		{
			protocols.add(protocol.getClass());
		}
		return protocols;
	}
	
	public static Set<String> getAccountTypes()
	{
		Set<Account> accounts = ProtocolManager.accounts.keySet();
		Set<String> value = new HashSet<String>();
		for(Account account : accounts)
		{
			value.add(account.type);
		}
		return value;
	}
	
	public static Protocol getProtocol(Account account)
	{
		return accounts.get(account);
	}

	public static void register(Protocol protocol)
			throws ProtocolAlreadyRegisteredException
	{
		if (accounts.containsKey(protocol.account()))
			throw new ProtocolAlreadyRegisteredException();
		accounts.put(protocol.account(), protocol);
	}

	public static void send(Context context, Message message, long contact)
	{
		// TODO accounts.get(account).send(message);
	}

	public static void presence(Context context, long status, String message, Account account)
	{
		//TODO : add logo
		accounts.get(account).presence(status);
	}

	public static void unregister(Protocol protocol)
	{
		if (accounts.containsValue(protocol)
				&& accounts.containsKey(protocol.account()))
			accounts.remove(protocol.account());
	}
	
	public static void receive(Context context, Message message, String contact, Account account)
	{
		
	}
	
	public static void newContact(Context context, Bitmap photo, String name, String contact, Account account)
	{
		NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		RemoteViews contentView = new RemoteViews(R.class.getPackage().getName(), R.layout.new_contact_notification);
		contentView.setImageViewBitmap(R.id.notificationPhoto, photo);
		contentView.setTextViewText(R.id.notificationContactName, name);
		contentView.setTextViewText(R.id.notificationContactId, contact);
		contentView.setTextViewText(R.id.notificationTime, 
				Calendar.getInstance().get(Calendar.HOUR) + ":" +
				Calendar.getInstance().get(Calendar.MINUTE));
		Notification notification;
		//TODO Factorize
		if(android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.HONEYCOMB)
		{
			Notification.Builder builder = new Notification.Builder(context);
			builder.setContent(contentView);
			builder.setSmallIcon(R.drawable.ic_notification);
			//TODO Remove hardcoded String
			builder.setTicker("A new contact request your authorisation.");
			notification = builder.getNotification();
		}
		else
		{
			notification = new Notification(R.drawable.ic_notification, "A new contact request your authorisation.", 0 );
			notification.contentView = contentView;
		}
//		Intent notificationIntent = new Intent(this, MyClass.class);
//		PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
//		notification.contentIntent = contentIntent;
		notificationManager.notify(NEW_CONTACT_NOTIFICATION_ID, notification);
		
	}
	
	public static void presence(Context context, long status, String message, String contact, Account account)
	{
		
	}
	
	public static void newRoster(String name, Array contacts)
	{
		// Creation du nouveau groupe 
		// ajout des contacts
		
	}

}
