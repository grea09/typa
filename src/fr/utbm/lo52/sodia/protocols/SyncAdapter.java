package fr.utbm.lo52.sodia.protocols;

import fr.utbm.lo52.sodia.logic.Contact;
import fr.utbm.lo52.sodia.logic.Message;
import fr.utbm.lo52.sodia.logic.RawContact;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.Context;
import android.content.OperationApplicationException;
import android.content.SyncResult;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;

/**
 * SyncAdapter implementation for syncing sample SyncAdapter contacts to the
 * platform ContactOperations provider. This sample shows a basic 2-way sync
 * between the client and a sample server. It also contains an example of how to
 * update the contacts' status messages, which would be useful for a messaging
 * or social networking client.
 */
public class SyncAdapter<E extends Protocol> extends AbstractThreadedSyncAdapter implements ProtocolListener
{
	@SuppressWarnings("unused")
	private final AccountManager accountManager;

	private final Context context;
	
	private E protocol;

	public Context getContext()
	{
		return context;
	}

	public SyncAdapter(Context context, boolean autoInitialize)
	{
		super(context, autoInitialize);
		this.context = context;
		accountManager = AccountManager.get(context);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onPerformSync(Account account, Bundle extras, String authority,
			ContentProviderClient provider, SyncResult syncResult)
	{
		Log.i(getClass().getSimpleName(), "Sync started.");
		protocol = (E) Protocol.get(account, context);
		protocol.add(this);
		protocol.connect();
	}

	@Override
	public void receive(Message message, Account account)
	{
		switch (message.type())
		{
		case POSITION:
			
			break;

		case PRESENCE:
			break;
		}
	}

	@Override
	public void contacts(Contact[] contacts, Account account)
	{
		for(Contact contact : contacts)
		{
			for(RawContact rawContact :contact.getRawContacts())
			{
				rawContact.setAccount(account);
			}
			try
			{
				contact.save();
			} catch (RemoteException e)
			{
				Log.e(getClass().getName(), "", e);
			} catch (OperationApplicationException e)
			{
				Log.e(getClass().getName(), "", e);
			}
		}
	}
	
	public void destroy()
	{
		Log.d(getClass().getName(), "Exterminate ! Destroy !");
		if(protocol instanceof Protocol)
		{
			//protocol.disconnect();
		}
	}

	
}
