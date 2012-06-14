package fr.utbm.lo52.sodia.ui;

import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

import android.accounts.Account;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.text.util.Linkify;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;

import fr.utbm.lo52.sodia.R;
import fr.utbm.lo52.sodia.logic.Chat;
import fr.utbm.lo52.sodia.logic.Contact;
import fr.utbm.lo52.sodia.logic.Message;
import fr.utbm.lo52.sodia.logic.Mime;
import fr.utbm.lo52.sodia.protocols.ProtocolListener;

public class ChatActivity extends SherlockActivity implements ProtocolListener
{

	private Chat chat;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.chat);
		
		// R�cup�ration des participants
		Intent intent = getIntent();
		long[] ids = intent.getLongArrayExtra("ids");
		Set<Contact> contacts = new HashSet<Contact>();
		for (int i = 0 ; i < ids.length ; i++){
			//contacts.add(Contact.get(ids[i]));
			Log.d("Chatting with [id]", ""+ids[i]);
		}
		
		// R�cup�ration du chat ou cr�ation si non existant
		this.chat = Chat.get(new Contact("Name"));

		if (contacts.size() > 1){
			setTitle("Group");
		}else{
			if (ids[0] != -1){
				setTitle(Contact.get(ids[0]).getName());
			}else{
				setTitle("Contact");
			}
		}
		
//		if (false){
//			// Cr�ation/R�cup�ration du chat
//			this.chat = Chat.get(contacts);
//			
//			// R�cup�ration des messages pr�c�dents
//			TextView t = (TextView)  findViewById(R.id.chatTextView);
//			List<Message> messages = this.chat.getMessages();
//			for (int i = 0 ; i < messages.size() ; i++){
//				Log.d("message", ""+i);
//				t.append((CharSequence) messages.get(i).data());	
//			}
//		}
		
		((TextView) findViewById(R.id.chatTextView)).setMovementMethod(new ScrollingMovementMethod());
		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		MenuInflater menuInflater = getSupportMenuInflater();
		menuInflater.inflate(R.menu.chat, menu);

		// Calling super after populating the menu is necessary here to ensure
		// that the
		// action bar helpers have a chance to handle this event.
		return super.onCreateOptionsMenu(menu);
	}
	
	public void sendMessage(View view){
		TextView t = (TextView)  findViewById(R.id.chatTextView);
		EditText editText = (EditText) findViewById(R.id.chatEditText);
		String smessage = editText.getText().toString();
		
		Message message = new Message(Mime.TEXT, smessage);
		this.chat.add(message);
		
		editText.setText("");
		if (this.getCurrentFocus() != null){
			InputMethodManager inputManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE); 
			inputManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
		}
		t.append("\nMe @"+Calendar.getInstance().get(Calendar.HOUR_OF_DAY) + ":" +
				Calendar.getInstance().get(Calendar.MINUTE) + ":" +
						Calendar.getInstance().get(Calendar.SECOND)+" > "+Html.fromHtml("")+":"+smessage);
		
		Linkify.addLinks(t, Linkify.ALL);
	}
	
	public boolean onOptionsItemSelected(MenuItem item) 
	{
		switch(item.getItemId()){
			case (android.R.id.home):
				Intent intent = new Intent(this, Main.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
				break;
			case (R.id.addcontacttochat):
				Intent intent2 = new Intent(this, AddContactToChat.class);
				intent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent2);
				break;
			case (R.id.locate):
				Intent intent3 = new Intent(this, LocateActivity.class);
				intent3.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent3);
			default:
				break;

		}
		return super.onOptionsItemSelected(item);

	}

	@Override
	public void receive(Message message, Account account)
	{
		// TODO Auto-generated method stub
		this.chat.add(message);
	}

	@Override
	public void contacts(Contact[] contacts, Account account)
	{
		// TODO Auto-generated method stub
		
	}

}
