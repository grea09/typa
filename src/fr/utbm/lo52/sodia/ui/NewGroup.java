package fr.utbm.lo52.sodia.ui;

import fr.utbm.lo52.sodia.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

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
		 // app icon in action bar clicked; go home
       Intent intent = new Intent(this, Main.class);
       intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
       startActivity(intent);
	}

}
