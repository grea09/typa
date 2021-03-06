package fr.utbm.lo52.sodia.ui;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.QuickContactBadge;
import android.widget.TextView;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import fr.utbm.lo52.sodia.R;
import fr.utbm.lo52.sodia.logic.Chat;
import fr.utbm.lo52.sodia.logic.Contact;
import fr.utbm.lo52.sodia.logic.DataBaseObject;
import fr.utbm.lo52.sodia.logic.Group;
import fr.utbm.lo52.sodia.logic.Message;
import fr.utbm.lo52.sodia.protocols.Authentificator;
import fr.utbm.lo52.sodia.protocols.Protocol;
import fr.utbm.lo52.sodia.protocols.ProtocolListener;
import fr.utbm.lo52.sodia.protocols.typa.Typa;
import java.util.*;

public class Main extends SherlockActivity implements ProtocolListener
{
	public static final Map<Integer, Class<? extends Activity>> intentMatch = new HashMap<Integer, Class<? extends Activity>>();
	static
	{
		intentMatch.put(android.R.id.home, Main.class);
		intentMatch.put(R.id.newContact, NewContact.class);
		intentMatch.put(R.id.newGroup, NewGroup.class);
	}

	private ExpandableListView expandableList = null;

	/** Called when the activity is first created. */
	@Override
	@SuppressWarnings("unused")
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		this.draw();		

		DataBaseObject.context = getApplicationContext();
		DataBaseObject.contentResolver = getContentResolver();
		  
		ArrayList<Contact> contacts = new ArrayList<Contact>();

		AccountManager accountManager = AccountManager.get(this);
		Typa protocol = new Typa();
		Account[] accounts = accountManager.getAccountsByType(protocol.getAccountType());
		if(accounts.length == 0)
		{
			final Intent intent = new Intent(this, Login.class);
			intent.putExtra(Authentificator.KEY_PROTOCOL_CLASS, protocol.getClass());
			intent.putExtra(Authentificator.KEY_PROTOCOL_NAME, protocol.getName());
			intent.putExtra(Authentificator.KEY_PROTOCOL_ACCOUNT_TYPE, protocol.getAccountType());
			intent.putExtra(Authentificator.KEY_PROTOCOL_HAS_PASSWORD, protocol.hasPassword());
			intent.putExtra(Authentificator.KEY_PROTOCOL_LOGO, protocol.getLogoRessource());
			startActivity(intent);
		}
		for(Account account : accounts)
		{
			ContentResolver.removePeriodicSync(account, ContactsContract.AUTHORITY, new Bundle());
			ContentResolver.requestSync(account, ContactsContract.AUTHORITY, new Bundle());
			Protocol.get(account).add(this);
			//contacts(Contact.getAll(account), account);
		}
	}
	
	@Override
	public void onResume()
	{
		super.onResume();
		Contact me = new Contact("Moi");

		QuickContactBadge quickContactBadge = (QuickContactBadge) findViewById(R.id.monContactBadge);
		ImageView image = (ImageView) findViewById(R.id.monStatusIcon);
		TextView textView = (TextView) findViewById(R.id.monName);
		TextView presence = (TextView) findViewById(R.id.maPresence);
		

		for(Protocol prot : Protocol.getProtocolsByType(Typa.class))
		{
			prot.setContext(this);
			me = prot.getMe();
			expandableList = (ExpandableListView) findViewById(R.id.GroupsList);		 
			registerForContextMenu(expandableList);

			Group[] groupes = Group.getByAccount(prot.account());

			expandableList.setAdapter(new ExpandableListAdapter(Main.this, groupes));
		}

		textView.setText(me.getName());
		image.setBackgroundResource(me.getBest().getPresence().getImage());
		presence.setText(me.getBest().getStatus());
		
		if(android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.HONEYCOMB)
		{
			quickContactBadge.setImageToDefault();
		}
		else
		{
			quickContactBadge.setImageResource(R.drawable.pic_contact_badge);
		}
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		MenuInflater menuInflater = getSupportMenuInflater();
		menuInflater.inflate(R.menu.main, menu);

		// Calling super after populating the menu is necessary here to ensure
		// that the
		// action bar helpers have a chance to handle this event.
		return super.onCreateOptionsMenu(menu);
	}



	protected void draw()
	{
		@SuppressWarnings("unused")
		ActionBar actionBar = getSupportActionBar();
		//actionBar.setDisplayHomeAsUpEnabled(true);
		setContentView(R.layout.main);
	}

	@Override
	protected void onStop()
	{
		// lock.release();
		super.onStop();
	}

	public boolean onOptionsItemSelected(MenuItem item) 
	{
		if(intentMatch.containsKey(item.getItemId()))
		{
			Intent intent = new Intent(this, intentMatch.get(item.getItemId()));
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		menu.add(0, 0, 0, "Locate");
		menu.add(0, 1, 0,  "Chat");
	}

	@SuppressWarnings("unused")
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
		switch (item.getItemId()) {
		case 0:
			return true;
		case 1:
			return true;
		default:
			return true;
		}
	}

	public void setSettings(View v){
		Intent intent = new Intent(this, Settings.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		Contact me = null;
		
		for(Protocol prot : Protocol.getProtocolsByType(Typa.class))
		{
			prot.setContext(this);
			me = prot.getMe();
		}
			
		intent.putExtra(Settings.CONTACT_KEY, me.getId());
		startActivity(intent);
	}

	public void receive(Message message, Account account) {
	    // TODO Auto-generated method stub
	    Set<Contact> participants = new HashSet<Contact>();
	    for (Contact contact : message.getTo()) {
		participants.add(contact);

	    }
	    Chat.get(participants).add(message);
	}

	public void contacts(Contact[] contacts, final Account account)
	{
		runOnUiThread(new Runnable() {

			public void run()
			{
				expandableList = (ExpandableListView) findViewById(R.id.GroupsList);		 
				registerForContextMenu(expandableList);

				Group[] groupes = Group.getByAccount(account);

				expandableList.setAdapter(new ExpandableListAdapter(Main.this, groupes));
			}
		});
	}

}
