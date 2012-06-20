package fr.utbm.lo52.sodia.ui;

import fr.utbm.lo52.sodia.R;
import fr.utbm.lo52.sodia.logic.Presence;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class Settings extends Activity {

	
	@Override
	public void onCreate(Bundle icicle)
	{
		super.onCreate(icicle);
		setContentView(R.layout.settings);

	}
	
	public void OnCheckedChangeListener(RadioGroup group, int checkedId){
		
		Intent intent = new Intent(this, Main.class);
	       intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	       startActivity(intent);
	}
	
	public void changeSettings(View view){
		
		Context context = getApplicationContext();
		Toast toast = Toast.makeText(context, "Settings modified !", Toast.LENGTH_LONG);
		toast.show();
		
		ImageView image = (ImageView) findViewById(R.id.monStatusIcon);
		TextView name = (TextView) findViewById(R.id.monName);
		TextView presence = (TextView) findViewById(R.id.maPresence);
		
		RadioGroup newStatus = (RadioGroup) findViewById(R.id.statusGroup);
		EditText newName = (EditText) findViewById(R.id.editName);
		EditText newPresence = (EditText) findViewById(R.id.editPresence);
		
		name = newName;
		presence = newPresence;
		
		image.setImageResource(Presence.values()[newStatus.getCheckedRadioButtonId()].getImage());
		
			
	
		
		Intent intent = new Intent(this, Main.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
		
		
	}
	
}
