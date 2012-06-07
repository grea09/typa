package fr.utbm.lo52.sodia.ui;

import fr.utbm.lo52.sodia.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class AddContactToChat extends Activity {
	@Override
	public void onCreate(Bundle icicle)
	{
		super.onCreate(icicle);
		setContentView(R.layout.addcontacttochat);
		
		ListView lvListe = (ListView)findViewById(R.id.listViewContactToAdd);
		String[] listeStrings = {"Pierre","Paul","Jacques","Geoffrey","Antoine", "Mathieu"};
		lvListe.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_multiple_choice,listeStrings));
		
	}
	
	public void addContactToChat(View view){
		
		
	}
}
