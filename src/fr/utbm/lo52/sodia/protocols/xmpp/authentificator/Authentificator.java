package fr.utbm.lo52.sodia.protocols.xmpp.authentificator;

import android.accounts.AbstractAccountAuthenticator;
import android.accounts.Account;
import android.accounts.AccountAuthenticatorResponse;
import android.accounts.AccountManager;
import android.accounts.NetworkErrorException;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

class Authenticator extends AbstractAccountAuthenticator 
{

	// Authentication Service context
	private final Context context;

	public Authenticator(Context context) 
	{
		super(context);
		this.context = context;
	}

	@Override
	public Bundle addAccount(AccountAuthenticatorResponse response, String accountType,
			String authTokenType, String[] requiredFeatures, Bundle options) 
	{
		Log.v(getClass().getSimpleName(), "addAccount()");
		final Intent intent = new Intent(context, AuthentificatorActivity.class);
		intent.putExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE, response);
		final Bundle bundle = new Bundle();
		bundle.putParcelable(AccountManager.KEY_INTENT, intent);
		return bundle;
	}

	@Override
	public Bundle confirmCredentials(
			AccountAuthenticatorResponse response, Account account, Bundle options) 
	{
		Log.v(getClass().getSimpleName(), "confirmCredentials()");
		return null;
	}

	@Override
	public Bundle editProperties(AccountAuthenticatorResponse response, String accountType) 
	{
		Log.v(getClass().getSimpleName(), "editProperties()");
		throw new UnsupportedOperationException();
	}

	@Override
	public Bundle hasFeatures(
			AccountAuthenticatorResponse response, Account account, String[] features) 
	{
		Log.v(getClass().getSimpleName(), "hasFeatures()");
		final Bundle result = new Bundle();
		result.putBoolean(AccountManager.KEY_BOOLEAN_RESULT, false);
		return result;
	}

	@Override
	public Bundle updateCredentials(AccountAuthenticatorResponse response, Account account,
			String authTokenType, Bundle loginOptions) {
		Log.v(getClass().getSimpleName(), "updateCredentials()");
		return null;
	}

	@Override
	public Bundle getAuthToken(AccountAuthenticatorResponse response,
			Account account, String authTokenType, Bundle options)
			throws NetworkErrorException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getAuthTokenLabel(String authTokenType) {
		// TODO Auto-generated method stub
		return null;
	}
}
