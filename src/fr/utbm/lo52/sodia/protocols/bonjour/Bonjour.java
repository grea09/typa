package fr.utbm.lo52.sodia.protocols.bonjour;

import fr.utbm.lo52.sodia.R;
import fr.utbm.lo52.sodia.logic.Message;
import fr.utbm.lo52.sodia.protocols.Protocol;

public class Bonjour extends Protocol
{


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
		return "Bonjour";
	}

	@Override
	public String getAccountType()
	{
		return "com.apple.bonjour";
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
