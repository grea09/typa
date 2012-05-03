package fr.utbm.lo52.sodia.ui;

import android.app.*;
import android.graphics.*;
import android.os.*;
import android.view.*;
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

}
