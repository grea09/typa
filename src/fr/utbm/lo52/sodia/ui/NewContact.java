package fr.utbm.lo52.sodia.ui;

import fr.utbm.lo52.sodia.R;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;


public class NewContact extends Activity
{
	public static final String INTENT_EXTRA_NAME = "new_contact.name";
	public static final String INTENT_EXTRA_ID = "new_contact.id";
	public static final String INTENT_EXTRA_PHOTO = "new_contact.photo";
	
	@Override
	public void onCreate(Bundle icicle)
	{
		super.onCreate(icicle);
		setContentView(R.layout.newcontact);

	}
	
	
	public void addContact(View view){
		Context context = getApplicationContext();
		Toast toast = Toast.makeText(context, " Contact request sent !", Toast.LENGTH_LONG);
		toast.show();
		
		Intent intent = new Intent(this, Main.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
	}
}
