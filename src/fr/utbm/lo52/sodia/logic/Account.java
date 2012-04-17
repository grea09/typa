package fr.utbm.lo52.sodia.logic;

public interface Account <E extends Protocol>
{
	
	public void add(Contact contact);
	public void remove(Contact contact);
	
	
	public void add(Group group);
	public void remove(Group group);
	
	public void send(Message message);
	
}
