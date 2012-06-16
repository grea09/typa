package fr.utbm.lo52.sodia.ui;

import java.lang.reflect.InvocationTargetException;

import android.accounts.Account;
import android.accounts.AccountAuthenticatorActivity;
import android.accounts.AccountManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import fr.utbm.lo52.sodia.R;
import fr.utbm.lo52.sodia.protocols.Authentificator;
import fr.utbm.lo52.sodia.protocols.Protocol;

public class Login extends AccountAuthenticatorActivity
{
	/**
	 * @author Android Open Source Project The Intent flag to confirm
	 *         credentials.
	 */
	public static final String PARAM_CONFIRM_CREDENTIALS = "confirmCredentials";

	/**
	 * @author Android Open Source Project The Intent extra to store password.
	 */
	public static final String PARAM_PASSWORD = "password";

	/**
	 * @author Android Open Source Project The Intent extra to store username.
	 */
	public static final String PARAM_USERNAME = "username";

	/**
	 * @author Android Open Source Project The Intent extra to store username.
	 */
	public static final String PARAM_AUTHTOKEN_TYPE = "authtokenType";

	private AccountManager accountManager;

	private String accountType = "";
	private Class<? extends Protocol> protocolClass;
	private Protocol protocol;
	private String protocolName;

	private TextView message;

	private String username;
	private EditText usernameEdit;

	private String password;
	private EditText passwordEdit;

	/** Was the original caller asking for an entirely new account? */
	protected boolean requestNewAccount = false;

	/** Do we prompt password ? **/
	private boolean hasPassword = true;

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void onCreate(Bundle icicle)
	{

		Log.i(getClass().getSimpleName(), "onCreate(" + icicle + ")");
		super.onCreate(icicle);

		accountManager = AccountManager.get(this);

		Log.i(getClass().getSimpleName(), "loading data from Intent");
		final Intent intent = getIntent();
		protocolClass = (Class<? extends Protocol>) intent
				.getSerializableExtra(Authentificator.KEY_PROTOCOL_CLASS);
		accountType = intent
				.getStringExtra(Authentificator.KEY_PROTOCOL_ACCOUNT_TYPE);
		protocolName = intent
				.getStringExtra(Authentificator.KEY_PROTOCOL_NAME);
		username = intent.getStringExtra(PARAM_USERNAME);
		requestNewAccount = username == null;
		hasPassword = intent.getBooleanExtra(
				Authentificator.KEY_PROTOCOL_HAS_PASSWORD,
				true);
		Log.i(getClass().getSimpleName(), "has password :" + hasPassword);
		Log.i(getClass().getSimpleName(), "	request new: " + requestNewAccount);
		requestWindowFeature(Window.FEATURE_LEFT_ICON);
		setContentView(R.layout.login);
		Log.i(getClass().getSimpleName(), "protocol : {" + protocolClass.getSimpleName() + ", " + accountType + ", " + protocolName + ", " + intent.getIntExtra(Authentificator.KEY_PROTOCOL_LOGO, 0) + "}");
		getWindow().setFeatureDrawableResource(
				Window.FEATURE_LEFT_ICON,
				intent.getIntExtra(Authentificator.KEY_PROTOCOL_LOGO,0));
		getWindow().setTitle(protocolName);
		message = (TextView) findViewById(R.id.message);
		usernameEdit = (EditText) findViewById(R.id.username_edit);
		passwordEdit = (EditText) findViewById(R.id.password_edit);
		if (!hasPassword)
		{
			passwordEdit.setVisibility(TRIM_MEMORY_UI_HIDDEN);
			findViewById(R.id.password_label).setVisibility(
					TRIM_MEMORY_UI_HIDDEN);
		}
		if (!TextUtils.isEmpty(username))
			usernameEdit.setText(username);
		// message.setText(getMessage());
	}

	/**
	 * Handles onClick event on the Submit button. Sends username/password to
	 * the server for authentication. The button is configured to call
	 * handleLogin() in the layout XML.
	 * 
	 * @param view
	 *            The Submit button for which this method is invoked
	 */
	public void handleLogin(View view)
	{
		if (requestNewAccount)
		{
			username = usernameEdit.getText().toString();
		}
		password = passwordEdit.getText().toString();
		if (TextUtils.isEmpty(username))
		{
			message.setText(getResources().getText(
					R.string.login_message_username));
		} else if (TextUtils.isEmpty(password) && hasPassword)
		{
			message.setText(getResources().getText(
					R.string.login_message_password));
		} else
		{
			// Show a progress dialog, and kick off a background task to perform
			// the user login attempt.
			// showProgress();
			// authTask = new UserLoginTask();
			// authTask.execute();

			final Account account = new Account(username, accountType);
			try
			{
				protocol = (Protocol) protocolClass.getMethod("get", Account.class, Context.class).invoke(null, account, this);
				protocol.connect();
			} catch (IllegalArgumentException e)
			{
				Log.e(getClass().getName(), "", e);
			} catch (IllegalAccessException e)
			{
				Log.e(getClass().getName(), "", e);
			} catch (InvocationTargetException e)
			{
				Log.e(getClass().getName(), "", e);
			} catch (NoSuchMethodException e)
			{
				Log.e(getClass().getName(), "", e);
			}

			if (requestNewAccount)
			{
				accountManager.addAccountExplicitly(account, password, null);
				// Set contacts sync for this account.
				ContentResolver.setSyncAutomatically(account,
						ContactsContract.AUTHORITY, true);
			} else
			{
				accountManager.setPassword(account, password);
			}
			final Intent intent = new Intent();
			intent.putExtra(AccountManager.KEY_ACCOUNT_NAME, username);
			intent.putExtra(AccountManager.KEY_ACCOUNT_TYPE, accountType);
			setAccountAuthenticatorResult(intent.getExtras());
			setResult(RESULT_OK, intent);
			finish();
		}
	}
}
