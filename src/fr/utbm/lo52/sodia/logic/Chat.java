package fr.utbm.lo52.sodia.logic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Chat
{
	private static Map<Set<Contact>, Chat> chats;
	static
	{
		chats = new HashMap<Set<Contact>, Chat>();
	}

	private Set<Contact> participants;
	private List<Message> messages;

	protected Chat(Set<Contact> participants)
	{
		chats.put(participants, this);
		this.participants = participants;
		this.messages = new ArrayList<Message>();
	}

	public void add(Message message)
	{
		this.messages.add(message);
	}

	public void add(Contact contact)
	{
		chats.remove(participants);
		participants.add(contact);
		chats.put(participants, this);
	}

	public void remove(Contact contact)
	{
		participants.remove(contact);
	}

	public static Map<Set<Contact>, Chat> getChats()
	{
		return chats;
	}

	@Override
	public boolean equals(Object object)
	{
		Chat chat = (Chat) object;
		return chat.participants.equals(this.participants);
	}
	
	public List<Message> getMessages(){
		return this.messages;
	}
	
	public Set<Contact> getParticipants(){
		return this.participants;
	}
	
	public static Chat get(Contact participant)
	{
		Set<Contact> participants = new HashSet<Contact>();
		participants.add(participant);
		return get(participants);
	}
	
	public static Chat get(Set<Contact> participants)
	{
		return ((chats.containsKey(participants))?(chats.get(participants)):(new Chat(participants)));
	}
}
