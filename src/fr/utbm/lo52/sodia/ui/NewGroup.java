package fr.utbm.lo52.sodia.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import fr.utbm.lo52.sodia.R;

public class NewGroup extends Activity {
	
	public static final String INTENT_EXTRA_NAME = "new_group.name";
	public static final String INTENT_EXTRA_ID = "new_group.id";
	// un tableau d'id de contact 
	
	@Override
	public void onCreate(Bundle icicle)
	{
		super.onCreate(icicle);
		setContentView(R.layout.newgroup);
		
	}
	
	public void addGroup(View view){
		// Récupération du nom du groupe à ajouter
		//TODO please English or UTF8 encoding
		EditText editText = (EditText) findViewById(R.id.groupName);
		String groupname = editText.getText().toString();
		
		// Rï¿½cupï¿½ration des contacts ï¿½ ajouter au groupe
		
		Context context = getApplicationContext();
		Toast toast = Toast.makeText(context, groupname+" added !", Toast.LENGTH_LONG);
		toast.show();
		
		Intent intent = new Intent(this, Main.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
	}

}
