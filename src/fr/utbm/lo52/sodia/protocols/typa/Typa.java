package fr.utbm.lo52.sodia.protocols.typa;

import fr.utbm.lo52.sodia.R;
import fr.utbm.lo52.sodia.logic.Message;
import fr.utbm.lo52.sodia.protocols.Protocol;

public class Typa extends Protocol
{
	public static final int PORT = 4242;

	@Override
	public void connect()
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

	@Override
	public String getName()
	{
		return "Typa";
	}

	@Override
	public String getAccountType()
	{
		return "fr.utbm.lo52.sodia.typa";
	}

	@Override
	public int getLogoRessource()
	{
		return R.drawable.ic_protocol_bonjour;
	}

	@Override
	public boolean hasPassword()
	{
		return false;
	}

	@Override
	public void send(Message message, String contact)
	{
		// TODO Auto-generated method stub
		
	}

	

}
