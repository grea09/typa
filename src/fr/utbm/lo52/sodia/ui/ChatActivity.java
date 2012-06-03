package fr.utbm.lo52.sodia.ui;

import java.util.Calendar;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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

public class ChatActivity extends SherlockActivity
{

	private Chat chat;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		// Récupération du contact
		Intent intent = getIntent();
		String id = intent.getStringExtra("id");
		Log.d("Chatting with : [id]=", id);

		// Création/Récupération du chat
		this.chat = new Chat(new Contact(id));
		
		// Récupération des messages précédents
		TextView t = (TextView)  findViewById(R.id.chatTextView);
		List<Message> messages = this.chat.getMessages();
		for (int i = 0 ; i < messages.size() ; i++){
			Log.d("message", ""+i);
			t.append((CharSequence) messages.get(i).data());	
		}
		setContentView(R.layout.chat);
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
		t.append("\nMe @"+Calendar.getInstance().get(Calendar.HOUR) + ":" +
				Calendar.getInstance().get(Calendar.MINUTE)+" > "+smessage);
	}
	
	public boolean onOptionsItemSelected(MenuItem item) 
	{
		switch(item.getItemId()){
			case (android.R.id.home):
				Intent intent = new Intent(this, Main.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
				break;
			default:
				break;

		}
		return super.onOptionsItemSelected(item);

	}
}
