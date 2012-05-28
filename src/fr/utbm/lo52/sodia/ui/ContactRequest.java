package fr.utbm.lo52.sodia.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import fr.utbm.lo52.sodia.R;

public class ContactRequest extends Activity{
	@Override
	public void onCreate(Bundle icicle)
	{
		super.onCreate(icicle);
		setContentView(R.layout.contactrequest);
		
		Intent i = getIntent();
		String id = i.getStringExtra("id");
		TextView t = (TextView)  findViewById(R.id.contactRequestTextView);
		t.setText(id+" wants to add you in his contact list. What you want to do ?");


	}
	
	
	public void action(View view){
		String toastMessage = "";
		switch(view.getId()){
			case R.id.buttonAccept:
				toastMessage = "Contact added !";
				break;
			case R.id.buttonLater:
				toastMessage = "I'll wait for it !";
				break;
			case R.id.buttonDecline:
				toastMessage = "Request rejected !";
				break;
		
		}
		
		Context context = getApplicationContext();
		Toast toast = Toast.makeText(context, toastMessage, Toast.LENGTH_LONG);
		toast.show();
		
		Intent intent = new Intent(this, Main.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
	}
}
