package fr.utbm.lo52.sodia.ui;

import android.app.*;
import android.content.Intent;
import android.graphics.*;
import android.os.*;
import android.view.*;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import fr.utbm.lo52.sodia.*;
import fr.utbm.lo52.sodia.protocols.*;

public class Main extends Activity
{

	android.net.wifi.WifiManager.MulticastLock lock;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		this.draw();
		
		ProtocolManager.newContact(this.getApplicationContext(), BitmapFactory.decodeResource(this.getApplicationContext().getResources(),
																						   R.drawable.ic_launcher), "Jean Jaques GRINGUEDIGUÈGLEGUEUX", "long@gmiel.com", null);
		// wifiMultiCastLock();
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
		MenuInflater menuInflater = getMenuInflater();
		menuInflater.inflate(R.menu.main, menu);

		// Calling super after populating the menu is necessary here to ensure
		// that the
		// action bar helpers have a chance to handle this event.
		return super.onCreateOptionsMenu(menu);
	}

	protected void draw()
	{
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		setContentView(R.layout.main);
	}

	@Override
	protected void onStop()
	{
		// lock.release();
		super.onStop();
	}
	
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	        case android.R.id.home:
	            Intent intent = new Intent(this, Main.class);
	            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	            startActivity(intent);
	            return true;
	        case R.id.newContact:
	        	Intent intent2 = new Intent(this, NewContact.class);
		        intent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		        startActivity(intent2);
	        	return true;
	        case R.id.newGroup:
	        	Intent intent3 = new Intent(this, NewGroup.class);
		        intent3.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		        startActivity(intent3);
	        	return true;
	        case R.id.settings:
	        	Intent intent4 = new Intent(this, Settings.class);
		        intent4.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		        startActivity(intent4);
	        	return true;
	        case R.id.chats:
	        	Intent intent5 = new Intent(this, Chat.class);
	        	intent5.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		        startActivity(intent5);
	        	return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	
}
