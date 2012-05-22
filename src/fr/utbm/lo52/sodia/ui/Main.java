package fr.utbm.lo52.sodia.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;

import fr.utbm.lo52.sodia.R;
import fr.utbm.lo52.sodia.logic.Contact;
import fr.utbm.lo52.sodia.logic.Group;

public class Main extends SherlockActivity
{
	public static final Map<Integer, Class<? extends Activity>> intentMatch = new HashMap<Integer, Class<? extends Activity>>();
	static
	{
		intentMatch.put(android.R.id.home, Main.class);
		intentMatch.put(R.id.newContact, NewContact.class);
		intentMatch.put(R.id.newGroup, NewGroup.class);
		intentMatch.put(R.id.settings, Settings.class);
	}

	private ExpandableListView expandableList = null;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		this.draw();
		
		setContentView(R.layout.main);
		
		expandableList = (ExpandableListView) findViewById(R.id.GroupsList);		 

		ArrayList<Group> groupes = new ArrayList<Group>();
		
		for (int i = 1; i < 3; i++) {
			Group groupe = new Group("Friends " + i);
			Set<Contact> contacts = new HashSet<Contact>();
			for (int x = 1; x < 5; x++) {
				//groupe.add(new Contact("Pierre Paul Jack" + x));
				Contact contact = new Contact("Pierre");
				contacts.add(contact);
				
			}
			groupe.add(contacts);
			groupes.add(groupe);
		}

		
		
		expandableList.setAdapter(new ExpandableListAdapter(this, groupes));
		
		ContactNotification.newContactNotification(this.getApplicationContext(), BitmapFactory.decodeResource(this.getApplicationContext().getResources(), R.drawable.ic_launcher), "Jean Jaques GRINGUEX", "long@gmiel.com", null);
		ContactNotification.newContactNotification(this.getApplicationContext(), BitmapFactory.decodeResource(this.getApplicationContext().getResources(), R.drawable.ic_launcher), "Jean Jaques GRINGUEX", "long@gmiel.com", null);

		//wifiMultiCastLock();
	}

//	private void wifiMultiCastLock()
//	{
//		android.net.wifi.WifiManager wifi = (android.net.wifi.WifiManager) getSystemService(android.content.Context.WIFI_SERVICE);
//		lock = wifi.createMulticastLock("mylockthereturn");
//		lock.setReferenceCounted(true);
//		lock.acquire();
//	}

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
	
	public void openChat(View view){
		Context context = getApplicationContext();    	
    	Intent intent = new Intent(context, Chat.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(intent);
	}
}
