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
			Log.i(getClass().getName(), "Create Me !!!");
			me = Contact.simpleCreate(account, account.name, 
				new Im(accountIm, 
					new Status(Presence.AVAILABLE,""), 
				ContactsContract.CommonDataKinds.Im.PROTOCOL_CUSTOM, 
				getName()
				), "LAN"
			);
		} catch (RemoteException e)
		{
			Log.e(getClass().getName(), "", e);
		} catch (OperationApplicationException e)
		{
			Log.e(getClass().getName(), "", e);
		}
	}

	@Deprecated
	public void generateFalseContactList() throws RemoteException, OperationApplicationException
	{
		//For test only
		String[][] names = new String[][]
		{
			{"Geoffrey TISSERAND", "gtisserand@192.168.1.42"},
			{"Mathieu JEVAUDAN", "mjevaudan@192.168.1.54"},
			{"Antoine GRÉA", "agrea@192.168.1.9"},
			{"Bernard TRICON", "nardnard@192.168.1.23"},
			{"Martin COMPTEUR", "m++@192.168.1.24"},
			{"Virgil LITTLETOWN", "odst@192.168.1.25"},
			{"Roger BLANC", "#ffffff@192.168.1.29"},
			{"Jean-Jaques GRINGUEDIGÈGLE", "jjgr@192.168.1.12"}
		};
		for (int i = 0; i < 3; i++)
		{
			Contact.simpleCreate(account, names[i][0], 
				new Im(names[i][1], 
					new Status(Presence.AVAILABLE,""), 
				ContactsContract.CommonDataKinds.Im.PROTOCOL_CUSTOM, 
				getName()
				), "Friends"
			);
		}
		for (int i = 3; i < names.length; i++)
		{
			Contact.simpleCreate(account, names[i][0], 
				new Im(names[i][1], 
					new Status(Presence.IDLE,""), 
				ContactsContract.CommonDataKinds.Im.PROTOCOL_CUSTOM, 
				getName()
				), "LAN"
			);
		}
	}

}
