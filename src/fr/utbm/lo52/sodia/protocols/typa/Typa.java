package fr.utbm.lo52.sodia.protocols.typa;

import java.net.InetAddress;
import java.util.ArrayList;

import android.content.Intent;
import fr.utbm.lo52.sodia.R;
import fr.utbm.lo52.sodia.logic.Contact;
import fr.utbm.lo52.sodia.logic.Im;
import fr.utbm.lo52.sodia.logic.Message;
import fr.utbm.lo52.sodia.logic.Status;
import fr.utbm.lo52.sodia.protocols.Protocol;

public class Typa extends Protocol
{
	static
	{
		Protocol.add(Typa.class);
	}
	
	public static final int PORT = 4242;
	private Server server;

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
		return R.drawable.ic_protocol_typa;
	}

	@Override
	public boolean hasPassword()
	{
		return false;
	}

	
	@Override
	public void connect()
	{
		if(!(server instanceof Server))
		{
			Intent intent = new Intent(context, Server.class);
			context.startService(intent);
			
		}
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
		if(server instanceof Server)
		{
			server.stopSelf();
		}
	}


	

}
