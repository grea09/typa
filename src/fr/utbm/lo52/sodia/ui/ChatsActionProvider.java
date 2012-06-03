package fr.utbm.lo52.sodia.ui;

import java.util.Iterator;
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

public class ChatsActionProvider extends ActionProvider implements OnMenuItemClickListener {

	static final int LIST_LENGTH = 5;

	Context mContext;

	public ChatsActionProvider(Context context) {
		super(context);
		mContext = context;
	}

	@Override
	public View onCreateActionView() {
		Log.d(this.getClass().getSimpleName(), "onCreateActionView");

		TextView textView = new TextView(mContext);
		textView.setText("Pick");

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
			Iterator<Chat> itchats = Chat.getChats().iterator();
			int i = 0;
			while (itchats.hasNext()) {
				Chat chat = itchats.next();
				Iterator<Contact> itcontacts = chat.getParticipants().iterator();
				String chatname = "Chat : ";
				while(itcontacts.hasNext()){
					Contact contact = itcontacts.next();
					chatname += contact.getName()+", ";
				}
				subMenu.add(0, i, i, chatname).setOnMenuItemClickListener(this).setIcon(null);
				i++;
			}
		}
	}

	public boolean onMenuItemClick(MenuItem item) {
		
		Intent intent = new Intent(mContext, ChatActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    	intent.putExtra("id", item.getTitle());
		mContext.startActivity(intent);
		
		return true;
	}
}
