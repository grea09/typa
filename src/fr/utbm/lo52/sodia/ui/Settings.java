package fr.utbm.lo52.sodia.ui;

import fr.utbm.lo52.sodia.R;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioGroup;
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
		
		Intent intent = new Intent(this, Main.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
		
		
	}
	
}
