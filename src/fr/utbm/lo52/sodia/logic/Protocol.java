package fr.utbm.lo52.sodia.logic;

public interface Protocol
{
	
	public static void connect(Account<Protocol> account);
	public static void send(Account<Protocol> account, Message message)
	public static void disconnect(Account<Protocol> account);
	
}
