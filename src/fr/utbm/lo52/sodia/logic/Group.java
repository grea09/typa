package fr.utbm.lo52.sodia.logic;

import java.util.Set;

import fr.utbm.lo52.sodia.common.GroupUpdater;

public class Group implements InterGroup<Contact>
{
	private Set<Contact> contacts;
	private String groupName;
	
	public Group(String name)
	{
		groupName = name;
	}
	
	public void add(Contact contact)
	{
		GroupUpdater.magicUpdate(contacts.contains(contact), contact, this);
	}
	
	public void remove(Contact contact)
	{
		GroupUpdater.magicUpdate(contacts.contains(contact), contact, this);
	}
	
	public Set<Contact> getContacts()
	{
		return contacts;
	}
	
	public void setContacts(Set<Contact> setContacts)
	{
		contacts = setContacts;
	}
	
	public String getGroupName()
	{
		return groupName;
	}
	
}
