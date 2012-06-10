package fr.utbm.lo52.sodia.protocols.typa;

import fr.utbm.lo52.sodia.R;
import fr.utbm.lo52.sodia.logic.Message;
import fr.utbm.lo52.sodia.logic.Status;
import fr.utbm.lo52.sodia.protocols.Protocol;

public class Typa extends Protocol
{
	public static final int PORT = 4242;
	private Server server;
	private Bonjour bonjour;

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
	public void connect()
	{
		// TODO Auto-generated method stub
		bonjour = new Bonjour();
		bonjour.execute((Void[]) null);
		server = new Server();
		server.execute((Void[]) null);
	}

	@Override
	public void send(Message message)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void presence(Status status)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void disconnect()
	{
		// TODO Auto-generated method stub
		
	}


	

}
