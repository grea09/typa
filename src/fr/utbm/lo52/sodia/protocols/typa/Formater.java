package fr.utbm.lo52.sodia.protocols.typa;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.util.Log;
import fr.utbm.lo52.sodia.logic.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Formater
{
	public enum Operation
	{
		GET,
		RET;
		@Override
		public String toString()
		{
			return this.name() + SEPARATOR;
		}
	}
	
	public enum Type
	{
		CONTACT,
		MESSAGE;
		
		@Override
		public String toString()
		{
			return this.name() + SEPARATOR;
		}
	}
	
	public int size;
	
	public Operation operation;
	
	public Type type;
	
	public static final char SEPARATOR = ';';
	
	public InputStream input;
	
	public OutputStream output;
	
	private ArrayList<Contact> contacts;
	
	private ArrayList<Message> messages;
	
	public Formater()
	{
		
	}
	
	public Formater(InputStream input) throws NumberFormatException, IOException
	{
		this.input = input;
		parse();
	}
	
	public Formater(OutputStream output)
	{
		this.output = output;
	}
	
	public void parseContact() throws IOException
	{
		String id = get();
		Contact contact = Contact.getByIm(id);
		String name = get();
		contact.setName(name);
		contacts.add(contact);
	}
	
	public void parseMessage() throws IOException
	{
		Mime mime = Mime.valueOf(Mime.class, get().toUpperCase());
		String from = get();
		int size = Integer.decode(get());
		String[] to = new String[size];
		for (int i = 0; i < size; i++)
		{
			to[i] = get();
		}
		Message message = null;
		switch(mime)
		{
			case TEXT :
				message = new Message(mime, get());
			case PRESENCE :
				message = new Message(mime, parseStatus());
			break;
			case POSITION :
				message = new Message(mime, parseLocation());
			
		}
		if(message != null)
		{
			
			message.setFrom(Contact.getByIm(from));
			ArrayList<Contact> contacts = new ArrayList<Contact>(to.length);
			for(String im:to)
			{
				contacts.add(Contact.getByIm(im));
			}
			message.setTo((Contact[])contacts.toArray());
			messages.add(message);
		}
		
	}
	
	private Status parseStatus() throws IOException
	{
		String message = get();
		Presence presence = Presence.valueOf(get());
		return new Status(presence, message, System.currentTimeMillis(), null, null);
	}

	public int[] parseLocation() throws IOException
	{
		String value = get();
		String[] values = value.split(",");
		
		return new int[]{Integer.decode(values[0]),Integer.decode(values[1])};
	}
	
	protected String get() throws IOException
	{
		String value = "";
		Set<Character> endOfField = new HashSet<Character>();
		endOfField.add(SEPARATOR);
		endOfField.add((char) -1);
		char read;
		while(!(endOfField.contains(read = (char)(input.read()))))
		{
			value += read;
		}
		Log.d(getClass().getName(), "get = " + value);
		return value;
	}
	
	public void parse() throws NumberFormatException, IOException, IllegalArgumentException
	{
		operation = Operation.valueOf(get().toUpperCase());
		size = Integer.decode(get());
		type = Type.valueOf(get());
		if(operation == Operation.RET)
		{
			for (int i = 0; i < size; i++)
			{
				switch(type)
				{
					case CONTACT :
						parseContact();
					break;
					case MESSAGE :
						parseMessage();
					break;
				}
			}
		}
		//input.close();
	}
	
	protected void send(Contact contact) throws IOException
	{
		for(RawContact rawContact : contact.getRawContacts())
		{
			
			for(Im im: rawContact.getImByProtocol((new Typa()).getName()))
			{
				set("" + im.getUserId() + SEPARATOR + rawContact.getName().getName() + SEPARATOR);
			}
		}
	}
	
	protected void sendContacts() throws IOException
	{
		for(Contact contact : contacts)
		{
			send(contact);
		}
	}
	
	protected void send(Message message) throws IOException
	{
		set("" + message.type() + SEPARATOR + 
				// TODO test if multi sender is needed
				message.getFrom().getImByProtocol((new Typa()).getName())[0] + SEPARATOR + 
				sizeOf(message.getTo()) + SEPARATOR);
		for (Contact contact : message.getTo())
		{
			for(Im im : contact.getImByProtocol((new Typa()).getName()))
			{
				set(im.getUserId() + SEPARATOR);
			}
		}
		switch (message.type())
		{
		case PRESENCE:
			Status status = (Status) message.data();
			set(status.getStatus() + SEPARATOR + status.getPresence());
			break;
		case POSITION:
			int[] position = (int[])(message.data());
			set("" + position[0] + SEPARATOR + position[1]);
			break;
		case PICTURE:
			((Bitmap)(message.data())).compress(CompressFormat.PNG, 0, output);
			break;
		case TEXT:
			set(message.data().toString());
			break;
		default:
			output.write((byte[]) message.data());
			break;
		}
	}
	
	protected void sendMessages() throws IOException
	{
		for(Message message : messages)
		{
			send(message);
		}
	}
	
	protected int sizeOf(Contact[] contacts)
	{
		int size = 0;
		for(Contact contact: contacts)
		{
			size += contact.getImByProtocol((new Typa()).getName()).length;
		}
		return size;
	}
	
	protected void set(String field) throws IOException
	{
		output.write(field.getBytes());
	}
	
	public void send() throws Throwable
	{
		set("" + operation + size + SEPARATOR + type);
		if(operation == Operation.GET)
		{
			assert size == 0;
			if(type == Type.MESSAGE)
			{
				set("" + SEPARATOR + messages.get(0).type());
			}
		}
		else if(operation == Operation.RET)
		{
			switch(type)
			{
				case CONTACT :
					sendContacts();
				break;
				case MESSAGE :
					sendMessages();
				break;
			}
		}
		output.flush();
		//output.close();
	}

	public ArrayList<Contact> getContacts()
	{
		return this.contacts;
	}

	public void setContacts(ArrayList<Contact> contacts)
	{
		this.contacts = contacts;
		size = sizeOf(contacts.toArray(new Contact[contacts.size()]));
	}

	public ArrayList<Message> getMessages()
	{
		return this.messages;
	}

	public void setMessages(ArrayList<Message> messages)
	{
		this.messages = messages;
		size = messages.size();
	}
}
