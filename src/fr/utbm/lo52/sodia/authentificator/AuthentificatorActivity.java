package fr.utbm.lo52.sodia.authentificator;

import android.accounts.Account;
import android.accounts.AccountAuthenticatorActivity;
import android.accounts.AccountManager;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import fr.utbm.lo52.sodia.R;

public class AuthentificatorActivity extends AccountAuthenticatorActivity
{
	/** @author Android Open Source Project
	 * The Intent flag to confirm credentials. */
	public static final String PARAM_CONFIRM_CREDENTIALS = "confirmCredentials";

	/**  @author Android Open Source Project
	 * The Intent extra to store password. */
	public static final String PARAM_PASSWORD = "password";

	/**  @author Android Open Source Project
	 * The Intent extra to store username. */
	public static final String PARAM_USERNAME = "username";

	/**  @author Android Open Source Project
	 * The Intent extra to store username. */
	public static final String PARAM_AUTHTOKEN_TYPE = "authtokenType";
	
	private AccountManager accountManager;
	
	/** for posting authentication attempts back to UI thread */
	private final Handler handler = new Handler();

	/** Keep track of the progress dialog so we can dismiss it */
	private ProgressDialog progressDialog = null;
	
	
	private TextView message;
	
	private String username;
	private EditText usernameEdit;
	
	private String password;
	private EditText passwordEdit;
	
	/**
	 * If set we are just checking that the user knows their credentials; this
	 * doesn't cause the user's password or authToken to be changed on the
	 * device.
	 */
	private Boolean confirmCredentials = false;
	
	/** Was the original caller asking for an entirely new account? */
	protected boolean requestNewAccount = false;
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onCreate(Bundle icicle) {

		Log.i(getClass().getSimpleName(), "onCreate(" + icicle + ")");
		super.onCreate(icicle);
		accountManager = AccountManager.get(this);
		Log.i(getClass().getSimpleName(), "loading data from Intent");
		final Intent intent = getIntent();
		username = intent.getStringExtra(PARAM_USERNAME);
		requestNewAccount = username == null;
		confirmCredentials = intent.getBooleanExtra(PARAM_CONFIRM_CREDENTIALS, false);
		Log.i(getClass().getSimpleName(), "	request new: " + requestNewAccount);
		requestWindowFeature(Window.FEATURE_LEFT_ICON);
		setContentView(R.layout.login);
		getWindow().setFeatureDrawableResource(
				Window.FEATURE_LEFT_ICON, android.R.drawable.ic_input_get);
		message = (TextView) findViewById(R.id.message);
		usernameEdit = (EditText) findViewById(R.id.username_edit);
		passwordEdit = (EditText) findViewById(R.id.password_edit);
		passwordEdit.setVisibility(TRIM_MEMORY_UI_HIDDEN);
		findViewById(R.id.password_label).setVisibility(TRIM_MEMORY_UI_HIDDEN);
		if (!TextUtils.isEmpty(username)) usernameEdit.setText(username);
		//message.setText(getMessage());
	}
	
	/**
     * Handles onClick event on the Submit button. Sends username/password to
     * the server for authentication. The button is configured to call
     * handleLogin() in the layout XML.
     *
     * @param view The Submit button for which this method is invoked
     */
    public void handleLogin(View view) {
        if (requestNewAccount) {
            username = usernameEdit.getText().toString();
        }
        password = passwordEdit.getText().toString();
        if (TextUtils.isEmpty(username) )//|| TextUtils.isEmpty(password)) {
        {
            message.setText("User name emtpy.");
        } else {
            // Show a progress dialog, and kick off a background task to perform
            // the user login attempt.
            //showProgress();
            //authTask = new UserLoginTask();
            //authTask.execute();
        	final Account account = new Account(username, "bonjour");
            if (requestNewAccount) {
                accountManager.addAccountExplicitly(account, password, null);
                // Set contacts sync for this account.
                ContentResolver.setSyncAutomatically(account, ContactsContract.AUTHORITY, true);
            } else {
                accountManager.setPassword(account, password);
            }
            final Intent intent = new Intent();
            intent.putExtra(AccountManager.KEY_ACCOUNT_NAME, username);
            intent.putExtra(AccountManager.KEY_ACCOUNT_TYPE, "bonjour");
            setAccountAuthenticatorResult(intent.getExtras());
            setResult(RESULT_OK, intent);
            finish();
        }
    }
}
