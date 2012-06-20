package fr.utbm.lo52.sodia.ui;

import android.accounts.Account;
import fr.utbm.lo52.sodia.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.OverlayItem;
import fr.utbm.lo52.sodia.logic.Contact;
import fr.utbm.lo52.sodia.protocols.Protocol;
import fr.utbm.lo52.sodia.protocols.typa.Typa;
import java.util.ArrayList;

public class AddContactToChat extends Activity {
	@Override
	public void onCreate(Bundle icicle)
	{
		super.onCreate(icicle);
		setContentView(R.layout.addcontacttochat);
		
		ListView lvListe = (ListView)findViewById(R.id.listViewContactToAdd);
		
		ArrayList<String> listeStrings = new ArrayList<String>();
		listeStrings.add("lol");
		for (Account account : Protocol.getAccountsByType(new Typa().getAccountType())){
		    for (Contact contact : Contact.getAll(account)){
			listeStrings.add(contact.getName());
		    }
		}
		
		
		lvListe.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_multiple_choice,listeStrings));
		
	}
	
	public void addContactToChat(View view){
		
		
	}
}
