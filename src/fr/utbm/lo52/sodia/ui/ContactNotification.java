package fr.utbm.lo52.sodia.ui;

import java.util.Calendar;

import android.accounts.Account;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.widget.RemoteViews;
import fr.utbm.lo52.sodia.R;

public class ContactNotification {
	
	public static final int NEW_CONTACT_NOTIFICATION_ID = 1;

	
	@SuppressWarnings("deprecation")
	public static void newContactNotification(Context context, Bitmap photo, String name, String contact, Account account)
	{
		NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		RemoteViews contentView = new RemoteViews(R.class.getPackage().getName(), R.layout.new_contact_notification);
		contentView.setImageViewBitmap(R.id.notificationPhoto, photo);
		contentView.setTextViewText(R.id.notificationLabel, "New contact request !");
		contentView.setTextViewText(R.id.notificationContactName, "");
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
			notification = new Notification(R.drawable.ic_notification, "A new contact request your authorisation.",PendingIntent.FLAG_ONE_SHOT );
			notification.contentView = contentView;
		}
		Intent notificationIntent = new Intent(context, ContactRequest.class);
		notificationIntent.putExtra("id",contact);
		notificationIntent.setAction("com.vantage.vcrm.android.telephony"+System.currentTimeMillis());
		PendingIntent contentIntent = PendingIntent.getActivity(context, (int) System.currentTimeMillis(), notificationIntent, 0);
		notification.contentIntent = contentIntent;
		notificationManager.notify(NEW_CONTACT_NOTIFICATION_ID, notification);
		
		
	}
	
}
