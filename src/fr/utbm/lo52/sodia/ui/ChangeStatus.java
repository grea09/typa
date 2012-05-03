package fr.utbm.lo52.sodia.ui;

import fr.utbm.lo52.sodia.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RadioGroup;

public class ChangeStatus extends Activity {

	
	@Override
	public void onCreate(Bundle icicle)
	{
		super.onCreate(icicle);
		setContentView(R.layout.changestatus);

	}
	
	public void OnCheckedChangeListener(RadioGroup group, int checkedId){
		
		Intent intent = new Intent(this, Main.class);
	       intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	       startActivity(intent);
	}
	
}
