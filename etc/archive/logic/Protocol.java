package fr.utbm.lo52.sodia.logic;

public interface Protocol
{
	
	public void connect(Account<Protocol> account);
	public void send(Account<Protocol> account, Message message);
	public void disconnect(Account<Protocol> account);
	
}
