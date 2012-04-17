package fr.utbm.lo52.sodia.logic;

import java.util.Set;

import fr.utbm.lo52.sodia.common.GroupUpdater;

public class Group implements InterGroup<Contact>
{
	private Set<Contact> contacts;
	
	public void add(Contact contact)
	{
		GroupUpdater.magicUpdate(contacts.contains(contact), contact, this);
	}
	
	public void remove(Contact contact)
	{
		GroupUpdater.magicUpdate(contacts.contains(contact), contact, this);
	}
	
	public final Set<Contact> getContacts()
	{
		return contacts;
	}
	
}
