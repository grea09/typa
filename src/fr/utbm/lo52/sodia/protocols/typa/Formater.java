package fr.utbm.lo52.sodia.protocols.typa;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

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
	}
	
	public int size;
	
	public Operation operation;
	
	public String type;
	
	public static final String SEPARATOR = ";";
	
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
		String from = get();
		int size = Integer.decode(get());
		String[] to = new String[size];
		for (int i = 0; i < size; i++)
		{
			to[i] = get();
		}
		Mime mime = Mime.valueOf(Mime.class, get().toUpperCase());
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
		while((char)(input.read()) != SEPARATOR.charAt(0))
		{
			value += (char)(input.read());
		}
		return value;
	}
	
	public void parse() throws NumberFormatException, IOException
	{
		operation = Operation.valueOf(Operation.class, get().toUpperCase());
		size = Integer.decode(get());
		type = get();
		if(operation == Operation.RET)
		{
			for (int i = 0; i < size; i++)
			{
				if(type.toUpperCase() == "CONTACT")
				{
					parseContact();
				}
				else if (type.toUpperCase() == "MESSAGE")
				{
					parseMessage();
				}
			}
		}
	}
	
	public String toString()
	{
		
		return "";
	}
}
