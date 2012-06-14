package fr.utbm.lo52.sodia.protocols.typa;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import android.util.Log;

class Client
{
	private static Map<InetAddress, Client> clients = new HashMap<InetAddress, Client>();
	
	private InetAddress address;
	private Socket clientSocket;
	
	protected Client(InetAddress address) throws IOException
	{
		this.address = address;
		clientSocket = new Socket(address, Typa.PORT);
		clients.put(address, this);
	}
	
	protected Client(InetAddress address, Socket serverSocket) throws IOException
	{
		this.address = address;
		clientSocket = new Socket(address, Typa.PORT);
		clients.put(address, this);
	}
	
	public void send(Formater formater) throws Throwable
	{
		formater.output = clientSocket.getOutputStream();
		formater.send();
	}
	
	@Override
	public void finalize()
	{
		try
		{
			clients.remove(address);
			clientSocket.close();
		} catch (IOException e)
		{
			Log.e(getClass().getName(), "", e);
		}
		
	}
	
	public static Client get(InetAddress address) throws IOException
	{
		if(clients.containsKey(address))
		{
			return clients.get(address);
		}
		return new Client(address);
	}

	public InetAddress getAddress()
	{
		return this.address;
	}

	public Socket getClientSocket()
	{
		return this.clientSocket;
	}
	
}