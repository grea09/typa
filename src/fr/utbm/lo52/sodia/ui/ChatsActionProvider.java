package fr.utbm.lo52.sodia.ui;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.view.ActionProvider;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.MenuItem.OnMenuItemClickListener;
import com.actionbarsherlock.view.SubMenu;

import fr.utbm.lo52.sodia.R;
import fr.utbm.lo52.sodia.logic.Chat;
import fr.utbm.lo52.sodia.logic.Contact;
import fr.utbm.lo52.sodia.logic.Group;

public class ChatsActionProvider extends ActionProvider implements OnMenuItemClickListener {

	Context mContext;
	ArrayList<Chat> chats;
	
	public ChatsActionProvider(Context context) {
		super(context);
		mContext = context;
	}

	@Override
	public View onCreateActionView() {
		Log.d(this.getClass().getSimpleName(), "onCreateActionView");
		return null;
	}

	@Override
	public boolean onPerformDefaultAction() {
		Log.d(this.getClass().getSimpleName(), "onPerformDefaultAction");

		return super.onPerformDefaultAction();
	}

	@Override
	public boolean hasSubMenu() {
		Log.d(this.getClass().getSimpleName(), "hasSubMenu");

		return true;
	}

	@Override
	public void onPrepareSubMenu(SubMenu subMenu) {
		Log.d(this.getClass().getSimpleName(), "onPrepareSubMenu");

		subMenu.clear();
	
		if (Chat.getChats().size() == 0){
			Toast toast = Toast.makeText(mContext, "No chat opened !", Toast.LENGTH_LONG);
			toast.show();
			
		}else{
			this.chats = new ArrayList<Chat>();
			Iterator<Chat> itchats = Chat.getChats().values().iterator();
			Log.d("Liste chats :", "  :");
			int i = 0;
			while (itchats.hasNext()) {
				Chat chat = itchats.next();
				this.chats.add(chat);
				Log.d("Chat :", ""+i);
				Iterator<Contact> itcontacts = chat.getParticipants().iterator();
				String chatname = "";
				while(itcontacts.hasNext()){
					Contact contact = itcontacts.next();
					if (chatname != "") chatname += ", ";
					chatname += contact.getName();
					Log.d("Participant : ", ""+contact.getName());

				}
				subMenu.add(0, i, i, chatname).setOnMenuItemClickListener(this).setIcon(R.drawable.ic_menu_light_social_contact);
				i++;
			}
		}
	}

	public boolean onMenuItemClick(MenuItem item) {
		
		Intent intent = new Intent(mContext, ChatActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		
		// Récupération du chat cliqué
		Chat chat = this.chats.get(item.getItemId());
		// Récupération des participants du chat
		Set<Contact> contacts = chat.getParticipants(); 
		long[] ids = new long[contacts.size()];
		Iterator<Contact> itcontacts = contacts.iterator();
		int i = 0;
		while(itcontacts.hasNext()){
			ids[i] = itcontacts.next().getId();
			i++;
		}
		
    	intent.putExtra("ids", ids);
		mContext.startActivity(intent);
		
		return true;
	}
}
