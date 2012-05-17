package fr.utbm.lo52.sodia.logic;

import java.util.HashSet;
import java.util.Set;

public class Chat
{
	private static Set<Chat> chats;
	private Set<Contact> participants;
	
	public Chat(Contact participant)
	{
		this(new HashSet<Contact>());
		this.add(participant);
	}
	
	public Chat(Set<Contact> participants)
	{
		this.participants = participants;
		chats.add(this);
	}
	
	public void add(Contact contact)
	{
		participants.add(contact);
	}
	
	public void remove(Contact contact)
	{
		participants.remove(contact);
	}
	
	public static Set<Chat> getChats()
	{
		return chats;
	}
}
