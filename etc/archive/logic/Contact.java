package fr.utbm.lo52.sodia.logic;

import java.util.Set;

import fr.utbm.lo52.sodia.common.GroupUpdater;

public class Contact implements InterGroup<Group>
{
	private String 
	private Set<Group> groups;
	private Set<Link> links;
	
	public void add(Group group)
	{
		GroupUpdater.magicUpdate(groups.contains(group), group, this);
	}
	
	public void remove(Group group)
	{
		GroupUpdater.magicUpdate(groups.contains(group), group, this);
	}
	
	public void add(Link link)
	{
		links.add(link);
	}
	
	public void remove(Link link)
	{
		links.remove(link);
	}
	
}
