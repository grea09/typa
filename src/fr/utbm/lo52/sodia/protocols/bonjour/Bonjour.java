package fr.utbm.lo52.sodia.protocols.bonjour;

import android.accounts.Account;
import fr.utbm.lo52.sodia.logic.Message;
import fr.utbm.lo52.sodia.protocols.Protocol;
import fr.utbm.lo52.sodia.protocols.ProtocolManager.ProtocolAlreadyRegisteredException;

public class Bonjour extends Protocol
{

	public Bonjour(Account account) throws IllegalArgumentException,
			ProtocolAlreadyRegisteredException
	{
		super(account);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void connect()
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void send(Message message, String contact)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void presence(long status)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void disconnect()
	{
		// TODO Auto-generated method stub
		
	}

	

}
