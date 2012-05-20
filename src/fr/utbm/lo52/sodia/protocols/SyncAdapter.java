package fr.utbm.lo52.sodia.protocols;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.Context;
import android.content.SyncResult;
import android.os.Bundle;

/**
 * SyncAdapter implementation for syncing sample SyncAdapter contacts to the
 * platform ContactOperations provider. This sample shows a basic 2-way sync
 * between the client and a sample server. It also contains an example of how to
 * update the contacts' status messages, which would be useful for a messaging
 * or social networking client.
 */
public class SyncAdapter<E extends Protocol> extends AbstractThreadedSyncAdapter
{
	@SuppressWarnings("unused")
	private final AccountManager accountManager;

	@SuppressWarnings("unused")
	private final Context context;

	public SyncAdapter(Context context, boolean autoInitialize)
	{
		super(context, autoInitialize);
		this.context = context;
		accountManager = AccountManager.get(context);
	}

	@Override
	public void onPerformSync(Account account, Bundle extras, String authority,
			ContentProviderClient provider, SyncResult syncResult)
	{
		//TODO call some function of current protocol (Create, Register, Contact, etc)
	}
}
