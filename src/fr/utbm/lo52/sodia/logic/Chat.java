package fr.utbm.lo52.sodia.logic;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Chat
{
	private static Set<Chat> chats;
	static
	{
		chats = new HashSet<Chat>();
	}
	
	private Set<Contact> participants;
	private List<Message> messages;

	public Chat(Contact participant)
	{
		this(new HashSet<Contact>());
		this.messages = new ArrayList<Message>();
		this.add(participant);
	}

	public Chat(Set<Contact> participants)
	{
		this.participants = participants;
		this.messages = new ArrayList<Message>();
		chats.add(this);
	}

	public void add(Message message)
	{
		this.messages.add(message);
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

	@Override
	public boolean equals(Object object)
	{
		Chat chat = (Chat) object;
		return chat.participants.containsAll(this.participants)
				&& this.participants.containsAll(chat.participants);
	}
}
