package fr.utbm.lo52.sodia.protocols.typa;

import android.content.Intent;
import android.content.OperationApplicationException;
import android.net.wifi.WifiManager;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.util.Log;
import fr.utbm.lo52.sodia.R;
import fr.utbm.lo52.sodia.logic.*;
import fr.utbm.lo52.sodia.protocols.Protocol;
import java.net.InetAddress;
import java.util.ArrayList;

public class Typa extends Protocol
{
	static
	{
		Protocol.add(Typa.class);
	}
	
	private class HostNameThread extends Thread
	{
		public String hostName;
		
		@Override
		public void run()
		{
			WifiManager wifi = (android.net.wifi.WifiManager) context.getSystemService(android.content.Context.WIFI_SERVICE);
			int ip = wifi.getConnectionInfo().getIpAddress();
			if(ip == 0)
			{
				hostName = "localhost";
			}
			else
			{
				hostName = String.format("%d.%d.%d.%d",
						(ip       & 0xff),
						(ip >>  8 & 0xff),
						(ip >> 16 & 0xff),
						(ip >> 24 & 0xff)
				);
			}
			Log.d(Typa.class.getSimpleName(), "localhost = " + hostName);
		}
	}
	
	private String getHostName()
	{
		HostNameThread hostNameThread = new HostNameThread();
		hostNameThread.start();
		while (hostNameThread.isAlive())
		{
			Thread.yield();
		}
		return hostNameThread.hostName;
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
			Log.i(getClass().getSimpleName(), "Connection ...");
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
	
	@Override
	protected void createMe()
	{
		if(me instanceof Contact)
		{
			return;
		}
		try
		{
			String accountIm = account.name + Im.USER_ID_SEPARATOR + getHostName();
			me = Contact.getByIm(accountIm);
			if(me == null)
			{
				Log.i(getClass().getName(), "Create Me !!!");
				me = new Contact(account.name);
				Im im = new Im(accountIm, new Status(Presence.AVAILABLE, "", System.currentTimeMillis(), null, null), ContactsContract.CommonDataKinds.Im.PROTOCOL_CUSTOM, getName()); // TODO Add label and icon
				RawContact rawContact = new RawContact(false, account, new Name(account.name, null, null, null));
				rawContact.addIm(im);
				Group group = Group.getByName("LAN", account);
				me.addRawContact(rawContact);
				me.add(group);
				me.save();
			}
		} catch (RemoteException e)
		{
			Log.e(getClass().getName(), "", e);
		} catch (OperationApplicationException e)
		{
			Log.e(getClass().getName(), "", e);
		}
	}

	

}
