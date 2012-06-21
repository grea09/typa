package fr.utbm.lo52.sodia.ui;

import android.accounts.Account;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import fr.utbm.lo52.sodia.R;
import fr.utbm.lo52.sodia.logic.Chat;
import fr.utbm.lo52.sodia.logic.Contact;
import fr.utbm.lo52.sodia.logic.Message;
import fr.utbm.lo52.sodia.logic.Mime;
import fr.utbm.lo52.sodia.protocols.Protocol;
import fr.utbm.lo52.sodia.protocols.typa.Typa;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class AddContactToChat extends Activity{
    
	private ListView contactlistview;
	private ArrayList<Contact> listeContacts;
	private Set<Contact> participants; 
	
	
	@Override
	public void onCreate(Bundle icicle)
	{
		super.onCreate(icicle);
		setContentView(R.layout.addcontacttochat);
		
		Intent intent = getIntent();
		long[] ids = intent.getLongArrayExtra("ids");
		participants = new HashSet<Contact>();
		for (int i = 0 ; i < ids.length ; i++){
			participants.add(Contact.get(ids[i]));
		}
		
		
		contactlistview = (ListView)findViewById(R.id.listViewContactToAdd);
		
		ArrayList<String> listeContactStrings = new ArrayList<String>();
		listeContacts = new ArrayList<Contact>();
		
		for (Account account : Protocol.getAccountsByType(new Typa().getAccountType())){
		    for (Contact contact : Contact.getAll(account)){
			if (!participants.contains(contact.getId())){
			    listeContacts.add(contact);
			    listeContactStrings.add(contact.getName());
			}
		    }
		}
		
		
		contactlistview.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_multiple_choice,listeContactStrings));
		contactlistview.setItemsCanFocus(false);
		contactlistview.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
	}
	
	public void addContactToChat(View view){
	    int count = this.contactlistview.getAdapter().getCount();
   
	    for (int i = 0; i < count; i++) {
		if (this.contactlistview.isItemChecked(i)) {
		     Chat.get(participants).add(listeContacts.get(i));
		     participants.add(listeContacts.get(i));
		}
	    }
	    Chat.get(participants).add(new Message(Mime.TEXT,"Contact added to chat"));
	    
	    finish();
		
	}
}
