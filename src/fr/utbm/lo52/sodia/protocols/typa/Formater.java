package fr.utbm.lo52.sodia.protocols.typa;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import fr.utbm.lo52.sodia.logic.Contact;
import fr.utbm.lo52.sodia.logic.Message;
import fr.utbm.lo52.sodia.logic.Mime;
import fr.utbm.lo52.sodia.logic.Presence;

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
	
	private ArrayList<Contact> contacts;
	
	private ArrayList<Message> messages;
	
	public Formater()
	{
		
	}
	
	public Formater(InputStream input) throws NumberFormatException, IOException
	{
		parse();
	}
	
	@SuppressWarnings("unused")
	public void parseContact() throws IOException
	{
		String id = get();
		//TODO get Contact by id
		String name = get();
		
		// contact.setName(name)
		// contacts.add(contact);
	}
	
	public void parseMessage() throws IOException
	{
		Mime mime = Mime.valueOf(Mime.class, get().toUpperCase());
		@SuppressWarnings("unused")
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
				message = new Message(mime, Presence.valueOf(Presence.class, get().toUpperCase()));
			break;
			case POSITION :
				message = new Message(mime, parseLocation());
			
		}
		if(message != null)
		{
			//TODO get Contact from id
			//message.setFrom(from);
			//message.setTo(to);
			messages.add(message);
		}
		
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
		while(endOfField.contains((char)(input.read())))
		{
			value += (char)(input.read());
		}
		return value;
	}
	
	public void parse() throws NumberFormatException, IOException
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
	}
	
	protected String toString(Contact contact)
	{
		return contact.getTypaIm() + SEPARATOR + contact.getName();
	}
	
	protected String contactsToString()
	{
		String value = "";
		for(Contact contact : contacts)
		{
			value += toString(contact) + SEPARATOR;
		}
		return value;
	}
	
	protected String toString(Message message)
	{
		String to = "" + message.getTo().length + SEPARATOR;
		for (Contact contact : message.getTo())
		{
			to += contact.getTypaIm() + SEPARATOR;
		}
		return "" + message.type() + SEPARATOR + message.getFrom().getTypaIm() + SEPARATOR + to + message.data();
	}
	
	protected String messagesToString()
	{
		String value = "";
		for(Message message : messages)
		{
			value += toString(message) + SEPARATOR;
		}
		return value;
	}
	
	
	@Override
	public String toString()
	{
		String value = "" + operation + size + SEPARATOR + type;
		if(operation == Operation.GET)
		{
			assert size == 0;
			if(type == Type.MESSAGE)
			{
				value += "" + SEPARATOR + messages.get(0).type();
			}
		}
		else if(operation == Operation.RET)
		{
			switch(type)
			{
				case CONTACT :
					value += contactsToString();
				break;
				case MESSAGE :
					value += messagesToString();
				break;
			}
		}
		return value;
	}

	public ArrayList<Contact> getContacts()
	{
		return this.contacts;
	}

	public void setContacts(ArrayList<Contact> contacts)
	{
		this.contacts = contacts;
	}

	public ArrayList<Message> getMessages()
	{
		return this.messages;
	}

	public void setMessages(ArrayList<Message> messages)
	{
		this.messages = messages;
	}
}
