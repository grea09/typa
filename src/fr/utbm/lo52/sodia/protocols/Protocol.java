package fr.utbm.lo52.sodia.protocols;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import android.accounts.Account;
import android.os.AsyncTask;
import fr.utbm.lo52.sodia.logic.Contact;
import fr.utbm.lo52.sodia.logic.Message;
import fr.utbm.lo52.sodia.logic.Status;

/**
 * @author antoine
 *
 */
public abstract class Protocol
{
	private static Map<Account, Protocol> accounts;
	
	private static Set<ProtocolListener> listeners = new HashSet<ProtocolListener>();
	
	public abstract String getName(); //eg Bonjour
	public abstract String getAccountType(); // com.apple.bonjour
	public abstract int getLogoRessource();
	
	public abstract boolean hasPassword();
	
	protected Account account;
	protected Contact me;
	
	public static Set<Account> getAccounts()
	{
		return accounts.keySet();
	}
	
	public static Set<Account> getAccountsByType(String type)
	{
		Set<Account> accounts = new HashSet<Account>();
		for(Account account: Protocol.accounts.keySet())
		{
			if(account.type == type)
			{
				accounts.add(account);
			}
		}
		return accounts;
	}
	
	public static Set<Class<? extends Protocol>> getProtocolsClass()
	{
		Set<Class<? extends Protocol>> protocols = new HashSet<Class<? extends Protocol>>();
		for(Protocol protocol : accounts.values())
		{
			protocols.add(protocol.getClass());
		}
		return protocols;
	}
	
	public static Set<? extends Protocol> getProtocols()
	{
		return (Set<? extends Protocol>) accounts.values();
	}
	
	@SuppressWarnings("unchecked")
	public static<T extends Protocol> Set<T> getProtocolsByType(Class<T> clazz)
	{
		Set<T> protocols = new HashSet<T>();
		for(Protocol protocol : accounts.values())
		{
			if(protocol.getClass() == clazz)
			{
				protocols.add((T) protocol);
			}
		}
		return protocols;
	}
	
	public static Set<String> getAccountTypes()
	{
		Set<Account> accounts = Protocol.accounts.keySet();
		Set<String> value = new HashSet<String>();
		for(Account account : accounts)
		{
			value.add(account.type);
		}
		return value;
	}
	
	public static void add(ProtocolListener listener)
	{
		listeners.add(listener);
	}
	
	public static void remove(ProtocolListener listener)
	{
		listeners.remove(listener);
	}
	
	public void receive(final Message message)
	{
		AsyncTask.execute(new Runnable()
			{
				
				@Override
				public void run()
				{
					for(ProtocolListener listener : listeners)
					{
						listener.receive(message);
					}
					
				}
			});
	}
	
	public void contacts(final Contact[] contacts)
	{
		AsyncTask.execute(new Runnable()
		{
			
			@Override
			public void run()
			{
				for(ProtocolListener listener : listeners)
				{
					listener.contacts(contacts);
				}
				
			}
		});
	}
	
	public static Protocol get(Account account)
	{
		if(accounts.containsKey(account))
		{
			return accounts.get(account);
		}
		return constructor(account.type);
	}
	
	public static Protocol constructor(String type)
	{
		Set<Class<? extends Protocol>> classes = getProtocolsClass();
		for(Class<? extends Protocol> clazz : classes)
		{
			try
			{
				Protocol value = clazz.newInstance();
				if(value.getAccountType() == type)
				{
					return value;
				}
			} catch (InstantiationException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}
	
	
	public Protocol()
	{
		//Virtual static only.
	}
	
	
	/**
	 * @throws IllegalArgumentException
	 **/
	protected Protocol(Account account) throws IllegalArgumentException
	{
		this.setAccount(account);
	}

	public Account account()
	{
		return account;
	}
	
	public void setAccount(Account account) throws IllegalArgumentException
	{
		if (account.type != getAccountType())
		{
			throw new IllegalArgumentException("Account type not suported");
		}
		this.account = account;
		try
		{
			me = Contact.getByIm(account.name + "@" + InetAddress.getLocalHost().getHostName());
		} catch (UnknownHostException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		accounts.put(account, this);
	}

	/**
	 * Connect to current account
	 */
	public abstract void connect();

	/**
	 * Send a message to contact using this protocol.
	 * @param message
	 * @param contact of the form "someone@server.org"
	 */
	public abstract void send(Message message);

	/**
	 * Set status of current account on the network.
	 * @param status status in the db
	 */
	public abstract void presence(Status status);

	/**
	 * Disconnect cuurent account
	 */
	public abstract void disconnect();
	
	public Contact getMe()
	{
		return this.me;
	}
}
