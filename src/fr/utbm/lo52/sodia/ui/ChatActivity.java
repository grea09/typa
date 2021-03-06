package fr.utbm.lo52.sodia.ui;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Layout;
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
import fr.utbm.lo52.sodia.protocols.Protocol;
import fr.utbm.lo52.sodia.protocols.ProtocolListener;
import fr.utbm.lo52.sodia.protocols.typa.Typa;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ChatActivity extends SherlockActivity implements ProtocolListener {

    private Chat chat;
    private String selectedImagePath;
    private String selectedMusicPath;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.chat);

	// R�cup�ration des participants
	Intent intent = getIntent();
	long[] ids = intent.getLongArrayExtra("ids");
	Set<Contact> contacts = new HashSet<Contact>();
	for (int i = 0; i < ids.length; i++) {
	    contacts.add(Contact.get(ids[i]));
	    Log.d("Chatting with [id]", "" + ids[i]);
	}


	if (contacts.size() > 1) {
	    setTitle("Group");
	} else {
	    if (ids[0] != -1) {
		setTitle(Contact.get(ids[0]).getName());
	    } else {
		setTitle("Contact");
	    }
	}

	// R�cup�ration du chat ou cr�ation si non existant
	this.chat = Chat.get(contacts);

	// R�cup�ration des messages pr�c�dents
	List<Message> messages = this.chat.getMessages();
	for (int i = 0; i < messages.size(); i++) {
	    Message m = messages.get(i);
	    switch (m.type()) {

		case TEXT:
		    appendMessage((CharSequence) m.getFrom().getName() + " > " + (CharSequence) messages.get(i).data() + "\n");

		    break;
		case PICTURE:
		    appendMessage((CharSequence) m.getFrom().getName() + " sent you a picture !\n");

		    break;
		case PRESENCE:
		    appendMessage((CharSequence) m.getFrom().getName() + " just turned " + m.data() + "\n");

		    break;
		case SOUND:
		    appendMessage((CharSequence) m.getFrom().getName() + " sent you a music !\n");

		    break;
		case RAW:
		    appendMessage((CharSequence) m.getFrom().getName() + " sent you a file !\n");

		    break;
	    }


	}
//		

	((TextView) findViewById(R.id.chatTextView)).setMovementMethod(new ScrollingMovementMethod());
	ActionBar actionBar = getSupportActionBar();
	actionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
	MenuInflater menuInflater = getSupportMenuInflater();
	menuInflater.inflate(R.menu.chat, menu);

	// Calling super after populating the menu is necessary here to ensure
	// that the
	// action bar helpers have a chance to handle this event.
	return super.onCreateOptionsMenu(menu);
    }

    public void sendMessage(View view) {
	EditText editText = (EditText) findViewById(R.id.chatEditText);
	String smessage = editText.getText().toString();

	Message message = new Message(Mime.TEXT, smessage);
	
	AccountManager accountManager = AccountManager.get(this);
	Typa protocol = new Typa();
	Account[] accounts = accountManager.getAccountsByType(protocol.getAccountType());
	Protocol p = Protocol.get(accounts[accounts.length-1]);
	try {
	    message.setTo(this.chat.getParticipants().toArray(new Contact[0]));
	    p.send(message);
	    this.chat.add(message);

	    //CLOSE KEYBOARD
	    editText.setText("");
	    if (this.getCurrentFocus() != null) {
		InputMethodManager inputManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
		inputManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
	    }

	    appendMessage("Me > " + smessage + "\n");
	    
	} catch (Throwable ex) {
	    Logger.getLogger(ChatActivity.class.getName()).log(Level.SEVERE, null, ex);
	}
	

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
	switch (item.getItemId()) {
	    case (android.R.id.home):
		Intent intent = new Intent(this, Main.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
		break;
	    case (R.id.addcontacttochat):
		Intent intent2 = new Intent(this, AddContactToChat.class);
		intent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

		Set<Contact> contacts = this.chat.getParticipants();
		long[] ids = new long[contacts.size()];
		Iterator<Contact> itcontacts = contacts.iterator();
		int i = 0;
		while (itcontacts.hasNext()) {
		    ids[i] = itcontacts.next().getId();
		    i++;
		}

		intent2.putExtra("ids", ids);
		startActivity(intent2);

		break;
	    case (R.id.locate):
		Intent intent3 = new Intent(this, LocateActivity.class);
		intent3.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent3);

		break;

	    case (R.id.sendImage):
		Intent intent4 = new Intent();
		intent4.setType("image/*");
		intent4.setAction(Intent.ACTION_GET_CONTENT);
		startActivityForResult(Intent.createChooser(intent4, "Select Picture"), 1);
		break;
	    case (R.id.sendMusic):
		Intent intent5 = new Intent();
		intent5.setType("audio/*");
		intent5.setAction(Intent.ACTION_GET_CONTENT);
		startActivityForResult(Intent.createChooser(intent5, "Select Music"), 2);


		break;
	    default:
		break;

	}
	return super.onOptionsItemSelected(item);

    }

    public void receive(Message message, Account account) {
	// TODO Auto-generated method stub
	Set<Contact> participants = new HashSet<Contact>();
	for (Contact contact : message.getTo()) {
	    participants.add(contact);

	}
	Chat.get(participants).add(message);
    }

    public void contacts(Contact[] contacts, Account account) {
	// TODO Auto-generated method stub
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
	if (resultCode == RESULT_OK) {
	    switch (requestCode) {
		case 1:
		    Uri selectedImageUri = intent.getData();

		    //OI FILE Manager
		    String filemanagerstring = selectedImageUri.getPath();

		    //MEDIA GALLERY
		    selectedImagePath = null;

		    String[] projection = {MediaStore.Images.Media.DATA};
		    Cursor cursor = managedQuery(selectedImageUri, projection, null, null, null);
		    if (cursor != null) {
			//HERE YOU WILL GET A NULLPOINTER IF CURSOR IS NULL
			//THIS CAN BE, IF YOU USED OI FILE MANAGER FOR PICKING THE MEDIA
			int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			cursor.moveToFirst();
			selectedImagePath = cursor.getString(column_index);
		    }


		    //DEBUG PURPOSE - you can delete this if you want
		    if (selectedImagePath != null) {
			System.out.println(selectedImagePath);
		    } else {
			System.out.println("selectedImagePath is null");
		    }
		    if (filemanagerstring != null) {
			System.out.println(filemanagerstring);
		    } else {
			System.out.println("filemanagerstring is null");
		    }

		    //NOW WE HAVE OUR WANTED STRING
		    if (selectedImagePath != null) {
			System.out.println("selectedImagePath is the right one for you!");
		    } else {
			System.out.println("filemanagerstring is the right one for you!");
		    }

		    appendMessage("Your picture has been sent !");
		    break;


		case 2:
		    break;
		default:
		    break;
	    }
	} else {
	    appendMessage("Nothing has been sent !");

	}
    }

    public void appendMessage(String m) {
	TextView t = (TextView) findViewById(R.id.chatTextView);

	t.append(m);
	Linkify.addLinks(t, Linkify.ALL);

	//SCROLL DOWN THE TEXTVIEW
	final Layout layout = t.getLayout();
	if (layout != null) {
	    int scrollDelta = layout.getLineBottom(t.getLineCount() - 1)
		    - t.getScrollY() - t.getHeight();
	    if (scrollDelta > 0) {
		t.scrollBy(0, scrollDelta);
	    }
	}

    }
}
