package fr.utbm.lo52.sodia.ui;

import fr.utbm.lo52.sodia.R;
import fr.utbm.lo52.sodia.logic.Presence;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.*;
import com.actionbarsherlock.app.SherlockActivity;
import fr.utbm.lo52.sodia.logic.Contact;
import fr.utbm.lo52.sodia.logic.Im;
import fr.utbm.lo52.sodia.logic.RawContact;
import fr.utbm.lo52.sodia.protocols.typa.Typa;

public class Settings extends SherlockActivity {

	public static final String CONTACT_KEY = "contact";
	private Contact contact;
	
	@Override
	public void onCreate(Bundle icicle)
	{
		super.onCreate(icicle);
		setContentView(R.layout.settings);
		
		EditText newName = (EditText) findViewById(R.id.editName);
		
		contact = Contact.get(getIntent().getLongExtra(CONTACT_KEY, MODE_PRIVATE));
		newName.setText(contact.getName());
		
				EditText newPresence = (EditText) findViewById(R.id.editPresence);
		newPresence.setText("Available");

	}
	
	public void OnCheckedChangeListener(RadioGroup group, int checkedId){
		
		Intent intent = new Intent(this, Main.class);
	       intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	       startActivity(intent);
	}
	
	public void changeSettings(View view){
		
		Context context = getApplicationContext();
		RadioGroup newStatus = (RadioGroup) findViewById(R.id.statusGroup);
		EditText newName = (EditText) findViewById(R.id.editName);
		EditText newPresence = (EditText) findViewById(R.id.editPresence);
		RadioButton radioButton = (RadioButton) findViewById(newStatus.getCheckedRadioButtonId());
		
		contact.setName("" +newName.getText());
		Presence presence = Presence.AVAILABLE;
		
		for(Presence pres : Presence.values())
		{
			if(getResources().getText(pres.getNameResource()).equals(radioButton.getText()))
			{
				presence = pres;
				break;
			}
		}
		
		for(RawContact rawContact : contact.getRawContacts())
		{
			for(Im im : rawContact.getImByProtocol((new Typa()).getName()))
			{
				im.getStatus().setPresence(presence);
				im.getStatus().setStatus("" + newPresence.getText());
			}
		}
		try
		{
			contact.save();
		}
		catch (RemoteException e)
		{
			Log.e(getClass().getSimpleName(), "",  e);
		}
		catch (OperationApplicationException e)
		{
			Log.e(getClass().getSimpleName(), "",  e);
		}
		
		finish();
		
		/*Intent intent = new Intent(this, Main.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);*/
		
		
	}
	
}
