package fr.utbm.lo52.sodia.protocols.typa;

import java.net.InetAddress;
import java.util.ArrayList;

import fr.utbm.lo52.sodia.R;
import fr.utbm.lo52.sodia.logic.Contact;
import fr.utbm.lo52.sodia.logic.Im;
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
	public void send(Message message) throws Throwable
	{
		message.setFrom(me);
		ArrayList<Message> messages = new ArrayList<Message>();
		messages.add(message);
		Formater formater = new Formater();
		formater.operation = Formater.Operation.RET;
		formater.type = Formater.Type.MESSAGE;
		formater.setMessages(messages);
		for(Contact contact : message.getTo())
		{
			for(Im im :contact.getImByProtocol((new Typa()).getName()))
			{
				Client.get(InetAddress.getByName(im.getUserId().split("" +Im.USER_ID_SEPARATOR)[1])).send(formater);
			}
		}
	}

	@Override
	public void presence(Status status) throws Throwable
	{
		//TODO Send to all contacts
	}

	@Override
	public void disconnect()
	{
		// TODO Auto-generated method stub
		bonjour.close();
		server.cancel(true);
	}


	

}
